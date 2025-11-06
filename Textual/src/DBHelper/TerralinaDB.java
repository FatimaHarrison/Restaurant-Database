//Fatima Harrison, CEN3024C, 10/31/25
//This section will allow the user to interact with sqlite by accessing the Terralina database within the IDE.
//The user would be able to use all CRUD and custom functions in the interaction with the database with sqlite query commands.
//Three tables reservation, auditlog, and employee records hold rows and columns with 20 different pieces of information to be display and modified from the user.
//User based roles still applies to access certain tables for instance the host will only be able to modify the Reservation table  and so on.
//Each inputs will consist a  sqlite query to be executed and called into a java file.
//The output will consist the fully executed command whether in the CRUD functions or the custom function.

//Importing package of DBHelper location
package DBHelper;
//Declaring functional imports
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

import static DBHelper.auditlog.records;

//Declaring primary class
public class TerralinaDB {
    //Main runtime function
    public static void main(String[] args) {
        //Creating an instance of the databases.
        auditlog db1 = new auditlog();
        Reservation db2 = new Reservation();
        employeeRecords db3 = new employeeRecords();
        //Creating a 2D arraylist to hold the results of a query and custom types.
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        //Method to retrieve the contents of a database.
        data = db1.getExecuteResult("select * from auditlog");
        //Method to display the arraylist data.
        for (List<Object> row : data) {
            System.out.println(data);
        }
        //Method to update
        db1.update(db1.user, "Jessica Rabbit",db1.timestamp, "2025/10/02 04:35 PM");
        System.out.println("Data has been Updated");
        data =db1.getExecuteResult("select * from auditlog");
        System.out.println("\n**Updated content**");
        printDatabase(data);

        //Method to create new data
        // Prompting user to insert data manually.
        //db1.execute(INSERT INTO auditlog("employid", "role", "action", "user") VALUES );
        //displayArea = db1.getExecuteResult("select * from auditlog");

        System.out.println("**Removed content**");
        System.out.println(data);
        //Method to delete rows from database based on ID number
        //Prompting the user to input the ID number.
        db1.delete("employid", null);
        //notify user that is has been deleted.
        System.out.println("Employee Data has been removed.");
        //displays the list again with the deletion.
        data = db1.getExecuteResult("select * from auditlog");
        System.out.println("\n**Removed content**");
        printDatabase(data);
        //Method to add a query command
    //Allows the user to input a query to execute.
        data = db1.getExecuteResult("select * from auditlog");
        System.out.println("\n**Displaying content**");
        printDatabase(data);//display

        //Another method to select the query instead of inputting it.
        data = db1.select("auditlog", "role", "Host", "DESC", "timestamp");
        System.out.println("\n**Displaying content2**");
        printDatabase(data);//display

        //Adding a new default model as a type.
        DefaultTableModel table = new DefaultTableModel();
        table = db1.selectToTable("auditlog", "role", "Host", "DESC", "timestamp", "");
        String data2 = null; //Setting a new data storage
        System.out.println("\n**Displaying Table content**");
        for (int row = 0; row < table.getRowCount(); row++) {//Getting each row count from the columns.
            for( int column = 0; column < table.getColumnCount(); column++){ //Getting the number of columns
                System.out.println(table.getValueAt(row, column).toString()+"/");
            }
            System.out.println();
        }
    }
    public static void printDatabase(ArrayList<ArrayList<Object>> data) {
        for (List<Object> record : data) {
            System.out.println(record.toString());
        }
    }
}

