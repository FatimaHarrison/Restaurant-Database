package DBHelper;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.util.ArrayList;

public class Reservation extends DBHelper {
	private final String TABLE_NAME = "Reservation";
	public static final String id = "id";
	public static final String name = "name";
	public static final String email = "email";
	public static final String phoneNumber = "phoneNumber";
	public static final String partySize = "partySize";
	public static final String date = "date";
	public static final String time = "time";
	public static final String notes = "notes";
	private ArrayList<ArrayList<Object>> data;

	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}

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

	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}

	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}

	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);

	}

	    // Optional passthrough
		public void execute (String query){
			super.execute(query);
		}


	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort, String s) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));

	}

	public ArrayList<ArrayList<Object>> getData() {
		return data;
	}

	public void setData(ArrayList<ArrayList<Object>> data) {
		this.data = data;
	}
}

