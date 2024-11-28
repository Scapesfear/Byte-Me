import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CustomerFileManager {
    private static final String CUSTOMER_FILE_PATH = "data/customers.json";
    private static CustomerFileManager instance;
    private Map<String, Map<String, String>> customerDatabase;

    private CustomerFileManager() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        customerDatabase = new HashMap<>();
        loadCustomers();
    }

    public static synchronized CustomerFileManager getInstance() {
        if (instance == null) {
            instance = new CustomerFileManager();
        }
        return instance;
    }

    private void loadCustomers() {
        File file = new File(CUSTOMER_FILE_PATH);
        if (!file.exists()) {
            saveCustomerDatabase();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            parseJsonContent(content.toString());
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    private void parseJsonContent(String content) {
        customerDatabase.clear();

        // Skip empty file or invalid JSON
        if (content.trim().isEmpty() || !content.trim().startsWith("{")) {
            return;
        }

        // Remove the outer braces
        content = content.trim().substring(1, content.length() - 1);

        // Split into individual customer entries
        String[] entries = content.split("(?<=}),\\s*\"");

        for (String entry : entries) {
            if (entry.isEmpty()) continue;

            // Add back the quote if it was removed during split
            if (!entry.startsWith("\"")) {
                entry = "\"" + entry;
            }

            // Parse each customer entry
            String[] parts = entry.split("\":\\s*\\{");
            if (parts.length == 2) {
                String userId = parts[0].replaceAll("\"", "");
                String customerData = parts[1].substring(0, parts[1].length() - 1);

                Map<String, String> userData = new HashMap<>();
                String[] fields = customerData.split(",");
                for (String field : fields) {
                    String[] keyValue = field.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replaceAll("\"", "");
                        String value = keyValue[1].trim().replaceAll("\"", "");
                        userData.put(key, value);
                    }
                }
                customerDatabase.put(userId, userData);
            }
        }
    }

    public void saveCustomer(Customer customer) {
        Map<String, String> customerData = new HashMap<>();
        customerData.put("password", customer.getPassword());
        customerDatabase.put(customer.getUserID(), customerData);
        saveCustomerDatabase();
    }

    private void saveCustomerDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE_PATH))) {
            writer.write(convertToJson());
        } catch (IOException e) {
            System.err.println("Error saving customer database: " + e.getMessage());
        }
    }

    private String convertToJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        Iterator<Map.Entry<String, Map<String, String>>> it = customerDatabase.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Map<String, String>> entry = it.next();
            json.append("    \"").append(entry.getKey()).append("\": {\n");

            Map<String, String> customerData = entry.getValue();
            Iterator<Map.Entry<String, String>> dataIt = customerData.entrySet().iterator();
            while (dataIt.hasNext()) {
                Map.Entry<String, String> dataEntry = dataIt.next();
                json.append("        \"").append(dataEntry.getKey()).append("\": \"")
                        .append(dataEntry.getValue()).append("\"");
                if (dataIt.hasNext()) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append("    }");
            if (it.hasNext()) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("}");
        return json.toString();
    }

    public Customer loadCustomer(String userID) {
        Map<String, String> customerData = customerDatabase.get(userID);
        if (customerData != null) {
            // Check if customer already exists in memory
            Customer existingCustomer = Customer.getCustomerRepo().get(userID);
            if (existingCustomer != null) {
                // Verify password matches
                if (existingCustomer.getPassword().equals(customerData.get("password"))) {
                    return existingCustomer;
                } else {
                    return null;
                }
            }

            // Create new customer if not in memory
            return new Customer(userID, customerData.get("password"));
        }
        return null;
    }
}