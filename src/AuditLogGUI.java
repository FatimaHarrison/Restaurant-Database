// Fatima Harrison, CEN 3024C, 10/21/25
// Terralina Audit Log GUI — Role-based report generator for employee actions

import Database.ManagerDB;
import Model.Manager;
import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

public class AuditLogGUI extends JFrame {
    private JPanel AuditLogGUI;
    private JTextField startField;
    private JTextField endField;
    private JTextField roleField;
    private JTextField idField;
    private JTextArea reportDisplay;
    private JButton Generatebtn;

    private final ManagerDB managerDB = new ManagerDB();
    private final String currentUserRole;
    private String dbPath = "";

    public AuditLogGUI(String currentUserRole) {
        this.currentUserRole = currentUserRole;
        this.dbPath = dbPath;

        setTitle("Audit Report Generator");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
       setContentPane(AuditLogGUI);
        setVisible(true);

        Generatebtn.addActionListener(e -> generateAuditReport());
    }

    private void generateAuditReport() {
        if (!currentUserRole.equalsIgnoreCase("manager") && !currentUserRole.equalsIgnoreCase("audit")) {
            JOptionPane.showMessageDialog(this, "Access denied. Authorized usage only.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate startLocal = LocalDate.parse(startField.getText().trim(), formatter);
            LocalDate endLocal = LocalDate.parse(endField.getText().trim(), formatter);
            LocalDateTime startDate = startLocal.atTime(11, 0);
            LocalDateTime endDate = endLocal.atTime(11, 59);

            LocalDateTime now = LocalDateTime.now();
            if (startDate.isBefore(now.minusDays(30)) || endDate.isAfter(now)) {
                JOptionPane.showMessageDialog(this, "Date range must be within the last 30 days.");
                return;
            }

            String roleFilter = roleField.getText().trim().toLowerCase();
            String keyword = idField.getText().trim().toLowerCase();

            ManagerDB.setDatabasePath(dbPath);
            List<Manager> logs = managerDB.getAll();
            List<Manager> filtered = logs.stream()
                    .filter(log -> !log.getTimestamp().isBefore(startDate) && !log.getTimestamp().isAfter(endDate))
                    .filter(log -> roleFilter.equals("all") || log.getRole().equalsIgnoreCase(roleFilter))
                    .filter(log -> keyword.isEmpty() ||
                            log.getEmployId().toString().toLowerCase().contains(keyword) ||
                            log.getEmployName().toLowerCase().contains(keyword) ||
                            log.getRecords().toLowerCase().contains(keyword))
                    .sorted(Comparator.comparing(Manager::getTimestamp))
                    .limit(10000)
                    .toList();

            DateTimeFormatter outFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");
            StringBuilder builder = new StringBuilder("\n--- Audit Report ---\n");
            for (Manager log : filtered) {
                String maskedNotes = currentUserRole.equalsIgnoreCase("audit") ? log.getRecords() : "***";
                builder.append(String.format("%d-%s-%s-%s-%s-%s\n",
                        log.getEmployId(),
                        log.getRole(),
                        log.getActionType(),
                        log.getTimestamp().format(outFormat),
                        log.getEmployName(),
                        maskedNotes));
            }
            builder.append("\nReport generated. Total entries: ").append(filtered.size());
            reportDisplay.setText(builder.toString());

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY/MM/DD.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage());
        }
    }
}
