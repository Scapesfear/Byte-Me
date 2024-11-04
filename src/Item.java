import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Item {
    private String itemID;
    private String name;
    private double price;
    private String type;
    private boolean isAvailable;
    private HashMap<String, String> reviews;  // Key: User ID, Value: Review
    private int itemcounter ;
    private static Map<String, Item> itemRepository = new HashMap<>();

    public Item( String itemID,String name, double price, String type) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.type = type;
        this.isAvailable = true;
        this.reviews = new HashMap<>();

        itemRepository.put(itemID, this);
    }

    public static Map<String, Item> getItemRepository() {
        return itemRepository;
    }

    public String getItemID() { return itemID; }
    public void setItemID(String itemID) { this.itemID = itemID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    public static void addOrUpdateReview(String userID) {
        Scanner scanner = new Scanner(System.in);

        // Search for item
        System.out.print("Enter the item ID to leave a review for: ");
        String itemID = scanner.nextLine();

        Item item = itemRepository.get(itemID); // Assumes itemRepository is a static map

        if (item == null) {
            System.out.println("Item not found. Please check the ID and try again.");
            return;
        }

        System.out.println("Item found: " + item.getName());

        // Gather review details


        System.out.print("Enter your review: ");
        String review = scanner.nextLine();

        item.reviews.put(userID, review);
        System.out.println("Thank you! Your review for " + item.getName() + " has been added/updated.");
    }


    public static void displayReviews(String userID) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Enter the item ID to view reviews: ");
        String itemID = scanner.nextLine();

        Item item = itemRepository.get(itemID);

        if (item == null) {
            System.out.println("Item not found. Please check the ID and try again.");
            return;
        }

        System.out.println("Reviews for " + item.getName() + " (" + item.getType() + "):");




        if (item.getReviews().containsKey(userID)) {
            System.out.println("Your Review: " + item.getReviews().get(userID));
        } else {
            System.out.println("You have not left a review yet.");
        }

        System.out.println("Other Reviews:");
        for (var entry : item.getReviews().entrySet()) {
            if (!entry.getKey().equals(userID)) {
                System.out.println("User " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public HashMap<String, String> getReviews() {
        return reviews;
    }

    public static void addItem(String itemID, String name, double price, String type) {
        if (!itemRepository.containsKey(itemID)) {
            Item newItem = new Item(itemID, name, price, type);
            System.out.println("New item " + name + " added to the repository.");
        } else {
            System.out.println("Item with ID " + itemID + " already exists.");
        }
    }






}
