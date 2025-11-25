//Declaring DBhelper location.
package DBHelper;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
//Importing class functions
import javax.swing.table.DefaultTableModel;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The {@code DBHelper} class provides foundational JDBC operations for interacting with a SQLite database.
 * <p>
 * It supports executing SQL commands, retrieving query results as nested lists or table models,
 * and managing connection lifecycle. Subclasses such as {@code auditlog}, {@code Reservation},
 * and {@code employeeRecords} extend this class to perform domain-specific operations.
 */
public class DBHelper {
	private Connection connection; //Database connection
	private Statement statement;   //Query statement
	private ResultSet resultSet;   //Getting the result set.

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
	 * Loads the SQLite JDBC driver and connects using the embedded database packaged in resources.
	 */
	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");

			// Locate DB inside Resources Root
			URL resource = DBHelper.class.getClassLoader().getResource("Terralina.db");
			if (resource == null) {
				throw new SQLException("Embedded DB not found in Resources folder.");
			}

			// Decode path in case of spaces or special characters
			String decodedPath = java.net.URLDecoder.decode(resource.getPath(), "UTF-8");
			String url = "jdbc:sqlite:" + decodedPath;

			connection = DriverManager.getConnection(url);
			if (connection == null) {
				throw new SQLException("Failed to establish SQLite connection.");
			}

			statement = connection.createStatement();
			if (statement == null) {
				throw new SQLException("Failed to create SQL statement.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Closes all JDBC resources including connection, statement, and result set.
	 * <p>
	 * Ensures proper cleanup after query execution to prevent resource leaks.
	 */
	private void close() {
		try {
			if (resultSet != null) resultSet.close();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Converts ArrayList to 2D Object array
	private Object[][] arrayListTo2DArray(ArrayList<ArrayList<Object>> list) {
		Object[][] array = new Object[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Object> row = list.get(i);
			array[i] = row.toArray(new Object[row.size()]);
		}
		return array;
	}

	// Executes SQL command (INSERT, UPDATE, DELETE)
	protected void execute(String sql) {
		try {
			connect();
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// Executes SELECT query and returns table model
	protected DefaultTableModel executeQueryToTable(String sql) {
		ArrayList<ArrayList<Object>> result = new ArrayList<>();
		ArrayList<Object> columns = new ArrayList<>();
		connect();
		try {
			resultSet = statement.executeQuery(sql);
			int columnCount = resultSet.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columns.add(resultSet.getMetaData().getColumnName(i));
			}
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					subresult.add(resultSet.getObject(i));
				}
				result.add(subresult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return new DefaultTableModel(arrayListTo2DArray(result), columns.toArray());
	}

	// Executes SELECT query and returns nested ArrayList
	protected ArrayList<ArrayList<Object>> executeQuery(String sql) {
		ArrayList<ArrayList<Object>> result = new ArrayList<>();
		connect();
		try {
			resultSet = statement.executeQuery(sql);
			int columnCount = resultSet.getMetaData().getColumnCount();
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					subresult.add(resultSet.getObject(i));
				}
				result.add(subresult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return result;
	}
}
