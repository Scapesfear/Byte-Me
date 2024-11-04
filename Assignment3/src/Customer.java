import java.util.*;

public class Customer extends User{


    private double Wallet;
    private Cart cart;

    private static Map<String, Customer> costumerRepository = new HashMap<>();





    public Customer(String userID, String password) {
        super(userID, password);

        this.Wallet = 5000;
        this.cart = new Cart(userID, false);
        costumerRepository.put(userID, this);

    }

    public void applyForVIP() {
        if (Wallet >= 500) {
            cart.setVIP(true);
            System.out.println("Congratulations! You are now a VIP customer.");
        } else {
            System.out.println("Insufficient funds to apply for VIP status.");
        }
    }

    public static Map<String, Customer> getCostumerRepo(){
        return costumerRepository;
    }

    public boolean deductWallet(double amount) {
        if (this.Wallet >= amount) {
            Wallet -= amount;
            System.out.println("Payment Successful");
            return true;
        } else {
            System.out.println("Insufficient Balance");
            return  false;
        }
    }

    public void refundMoney(double price){
        this.Wallet+=price;
    }

    public Cart getCart(){
        return cart;
    }

    public void menuOptions() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Menu menu = Menu.getInstance();
        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. View Items");
            System.out.println("2. Search Items");
            System.out.println("3. Filter by Category");
            System.out.println("4. Sort by Price");
            System.out.println("5. Review an Item");
            System.out.println("6. View Reviews of an Item");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    menu.viewMenu();
                    break;
                case 2:
                    menu.searchItemByName();
                    break;
                case 3:
                    menu.filterByCategory();
                    break;
                case 4:
                    menu.sortItemsByPrice();
                    break;
                case 5:
                    Item.addOrUpdateReview(this.getUserID());
                    break;
                case 6:
                    Item.displayReviews(this.getUserID());
                    break;
                case 7:
                    System.out.println("Exiting menu...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");

            }
        }
    }

    public void CartOptions(){
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- Cart Options ---");
            System.out.println("1. View Cart");
            System.out.println("2. Add Item to Cart");
            System.out.println("3. Modify Item Quantity");
            System.out.println("4. Remove Item from Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    cart.viewCart();
                    break;
                case 2:
                    System.out.print("Enter the item ID to add to the cart: ");
                    String itemID = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    int quantity = scanner.nextInt();
                    cart.addItem(itemID,quantity);
                    break;
                case 3:
                    System.out.print("Enter the item ID to modify quantity: ");
                    String itemID2 = scanner.nextLine();
                    cart.modifyQuantity(itemID2);
                    break;
                case 4:
                    System.out.print("Enter the item ID to remove from the cart: ");
                    String itemID3 = scanner.nextLine();
                    cart.removeItem(itemID3);
                    break;
                case 5:
                    cart.checkout();
                    break;
                case 6:
                    System.out.println("Exiting cart...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }





    public void viewOrderHistory() {
        Map<String, Order> orderHistory = Order.getOrderHistory().get(this.getUserID());
        if (orderHistory.isEmpty()) {
            System.out.println("No orders found in order history.");
        } else {
            System.out.println("\n--- Order History ---");
            for (Map.Entry<String, Order> entry : orderHistory.entrySet()) {
                System.out.println("Order ID: " + entry.getKey() + " | Total Price: " + entry.getValue().getTotalPrice());
            }
        }
    }


    public void OrderOptions(){
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- Order Options ---");
            System.out.println("1. View Order History");
            System.out.println("2. View Order Details");
            System.out.println("3. Reorder");
            System.out.println("4.Cancel Order");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewOrderHistory();
                    break;
                case 2:
                    viewOrderDetails();
                    break;
                case 3:
                    System.out.println("Enter the Order ID to reorder: ");
                    String orderID2= scanner.nextLine();
                    Order order = Order.getOrderHistory().get(this.getUserID(), orderID2);
                    if (order == null) {
                        System.out.println("Order not found.");
                        break;
                    } else {
                        order.reorder();
                    }
                    break;
                case 4:
                    System.out.println("Enter Order ID to cancel: ");
                    String orderID = scanner.nextLine();
                    Order order1 = Order.getOrderHistory().get(this.getUserID(), orderID);
                    if (order1 == null) {
                        System.out.println("Order not found.");
                        break;
                    }else {
                        order1.cancelOrder();
                    }
                    break;
                case 5:
                    System.out.println("Exiting order options...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void viewOrderDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Order ID to view details: ");
        String orderID = scanner.nextLine();

        if (Order.getOrderHistory().containsKeys(this.getUserID(),orderID)) {
           Order order = Order.getOrderHistory().get(this.getUserID(), orderID);
           order.viewOrderDetails();
        } else {
            System.out.println("Order not found.");
        }
    }

    public void CustomerOptions(){
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- Customer Options ---");
            System.out.println("1. View Menu");
            System.out.println("2. Cart Options");
            System.out.println("3. Order Options");
            System.out.println("4. Apply for VIP");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    menuOptions();
                    break;
                case 2:
                    CartOptions();
                    break;
                case 3:
                    OrderOptions();
                    break;
                case 4:
                    applyForVIP();
                    break;
                case 5:
                    System.out.println("Exiting customer options...");
                    logout();
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }





}
