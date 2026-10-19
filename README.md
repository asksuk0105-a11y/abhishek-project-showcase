# Abhishek Singh — Project Showcase

**Computer Science graduate · MSc Artificial Intelligence student**  
**Portfolio project journey: 2023–present**

This repository contains original academic-style and self-directed portfolio work. The showcase is written in plain HTML, CSS, and JavaScript. It presents three static front-end concepts plus two runnable learning projects: a Java browser-and-server shop and a Python machine-learning estimator. Each project note explains the code and its limitations.

> These projects are presented as portfolio work and learning exercises. Their 2023–present label describes the portfolio timeline; it does not claim that every current demo was built in a particular year or for a real client.

## Projects

| Project | What is implemented | Main technologies | Detailed notes |
|---|---|---|---|
| Marketday Online Store | Static responsive storefront interface concept | HTML, CSS | [Project notes](marketday/README.md) |
| Carepoint Hospital Manager | Static hospital dashboard interface concept with fictional records | HTML, CSS | [Project notes](carepoint/README.md) |
| Studywise AI Learning Companion | Static learning-support interface concept; no AI service is connected | HTML, CSS | [Project notes](studywise/README.md) |
| Used Car Price Estimator | Runnable scikit-learn regression pipeline with generated synthetic data | Python, pandas, NumPy, scikit-learn | [Project notes and code guide](car-price-prediction/README.md) |
| Java Campus Shop | Runnable browser interface and Java HTTP API; sample orders update in-memory stock | Java 17, JDK HttpServer, HTML, CSS, JavaScript | [Project notes and code guide](java-campus-shop/README.md) |

## Run the showcase

Open `index.html` in a browser, or see the [published project showcase](https://asksuk0105-a11y.github.io/abhishek-project-showcase/). It has no build step or third-party JavaScript dependency. The category controls filter the project cards. The screen previews are HTML/CSS mockups, not connected web applications.

### How the showcase code is organized

- **HTML** defines the page landmarks, headings, project cards, filter buttons, mock screens, and links.
- **CSS** provides the responsive two-column project layout, typography, color tokens, cards, and the small-screen layout inside the `<style>` block.
- **JavaScript** at the end of `index.html` listens for filter-button clicks, updates each button's `aria-pressed` state, and hides cards whose category does not match.
- **Accessibility details** include a named navigation landmark, native buttons and links, heading hierarchy, and `aria-pressed` state on the filters. This is a learning showcase, not a certified accessibility audit.

## Code walkthrough: car price estimator

The Java app in `java-campus-shop/` and Python project in `car-price-prediction/` are the runnable code examples. See their linked READMEs for full walkthroughs. The car ML pipeline:

1. `make_synthetic_data()` generates reproducible teaching rows from a fixed seed; it writes no real vehicle listings.
2. `train_test_split()` holds back a portion of rows for a simple evaluation.
3. A `ColumnTransformer` scales numeric features and one-hot encodes categories.
4. A scikit-learn `Pipeline` keeps preprocessing and the Random Forest model together, reducing the risk of applying different transformations at prediction time.
5. MAE, RMSE, and R² are printed for the synthetic holdout. They describe only this generated dataset and are not evidence of real-world price accuracy.
6. A sample record is passed through the fitted pipeline to demonstrate inference.

Follow the project's README to create an environment and run it locally.

## Java shop code walkthrough

The Java Campus Shop uses JDK `HttpServer` route handlers, in-memory product/order collections, server-side quantity and stock checks, and a small static browser client. The browser retrieves JSON with `fetch()`, submits a URL-encoded order form, displays status feedback, and refreshes the catalogue. The detailed Java README explains each class/method and the run command.

## References and inspiration

These public references informed the learning topics and design conventions. They are references for independent work, not claims of affiliation, endorsement, or copied source code.

- [MDN: HTML](https://developer.mozilla.org/en-US/docs/Web/HTML) and [MDN: CSS](https://developer.mozilla.org/en-US/docs/Web/CSS) — web platform documentation.
- [W3C WAI: ARIA Authoring Practices](https://www.w3.org/WAI/ARIA/apg/) — guidance for accessible interactive controls.
- [GOV.UK Design System](https://design-system.service.gov.uk/) — public-sector patterns and accessible form guidance; a useful reference for the clarity goals of the hospital dashboard.
- [NHS Service Manual](https://service-manual.nhs.uk/) — healthcare service design guidance; used as a domain reference for Carepoint's fictional workflow concept.
- [Shopify online store documentation](https://help.shopify.com/en/manual/online-store) — reference for common storefront and product-navigation concepts; Marketday is independently named and implemented.
- [scikit-learn: preprocessing data](https://scikit-learn.org/stable/modules/preprocessing.html), [composite estimators](https://scikit-learn.org/stable/modules/compose.html), and [model evaluation](https://scikit-learn.org/stable/modules/model_evaluation.html) — references for the car model pipeline.
- [pandas: introductory tutorials](https://pandas.pydata.org/docs/getting_started/intro_tutorials/) — reference for tabular data handling.

## Connect

- [Personal portfolio](https://asksuk0105-a11y.github.io/abhishek-singh-portfolio/)
- [GitHub profile](https://github.com/asksuk0105-a11y)
- [Email Abhishek](mailto:abhisheksingh00015@gmail.com)
