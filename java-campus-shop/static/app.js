const productsNode = document.querySelector("#products");
const productSelect = document.querySelector("#product");
const statusNode = document.querySelector("#status");
const ordersNode = document.querySelector("#orders");

// Load the catalogue from the Java API, then use safe textContent for user-visible values.
async function loadProducts() {
  statusNode.textContent = "Loading products…";
  try {
    const response = await fetch("/api/products");
    if (!response.ok) throw new Error("The catalogue could not be loaded.");
    const products = await response.json();
    productsNode.replaceChildren();
    productSelect.replaceChildren();
    for (const product of products) {
      const card = document.createElement("article");
      card.className = "product";
      const name = document.createElement("h3");
      name.textContent = product.name;
      const description = document.createElement("p");
      description.textContent = product.description;
      const price = document.createElement("span");
      price.className = "price";
      price.textContent = `£${product.price.toFixed(2)}`;
      const stock = document.createElement("span");
      stock.className = "stock";
      stock.textContent = `${product.stock} in stock`;
      card.append(name, description, price, stock);
      productsNode.append(card);

      const option = document.createElement("option");
      option.value = product.id;
      option.textContent = `${product.name} — £${product.price.toFixed(2)}`;
      option.disabled = product.stock === 0;
      productSelect.append(option);
    }
    statusNode.textContent = `${products.length} sample products loaded from the Java server.`;
  } catch (error) {
    statusNode.textContent = error.message;
  }
}

// Submit a simple form-encoded order; the server validates quantity and stock.
document.querySelector("#order-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  statusNode.textContent = "Submitting order…";
  try {
    const response = await fetch("/api/orders", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams(new FormData(event.currentTarget)),
    });
    const result = await response.json();
    if (!response.ok) throw new Error(result.error || "Order could not be placed.");
    const item = document.createElement("li");
    item.textContent = `#${result.orderId} · ${result.quantity} × ${result.product} · £${result.total.toFixed(2)}`;
    if (ordersNode.textContent === "No orders yet.") ordersNode.replaceChildren();
    ordersNode.prepend(item);
    statusNode.textContent = `Order #${result.orderId} placed. The server updated the stock.`;
    event.currentTarget.reset();
    await loadProducts();
  } catch (error) {
    statusNode.textContent = error.message;
  }
});

document.querySelector("#refresh").addEventListener("click", loadProducts);
loadProducts();
