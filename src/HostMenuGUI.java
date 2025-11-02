import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

import Database.DatabaseSelect;
import Database.HostDB;
import Model.Reservation;

public class HostMenuGUI extends JFrame {
    private JButton databtn;
    private JButton displayDataButton;
    private JButton newReservationButton;
    private JButton cancelReservationButton;
    private JButton updateReservationButton;
    private JButton logoutButton;
    private JList list2;
    private JTextArea display2;
    private JPanel HostMenuGUI;

    private final HostDB hostDB = new HostDB();
    private String dbPath;

    public HostMenuGUI() {
        setTitle("Host Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setContentPane(HostMenuGUI);
        setVisible(true);

        // Load database path
        databtn.addActionListener(e -> {
            dbPath = DatabaseSelect.promptForDatabasePath((ActionListener) this);
            if (dbPath != null) {
                HostDB.setDatabasePath(dbPath);
                JOptionPane.showMessageDialog(this, "Database loaded:\n" + dbPath);
            }
        });

        // Display reservations
        displayDataButton.addActionListener(e -> {
            if (dbPath == null) {
                JOptionPane.showMessageDialog(this, "Please load a database first.");
                return;
            }
            List<Reservation> reservations = hostDB.getAll();
            display2.setText("");
            for (Reservation r : reservations) {
                display2.append(r.toString() + "\n");
            }
        });

        // Create reservation
        newReservationButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField partySizeField = new JTextField();
            JTextField dateField = new JTextField();
            JTextField timeField = new JTextField();
            JTextField notesField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Name:")); panel.add(nameField);
            panel.add(new JLabel("Email:")); panel.add(emailField);
            panel.add(new JLabel("Phone:")); panel.add(phoneField);
            panel.add(new JLabel("Party Size:")); panel.add(partySizeField);
            panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(dateField);
            panel.add(new JLabel("Time (HH:MM):")); panel.add(timeField);
            panel.add(new JLabel("Notes:")); panel.add(notesField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Create Reservation", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Reservation r = new Reservation(
                            nameField.getText().trim(),
                            emailField.getText().trim(),
                            phoneField.getText().trim(),
                            Integer.parseInt(partySizeField.getText().trim()),
                            dateField.getText().trim(),
                            timeField.getText().trim(),
                            notesField.getText().trim()
                    );
                    boolean success = hostDB.save(r);
                    JOptionPane.showMessageDialog(this, success ? "Reservation saved." : "Failed to save.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
                }
            }
        });

        // Update reservation
        updateReservationButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter reservation ID to update:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            try {
                int id = Integer.parseInt(idStr.trim());
                Reservation existing = hostDB.getById(id);
                if (existing == null) {
                    JOptionPane.showMessageDialog(this, "Reservation not found.");
                    return;
                }

                JTextField nameField = new JTextField(existing.getName());
                JTextField emailField = new JTextField(existing.getEmail());
                JTextField phoneField = new JTextField(existing.getPhone());
                JTextField partySizeField = new JTextField(String.valueOf(existing.getPartySize()));
                JTextField dateField = new JTextField(existing.getDate());
                JTextField timeField = new JTextField(existing.getTime());
                JTextField notesField = new JTextField(existing.getNotes());

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Name:")); panel.add(nameField);
                panel.add(new JLabel("Email:")); panel.add(emailField);
                panel.add(new JLabel("Phone:")); panel.add(phoneField);
                panel.add(new JLabel("Party Size:")); panel.add(partySizeField);
                panel.add(new JLabel("Date:")); panel.add(dateField);
                panel.add(new JLabel("Time:")); panel.add(timeField);
                panel.add(new JLabel("Notes:")); panel.add(notesField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Update Reservation", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    Reservation updated = new Reservation(
                            id,
                            nameField.getText().trim(),
                            emailField.getText().trim(),
                            phoneField.getText().trim(),
                            Integer.parseInt(partySizeField.getText().trim()),
                            dateField.getText().trim(),
                            timeField.getText().trim(),
                            notesField.getText().trim()
                    );
                    boolean success = hostDB.update(updated);
                    JOptionPane.showMessageDialog(this, success ? "Reservation updated." : "Update failed.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Delete reservation
        cancelReservationButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter reservation ID to delete:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            try {
                int id = Integer.parseInt(idStr.trim());
                boolean success = hostDB.delete(id);
                JOptionPane.showMessageDialog(this, success ? "Reservation deleted." : "Reservation not found.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        logoutButton.addActionListener(e -> System.exit(0));
    }
}
