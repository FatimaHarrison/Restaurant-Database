//Declaring package location
package DBHelper;
//Importing file functions.
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
/**
 * The {@code auditlog} class provides database operations for managing audit log records.
 * It supports inserting, updating, deleting, filtering, and displaying audit entries.
 * <p>
 * This class extends {@code DBHelper} and interacts with the {@code auditlog} table.
 */
//Declaring primary class extend to DBHelper.
public class auditlog extends DBHelper {
	//Class attributes.
	private final String TABLE_NAME = "auditlog";//Used for audit log
	public static final String employid = "employid";//Store in employee ID.
	public static final String role = "role";//Store in employee role.
	public static final String action = "action";//Store in employee action.
	public static final String user = "user";//Store in employee user.
	public static final String timestamp = "timestamp";//Store in employee timestamp.
	public static final String records = "records";//Store in employee records.
	/**
	 * Prepares a SQL SELECT query with optional filtering and sorting.
	 *
	 * @param fields     fields to select (null for all)
	 * @param whatField  field to filter by
	 * @param whatValue  value to filter
	 * @param sortField  field to sort by
	 * @param sort       sort direction ("ASC" or "DESC")
	 * @return SQL query string
	 */
	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT "; //to select from.
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}
	/**
	 * Inserts a new audit log record into the database.
	 *
	 * @param employid  employee ID
	 * @param role      user role
	 * @param action    action performed
	 * @param user      username
	 * @param timestamp timestamp of the action
	 * @param records   additional record details
	 */
	//Inserts a new employee record into the database.
	//Nonnull fields are used included in the INSERT statement
	// to ensure there's data in the table.
	public void insert(Integer employid, String role, String action, String user, String timestamp, String records) {
		role = role != null ? "\"" + role + "\"" : null;
		action = action != null ? "\"" + action + "\"" : null;
		user = user != null ? "\"" + user + "\"" : null;
		timestamp = timestamp != null ? "\"" + timestamp + "\"" : null;
		records = records != null ? "\"" + records + "\"" : null;
		// Prepare arrays of values and corresponding fields
		Object[] values_ar = {employid, role, action, user, timestamp, records};
		String[] fields_ar = {auditlog.employid, auditlog.role, auditlog.action, auditlog.user, auditlog.timestamp, auditlog.records};
		// Build comma-separated field and value strings
		//Calls the values and corresponding array fields
		String values = "", fields = "";
		for (int i = 0; i < values_ar.length; i++) {
			if (values_ar[i] != null) {
				values += values_ar[i] + ", ";
				fields += fields_ar[i] + ", ";
			}
		}// Trim trailing commas and execute the INSERT query
		//Used to modify the values and fields.
		if (!values.isEmpty()) {
			values = values.substring(0, values.length() - 2);
			fields = fields.substring(0, fields.length() - 2);
			super.execute("INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");");
		}
	}
	/**
	 * Deletes a record from the audit log table.
	 *
	 * @param whatField field to match
	 * @param whatValue value to match
	 */
	//whatField  Field to filter out the column name. employeeID, name etc.
	//whatValue  Value to match for deletion
	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}
	/**
	 * Updates a record in the audit log table.
	 *
	 * @param whatField  field to update
	 * @param whatValue  new value
	 * @param whereField field to match
	 * @param whereValue value to match
	 */
	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}
	/**
	 * Selects records from the audit log table with optional filtering and sorting.
	 *
	 * @param fields     fields to select
	 * @param whatField  filter field
	 * @param whatValue  filter value
	 * @param sortField  sort field
	 * @param sort       sort direction
	 * @return list of records
	 */
	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}
	/**
	 * Searches audit logs using date range, role, and keyword filters.
	 *
	 * @param startDate start date (inclusive)
	 * @param endDate   end date (inclusive)
	 * @param role      role filter
	 * @param keyword   keyword filter
	 * @return filtered audit logs as a table model
	 */
	public DefaultTableModel searchAuditLogs(String startDate, String endDate, String role, String keyword) {
		StringBuilder query = new StringBuilder("SELECT * FROM auditlog WHERE ");
		// Date range filter
		query.append("timestamp >= '").append(startDate).append("' AND timestamp <= '").append(endDate).append("'");

		// Role filter
		if (role != null && !role.trim().equalsIgnoreCase("all") && !role.trim().isEmpty()) {
			query.append(" AND LOWER(role) = '").append(role.trim().toLowerCase()).append("'");
		}
		// Keyword filter
		if (keyword != null && !keyword.trim().isEmpty()) {
			String kw = keyword.trim().toLowerCase();
			query.append(" AND (");
			query.append("CAST(employid AS TEXT) LIKE '%").append(kw).append("%' OR ");
			query.append("LOWER(user) LIKE '%").append(kw).append("%' OR ");
			query.append("LOWER(records) LIKE '%").append(kw).append("%')");
		}    /**
		 * Executes a raw SQL query and returns the result.
		 *
		 * @param query SQL query string
		 * @return list of records
		 */
		// Sort by timestamp descending
		query.append(" ORDER BY timestamp DESC");
		return super.executeQueryToTable(query.toString());
	}
	/**
	 * Executes a raw SQL query without returning results.
	 *@param query SQL query string
	 */
	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}
	public void execute(String query) {
		super.execute(query);
	}   /**
	 * Selects records and returns them as a table model.
	 *
	 * @param fields     fields to select
	 * @param whatField  filter field
	 * @param whatValue  filter value
	 * @param sortField  sort field
	 * @param sort       sort direction
	 * @param string     unused parameter
	 * @return table model of selected records
	 */
	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort, String string) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

}