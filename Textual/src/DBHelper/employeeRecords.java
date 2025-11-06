package DBHelper; // Declares the package this class belongs to

import javax.swing.table.DefaultTableModel; // Used for displaying tabular data in Swing GUIs
import java.time.LocalDate;
import java.util.ArrayList; // Used for dynamic arrays to store query results
//Declaring primary class
// Extends DBHelper, which handles the actual JDBC execution logic.
public class employeeRecords extends DBHelper {
	// Constant representing the table name in the database
	private final String TABLE_NAME = "employeeRecords";
	// Column name constants for easier reference and to avoid hardcoding strings
	public static final String employeeID = "employeeID";
	public static final String Name = "Name";
	public static final String Gender = "Gender";
	public static final String Role = "Role";
	public static final String HireDate = "HireDate";
	public static String Status = "Status"; // status field partTime FullTime
//Providing insert, update, delete, and query employee data from the database.
//Constructs a SQL SELECT query based on optional filtering and sorting parameters.
//fields  Comma-separated list of fields to select (null for all)
//whatField  Field to filter by ex. Role
//whatValue  Value to match in the filter field ex.Manager the role name
//sortField  Field to sort by (HireDate)
//sort Sorting direction ASC or DESC
	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		//To select from
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME; //Using the table name
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : ""; //Adding the 4 Ws.
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";//Using the sort to help get the specified data.
		return query; // return to complete SQL SELECT query string
	}

	//Inserts a new employee record into the database.
	//Nonnull fields are used included in the INSERT statement
	// to ensure there's data in the table.
	public void insert(Integer employeeID, String Name, String Gender, String Role, String Status, String HireDate) {
		// Wrap string values in quotes for SQL syntax
		Name = Name != null ? "\"" + Name + "\"" : null;
		Gender = Gender != null ? "\"" + Gender + "\"" : null;
		Role = Role != null ? "\"" + Role + "\"" : null;
		Status = Status != null ? "\"" + Status + "\"" : null;
		HireDate = HireDate != null ? "\"" + HireDate + "\"" : null;

		// Prepare arrays of values and corresponding fields
		Object[] values_ar = {employeeID, Name, Gender, Role, HireDate, Status};
		String[] fields_ar = {employeeRecords.employeeID, employeeRecords.Name, employeeRecords.Gender,employeeRecords.Role, employeeRecords.HireDate, employeeRecords.Status};

		// Build comma-separated field and value strings
		//Calls the values and corresponding array fields
		String values = "", fields = "";
		for (int i = 0; i < values_ar.length; i++) {
			if (values_ar[i] != null) {
				values += values_ar[i] + ", ";
				fields += fields_ar[i] + ", ";
			}
		}

		// Trim trailing commas and execute the INSERT query
		//Used to modify the values and fields.
		if (!values.isEmpty()) {
			values = values.substring(0, values.length() - 2);
			fields = fields.substring(0, fields.length() - 2);
			//Finalize the execute method
			super.execute("INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");");
		}
	}
	//whatField  Field to filter out the column name. employeeID, name etc.
	//whatValue  Value to match for deletion
	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}


	 //whatField   Field to update (e.g., "Status")
	 //whatValue   New value to set
	 //whereField  Field to filter by (e.g., "employeeID")
	 //whereValue  Value to match for update
	 //Updates a specific field in records that match a given condition.
	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}

	 //fields  Comma-separated list of fields to select
	 //whatField  Field to filter by
	 //whatValue  Value to match
	 //sortField  Field to sort by
	 //sort  Sorting direction
	 //return  A 2D ArrayList of query results
	//Retrieves employee records based on optional filters and sorting.
	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}


	//query SQL query string
	//return Query result as nested ArrayList
	//Executes a raw SQL query and returns the result as a 2D ArrayList.
	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}

	//Executes a raw SQL query without returning results.
	// query  SQL query string
	public void execute(String query) {
		super.execute(query);
	}



	 //Useful for displaying results in a JTable.
    //Fields to select
    // whatField  Filter field
	//whatValue  Filter value
	//sortField  Sort field
	//sort   Sorting direction
	// Unused parameter (possibly placeholder)
	//return  Table model for GUI display

	//Executes a SELECT query and returns the result as a DefaultTableModel.
	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort, String s) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

    public void insert(int employeeId, String trim, String trim1, String trim2, String trim3, String trim4, String trim5, String string) {
    }
}
