
import java.util.Scanner;

public class AuthPage {


    private static Scanner scanner = new Scanner(System.in);

    private AuthPage() {

    }



    public static void authMenu() {
        System.out.println("Welcome to the Auth Page");
        System.out.println("1. Sign In");
        System.out.println("2. Sign Up");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                loginMenu();  // Switch this to the login menu for signing in
                break;
            case 2:
                signUp();  // Switch this to sign-up functionality
                break;
            case 3:
                System.out.println("Returning to the main menu...");
                break;
            default:
                System.out.println("Invalid option. Try again.");
                authMenu();
                break;
        }
    }


    private static void signUp() {
        System.out.println("Please enter your user type: 1.Customer  2.Back");

        int userType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (userType) {
            case 1:
                customerSignUp();
                break;
            case 2:
                authMenu();
                break;
            default:
                System.out.println("Invalid user type.");
                signUp();
                break;
        }
    }

    private static void customerSignUp() {
        System.out.print("Enter Customer ID: ");
        String email = scanner.nextLine();
        System.out.print("Enter Customer Password: ");
        String password = scanner.nextLine();

        if (Customer.getCostumerRepo().containsKey(email)) {
            System.out.println("Customer already exists. Please login.");
            loginMenu();
        } else {
            Customer customer = new Customer(email, password);
            System.out.println("Customer created successfully.");
            customer.CustomerOptions();
        }
    }

    public static void loginMenu() {
        System.out.println("Please enter your user type: 1.Customer 2.Admin  3.Back");

        int userType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (userType) {
            case 1:
                customerLogin();
                break;
            case 2:
                adminLogin();
                break;
            case 3:
                authMenu();
                break;
            default:
                System.out.println("Invalid user type.");
                loginMenu();
                break;
        }
    }

    private static void customerLogin() {
        System.out.print("Enter Customer ID: ");
        String email = scanner.nextLine();
        System.out.print("Enter Customer Password: ");
        String password = scanner.nextLine();

        Customer customer = Customer.getCostumerRepo().get(email);
        if (customer != null && customer.getPassword().equals(password)) {
            System.out.println("Login successful");
            customer.CustomerOptions();
        } else {
            System.out.println("Invalid email or password. Please try again.");
            customerLogin();
        }
    }

    private static void adminLogin() {
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        if (password.equals("admin123")) {
            System.out.println("Login successful");
            Admin.getInstance().AdminOptions();
        } else {
            System.out.println("Invalid password. Please try again.");
            adminLogin();
        }

    }
    public static void prepareData() {
        new Customer("customer1", "password1");
        new Customer("customer2", "password2");
        new Customer("customer3", "password3");

        new Item("1", "pizza", 200.0, "Snacks");
        new Item("2", "burger", 100.0, "Snacks");
        new Item("3", "coffee", 150.0, "beverages");
    }




}
