package Database;
import DBHelper.Reservation;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class HostDB {
    public static void main(String[] args) {
        Reservation db2 = new Reservation();
        // Load and display all audit logs
        ArrayList<ArrayList<Object>> resoData = db2.getExecuteResult("SELECT * FROM Reservation");
        System.out.println("**Reservation Contents**");
        printDatabase(resoData);

        // Display filtered audit logs by role
        resoData = db2.select("Reservation", "name", "Princess Peach", "partySize", "DESC");
        System.out.println("\n**Filtered Reservation Contents**");
        printDatabase(resoData);


        db2.insert(23,"Olivia Baker", "Bakerthings@gmail.com","5479806775",13,"2025/11/23","11:25am", "NA");
        // Display audit logs in table format

        // Remove a record by ID (simulate GUI input)
        db2.delete("id", "11"); // Replace with actual ID from GUI
        System.out.println("\n**Record removed**");

        // Update a record (simulate GUI input)
        db2.update("user", "Ashely Hernandez", "email", "RHall@gmail.com");
        System.out.println("\n**Record updated**");


        DefaultTableModel displayTable = db2.selectToTable("Reservation", "name", "Princess Peach", "time", "DESC", "");
        System.out.println("\n**Displaying Content**");
        printTable(displayTable);

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



