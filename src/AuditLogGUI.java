//Declaring GUI and database imports
import DBHelper.auditlog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * AuditLogGUI provides the manager interface for viewing and filtering audit logs.
 * It allows managers to load audit data from a file, filter logs by date, role, and keyword,
 * and display results in a JTable. It also supports navigation back to the main dashboard.
 * <p>
 * This class integrates with the {@code auditlog} database helper and validates access via {@code DMS}.
 */
//Declaring primary class to JFrame
public class AuditLogGUI extends JFrame {
//Setting class attributes
    private JPanel AuditLogGUI;//Name of panel
    private JLabel AuditLog;//Label used
    private JTextField startField;//Data start field
    private JTextField keywordField;//Searched keyword field
    private JTextField endField;//End data field
    private JTextField roleField;//Searched role field
    private JLabel startDate;//Used label
    private JLabel endDate;//Used label
    private JLabel role;//used label
    private JLabel keyword;//Used label
    private JButton mainMenuButton;//ManaMenuGUI relocation
    private JButton displaybtn;//Button to display
    private JTable table1;//Displaying area
    private JButton loadButton;
    private final List<String> logLines = new ArrayList<>();
    //Connection to auditlog queries
    auditlog db1 = new auditlog();
    ArrayList<ArrayList<Object>> data = new ArrayList<>();//Retrieves each object from the columns and rows
    /**
     * This portion constructs the AuditLogGUI window of the GUI Panel.
     * It initializes the layout, sets up button listeners,
     * and prepares the reservation management interface.
     */
    //Setting GUI layouts
    public AuditLogGUI() {

        setTitle("Manager Dashboard"); //Title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop run
        setSize(500, 550);//Set size
        setLocationRelativeTo(null);//Display pop up
        setContentPane(AuditLogGUI); // Use the field, not a new panel
        setVisible(true);//Display panel
        /**
         * Displays audit log data in the CLI and optionally in the GUI table.
         * Queries the auditlog table and prints each row.
         * Filters and displays audit logs based on user input.
         * Validates date range (must be within last 30 days), formats inputs,
         * and queries the database using role and keyword filters.
         *
         * @throws DateTimeParseException if date format is invalid
         * @throws Exception if database query fails
         */
        //Data displaying btn
        displaybtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Parse date inputs
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");//Date formatter must match
                    LocalDate startLocal = LocalDate.parse(startField.getText().trim(), formatter);//Gets the start date from user
                    LocalDate endLocal = LocalDate.parse(endField.getText().trim(), formatter);//Gets the end date from user.
                    LocalDateTime startDate = startLocal.atTime(11, 0);//Uses 11:00 am
                    LocalDateTime endDate = endLocal.atTime(23, 59);//Uses 11:59 pm

                    // Validate 30-day range
                    LocalDateTime now = LocalDateTime.now(); //Uses current date
                    LocalDateTime thirtyDaysAgo = now.minusDays(30); //And 30 days from the current date
                    if (startDate.isBefore(thirtyDaysAgo) || endDate.isAfter(now) || startDate.isAfter(endDate)) {
                        JOptionPane.showMessageDialog(null, "Date range must be within the last 30 days.");//error message
                        return;//Returns to JPanel
                    }
                    // Format for SQLite string comparison
                    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");
                    String startStr = startDate.format(dbFormat);
                    String endStr = endDate.format(dbFormat);
                    // Get filters
                    String role = roleField.getText().trim();//Searches for user role.
                    String keyword = keywordField.getText().trim();//Searches for user name, id, and other keyword fields.
                    // Query database
                    DefaultTableModel model = db1.searchAuditLogs(startStr, endStr, role, keyword);
                    //Notify user if nothing was found.
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "No logs found for given filters.");
                        return;
                    }
                    //Other notifications to notify the user is the log was found or not.
                    table1.setModel(model);
                    JOptionPane.showMessageDialog(null, "Filtered audit log loaded.");
                } catch (DateTimeParseException ex) { //Gives the user the giving format date.
                    JOptionPane.showMessageDialog(null, "Invalid date format. Use YYYY/MM/DD.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error loading logs: " + ex.getMessage());
                    ex.printStackTrace();//Print traces the message
               }
            }
        });
        /**
         * Navigates back to the Manager Dashboard.
         * Disposes the current window and opens {@code ManaMenuGUI}.
         */
        //Btn to go back to ManaMenuGUI
        mainMenuButton.addActionListener(new ActionListener() {
            @Override//Wired GUI
            public void actionPerformed(ActionEvent e) {
                //Notify user of action.
                JOptionPane.showMessageDialog(null, "Returning to Manager Dashboard...");
                new ManaMenuGUI();
                dispose();
            }
        });
        /**
         * Loads audit log data from a selected file.
         * Opens a file chooser, reads the file line-by-line, and stores each line in {@code logLines}.
         *
         * @throws Exception if file reading fails
         */
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser dialog for selecting a log file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select the database:");//Prompting user to select

                // Show the dialog and capture the result
                int result = fileChooser.showOpenDialog(AuditLogGUI);
                if (result == JFileChooser.APPROVE_OPTION) { //If file contain .db or .sqlite it's approved
                    // Store the selected file and clear any previously loaded log lines
                    File loadedFile = fileChooser.getSelectedFile();//Getting the selected file
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
    }

        }





