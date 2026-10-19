# Marketday Online Store — project notes

## Goal
Explore how a small online shop can make product discovery and the path to a basket easy to scan. Marketday is a portfolio interface concept; product names and prices are fictional.

## What the demo contains
The storefront preview in the root `index.html` is hand-authored HTML and CSS: a brand header, three sample product tiles, category labels, and a basket summary. It is a static mock screen. There is no product database, live cart state, checkout, payment processing, authentication, or backend.

## Code guide
- `.mock` creates the storefront panel inside the project card.
- `.mockhead` aligns the store name and bag label.
- `.cards` uses CSS Grid for product tiles; `.tile` styles each fictional item.
- The `@media(max-width:760px)` rule adapts the showcase for narrow screens.
- The page JavaScript only filters showcase cards; it does not implement store behavior.

## How I would extend it
Add a product data model, search and category filters, persistent basket state, form validation, and a secure server-side checkout integration. Production payment processing should not be simulated as client-only code.

## References
- [Shopify online store documentation](https://help.shopify.com/en/manual/online-store) — common storefront concepts.
- [MDN CSS Grid](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout) — product tile layout reference.
- [W3C WAI Images Tutorial](https://www.w3.org/WAI/tutorials/images/) — text alternatives for future product photos.

These links are learning references; Marketday is independently named and implemented for this portfolio.
