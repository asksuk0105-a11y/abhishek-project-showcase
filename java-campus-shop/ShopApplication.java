import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** A small Java HTTP server and in-memory campus shop API for a portfolio demo. */
public final class ShopApplication {
    private static final Map<Integer, Product> PRODUCTS = new LinkedHashMap<>();
    private static final List<Order> ORDERS = new ArrayList<>();
    private static int nextOrderId = 1;

    private ShopApplication() {}

    public static void main(String[] args) throws IOException {
        seedProducts();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        server.createContext("/api/products", ShopApplication::handleProducts);
        server.createContext("/api/orders", ShopApplication::handleOrders);
        server.createContext("/", ShopApplication::handleStaticFiles);
        server.setExecutor(null);
        server.start();
        System.out.println("Campus Shop is running at http://localhost:8080");
    }

    private static void seedProducts() {
        PRODUCTS.put(1, new Product(1, "Campus notebook", "Recycled paper · 160 pages", 4.50, 24));
        PRODUCTS.put(2, new Product(2, "Insulated bottle", "Stainless steel · 500 ml", 12.00, 12));
        PRODUCTS.put(3, new Product(3, "Study desk lamp", "Warm LED · USB powered", 18.75, 8));
        PRODUCTS.put(4, new Product(4, "Canvas tote", "Reusable cotton carry bag", 7.25, 18));
    }

    /** GET returns the current demo catalogue; unsupported methods receive 405. */
    private static void handleProducts(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        List<String> rows = new ArrayList<>();
        synchronized (PRODUCTS) {
            for (Product p : PRODUCTS.values()) {
                rows.add("{\"id\":" + p.id + ",\"name\":" + quote(p.name)
                        + ",\"description\":" + quote(p.description)
                        + ",\"price\":" + p.price + ",\"stock\":" + p.stock + "}");
            }
        }
        sendJson(exchange, 200, "[" + String.join(",", rows) + "]");
    }

    /** POST accepts productId and quantity as form fields, validates stock, and records an order. */
    private static void handleOrders(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<String> rows = new ArrayList<>();
            synchronized (PRODUCTS) {
                for (Order order : ORDERS) {
                    rows.add("{\"id\":" + order.id + ",\"product\":" + quote(order.productName)
                            + ",\"quantity\":" + order.quantity + ",\"total\":" + order.total + "}");
                }
            }
            sendJson(exchange, 200, "[" + String.join(",", rows) + "]");
            return;
        }
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        Map<String, String> fields = parseForm(exchange);
        try {
            int productId = Integer.parseInt(fields.getOrDefault("productId", ""));
            int quantity = Integer.parseInt(fields.getOrDefault("quantity", ""));
            if (quantity < 1 || quantity > 20) {
                sendJson(exchange, 400, "{\"error\":\"Quantity must be between 1 and 20\"}");
                return;
            }
            synchronized (PRODUCTS) {
                Product product = PRODUCTS.get(productId);
                if (product == null) {
                    sendJson(exchange, 404, "{\"error\":\"Product not found\"}");
                    return;
                }
                if (quantity > product.stock) {
                    sendJson(exchange, 409, "{\"error\":\"Not enough stock available\"}");
                    return;
                }
                product.stock -= quantity;
                Order order = new Order(nextOrderId++, product.name, quantity, product.price * quantity);
                ORDERS.add(order);
                sendJson(exchange, 201, "{\"message\":\"Order placed\",\"orderId\":" + order.id
                        + ",\"product\":" + quote(order.productName) + ",\"quantity\":" + quantity
                        + ",\"total\":" + order.total + "}");
            }
        } catch (NumberFormatException ex) {
            sendJson(exchange, 400, "{\"error\":\"Enter a valid product and quantity\"}");
        }
    }

    /** Serve only the three known static assets; never map a request path to an arbitrary file. */
    private static void handleStaticFiles(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String file = switch (path) {
            case "/", "/index.html" -> "index.html";
            case "/app.js" -> "app.js";
            case "/styles.css" -> "styles.css";
            default -> null;
        };
        if (file == null || !"GET".equals(exchange.getRequestMethod())) {
            sendText(exchange, 404, "Not found", "text/plain; charset=utf-8");
            return;
        }
        try (var input = ShopApplication.class.getResourceAsStream("/static/" + file)) {
            if (input == null) {
                sendText(exchange, 500, "Static asset missing from classpath", "text/plain; charset=utf-8");
                return;
            }
            byte[] bytes = input.readAllBytes();
            String contentType = file.endsWith(".css") ? "text/css; charset=utf-8"
                    : file.endsWith(".js") ? "text/javascript; charset=utf-8" : "text/html; charset=utf-8";
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
        }
        exchange.close();
    }

    private static Map<String, String> parseForm(HttpExchange exchange) throws IOException {
        String raw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> fields = new LinkedHashMap<>();
        for (String pair : raw.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            fields.put(key, value);
        }
        return fields;
    }

    private static String quote(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        sendText(exchange, status, json, "application/json; charset=utf-8");
    }

    private static void sendText(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static final class Product {
        final int id;
        final String name;
        final String description;
        final double price;
        int stock;
        Product(int id, String name, String description, double price, int stock) {
            this.id = id; this.name = name; this.description = description; this.price = price; this.stock = stock;
        }
    }

    private static final class Order {
        final int id;
        final String productName;
        final int quantity;
        final double total;
        Order(int id, String productName, int quantity, double total) {
            this.id = id; this.productName = productName; this.quantity = quantity; this.total = total;
        }
    }
}
