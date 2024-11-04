import java.util.HashMap;
import java.util.Map;

public class User {
    private String userID;
    private String password;
    private static Map<String, User> userRepo = new HashMap<>();
    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
        userRepo.put(userID, this);
    }

    public static Map<String, User> getUserRepo(){
        return userRepo;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void viewProfile() {
        System.out.println("User ID: " + userID);
    }

    public void changePassword(String newPassword) {
        password = newPassword;
    }

    public void login(String userID, String password) {
        if (this.userID.equals(userID) && this.password.equals(password)) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    public void logout() {
        System.out.println("Logged out.");
        AuthPage.loginMenu();
    }

}
