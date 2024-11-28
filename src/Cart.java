import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cart {
    private Map<Item, Integer> cartItems; // Map of Items with their quantities
    private Map<String, Customer> CostumerRepo = Customer.getCustomerRepo();
    private double totalPrice;
    private String customerID;
    private boolean isVIP;
    private Map<String, Item> items = Item.getItemRepository();
    public static int orderCounter = 1;

    public Cart(String customerID, boolean isVIP) {
        this.cartItems = new HashMap<>();
        this.totalPrice = 0.0;
        this.customerID = customerID;
        this.isVIP = isVIP;
    }


    public void addItem(String itemID,int quantity) {



        Item item = items.get(itemID); // Assuming `items` is a Map<String, Item> with available items

        if (item != null && item.isAvailable()) {
            cartItems.put(item, cartItems.getOrDefault(item, 0) + quantity);
            calculateTotal();
            System.out.println(quantity + " x " + item.getName() + " added to cart.");
            viewCart();
        } else {
            System.out.println("Item not found or is unavailable.");
        }
    }



    public void modifyQuantity(String itemID, int newQuantity)  {
        Item item = items.get(itemID);

        if (item != null && cartItems.containsKey(item)) {
            if (newQuantity < 0) {
                System.out.println("Invalid quantity.");
                return;
            }
            if (newQuantity == 0) {
                cartItems.remove(item);
            } else {
                cartItems.put(item, newQuantity);
            }
            calculateTotal();
            viewCart();
        } else {
            System.out.println("Item not found in the cart.");
        }
    }



    public void removeItem(String itemID) {

        Item item = items.get(itemID);

        if (item != null && cartItems.containsKey(item)) {
            cartItems.remove(item);
            calculateTotal();
            System.out.println(item.getName() + " removed from cart.");
            viewCart();
        } else {
            System.out.println("Item not found in the cart.");
        }
    }

    private void calculateTotal() {
        totalPrice = 0.0;
        for (Map.Entry<Item, Integer> entry : cartItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
        }
    }


    public void viewCart() {
        System.out.println("Your Cart:");
        for (Map.Entry<Item, Integer> entry : cartItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(item.getName() + " - Quantity: " + quantity + " - Price: $" + item.getPrice() * quantity);
        }
        System.out.println("Total Price: $" + totalPrice);
    }


    public void checkout() {
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty. Add items before checkout.");
            return;
        }

        Customer user= CostumerRepo.get(customerID);
        if (user.deductWallet(this.getTotalPrice())){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter any special requests for your order (or leave blank if none): ");
            String specialRequest = scanner.nextLine();
            System.out.print("Enter delivery address: ");
            String deliveryAddress = scanner.nextLine();
            String orderID = "ORD" + (System.currentTimeMillis() % 1000)  + orderCounter++;
            Order newOrder = new Order(orderID, customerID, new HashMap<>(cartItems), totalPrice, isVIP, specialRequest, deliveryAddress);



//            if (!specialRequest.isEmpty()) {
//                newOrder.addSpecialRequest(specialRequest);
//            }

            SalesReport.getInstance().addOrder(newOrder);

            clearCart();

            System.out.println("Order " + orderID + " placed successfully!");
        }

        else{
            System.out.println("Order Unsuccessful");
        }


    }


    private void clearCart() {
        cartItems.clear();
        totalPrice = 0.0;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setVIP(boolean b) {
        isVIP = b;
    }

    public boolean getVIP(){
        return isVIP;
    }
}
