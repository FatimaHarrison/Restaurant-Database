//Declaring imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.awt.event.*;
import java.io.*;
import DBHelper.auditlog;
/**
 * ManaMenuGUI provides the manager's main dashboard interface for the Terralina restaurant system.
 * It allows managers to load audit logs, display data, remove employee records,
 * and access audit and employee record modules with authorization.
 * <p>
 * This class integrates with the auditlog database and validates access via DMS.
 */
//Declaring class extended to the JFrame
//Declaring GUI layouts
public class ManaMenuGUI extends JFrame {
    //Class attributes
    private JButton Load;//Load sqlite file
    private JButton RemoveData;//remove a data
    private JButton AuditLog;//Custom action
    private JButton EmployeeRecords;//Access staff records
    private JButton Logout;//Close of runtime
    private JButton Display;//Display current data

    private JPanel ManaMenuGUI;//Name of panel
    private JList list1;//Display menu list
    private JTable tableDisplay;//Place to display data
    private JLabel MainMenu;//Label used
    private JLabel options;//Label used
    private JScrollPane Jpanel2;
    private final List<String> logLines = new ArrayList<>();//Retrieve the array list from rows.
    private File loadedFile = null;// replaces 'logFiles'
    auditlog db1 = new auditlog();
    ArrayList<ArrayList<Object>> data = new ArrayList<>();//Retrieves each object from the columns and rows
  //Setting GUI layouts
    /**
     * This portion constructs the ManaMenuGUI window of the GUI Panel.
     * It initializes the layout, sets up button listeners,
     * and prepares the reservation management interface.
     */
    public ManaMenuGUI() {
        DMS.loadFromFile(); // Load roles from Data.txt
        setTitle("Manager Dashboard"); //Title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop run
        setSize(500, 550);//Set size
        setLocationRelativeTo(null);//Disply pop up
        setContentPane(ManaMenuGUI); // Use the field, not a new panel
        setVisible(true);//Display panel
        /**
         * Loads a database file selected by the manager.
         * Reads the file line-by-line and stores its contents in logLines.
         * Displays a success or error message based on the outcome.
         */
        // Action listener for the "Load" button
        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser dialog for selecting a log file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select the database:");//Prompting user to select

                // Show the dialog and capture the result
                int result = fileChooser.showOpenDialog(ManaMenuGUI);
                if (result == JFileChooser.APPROVE_OPTION) { //If file contain .db or .sqlite it's approved
                    // Store the selected file and clear any previously loaded log lines
                    loadedFile = fileChooser.getSelectedFile();
                    logLines.clear();

                    // Reading the file line-by-line and storing each line
                    try (Scanner scanner = new Scanner(loadedFile)) {
                        while (scanner.hasNextLine()) {
                            logLines.add(scanner.nextLine());
                        }
                        // Notify user of successful file load
                        JOptionPane.showMessageDialog(null, "File loaded successfully:\n" + loadedFile.getName());
                    } catch (Exception ex) {
                        // Show error if file reading fails
                        JOptionPane.showMessageDialog(null, "Error reading file:\n" + ex.getMessage());
                    }
                }
            }
        });
        /**
         * Displays audit log data in the CLI and optionally in the GUI table.
         * Queries the auditlog table and prints each row.
         *
         * @throws Exception if data retrieval fails
         */
        // Action listener to display the data
        Display.addActionListener(new ActionListener() {
            @Override //Wired to GUI
            public void actionPerformed(ActionEvent e) { //Action button active listener
                try {
                    // Run query to get all audit log entries
                    // build your WHERE clause here...
                    DefaultTableModel model = db1.selectToTable(null, null, null, "user", "DESC", "");
                    data = db1.getExecuteResult("select * from auditlog");
                    System.out.println("Displayed in CLI");
                    //Method to display the arraylist data.
                    for (List<Object> row : data) {
                        System.out.println(row);
                    }
                    // Check if model has data
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "**Data displayed in CLI**.");
                    }//Notify user if there's an error
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error loading logs: " + ex.getMessage());
                    ex.printStackTrace();//Displays error message
                }
            }
        });
        /**
         * Removes an employee record by ID.
         * Validates input format and deletes the record from the database.
         *
         * @throws Exception if deletion fails or input is invalid
         */
        // Action listener to remove data
        RemoveData.addActionListener(new ActionListener() {
            @Override//Wired to GUI
            public void actionPerformed(ActionEvent e) {
                try {
                    // Prompt user for ID to remove
                    String input = JOptionPane.showInputDialog("Enter staff ID to remove**");
                    if (input == null || !input.matches("\\d{7}")) {
                        JOptionPane.showMessageDialog(null, "Invalid ID format. Must be 7 digits.");
                        return;
                    }
                    //Does not exist.
                    if (input.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "ID has been removed or does not exist.");
                        return;
                    }
                    //Parsing employee id as an integer.
                    int employid = Integer.parseInt(input.trim());
                    // Delete from database
                    db1.delete("employid", String.valueOf((employid)));//deletes data if exists from column
                    JOptionPane.showMessageDialog(null, "**Employee data with ID " + employid + " has been removed**");//Notify user it's removed.

                    // Refresh data and table
                    data = db1.getExecuteResult("SELECT * FROM auditlog");
                    //Setting the default table model
                    // build your WHERE clause here...

                    DefaultTableModel updatedModel = db1.selectToTable(null, null, null, "timestamp", "DESC", "");
                    tableDisplay.setModel(updatedModel);//Display updated table
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error removing data:\n" + ex.getMessage());
                }
            }
        });
        /**
         * Removes an employee record by ID.
         * Validates input format and deletes the record from the database.
         *
         * @throws Exception if deletion fails or input is invalid
         */
        // Action listener for displaying the AuditLog
        AuditLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt for manager ID to authorize access
                String authIdInput = JOptionPane.showInputDialog("Authorized Manager ID:");
                if (authIdInput != null && !authIdInput.trim().isEmpty()) {
                    try {
                        int authId = Integer.parseInt(authIdInput.trim());
                        // Check if ID exists and Manager role
                        if (DMS.validUsers.containsKey(authId) && //If data matches user validation in DMS
                                DMS.validUsers.get(authId).equalsIgnoreCase("Manager")) {
                            //Grants access if passed
                            JOptionPane.showMessageDialog(null, "Access granted.");
                            new AuditLogGUI(); // Launch employee record interface
                        } else { //Denies access if not
                            JOptionPane.showMessageDialog(null, "Access denied. Invalid Manager ID.");
                        }
                    } catch (NumberFormatException ex) {
                        // Handle invalid numeric input
                        JOptionPane.showMessageDialog(null, "Invalid ID format.");
                    }
                }
            }
        });
        /**
         * Opens the EmployeeRecordGUI if the manager ID is authorized.
         * Validates the ID against the DMS user map.
         *
         * @throws NumberFormatException if ID is not numeric
         */
        // Action listener to direct to the EmployeeRecords
        EmployeeRecords.addActionListener(new ActionListener() {
            @Override //Wired GUI
            public void actionPerformed(ActionEvent e) {
                // Prompt for manager ID to authorize access
                String authIdInput = JOptionPane.showInputDialog("Authorized Manager ID:");
                if (authIdInput != null && !authIdInput.trim().isEmpty()) {
                    try {
                        int authId = Integer.parseInt(authIdInput.trim());
                        // Check if ID exists and Manager role
                        if (DMS.validUsers.containsKey(authId) && //If data matches user validation in DMS
                                DMS.validUsers.get(authId).equalsIgnoreCase("Manager")) {
                            //Grants access if passed
                            JOptionPane.showMessageDialog(null, "Access granted.");
                            new EmployeeRecordGUI(); // Launch employee record interface
                        } else { //Denies access if not
                            JOptionPane.showMessageDialog(null, "Access denied. Invalid Manager ID.");
                        }
                    } catch (NumberFormatException ex) {
                        // Handle invalid numeric input
                        JOptionPane.showMessageDialog(null, "Invalid ID format.");
                    }
                }
            }
        });

        /**
         * Terminates the application when the logout button is clicked.
         */
        // Action listener to exit menu
        Logout.addActionListener(new ActionListener() {
            @Override //Action to be performed
            public void actionPerformed(ActionEvent e) {
                // Terminate the application
                System.exit(0);
            }
        });
    }

}

