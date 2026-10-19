# Java Campus Shop

A self-directed portfolio project inspired by my Java full-stack course. It pairs a browser interface with a Java HTTP server and JSON API. Browse a sample catalogue, submit an order, and see in-memory stock update.

## Run locally

Requires JDK 17+. No third-party library download is needed. From this directory run:

```bash
javac --add-modules jdk.httpserver ShopApplication.java
java --add-modules jdk.httpserver -cp . ShopApplication
```

Open <http://localhost:8080>. Stop with `Ctrl+C`. The server binds to `127.0.0.1:8080`.

## Routes

- `GET /` serves the shop interface.
- `GET /api/products` returns the sample catalogue as JSON.
- `GET /api/orders` lists orders from the current server session.
- `POST /api/orders` accepts URL-encoded `productId` and `quantity`, validates stock, decrements inventory, records an order, and returns JSON.

## Code walkthrough

### Java server (`ShopApplication.java`)

1. `PRODUCTS` and `ORDERS` are in-memory collections; `seedProducts()` supplies fictional items at startup.
2. `main()` starts JDK `HttpServer` on localhost and registers handlers for products, orders, and static assets.
3. `handleProducts()` permits `GET`, builds the catalogue response, and returns 405 for other methods.
4. `handleOrders()` handles list and order-creation requests. It parses form fields, validates numbers and quantity limits, checks product/stock, and uses 409 for insufficient inventory. A synchronized section protects the stock decrement and order append together.
5. `handleStaticFiles()` serves only three explicitly named classpath assets; it never maps arbitrary URL paths to filesystem files.
6. `parseForm()`, `quote()`, `sendJson()`, and `sendText()` handle decoding, safe demo JSON strings, content types, and response writing.

### Browser client (`static/`)

- `index.html` defines the catalogue, order form, labels, and a status region announced with `aria-live`.
- `styles.css` provides the responsive card, form, and mobile layouts.
- `app.js` fetches products, creates DOM elements using `textContent`, submits URL-encoded form data, displays errors, and refreshes inventory after a successful order.

## Scope and limitations

This is a local learning demo, not a production shop. Products and orders are in memory and reset when the server stops. There is no database, login, payment service, persistent history, or deployment configuration. All stock and prices are fictional; do not enter real personal or payment data.

## References

- [Oracle Java 17 HttpServer API](https://docs.oracle.com/en/java/javase/17/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html) — built-in HTTP server and route contexts.
- [MDN Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API) — browser requests to the Java API.
- [MDN Forms](https://developer.mozilla.org/en-US/docs/Learn/Forms) and [W3C WAI Forms Tutorial](https://www.w3.org/WAI/tutorials/forms/) — form and accessibility guidance.
- [Shopify online store documentation](https://help.shopify.com/en/manual/online-store) — common e-commerce concepts; no Shopify code or data is used.

These references informed the learning process; this project is independently written and is not affiliated with those sites.
