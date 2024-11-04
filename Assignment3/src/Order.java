import java.util.*;

public class Order {
    private String orderID;
    private String customerID;
    private Map<Item, Integer> items;
    private double totalPrice;
    private String specialRequests;
    private String deliveryAddress;
    private String status;
    private boolean isVIP;
    private long timestamp;
    private Map<String, Customer> customerRepo = Customer.getCostumerRepo();
    private static Map<String, Pair<Order,String>> CanclledOrders= new HashMap<>();

    private static Map<String,Order> allOrders= new HashMap<>();
    private static PriorityQueue<Order> pendingOrders = new PriorityQueue<>(
            Comparator.comparing(Order::isVIP).reversed().thenComparing(Order::getTimestamp)
    );

    // Complex map to store order history by customer and order ID
    public static BiHashMap<String,String,Order> orderHistory = new BiHashMap<String,String,Order>();

    public Order(String orderID, String customerID, Map<Item, Integer> items, double totalPrice, boolean isVIP) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.items = new HashMap<>(items);
        this.totalPrice = totalPrice;
        this.status = "received";
        this.isVIP = isVIP;
        this.timestamp = System.currentTimeMillis();

        // Add order to customer-specific order history
        orderHistory.put(customerID,orderID,this);

        // Add order to pending orders queue
        pendingOrders.add(this);

        allOrders.put(orderID,this);
    }

    public static Map<String,Pair<Order,String>> getCanclledOrders(){
        return CanclledOrders;
    }

    public String getOrderID() { return orderID; }
    public String getCustomerID() { return customerID; }
    public Map<Item, Integer> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public boolean isVIP() { return isVIP; }
    public long getTimestamp() { return timestamp; }
    public static Map<String,Order> getAllOrders() { return allOrders;}
    public void viewSpecialRequests() {
        if (specialRequests != null && !specialRequests.isEmpty()) {
            System.out.println("Special Requests for Order " + orderID + ": " + specialRequests);
        } else {
            System.out.println("No special requests for Order " + orderID + ".");
        }
    }

    public static BiHashMap<String,String,Order> getOrderHistory() {
        return orderHistory;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        System.out.println("Delivery address updated: " + deliveryAddress);
    }

    public void cancelOrder() {
        if (!status.equals("out for delivery") && !status.equals("preparing") && !status.equals("denied") && !status.equals("cancelled")&& !status.equals("refunded")) {
            if(status.equals("recieved")){
                this.updateStatus("cancelled");
                processRefund();
            }
            else {
            Scanner scanner= new Scanner(System.in);
            System.out.println("Enter the reason for cancellation: ");
            String reason= scanner.nextLine();
            this.updateStatus("cancelled");
            CanclledOrders.put(this.orderID,new Pair<>(this,reason));}
            System.out.println("Order " + orderID + " has been canceled.");
        } else {
            System.out.println("Order " + orderID + " cannot be canceled .");
        }
    }

    public void addSpecialRequest(String request) {
        this.specialRequests = request;
        System.out.println("Special request added: " + request);
    }

    public void viewOrderDetails() {
        System.out.println("Order ID: " + orderID);
        System.out.println("Customer ID: " + customerID);
        System.out.println("VIP Status: " + (isVIP ? "Yes" : "No"));
        System.out.println("Order Status: " + status);
        System.out.println("Timestamp: " + new Date(timestamp));
        System.out.println("Total Price: $" + totalPrice);
        System.out.println("Delivery Address: " + (deliveryAddress != null ? deliveryAddress : "Not provided"));
        System.out.println("Special Requests: " + (specialRequests != null ? specialRequests : "None"));
        System.out.println("Items Ordered:");
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            System.out.println(entry.getKey().getName() + " x" + entry.getValue());
        }
    }



    public static List<Order> getPendingOrders() {
        return new ArrayList<>(pendingOrders);
    }

    public static PriorityQueue<Order> getPendingOrdersQueue() {
        return pendingOrders;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Order: " + this.orderID + " status updated to: " + newStatus);

        if (newStatus.equals("completed") ||  newStatus.equals("denied") || newStatus.equals("cancelled")) {
            pendingOrders.remove(this);
        }
    }

    public void processRefund() {
        if (status.equals("cancelled") || status.equals("denied")) {
            customerRepo.get(customerID).refundMoney(totalPrice);
            SalesReport.getInstance().processRefund(orderID);
            this.updateStatus("refunded");
            System.out.println("Refund processed for Order " + orderID + ".");

        } else {
            System.out.println("Order " + orderID + " is not eligible for a refund.");
        }
    }

    public  void reorder(){
        Customer user= Customer.getCostumerRepo().get(customerID);
        if(user.deductWallet(totalPrice)){
            String OrderID2=   "ORD" + (System.currentTimeMillis() % 1000)  + Cart.orderCounter++;
            boolean isVIP2= user.getCart().getVIP();
            Order newOrder= new Order(OrderID2,customerID,items,totalPrice,isVIP2);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter any special requests for your order (or leave blank if none): ");
            String specialRequest = scanner.nextLine();
            System.out.print("Enter delivery address: ");
            String deliveryAddress = scanner.nextLine();
            newOrder.setDeliveryAddress(deliveryAddress);

            if (!specialRequest.isEmpty()) {
                newOrder.addSpecialRequest(specialRequest);
            }

            SalesReport.getInstance().addOrder(newOrder);
            System.out.println("Order " + orderID + " has been reordered.");
        }
        else{
            System.out.println("Insufficient funds to reorder.");
        }

    }
}
