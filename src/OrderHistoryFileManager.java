import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class OrderHistoryFileManager {
    private static OrderHistoryFileManager instance;
    private Map<String, List<Order>> orderHistoryData;
    private static final String FILE_PATH = "data/orderHistory.json";
    private static final String INDENT = "    "; // 4 spaces for indentation

    private OrderHistoryFileManager() {
        orderHistoryData = new HashMap<>();
        loadOrderHistory();
    }

    public static synchronized OrderHistoryFileManager getInstance() {
        if (instance == null) {
            instance = new OrderHistoryFileManager();
        }
        return instance;
    }

    private void loadOrderHistory() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write("{}".getBytes(StandardCharsets.UTF_8));
                }
                return;
            }

            try (FileInputStream fis = new FileInputStream(file)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                parseOrderHistory(content.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            orderHistoryData = new HashMap<>();
        }
    }

    private void parseOrderHistory(String jsonContent) {
        orderHistoryData.clear();
        jsonContent = jsonContent.trim();
        if (jsonContent.startsWith("{") && jsonContent.endsWith("}")) {
            jsonContent = jsonContent.substring(1, jsonContent.length() - 1).trim();
        }

        if (jsonContent.isEmpty()) {
            return;
        }

        String[] customerEntries = jsonContent.split(",\"");
        for (String entry : customerEntries) {
            entry = entry.trim().replace("\"", "");
            String[] parts = entry.split(":");
            if (parts.length < 2) continue;

            String customerID = parts[0].trim();
            String ordersString = parts[1].trim();
            List<Order> customerOrders = parseCustomerOrders(customerID, ordersString);
            orderHistoryData.put(customerID, customerOrders);
        }
    }

    private List<Order> parseCustomerOrders(String customerID, String ordersString) {
        List<Order> orders = new ArrayList<>();
        return orders;
    }

    public void saveOrderHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("{\n");

            Iterator<Map.Entry<String, List<Order>>> customerIterator = orderHistoryData.entrySet().iterator();
            while (customerIterator.hasNext()) {
                Map.Entry<String, List<Order>> customerEntry = customerIterator.next();

                // Write customer ID
                writer.write(INDENT + "\"" + customerEntry.getKey() + "\": [\n");

                // Write orders
                Iterator<Order> orderIterator = customerEntry.getValue().iterator();
                while (orderIterator.hasNext()) {
                    Order order = orderIterator.next();
                    writer.write(INDENT + INDENT + "{\n");
                    writer.write(constructOrderJson(order, 3)); // 3 levels of indentation for order content
                    writer.write(INDENT + INDENT + "}");
                    if (orderIterator.hasNext()) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }

                writer.write(INDENT + "]");
                if (customerIterator.hasNext()) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String constructOrderJson(Order order, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = INDENT.repeat(indentLevel);

        // Order details
        sb.append(indent).append("\"orderID\": \"").append(order.getOrderID()).append("\",\n");
        sb.append(indent).append("\"timestamp\": ").append(order.getTimestamp()).append(",\n");
        sb.append(indent).append("\"totalPrice\": ").append(order.getTotalPrice()).append(",\n");
        sb.append(indent).append("\"isVIP\": ").append(order.isVIP()).append(",\n");

        // Items
        sb.append(indent).append("\"items\": {\n");

        Iterator<Map.Entry<Item, Integer>> itemIterator = order.getItems().entrySet().iterator();
        while (itemIterator.hasNext()) {
            Map.Entry<Item, Integer> itemEntry = itemIterator.next();
            Item item = itemEntry.getKey();
            Integer quantity = itemEntry.getValue();

            sb.append(indent).append(INDENT).append("\"").append(item.getItemID()).append("\": {\n");
            sb.append(indent).append(INDENT).append(INDENT).append("\"name\": \"").append(item.getName()).append("\",\n");
            sb.append(indent).append(INDENT).append(INDENT).append("\"price\": ").append(item.getPrice()).append(",\n");
            sb.append(indent).append(INDENT).append(INDENT).append("\"quantity\": ").append(quantity).append("\n");
            sb.append(indent).append(INDENT).append("}");

            if (itemIterator.hasNext()) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(indent).append("}\n");
        return sb.toString();
    }

    public void saveOrderToHistory(Order order) {
        String customerID = order.getCustomerID();
        List<Order> customerOrders = orderHistoryData.computeIfAbsent(customerID, k -> new ArrayList<>());
        customerOrders.add(order);
        saveOrderHistory();
    }

    public List<Order> loadOrderHistoryForCustomer(String customerID) {
        return orderHistoryData.getOrDefault(customerID, new ArrayList<>());
    }
}