//Declaring DBhelper location.
package DBHelper;
//Importing class functions
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
//Declaring primary class
/**
 * The {@code DBHelper} class provides foundational JDBC operations for interacting with a SQLite database.
 * <p>
 * It supports executing SQL commands, retrieving query results as nested lists or table models,
 * and managing connection lifecycle. Subclasses such as {@code auditlog}, {@code Reservation},
 * and {@code employeeRecords} extend this class to perform domain-specific operations.
 */
public class DBHelper {
	//Class attributes.
	private final String DATABASE_NAME = "C:\\Users\\tutor\\sqlite\\Terralina.db";//Setting database class.
	private Connection connection;//Databse connection
	private Statement statement;//Query statement
	private ResultSet resultSet;//Getting the result set.
    //Setting everything to null
	/**
	 * Constructs a {@code DBHelper} instance with null-initialized JDBC resources.
	 */
	public DBHelper() {
		connection = null;
		statement = null;
		resultSet = null;
	}
	/**
	 * Opens a connection to the SQLite database and initializes the statement object.
	 * <p>
	 * Loads the SQLite JDBC driver and connects using the configured database path.
	 */
	// Opens connection to SQLite database
	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");// Load SQLite JDBC driver
		} catch (ClassNotFoundException e) {//error message
			e.printStackTrace();//Display error message.
		}
		try {//get the connection from driver.
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			statement = connection.createStatement();
		} catch (SQLException e) {//error message
			e.printStackTrace();//Display error message.
		}
	}
	/**
	 * Closes all JDBC resources including connection, statement, and result set.
	 * <p>
	 * Ensures proper cleanup after query execution to prevent resource leaks.
	 */
	// Closes all JDBC resources
	private void close() {
		try {
			connection.close(); // Close DB connection
			statement.close(); // Close statement
			if (resultSet != null) // Close resultSet if fails
			resultSet.close();// Close resultSet if it exists
		} catch (SQLException e) {
			e.printStackTrace();//Display error message
		}
	}
	/**
	 * Converts a nested {@code ArrayList<ArrayList<Object>>} into a 2D {@code Object[][]} array.
	 *
	 * @param list the nested list representing rows and columns
	 * @return a 2D array suitable for table model construction
	 */
	//ArrayList to 2D Object array
	private Object[][] arrayListTo2DArray(ArrayList<ArrayList<Object>> list) {
		Object[][] array = new Object[list.size()][];// Creating outer array
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Object> row = list.get(i); // Get each row
			array[i] = row.toArray(new Object[row.size()]);// Convert row to array
		}
		return array;// Return 2D array
	}
	// Executes SQL command (INSERT, UPDATE, DELETE)
	protected void execute(String sql) {
		try {
			connect();// Open DB connection
			statement.execute(sql);// Run SQL command
		} catch (SQLException e) { // Print error if execution fails
			e.printStackTrace();
		}
		finally {
			close();
		}
	}
	/**
	 * Executes a non-query SQL command such as {@code INSERT}, {@code UPDATE}, or {@code DELETE}.
	 *@param sql the SQL command to execute
	 */
	// Executes SELECT query and returns table model
	protected DefaultTableModel executeQueryToTable(String sql) {
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();//Store row data
		ArrayList<Object> columns = new ArrayList<Object>();//Store column data
		connect();//open database
		try {
			resultSet = statement.executeQuery(sql);//Run select query
			int columnCount = resultSet.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++)//adding a column name
			columns.add(resultSet.getMetaData().getColumnName(i));
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<Object>();//store into row
				for (int i = 1; i <= columnCount; i++)//adding column count
				subresult.add(resultSet.getObject(i));//stores it
				result.add(subresult);//ends with results.
			}
		} catch (SQLException e) {
			e.printStackTrace();//Error message.
		}
		close();//close table model
		return new DefaultTableModel(arrayListTo2DArray(result), columns.toArray());//return to table model
	}

// Executes SELECT query and returns nested ArrayList
	/**
	 * Executes a {@code SELECT} query and returns the result as a nested {@code ArrayList}.
	 * <p>
	 * Useful for backend processing, logging, or non-GUI workflows.
	 *
	 * @param sql the SQL query to execute
	 * @return a nested list of query results
	 */
	protected ArrayList<ArrayList<Object>> executeQuery(String sql) {

		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();//Storing a row
		connect();
		try {
			resultSet = statement.executeQuery(sql);
			int columnCount = resultSet.getMetaData().getColumnCount();//Column count
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<Object>();
				for (int i = 1; i <= columnCount; i++) { //Adding column and row
					subresult.add(resultSet.getObject(i));//adding row to the results
				}
				result.add(subresult);
			}
		} catch (SQLException e){
			e.printStackTrace();//Display error message.
		}
		close();//Close table model
		return result;//return to table model.
	}

}