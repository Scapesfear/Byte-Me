import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AuthPage.prepareData();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("BBBBBBBBB     Y       Y     TTTTTTTTTT     EEEEEEEEE               M       M     EEEEEEEEE ");
            System.out.println("B       B      Y     Y          T          E                      M M     M  M   E         ");
            System.out.println("B       B       Y   Y           T          E            -----    M   M   M   M   E         ");
            System.out.println("BBBBBBBBB         Y             T          EEEEEEEEE    -----   M    M  M    M   EEEEEEEE  ");
            System.out.println("B       B         Y             T          E            -----   M     M      M   E         ");
            System.out.println("B       B         Y             T          E                    M            M   E         ");
            System.out.println("BBBBBBBBB         Y             T          EEEEEEEEE            M            M   EEEEEEEEE ");
            System.out.println(" ");
            System.out.println("1. Enter the application");
            System.out.println("2. Exit the application,Everything will be lost");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                AuthPage.authMenu();
            } else if (choice == 2) {
                running = false;
                System.out.println("Exiting the program. Goodbye!");
            } else {
                System.out.println("Invalid option. Please choose again.");
            }
        }

        scanner.close();
    }
}
