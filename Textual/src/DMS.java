//Importing GUI functions
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;
//Declaring primary class
public class DMS {
    private static final String DEFAULT_DATA_PATH = "Terralina GUI/Textual/Data"; // GUI-safe default path
    public static Map<Integer, String> validUsers = new HashMap<>();//User mapping validation setup

    //Load user roles from file
    public static void loadFromFile() {
        validUsers.clear();//Retrieves data from file
        File dataFile = new File(DEFAULT_DATA_PATH);
        //If data file is not found it notify user.
        if (!dataFile.exists()) {
            JOptionPane.showMessageDialog(null, "Login file not found: " + DEFAULT_DATA_PATH);
            return;
        } //File scanner
        try (Scanner scanner = new Scanner(dataFile)) {
            while (scanner.hasNextLine()) { //Scaans through lines
                String line = scanner.nextLine().trim();
                String[] parts = line.split("-", 3); //Separator along with last element of the rest of the string.
               //If array index match 7 digits vaildation.
                if (parts.length >= 2 && parts[0].matches("\\d{7}")) {
                    int id = Integer.parseInt(parts[0]);
                    String role = parts[1].trim(); // If array index matches role
                    validUsers.put(id, role); //Validations
                }
            }
        } catch (Exception e) { //Otherwise gives an error.
            JOptionPane.showMessageDialog(null, "Error loading login file:\n" + e.getMessage());
        }
    }
    //  Validate login for GUI use
    public static boolean validateLogin(int id, String role) {
        return validUsers.containsKey(id) && //Stores in the id and role for login.
                validUsers.get(id).equalsIgnoreCase(role);
    }
    //GUI-prompt
    public static boolean promptLogin(String expectedRole) {
        //Prompts user for input.
        String input = JOptionPane.showInputDialog("Enter your Authorized " + expectedRole + " ID:");
        if (input == null || !input.matches("\\d{7}")) {// Gives error message if not meet.
            JOptionPane.showMessageDialog(null, "Invalid ID format. Must be 7 digits.");
            return false;
        }
        //If both role and id passed access is granted
        int id = Integer.parseInt(input.trim());
        if (validateLogin(id, expectedRole)) {
            JOptionPane.showMessageDialog(null, "Access granted.");
            return true;
        } else {//Otherwise it's denied.
            JOptionPane.showMessageDialog(null, "Access denied. Invalid " + expectedRole + " ID.");
            return false;
        }
    }

    public static void insertauditLog(int employeeId, String role, String login, String userSuccessfullyAuthenticated) {
    }
}

