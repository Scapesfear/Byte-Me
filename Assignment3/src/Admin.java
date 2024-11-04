import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    private static Admin instance;
    private Menu menu = Menu.getInstance();
    private SalesReport salesReport = SalesReport.getInstance();


    private Admin() {
        super("admin", "admin123");
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    public void handleSpecialRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the order ID: ");
        String orderID = scanner.nextLine();

        Order order = Order.getAllOrders().get(orderID);
        if (order != null) {
            order.viewSpecialRequests();
            if (order.getSpecialRequests() != null) {
                System.out.println("Special request for order is handled");
            }
        } else {
            System.out.println("Order not found.");
        }
    }

    public void AdminOptions() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n------Admin Options --------");
            System.out.println("1. View Menu");
            System.out.println("2. Add new items to menu");
            System.out.println("3. Update item in menu");
            System.out.println("4. Remove Item from menu");
            System.out.println("5. Reactivate Item");
            System.out.println("6. View pending orders");
            System.out.println("7. Update Order Status");
            System.out.println("8. Refund Order");
            System.out.println("9. Handle Special Request");
            System.out.println("10. Generate Sales Report");
            System.out.println("11. Reset Sales Report/New day");
            System.out.println("12. Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    menu.viewMenu();
                    break;
                case 2:
                    System.out.println("Enter the item ID: ");
                    String itemID = scanner.nextLine();
                    System.out.println("Enter the item name: ");
                    String name = scanner.nextLine();
                    System.out.println("Enter the item price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Enter the item type: ");
                    String type = scanner.nextLine();
                    menu.addNewItem(itemID, name, price, type);
                    break;

                case 3:
                    System.out.println("Enter the item ID: ");
                    String itemID1 = scanner.nextLine();
                    System.out.println("Enter the new item name: ");
                    String name1 = scanner.nextLine();
                    System.out.println("Enter the new item price: ");
                    double price1 = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Enter the new item type: ");
                    String type1 = scanner.nextLine();
                    menu.updateItem(itemID1, name1, price1, type1);
                    break;

                case 4:
                    System.out.println("Enter the item ID: ");
                    String itemID2 = scanner.nextLine();
                    menu.removeItem(itemID2);
                    break;

                case 5:
                    menu.viewPreviousItems();
                    System.out.println("Enter the item ID: ");
                    String itemID3 = scanner.nextLine();
                    menu.reactivateItem(itemID3);
                    break;

                case 6:
                    List<Order> orders = Order.getPendingOrders();
                    if (orders.isEmpty()) {
                        System.out.println("No pending orders.");
                    } else {
                        for (Order order : orders) {
                            order.viewOrderDetails();
                        }
                    }
                    break;

                case 7:
                    System.out.println("Enter the order ID: ");
                    String orderID = scanner.nextLine();
                    System.out.println("Enter the new status: ");
                    String status = scanner.nextLine();
                    Order order = Order.getAllOrders().get(orderID);
                    if (order != null) {
                        order.updateStatus(status);
                    } else {
                        System.out.println("Order not found.");
                    }
                    break;

                case 8:
                    for (String orderID1 : Order.getCanclledOrders().keySet()) {
                        Pair<Order, String> pair = Order.getCanclledOrders().get(orderID1);
                        System.out.println("Order ID: " + orderID1 + " Reason: " + pair.getSecond());
                    }

                    System.out.println("Do you want to refund any order? (yes/no)");
                    String ans = scanner.nextLine();
                    if (ans.equalsIgnoreCase("yes")) {
                        System.out.println("Enter the order ID to refund: ");
                        String orderID2 = scanner.nextLine();
                        Order order1 = Order.getAllOrders().get(orderID2);
                        if (order1 != null) {
                            order1.processRefund();
                        } else {
                            System.out.println("Order not found.");
                        }
                    }
                    break;

                case 9:
                    handleSpecialRequest();
                    break;

                case 10:
                    salesReport.generateSalesReport();
                    break;

                case 11:
                    salesReport.resetSalesData();
                    break;

                case 12:
                    System.out.println("Exiting Admin options...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
