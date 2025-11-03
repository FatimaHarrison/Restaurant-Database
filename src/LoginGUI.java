
import Database.ManagerDB;
import Model.Manager;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;

public class LoginGUI extends JFrame {
    private JPanel LoginGUI;
    private JTextField userRole;
    private JPasswordField employId;
    private JButton btnSubmit;
    private JLabel Password;
    private JLabel Username;

    private final ManagerDB managerDB = new ManagerDB();

    public LoginGUI() {
        setTitle("Compass Group Employee Login");
        setContentPane(LoginGUI);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setVisible(true);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String role = userRole.getText().trim();
                String idInput = new String(employId.getPassword()).trim();

                // Validate role
                if (!role.equalsIgnoreCase("Host") && !role.equalsIgnoreCase("Manager")) {
                    logAudit(idInput, role, "Login Failed: Invalid Role", "System");
                    JOptionPane.showMessageDialog(null, "Access Denied: Unauthorized User.");
                    return;
                }

                // Validate ID format
                if (!idInput.matches("\\d{7}")) {
                    logAudit(idInput, role, "Login Failed: Invalid ID Format", "System");
                    JOptionPane.showMessageDialog(null, "Invalid ID format. Must be 7 digits.");
                    return;
                }

                int employeeId = Integer.parseInt(idInput);

                // Validate credentials
                DMS.loadFromFile();
                if (!DMS.validateLogin(employeeId, role)) {
                    logAudit(idInput, role, "Login Failed: Credential Mismatch", "System");
                    JOptionPane.showMessageDialog(null, "Access Denied: ID or Role do not match system records.");
                    return;
                }

                // Successful login
                logAudit(idInput, role, "Login Success", "System");
                JOptionPane.showMessageDialog(null, "Logged in as: " + role);

                if (role.equalsIgnoreCase("Host")) {
                    new HostMenuGUI();
                } else {
                    new ManaMenuGUI();
                }
                dispose();
            }
        });
    }

    private String promptForDatabasePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select SQLite Database File");
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file.getName().endsWith(".db") || file.getName().endsWith(".sqlite")) {
                return file.getAbsolutePath();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a .db or .sqlite file.");
            }
        }
        return null;
    }

    private void logAudit(String idStr, String role, String action, String userName) {
        try {
            int id = Integer.parseInt(idStr);
            Manager entry = new Manager(id, role, action, userName, LocalDateTime.now(), "Login attempt recorded.");
            managerDB.save(entry);
        } catch (Exception ex) {
            System.out.println("Audit log failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
