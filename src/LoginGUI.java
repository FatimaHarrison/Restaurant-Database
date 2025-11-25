//Fatima Harrison, CEN 3024C, 10/21/25
// In this project users would be able to interact with live GUIs panels based from a famous restaurant theme Terralina.
// User will be able to access their role based menus and are prompted to login and access files throughout the process.
//Overall, instead of using the CLI and database, this project is a digital screen popup feature that is programed for easy usages.
// Each menu options consist action buttons for users to load files, use CRUD functions and for manager role allow it to access the custom action.
//If errors occurs, exceptions would notify users whether a file can't be loaded or a invalid entry has been detected.
// If everything passes, the output would display the loaded files and all CRUD functions.

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The {@code LoginGUI} class provides a graphical login interface for the Restaurant Database Management System.
 * <p>
 * It prompts users to enter their role and employee ID, validates credentials against the system records,
 * and launches the appropriate dashboard based on the authenticated role.
 * <ul>
 *   <li>Supports role-based access for Host and Manager users.</li>
 *   <li>Validates employee IDs to ensure proper format (7 digits).</li>
 *   <li>Records login attempts in the audit log for traceability.</li>
 * </ul>
 * This class extends {@link JFrame} to provide a standalone GUI window.
 */
public class LoginGUI extends JFrame {
    //Declaring class attributes
    //Declaring HostMenuGUI layout functions
    private JPanel LoginGUI; //Panel name
    private JTextField userRole; //Role text area
    private JPasswordField employId; //Enter password field
    private JButton btnSubmit; //Submit btn
    private JLabel Password; //Used label
    private JLabel Username; //Used label

    /**
     * Constructs the {@code LoginGUI} window.
     * <p>
     * Initializes the login panel, sets up the frame properties, and wires the submit button
     * to validate user credentials. If login succeeds, the appropriate role-based dashboard
     * is launched; otherwise, an error message is displayed.
     */
    public LoginGUI() {
        DMS.loadFromFile(); // Load roles from Data.txt
        setTitle("Compass Group Employee Login"); //Title
        setContentPane(LoginGUI); //Set content to display layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Stop run
        setSize(350, 250); //Setting size
        setLocationRelativeTo(null); //Location of popup menu
        setVisible(true); //Display panel

        //Load login data once

        //Action button to submit
        btnSubmit.addActionListener(new ActionListener() {
            @Override //Wired function from panel
            public void actionPerformed(ActionEvent e) {
                String role = userRole.getText().trim(); //Username
                String idInput = new String(employId.getPassword()).trim(); //Password

                //Role validation
                if (!role.equalsIgnoreCase("Host") && !role.equalsIgnoreCase("Manager")) {
                    //Notify user of no access
                    JOptionPane.showMessageDialog(null, "Access Denied: Unauthorized User.");
                    return;
                }

                //ID format validation
                if (!idInput.matches("\\d{7}")) {
                    //Notify user of error
                    JOptionPane.showMessageDialog(null, "Invalid ID format. Must be 7 digits.");
                    return;
                }
                //Parsing employee ID for integer.
                int employId = Integer.parseInt(idInput);

                // Login using DMS validation
                if (!DMS.validateLogin(employId, role)) {
                    //Notify user of denied access.
                    JOptionPane.showMessageDialog(null, "Access Denied: ID or Role do not match system records.");
                    return;
                }

                // System launch appropriate dashboard based on user login.
                JOptionPane.showMessageDialog(null, "Logged in as: " + role);
                DMS.insertauditLog(employId, role, "Login", "User successfully authenticated");

                if (role.equalsIgnoreCase("Host")) {
                    new HostMenuGUI();
                } else {
                    if (role.equalsIgnoreCase("Manager")) {
                        new ManaMenuGUI();
                    }
                }
                dispose(); // Close login window
            }
        });
    }

    /**
     * Entry point for launching the {@code LoginGUI}.
     * <p>
     * Uses {@link SwingUtilities#invokeLater} to ensure the GUI is created on the Event Dispatch Thread.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
