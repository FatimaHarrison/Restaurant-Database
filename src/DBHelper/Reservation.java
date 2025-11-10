//Package location
package DBHelper;
//Importing file functions.
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.util.ArrayList;
/**
 * The {@code Reservation} class provides database operations for managing reservation records.
 * <p>
 * It supports inserting, updating, deleting, filtering, and displaying reservation data.
 * This class extends {@link DBHelper} to leverage JDBC execution logic.
 */
public class Reservation extends DBHelper {
	// Table name used for reservation operations
	private final String TABLE_NAME = "Reservation";
	// Column name constants for reservation fields
	public static final String id = "id";
	public static final String name = "name";
	public static final String email = "email";
	public static final String phoneNumber = "phoneNumber";
	public static final String partySize = "partySize";
	public static final String date = "date";
	public static final String time = "time";
	public static final String notes = "notes";
	private ArrayList<ArrayList<Object>> data;
	// Internal data cache
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
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}
	/**
	 * Inserts a new reservation record into the database.
	 *
	 * @param id          reservation ID
	 * @param name        guest name
	 * @param email       guest email
	 * @param phoneNumber guest phone number
	 * @param partySize   number of guests
	 * @param date        reservation date
	 * @param time        reservation time
	 * @param notes       additional notes
	 */
	public void insert(Integer id, String name, String email, String phoneNumber, Integer partySize, String date, String time, String notes) {
		name = name != null ? "\"" + name + "\"" : null;
		email = email != null ? "\"" + email + "\"" : null;
		phoneNumber = phoneNumber != null ? "\"" + phoneNumber + "\"" : null;
		date = date != null ? "\"" + date + "\"" : null;
		time = time != null ? "\"" + time + "\"" : null;
		notes = notes != null ? "\"" + notes + "\"" : null;

		Object[] values_ar = {id, name, email, phoneNumber, partySize, date, time, notes};
		String[] fields_ar = {Reservation.id, Reservation.name, Reservation.email, Reservation.phoneNumber, Reservation.partySize, Reservation.date, Reservation.time, Reservation.notes};
		String values = "", fields = "";
		for (int i = 0; i < values_ar.length; i++) {
			if (values_ar[i] != null) {
				values += values_ar[i] + ", ";
				fields += fields_ar[i] + ", ";
			}
		}
		if (!values.isEmpty()) {
			values = values.substring(0, values.length() - 2);
			fields = fields.substring(0, fields.length() - 2);
			super.execute("INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");");
		}
	}

	/**
	 * Deletes a reservation record from the database.
	 *
	 * @param whatField field to match (e.g., id)
	 * @param whatValue value to match
	 */
	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}
	/**
	 * Updates a field in a reservation record.
	 *
	 * @param whatField  field to update
	 * @param whatValue  new value
	 * @param whereField field to match
	 * @param whereValue value to match
	 */
	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}
	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}
	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);

	}
	/**
	 * Executes a raw SQL query and returns the result.
	 *
	 * @param query SQL query string
	 * @return list of records
	 */
	// Optional passthrough
	public void execute (String query){
			super.execute(query);
		}
	/**
	 * Selects records and returns them as a table model for GUI display.
	 *
	 * @param fields     fields to select
	 * @param whatField  filter field
	 * @param whatValue  filter value
	 * @param sortField  sort field
	 * @param sort       sort direction
	 * @param s          unused parameter
	 * @return table model of selected records
	 */
		public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort, String s) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));

	} /**
	 * Gets the cached reservation data.
	 *@return reservation data
	 */
	public ArrayList<ArrayList<Object>> getData() {
		return data;
	}
	/**
	 * Sets the cached reservation data.
	 * @param data reservation data
	 */
	public void setData(ArrayList<ArrayList<Object>> data) {
		this.data = data;
	}
}

