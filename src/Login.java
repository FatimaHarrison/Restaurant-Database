import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//Declaring primary class
public class Login {
    //Static method from validUser on LoginGUI file.
    public static Map<Integer, String> validUsers = new HashMap<>();
//Login file scanner retrieve information fro data file.
    /**
     * The {@code Login} class is responsible for loading and validating user credentials
     * from a local data file. It populates a static map of valid user IDs and roles,
     * which can be referenced by GUI components or authentication workflows.
     */
    public Login() {
        /**
         * Stores valid user ID and role mappings.
         * <p>
         * This map is populated from a file and used for login validation in the GUI.
         * Keys are 7-digit employee IDs, and values are role names (e.g., "Manager", "Host").
         */
        //Setting file path
        /**
         * Constructs a {@code Login} instance and loads user credentials from a file.
         * <p>
         * The file is expected to contain lines in the format: {@code 1234567-RoleName}.
         * Only entries with a valid numeric ID and non-empty role are stored.
         * If the file is missing or unreadable, an error message is printed to the console.
         */
        File dataFile = new File("Terralina GUI/Textual/Data"); // Adjust path if needed
        try (Scanner scanner = new Scanner(dataFile)) {
            while (scanner.hasNextLine()) { //Scans over file
                String line = scanner.nextLine().trim();
                String[] parts = line.split("-"); //Line splitter
                //Extracts only the first to arrays
                if (parts.length >= 2) {
                    try { //Only gets the ID and name from file
                        int id = Integer.parseInt(parts[0].trim());
                        String role = parts[1].trim();
                        validUsers.put(id, role);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }//Shows error message.
        } catch (Exception e) {
            System.out.println("Error loading manager data: " + e.getMessage());
        }
    }

}
