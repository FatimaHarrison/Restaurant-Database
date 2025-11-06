//Importing GUI and databases functions
import DBHelper.Reservation; //assigned DBHelper form location
//GUi panel functions
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//File reader
import java.io.File;
//Table array list
import java.util.ArrayList;
import java.util.List;
//File and arraylist scanner
import java.util.Scanner;
//Declaring primary class extend to JTrame
public class HostMenuGUI extends JFrame {
    // GUI layout components
    private JPanel HostMenuGUI;//Name of panel
    private JButton loadDataButton, displayDataButton, newReservationButton, cancelReservationButton, updateReservationButton, logoutButton; //Active btns
    private JTable displayTable; //Displaying table
    private JList<String> list1;  //Menu list
    private JPanel Jpanel2;

    // Data and database
    private File loadedFile = null;
    private final List<Record> records = new ArrayList<>();
    private final List<String> logLines = new ArrayList<>();
    private final Reservation reservation = new Reservation(); // or reservationHelper
    private ArrayList<ArrayList<Object>> data2 = new ArrayList<>();
    private ArrayList<ArrayList<Object>> data;
    Reservation db2 = new Reservation();
//GUI menu setup
    public HostMenuGUI() {
        setTitle("Host Dashboard");//Tilte
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Close menu runtime
        setSize(700, 600);//Set size
        setLocationRelativeTo(null);//Display in hte middle of screen
        setContentPane(HostMenuGUI);//Get panel content
        setVisible(true);//Display panel

        //Loading file
        loadDataButton.addActionListener(e -> {
            //Allows user to choose a file.
            JFileChooser fileChooser = new JFileChooser();//Prompt user to choose database file.
            fileChooser.setDialogTitle("Select the database:");//
            int result = fileChooser.showOpenDialog(HostMenuGUI);//Shows in dialog
            if (result == JFileChooser.APPROVE_OPTION) {//Approved if file end in .db or .sqlite
                loadedFile = fileChooser.getSelectedFile();//Gets the selected file.
                logLines.clear();
                try (Scanner scanner = new Scanner(loadedFile)) {
                    //Notify user if it passed
                    while (scanner.hasNextLine()) logLines.add(scanner.nextLine());
                    JOptionPane.showMessageDialog(null, "File loaded successfully:\n" + loadedFile.getName());
                } catch (Exception ex) {//Notify user if failed
                    JOptionPane.showMessageDialog(null, "Error reading file:\n" + ex.getMessage());
                }
            }
        });
        //Create new reservation
        newReservationButton.addActionListener(e -> {
            try {//Prompts user to insert ID number
                String input = JOptionPane.showInputDialog("Enter New Reservation ID (7 digits):");
                if (input == null || !input.matches("\\d{7}")) {//Must be 7 digits.
                    JOptionPane.showMessageDialog(null, "Invalid ID format.");//error message.
                    return;
                }
                int id = Integer.parseInt(input.trim()); //Parsing id as integer.
                boolean exists = records.stream().anyMatch(r -> r.getId() == id);
                if (exists) {//Duplication checker
                    JOptionPane.showMessageDialog(null, "Reservation ID already exists.");//error message.
                    return;
                }
                //Prompt user to input each new reservation fields.
                String name = JOptionPane.showInputDialog("Name:"); //Costumer name
                String email = JOptionPane.showInputDialog("Email:");//Costumer email
                String phoneNumber = JOptionPane.showInputDialog("Phone Number:");//Customer phone number.
                String partySizeInput = JOptionPane.showInputDialog("Party Size:");//Desired party size
                String date = JOptionPane.showInputDialog("Date (YYYY/MM/DD):");//Data format
                String time = JOptionPane.showInputDialog("Time (hh:mm AM/PM):");//Reso time
                String notes = JOptionPane.showInputDialog("Notes (optional):");//Additional notes.

                int partySize;
                try {//Party size validator
                    partySize = Integer.parseInt(partySizeInput.trim());
                    if (partySize < 1 || partySize > 13) throw new NumberFormatException();//Must be at least the given number.
                } catch (NumberFormatException ex) {//gives error message.
                    JOptionPane.showMessageDialog(null, "Invalid party size.");
                    return; //returns to next line
                }
                //Stores in new reservation
                Record newRecord = new Record(id, name.trim(), email.trim(), phoneNumber.trim(), partySize, date.trim(), time.trim(), notes.trim());
                db2.insert(id, name.trim(), email.trim(), phoneNumber.trim(), partySize, date.trim(), time.trim(), notes.trim());
                JOptionPane.showMessageDialog(null, "Reservation created:\n" + newRecord);//Notify user it's been created.
                //Method to display in table
                DefaultTableModel updatedModel = db2.selectToTable(null, null, null, "partySize", "DESC", "");
                displayTable.setModel(updatedModel);

            } catch (Exception ex) {
                //Error message  if it wasn't created.
                JOptionPane.showMessageDialog(null, "Error creating reservation:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        });
        //Update reservation button
        updateReservationButton.addActionListener(e -> {
            try {
                //Prompt for Reservation ID
                String idInput = JOptionPane.showInputDialog("Enter Reservation ID to update:");
                if (idInput == null || !idInput.matches("\\d+")) { //ID validation checker.
                    JOptionPane.showMessageDialog(null, "Invalid ID format. Must be numeric.");//error message
                    return;
                }
                String reservationId = idInput.trim(); //Parsing iD into integer.
                //Prompt for field to update
                String updateField = JOptionPane.showInputDialog( //Ask user the desired field.
                        "Which field would you like to update: name, email, phoneNumber, partySize, date, time, notes"
                );
                if (updateField == null || updateField.trim().isEmpty()) {//If user did not answer it gives an error message.
                    JOptionPane.showMessageDialog(null, "No field selected.");
                    return;
                }
                updateField = updateField.trim();//Store in user input.

                //3: Prompt for new value
                String updateValue = JOptionPane.showInputDialog("Enter new value for " + updateField + ":");
                if (updateValue == null || updateValue.trim().isEmpty()) {//Validation checker.
                    JOptionPane.showMessageDialog(null, "No value entered.");//Gives user error message.
                    return;
                }

                //Validate partySize if applicable
                if (updateField.equals("partySize")) {
                    try {
                        int size = Integer.parseInt(updateValue.trim());
                        if (size < 1 || size > 13) { //Validation checker.
                            JOptionPane.showMessageDialog(null, "Party size must be between 1 and 13.");//Error message
                            return;
                        }
                    } catch (NumberFormatException ex) {// Checker if user input invalid entry.
                        JOptionPane.showMessageDialog(null, "Party size must be a number.");
                        return;
                    }
                }

                //Performs update method
                db2.update(updateField, updateValue.trim(), "id", reservationId);
                JOptionPane.showMessageDialog(null, "Updated " + updateField + " to '" + updateValue + "' for Reservation ID " + reservationId);

                //Refreshes table once updste is completed.
                db2.getExecuteResult("SELECT * FROM Reservation");//Select from the table.
                ArrayList<ArrayList<Object>> refreshedData = db2.getData(); //Inputs the data into table.
                DefaultTableModel updatedModel = db2.selectToTable(null, null, null, "partySize", "DESC", "");
                displayTable.setModel(updatedModel);//Table display area.

                //Additional CLI printout version.
                System.out.println("Updated Reservation Table:");//Notify user.
                for (int i = 0; i < updatedModel.getRowCount(); i++) {//gets row count
                    for (int j = 0; j < updatedModel.getColumnCount(); j++) {//get column count
                        System.out.print(updatedModel.getValueAt(i, j) + "\t");//input new update value.
                    }
                    System.out.println();//display
                }

            } catch (Exception ex) { //error message.
                JOptionPane.showMessageDialog(null, "Error updating reservation:\n" + ex.getMessage());
                ex.printStackTrace();//Displaying error message.
            }
        });

        //Cancel reservation
        cancelReservationButton.addActionListener(e -> {
            try {
                //Prompt user to input ID number.
                String input = JOptionPane.showInputDialog("Enter Reservation ID to cancel:");
                if (input == null || !input.matches("\\d{7}")) {//Validation checker
                    JOptionPane.showMessageDialog(null, "Invalid ID format.");//error message.
                    return;
                }
                int id = Integer.parseInt(input.trim());//Parsing id into integer.
                db2.delete("id", String.valueOf(id));
                //Notify user what is removed.
                JOptionPane.showMessageDialog(null, "Reservation with ID " + id + " has been removed.");
                //refreshes table
                DefaultTableModel updatedModel = db2.selectToTable(null, null, null, "partySize", "DESC", "");
                displayTable.setModel(updatedModel);//Displaying the removed data.

            } catch (Exception ex) {//error message.
                JOptionPane.showMessageDialog(null, "Error removing reservation:\n" + ex.getMessage());
                ex.printStackTrace();//Displaying error message.
            }
        });
        // Display reservations
        displayDataButton.addActionListener(e -> {
            try {//Verifies default section in reservation table
                DefaultTableModel model = db2.selectToTable(null, null, null, "partySize", "DESC", "");
                data2 = db2.getExecuteResult("SELECT * FROM Reservation"); //Select the table
                for (List<Object> row : data2) System.out.println(row);//Gets objects from each row
                displayTable.setModel(model);//Displays in on panel table
                if (model.getRowCount() == 0) {//Displays it on CLI
                    //Notify user
                    JOptionPane.showMessageDialog(null, "**Data displayed in CLI**.");
                }
            } catch (Exception ex) {//Catch error message
                JOptionPane.showMessageDialog(null, "Error loading logs: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        //Loging out of system option.
        logoutButton.addActionListener(e -> System.exit(0)); //Terminating the application

        }
        public static void main(String[] args) {
        SwingUtilities.invokeLater(HostMenuGUI::new);
    }
}
