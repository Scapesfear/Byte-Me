import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;

public class Menu {

    private static Menu instance;
    private Map<String, Item> items = Item.getItemRepository();


    private Menu() {}

    public static Menu getInstance() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }



    public static ObservableList<Item> getAvailableMenuItems() {


        return FXCollections.observableArrayList(Item.getItemRepository().values());
    }

    public void addNewItem(String itemID, String name, double price, String type) {
        Item.addItem(itemID, name, price, type);
    }


    public void viewPreviousItems() {
        System.out.println("Items not available in menu:");
        for (Item item : items.values()) {
            if (!item.isAvailable()) {
                System.out.println(item.getItemID() + ": " + item.getName() + " - " + item.getType() + " - $" + item.getPrice());
            }
        }
    }


    public void updateItem(String itemID,String name, Double price, String type) {
        Item item = items.get(itemID);
        if (item != null) {
            if (price != null) item.setPrice(price);
            if (name != null) item.setName(name);
            if (type != null) item.setType(type);
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }


    public  void removeItem(String itemID) {
        Item item = items.get(itemID);
        if (item != null) {
            item.setAvailable(false);
            System.out.println("Item " + item.getName() + " has been removed from the menu.");
            updateOrdersContainingItem(item);
        } else {
            System.out.println("Item with ID " + itemID + " not found.");
        }
    }

    private static void updateOrdersContainingItem(Item removedItem) {
        for (Order order : Order.getPendingOrdersSet()) {
            if (order.getItems().containsKey(removedItem)) {
                order.updateStatus("denied");

                order.processRefund();
                System.out.println("Order " + order.getOrderID() + " containing removed item "
                        + removedItem.getName() + " has been denied and refunded.");
            }
        }
    }

    public  void reactivateItem(String itemID) {
        Item item = Item.getItemRepository().get(itemID);
        if (item != null) {
            item.setAvailable(true);
            System.out.println("Item " + item.getName() + " is now available in the menu.");
        } else {
            System.out.println("Item with ID " + itemID + " not found.");
        }
    }


    public void viewMenu() {
        // Print menu items from the internal data structure
        System.out.println("Menu:");
        for (Item item : items.values()) {
            if (item.isAvailable()) {
                System.out.println("Item ID: " + item.getItemID() + ", Name: " + item.getName()
                        + ", Type: " + item.getType() + ", Price: $" + item.getPrice());
            }
        }

        // Print items from the observable list
        System.out.println("Items in ObservableList:");
        for (Item item : Menu.getAvailableMenuItems()) {
            System.out.println("Item ID: " + item.getItemID() + ", Name: " + item.getName());
        }

        // Launch the GUI
        SwingUtilities.invokeLater(() -> {
            MenuGUI gui = new MenuGUI();
            gui.setVisible(true);
        });
    }

    public void searchItemByName() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the item to search: ");
        String keyword = scanner.nextLine();

        System.out.println("Search Results for \"" + keyword + "\":");
        boolean found = false;

        for (Item item : items.values()) {
            if (item.isAvailable() && item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(item.getItemID() + ": " + item.getName() + " - $" + item.getPrice());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No available items found matching the keyword \"" + keyword + "\".");
        }
    }


    public void filterByCategory() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the category to filter by: ");
        String category = scanner.nextLine();

        System.out.println("Items in Category: " + category);
        boolean found = false;

        for (Item item : items.values()) {
            if (item.isAvailable() && item.getType().equalsIgnoreCase(category)) {
                System.out.println(item.getItemID() + ": " + item.getName() + " - $" + item.getPrice());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No available items found in the category \"" + category + "\".");
        }
    }


    public void sortItemsByPrice() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter '1' to sort by price (Low to High) or '2' to sort by price (High to Low): ");
        int choice = scanner.nextInt();
        boolean ascending = choice == 1;

        List<Item> itemList = new ArrayList<>(items.values());
        itemList.removeIf(item -> !item.isAvailable());

        itemList.sort((a, b) -> ascending ? Double.compare(a.getPrice(), b.getPrice()) : Double.compare(b.getPrice(), a.getPrice()));

        System.out.println(ascending ? "Menu Sorted by Price (Low to High):" : "Menu Sorted by Price (High to Low):");
        for (Item item : itemList) {
            System.out.println(item.getItemID() + ": " + item.getName() + " - $" + item.getPrice());
        }
    }


}
