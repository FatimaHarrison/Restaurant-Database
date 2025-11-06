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
    //Setting GUI layouts
    public AuditLogGUI() {
        DMS.loadFromFile(); // Load roles from Data.txt
        setTitle("Manager Dashboard"); //Title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop run
        setSize(500, 550);//Set size
        setLocationRelativeTo(null);//Display pop up
        setContentPane(AuditLogGUI); // Use the field, not a new panel
        setVisible(true);//Display panel
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
                    String role = roleField.getText().trim();
                    String keyword = keywordField.getText().trim();

                    // Query database
                    DefaultTableModel model = db1.searchAuditLogs(startStr, endStr, role, keyword);

                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "No logs found for given filters.");
                        return;
                    }

                    table1.setModel(model);
                    JOptionPane.showMessageDialog(null, "Filtered audit log loaded.");
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Use YYYY/MM/DD.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error loading logs: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

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
        public static void main(String[] args) {
        new AuditLogGUI();
        }

    }



