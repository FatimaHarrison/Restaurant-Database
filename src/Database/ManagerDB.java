//Importing database location.
package Database;
//Importing file functions.
import DBHelper.auditlog;
import DBHelper.employeeRecords;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
/**
 * ManagerDB is a utility class for testing and displaying audit and employee records.
 * It simulates manager-level operations such as viewing logs, filtering by role,
 * updating and deleting records, and printing data to the console.
 * <p>
 * This class interacts with {@code auditlog} and {@code employeeRecords} database helpers.
 */
//Primary class
public class ManagerDB {
    /**
     * Main method to execute manager database operations.
     * Loads audit logs, filters by role, displays data in table format,
     * and performs simulated update and delete operations.
     *
     * @param args command-line arguments (not used)
     */
    //Main file runner
    public static void main(String[] args) {
        // Create instances of database helpers
        auditlog db1 = new auditlog();
        employeeRecords db2 = new employeeRecords();
        // Load and display all audit logs
        //Executed query
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
    /**
     * Prints a list of database records to the console.
     *@param data a list of rows, where each row is a list of column values
     */
    public static void printDatabase(ArrayList<ArrayList<Object>> data) {
        for (List<Object> record : data) {
            // Print the entire row as a single string val1, val2, val3)
            System.out.println(record.toString());
        }
    }
    /**
     * Prints a {@code DefaultTableModel} to the console in tabular format.
     *@param table the table model containing rows and columns to display
     */
    public static void printTable(DefaultTableModel table) {
        // Loop through each row in the table
        for (int row = 0; row < table.getRowCount(); row++) {
            // Loop through each column in the current row
            for (int column = 0; column < table.getColumnCount(); column++) {
                // Print the value at the current cell followed by a separator
                System.out.print(table.getValueAt(row, column).toString() + " | ");
            }
            // Move to the next line after printing all columns in the row
            System.out.println();
        }
    }
}

