import java.util.*;

public class SalesReport {
    private static SalesReport instance;

    private Map<String, Order> dailyOrders = new HashMap<>();
    private double totalDailySales = 0.0;
    private Map<String, Integer> itemSalesCount = new HashMap<>();

    private SalesReport() {}

    public static SalesReport getInstance() {
        if (instance == null) {
            instance = new SalesReport();
        }
        return instance;
    }

    public void resetSalesData() {
        dailyOrders.clear();
        totalDailySales = 0.0;
        itemSalesCount.clear();
    }

    public void addOrder(Order order) {
        dailyOrders.put(order.getOrderID(), order);
        totalDailySales += order.getTotalPrice();

        for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            itemSalesCount.put(item.getName(), itemSalesCount.getOrDefault(item.getName(), 0) + quantity);
        }
    }

    public void processRefund(String orderID) {
        Order order = dailyOrders.get(orderID);
        if (order != null) {

            totalDailySales -= order.getTotalPrice();


            for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                itemSalesCount.put(item.getName(), itemSalesCount.get(item.getName()) - quantity);
            }


            dailyOrders.remove(orderID);

        }
    }

    public void generateSalesReport() {
        System.out.println("=== Daily Sales Report ===");
        System.out.println("Total Sales: $" + totalDailySales);
        System.out.println("Total Orders: " + dailyOrders.size());

        System.out.println("Most Popular Items:");
        itemSalesCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue() + " sold"));

        System.out.println("===========================");
    }
}
