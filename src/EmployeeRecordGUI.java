import Database.DatabaseSelect;
import Database.EmployeeDB;
import Model.EmployeeRec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeRecordGUI extends JFrame {
    private JList list3;
    private JButton loadButton;
    private JButton displayButton;
    private JButton addNewInfoButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton logoutButton;
    private JTextArea displayArea;
    private JPanel EmployeeRecordGUI;

    private final EmployeeDB employeeDB = new EmployeeDB();
    private String dbPath;

    public EmployeeRecordGUI() {
        setTitle("Employee Records");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setContentPane(EmployeeRecordGUI);
        setVisible(true);

        // Load database
        loadButton.addActionListener(e -> {
            dbPath = DatabaseSelect.promptForDatabasePath((ActionListener) this);
            if (dbPath != null) {
                EmployeeDB.setDatabasePath(dbPath);
                JOptionPane.showMessageDialog(this, "Database loaded:\n" + dbPath);
            }
        });

        // Display employees
        displayButton.addActionListener(e -> {
            if (dbPath == null) {
                JOptionPane.showMessageDialog(this, "Please load a database first.");
                return;
            }
            List<EmployeeRec> employees = employeeDB.getAll();
            displayArea.setText("");
            for (EmployeeRec emp : employees) {
                displayArea.append(emp.toString() + "\n");
            }
        });

        // Create employee
        addNewInfoButton.addActionListener(e -> {
           JTextField idField = new JTextField(10);
            JTextField nameField = new JTextField();
            JTextField genField = new JTextField();
            JTextField roleField = new JTextField();
            JTextField statField = new JTextField();
            JTextField dateField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Reservation ID")); panel.add(idField);
            panel.add(new JLabel("Name:")); panel.add(nameField);
            panel.add(new JLabel("Gender:")); panel.add(genField);
            panel.add(new JLabel("Role:")); panel.add(roleField);
            panel.add(new JLabel("Status:")); panel.add(statField);
            panel.add(new JLabel("Hire Date (YYYY-MM-DD):")); panel.add(dateField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Create Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    EmployeeRec emp = new EmployeeRec(
                            Integer.parseInt(idField.getText().trim()),
                            nameField.getText().trim(),
                            genField.getText().trim(),
                            roleField.getText().trim(),
                            statField.getText().trim(),
                            dateField.getText().trim()
                    );
                    boolean success = employeeDB.save(emp);
                    JOptionPane.showMessageDialog(this, success ? "Employee saved." : "Failed to save.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
                }
            }
        });

        // Update employee
        updateButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter employee ID to update:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            try {
                int id = Integer.parseInt(idStr.trim());
                EmployeeRec existing = employeeDB.getById(id);
                if (existing == null) {
                    JOptionPane.showMessageDialog(this, "Employee not found.");
                    return;
                }

                JTextField nameField = new JTextField(existing.getEmployName());
                JTextField genField = new JTextField(existing.getEmployGen());
                JTextField roleField = new JTextField(existing.getEmployRole());
                JTextField statField = new JTextField(existing.getEmployStat());
                JTextField dateField = new JTextField(existing.getHireDate());

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Name:")); panel.add(nameField);
                panel.add(new JLabel("Gender:")); panel.add(genField);
                panel.add(new JLabel("Role:")); panel.add(roleField);
                panel.add(new JLabel("Status:")); panel.add(statField);
                panel.add(new JLabel("Hire Date:")); panel.add(dateField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    EmployeeRec updated = new EmployeeRec(
                            id,
                            nameField.getText().trim(),
                            genField.getText().trim(),
                            roleField.getText().trim(),
                            statField.getText().trim(),
                            dateField.getText().trim()
                    );
                    boolean success = employeeDB.update(updated);
                    JOptionPane.showMessageDialog(this, success ? "Employee updated." : "Update failed.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Delete employee
        deleteButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter employee ID to delete:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            try {
                int id = Integer.parseInt(idStr.trim());
                boolean success = employeeDB.delete(id);
                JOptionPane.showMessageDialog(this, success ? "Employee deleted." : "Employee not found.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        logoutButton.addActionListener(e -> System.exit(0));
    }
}
