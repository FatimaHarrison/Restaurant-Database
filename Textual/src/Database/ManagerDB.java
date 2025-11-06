package Database;
import DBHelper.auditlog;
import DBHelper.employeeRecords;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ManagerDB {
    public static void main(String[] args) {
        // Create instances of database helpers
        auditlog db1 = new auditlog();
        employeeRecords db2 = new employeeRecords();

        // Load and display all audit logs
        ArrayList<ArrayList<Object>> auditData = db1.getExecuteResult("SELECT * FROM auditlog");
        System.out.println("**Audit Log Contents**");
        printDatabase(auditData);

        // Display filtered audit logs by role
        auditData = db1.select("auditlog", "role", "Manager", "DESC", "\"timestamp\"");
        System.out.println("\n**Filtered Audit Logs (Manager Role)**");
        printDatabase(auditData);

        // Display audit logs in table format
        DefaultTableModel auditTable = db1.selectToTable("auditlog", "role", "Manager", "DESC", "\"timestamp\"", "");
        System.out.println("\n**Audit Log Table View**");
        printTable(auditTable);

        // Remove a record by ID (simulate GUI input)
        db1.delete("employid", "102"); // Replace with actual ID from GUI
        System.out.println("\n**Record removed**");

        // Update a record (simulate GUI input)
        db1.update("user", "Ashely Hernandez", "timestamp", "2025/11/03 06:00 PM");
        System.out.println("\n**Record updated**");

        // Display employee records
        ArrayList<ArrayList<Object>> empData = db2.getExecuteResult("SELECT * FROM employeeRecords");
        System.out.println("\n**Employee Records**");
        printDatabase(empData);
    }

    public static void printDatabase(ArrayList<ArrayList<Object>> data) {
        for (List<Object> record : data) {
            System.out.println(record.toString());
        }
    }

    public static void printTable(DefaultTableModel table) {
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int column = 0; column < table.getColumnCount(); column++) {
                System.out.print(table.getValueAt(row, column).toString() + " | ");
            }
            System.out.println();
        }
    }
}

