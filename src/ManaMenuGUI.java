import Database.DatabaseSelect;
import Database.ManagerDB;
import Model.Manager;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManaMenuGUI extends JFrame {
    private JButton Load;
    private JButton Display;
    private JButton RemoveData;
    private JButton AuditLog;
    private JButton EmployeeRecords;
    private JButton Logout;
    private JButton Generatebtn;
    private JButton exportAuditLogButton;
    private JTextArea logDisplay;
    private JPanel ManaMenuGUI;
    private JList list1;

    private String dbPath;
    private final ManagerDB managerDB = new ManagerDB();

    public ManaMenuGUI() {
        DMS.loadFromFile();
        setTitle("Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setContentPane(ManaMenuGUI);
        setVisible(true);

        Load.addActionListener(e -> {
            dbPath = DatabaseSelect.promptForDatabasePath((ActionListener) this);
            if (dbPath != null) {
                ManagerDB.setDatabasePath(dbPath);
                JOptionPane.showMessageDialog(this, "Database loaded:\n" + dbPath);
            }
        });

        Display.addActionListener(e -> {
            if (checkDatabaseLoaded()) return;
            List<Manager> logs = managerDB.getAll();
            if (logs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No audit logs found.");
                return;
            }
            logDisplay.setText(formatAuditList(logs));
        });

        RemoveData.addActionListener(e -> {
            if (checkDatabaseLoaded()) return;
            String idStr = JOptionPane.showInputDialog("Enter employ ID to remove:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            try {
                int id = Integer.parseInt(idStr.trim());
                boolean success = managerDB.delete(id);
                JOptionPane.showMessageDialog(this, success ? "Audit entry removed." : "No matching entry found.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        AuditLog.addActionListener(e -> {
            if (!checkDatabaseLoaded()) return;

            String[] options = {"Search by Role", "Search by Employ ID"};
            String choice = (String) JOptionPane.showInputDialog(this, "Choose search type:", "Audit Log Search",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null) return;

            List<Manager> logs = managerDB.getAll();
            LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
            StringBuilder builder = new StringBuilder();
            int count = 0;

            if (choice.equals("Search by Role")) {
                String role = JOptionPane.showInputDialog("Enter role (e.g., Host, Manager):");
                if (role == null || role.trim().isEmpty()) return;

                for (Manager m : logs) {
                    if (m.getRole().equalsIgnoreCase(role.trim()) && m.getTimestamp().isAfter(cutoff)) {
                        builder.append(formatAuditEntry(m)).append("\n");
                        count++;
                    }
                }
            } else {
                String idStr = JOptionPane.showInputDialog("Enter Employ ID:");
                if (idStr == null || idStr.trim().isEmpty()) return;

                try {
                    int id = Integer.parseInt(idStr.trim());
                    for (Manager m : logs) {
                        if (m.getEmployId() == id && m.getTimestamp().isAfter(cutoff)) {
                            builder.append(formatAuditEntry(m)).append("\n");
                            count++;
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID format.");
                    return;
                }
            }

            logDisplay.setText(count == 0 ? "No matching entries found." : builder.append("\nTotal entries found: ").append(count).toString());
        });


        Generatebtn.addActionListener(e -> {
            String role = JOptionPane.showInputDialog("Enter your role (Manager or Audit):");
            if (role != null && !role.trim().isEmpty()) {
                new AuditLogGUI(role.trim());
            }
        });


        exportAuditLogButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save audit log to file");
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.write(logDisplay.getText());
                    JOptionPane.showMessageDialog(this, "Logs exported to:\n" + file.getAbsolutePath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting logs: " + ex.getMessage());
                }
            }
        });

        EmployeeRecords.addActionListener(e -> {
            String authIdInput = JOptionPane.showInputDialog("Authorized Manager ID:");
            if (authIdInput != null && !authIdInput.trim().isEmpty()) {
                try {
                    int authId = Integer.parseInt(authIdInput.trim());
                    if (DMS.validUsers.containsKey(authId) &&
                            DMS.validUsers.get(authId).equalsIgnoreCase("Manager")) {
                        JOptionPane.showMessageDialog(this, "Access granted.");
                        new EmployeeRecordGUI();
                    } else {
                        JOptionPane.showMessageDialog(this, "Access denied. Invalid Manager ID.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID format.");
                }
            }
        });

        Logout.addActionListener(e -> System.exit(0));
    }

    private boolean checkDatabaseLoaded() {
        if (dbPath == null) {
            JOptionPane.showMessageDialog(this, "Please load a database first.");
            return true;
        }
        return false;
    }

    private String formatAuditEntry(Manager m) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");
        return String.format("%d-%s-%s-%s-%s-%s",
                m.getEmployId(),
                m.getRole(),
                m.getActionType(),
                m.getTimestamp().format(formatter),
                m.getEmployName(),
                m.getRecords());
    }

    private String formatAuditList(List<Manager> logs) {
        StringBuilder builder = new StringBuilder();
        for (Manager m : logs) {
            builder.append(formatAuditEntry(m)).append("\n");
        }
        return builder.toString();
    }
}
