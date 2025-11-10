//Location of package database
package Database;
//Importing file functions.
import DBHelper.Reservation;
//GUI Imports.
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
/**
 * The {@code HostDB} class serves as a test harness and demonstration utility
 * for interacting with the Reservation database using the {@link Reservation} DAO.
 * <p>
 * It performs a series of operations including querying, inserting, updating,
 * deleting, and displaying data in both raw and tabular formats.
 */
//Primary class used.
public class HostDB {
    /**
     * Entry point for executing Reservation database operations.
     * <p>
     * This method demonstrates:
     * <ul>
     *   <li>Executing raw SQL queries</li>
     *   <li>Filtering and sorting results</li>
     *   <li>Inserting new reservation records</li>
     *   <li>Deleting and updating records</li>
     *   <li>Displaying results in console and table format</li>
     * </ul>
     *
     * @param args command-line arguments (not used)
     */
    //Main file runner
    public static void main(String[] args) {
        Reservation db2 = new Reservation(); //
        // Load and display all audit logs
        //Executed query.
        ArrayList<ArrayList<Object>> resoData = db2.getExecuteResult("SELECT * FROM Reservation");
        System.out.println("**Reservation Contents**");
        printDatabase(resoData);//Prints outs data from table.
        // Display filtered audit logs by role
        resoData = db2.select("Reservation", "name", "Princess Peach", "partySize", "DESC");
        System.out.println("\n**Filtered Reservation Contents**");
        printDatabase(resoData);//Displays out filtered fields.
        //Method to add in data from a query.
        db2.insert(23,"Olivia Baker", "Bakerthings@gmail.com","5479806775",13,"2025/11/23","11:25am", "NA");
        // Display audit logs in table format
        // Remove a record by ID (simulate GUI input)
        db2.delete("id", "11"); // Replace with actual ID from GUI
        System.out.println("\n**Record removed**");
        // Update a record (simulate GUI input)
        db2.update("user", "Ashely Hernandez", "email", "RHall@gmail.com");
        System.out.println("\n**Record updated**");
        //To set up a table model o display in.
        DefaultTableModel displayTable = db2.selectToTable("Reservation", "name", "Princess Peach", "time", "DESC", "");
        System.out.println("\n**Displaying Content**");
        printTable(displayTable);//Display table
        // Display employee records
        //Executed query.
        ArrayList<ArrayList<Object>> empData = db2.getExecuteResult("SELECT * FROM employeeRecords");
        System.out.println("\n**Employee Records**");
        printDatabase(empData);
    }
    /**
     * Prints each record from a nested ArrayList representing database rows.
     * Each inner list is treated as a row, and its contents are printed as a string.
     *
     * @param data a list of rows, where each row is a list of column values
     */
    public static void printDatabase(ArrayList<ArrayList<Object>> data) {
        for (List<Object> record : data) {
            // Print the entire row as a single string val1, val2, val3)
            System.out.println(record.toString());
        }
    }
    /**
     * Prints the contents of a DefaultTableModel in a tabular format.
     * Each row is printed with its column values separated by a pipe symbol.
     *
     * @param table the DefaultTableModel containing tabular data (e.g., from a JTable)
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





