//GUI and database format imports
import DBHelper.employeeRecords;
//GUI panel setups
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//File loader
import java.io.File;
//formatters
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//Table lists
import java.util.ArrayList;
import java.util.List;
//File scanner
import java.util.Scanner;

//Declaring primary class
public class EmployeeRecordGUI extends JFrame {
    //Declaring class attributes
    //Declaring GUI layout functions
    private JList list3; //Display menu
    private JButton loadButton;//Load file
    private JButton addNewInfoButton;//Create new reservation
    private JButton deleteEmployeeButton;//remove reservation
    private JButton displyRecordsButton;//Display reservation
    private JButton mainMenuButton;//going back to ManaMenuGUI
    private JPanel EmployeeRecordGUI;//Name of panel
    private JPanel Jpanel5;
    private JTable tableDis;//Displaying area
    private JScrollPane FSN;
    private final List<String> logLines = new ArrayList<>();
    //file loader
    private File loadedFile = null;
    //To get a specific file (employee records)
    private final List<EmployeeRecord> employeeRecords = new ArrayList<>();
    employeeRecords db3 = new employeeRecords();//Connection to query setups
    ArrayList<ArrayList<Object>> data3 = new ArrayList<>();//Table ros and columns.

    //GUI setters
    public EmployeeRecordGUI() {
        setTitle("Record Dashboard");//Title
        setContentPane(EmployeeRecordGUI);//Set content to display layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop run
        setSize(500, 550);//Set size
        setVisible(true);//Display panel
        setLocationRelativeTo(null);//Location of popup menu

        //Load employee records from file
        loadButton.addActionListener(e -> {
            // Open a file chooser dialog for selecting a log file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select the database:");//Prompting user to select

            // Show the dialog and capture the result
            int result = fileChooser.showOpenDialog(EmployeeRecordGUI);
            if (result == JFileChooser.APPROVE_OPTION) { //If file contain .db or .sqlite it's approved
                // Store the selected file and clear any previously loaded log lines
                loadedFile = fileChooser.getSelectedFile();//Getting the selected file
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
        });

        // Add new employee and save into the file.
        // Add new employee and save into the file.
        addNewInfoButton.addActionListener(e -> {
            if (loadedFile == null) { //error message
                JOptionPane.showMessageDialog(null, "No file loaded.");
                return;
            }
            try {//Prompting user to insert new employee info.
                String idInput = JOptionPane.showInputDialog("Enter New Employee ID:");
                if (idInput == null || !idInput.matches("\\d{7}")) { //Must be 7 digits
                    JOptionPane.showMessageDialog(null, "Employee ID must be 7 digits."); //Notify user invalid input
                    return;
                }
                //Duplication checker
                int employId = Integer.parseInt(idInput.trim());
                //Shows invalid entry for existing ID number
                boolean exists = employeeRecords.stream().anyMatch(emp -> emp.getEmployID() == employId);
                if (exists) { //Notify user if ID is already in the system.
                    JOptionPane.showMessageDialog(null, "Employee record already exists.");
                    return;
                }
                //Prompts user to input other data.
                String name = JOptionPane.showInputDialog("Enter Employee Full Name:");//New name
                String gender = JOptionPane.showInputDialog("Enter Employee Gender:");
                String role = JOptionPane.showInputDialog("Enter Employee Role:");//etc..
                String status = JOptionPane.showInputDialog("Enter Working Status:");//etc..
                String hireDate = JOptionPane.showInputDialog("Enter Hire Date (yyyy/MM/dd):");

                //Validates employee status
                //Must choose between the two
                if (status == null || (!status.equalsIgnoreCase("FullTime") && !status.equalsIgnoreCase("PartTime"))) {
                    JOptionPane.showMessageDialog(null, "Status must be 'FullTime' or 'PartTime'."); //Error message
                    return;
                }
                //Name validation
                if (name == null || name.trim().length() > 25) {
                    JOptionPane.showMessageDialog(null, "Name must be 25 characters or fewer.");
                    return;
                }
                //Goes over user input
                // Create new employee record object
                EmployeeRecord newEmp = new EmployeeRecord(employId, name.trim(), gender.trim(), role.trim(), status.trim(), hireDate.trim());
                // Insert into database
                db3.insert(employId, name.trim(), gender.trim(), role.trim(), status.trim(), hireDate.trim());
                // Show confirmation
                JOptionPane.showMessageDialog(null, "**Employee Added**\n" + newEmp);
                db3.getExecuteResult("SELECT * FROM employeeRecords");
                // Refresh table display
                DefaultTableModel updatedModel = db3.selectToTable(null, null, null, "Gender", "DESC", "");
                tableDis.setModel(updatedModel);

            } catch (Exception ex) {//Gets error message if failed.
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // Delete employee and save
        deleteEmployeeButton.addActionListener(e -> {
            try {
                // Prompt user for ID to remove
                String input = JOptionPane.showInputDialog("Enter staff ID to remove**");
                if (input == null || !input.matches("\\d{7}")) {//Digit validation
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
                db3.delete("employeeId", String.valueOf((employid)));//deletes data if exists from column
                JOptionPane.showMessageDialog(null, "**Employee  " + employid + " has been removed**");//Notify user it's removed.

                // Refresh data and table
                data3 = db3.getExecuteResult("SELECT * FROM employeeRecords");
                //Setting the default table model
                // build your WHERE clause here...
                //To refresh and display removed employee
                DefaultTableModel updatedModel = db3.selectToTable(null, null, null, "Status", "DESC", "");
                tableDis.setModel(updatedModel);//Display updated table
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error removing data:\n" + ex.getMessage());
            }

        });
        // Display current employee records
        displyRecordsButton.addActionListener(e -> {
            try {
                // Running query to get all audit log entries
                DefaultTableModel model = db3.selectToTable(null, null, null, "Status", "DESC", "");
                data3 = db3.getExecuteResult("select * from employeeRecords");
                //Method to display the arraylist data.
                System.out.println("**Employee records**");
                for (List<Object> row : data3) {
                    System.out.println(row);
                }
                // Displayed on CLI
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "**Data displayed in CLI**.");
                }//Notify user if there's an error
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error loading logs: " + ex.getMessage());
                ex.printStackTrace();//Displays error message
            }
        });
        // Return to manager menu
        mainMenuButton.addActionListener(e -> {
            //Notify user of action
            JOptionPane.showMessageDialog(null, "Returning to Manager Dashboard...");
            new ManaMenuGUI();
            dispose();
        });
    }
}





