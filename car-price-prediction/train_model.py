"""Train a teaching example car-price model on generated synthetic data.

All generated values are simulated and are not suitable for real pricing.
"""

from pathlib import Path

import numpy as np
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder, StandardScaler


ROOT = Path(__file__).resolve().parent
DATA_PATH = ROOT / "data" / "synthetic_used_cars.csv"
RANDOM_SEED = 42


def make_synthetic_data(rows: int = 1_200) -> pd.DataFrame:
    """Create plausible-looking but entirely simulated teaching data."""
    rng = np.random.default_rng(RANDOM_SEED)
    makes = np.array(["Aster", "Boreal", "Caspian", "Dorset", "Everton"])
    make_tiers = {"Aster": 1.00, "Boreal": 1.12, "Caspian": 0.92, "Dorset": 1.20, "Everton": 1.05}
    make = rng.choice(makes, size=rows)
    age = rng.integers(0, 16, size=rows)
    mileage = rng.integers(0, 180_000, size=rows)
    engine = rng.choice([1.0, 1.2, 1.4, 1.6, 2.0, 2.5, 3.0], size=rows)
    fuel = rng.choice(["Petrol", "Diesel", "Hybrid"], size=rows, p=[0.58, 0.32, 0.10])
    transmission = rng.choice(["Manual", "Automatic"], size=rows, p=[0.62, 0.38])

    fuel_adjustment = np.select([fuel == "Hybrid", fuel == "Diesel"], [2_100, 500], default=0)
    transmission_adjustment = np.where(transmission == "Automatic", 1_250, 0)
    starting_value = 8_500 + engine * 2_600
    depreciation = np.exp(-age * 0.105)
    mileage_effect = mileage * 0.022
    noise = rng.normal(0, 1_250, size=rows)
    price = (starting_value * depreciation * pd.Series(make).map(make_tiers).to_numpy()
             + fuel_adjustment + transmission_adjustment - mileage_effect + noise)
    price = np.clip(price, 700, None).round(0)

    return pd.DataFrame({
        "make": make,
        "age_years": age,
        "mileage_km": mileage,
        "engine_litres": engine,
        "fuel_type": fuel,
        "transmission": transmission,
        "price_gbp": price,
    })


def main() -> None:
    data = make_synthetic_data()
    DATA_PATH.parent.mkdir(parents=True, exist_ok=True)
    data.to_csv(DATA_PATH, index=False)

    target = "price_gbp"
    numeric = ["age_years", "mileage_km", "engine_litres"]
    categorical = ["make", "fuel_type", "transmission"]
    preprocess = ColumnTransformer([
        ("numeric", StandardScaler(), numeric),
        ("categories", OneHotEncoder(handle_unknown="ignore"), categorical),
    ])
    model = Pipeline([
        ("preprocess", preprocess),
        ("regressor", RandomForestRegressor(
            n_estimators=250, min_samples_leaf=2, random_state=RANDOM_SEED, n_jobs=-1
        )),
    ])

    features = data[numeric + categorical]
    labels = data[target]
    x_train, x_test, y_train, y_test = train_test_split(
        features, labels, test_size=0.2, random_state=RANDOM_SEED
    )
    model.fit(x_train, y_train)
    predictions = model.predict(x_test)
    rmse = np.sqrt(mean_squared_error(y_test, predictions))

    print(f"Synthetic rows saved to: {DATA_PATH}")
    print("Holdout metrics (synthetic data only):")
    print(f"  MAE:  £{mean_absolute_error(y_test, predictions):,.0f}")
    print(f"  RMSE: £{rmse:,.0f}")
    print(f"  R²:   {r2_score(y_test, predictions):.3f}")

    example = pd.DataFrame([{
        "make": "Boreal", "age_years": 4, "mileage_km": 42_000,
        "engine_litres": 1.6, "fuel_type": "Hybrid", "transmission": "Automatic",
    }])
    print(f"Example estimate on generated data: £{model.predict(example)[0]:,.0f}")
    print("Reminder: generated-data estimates are educational examples, not market valuations.")


if __name__ == "__main__":
    main()
