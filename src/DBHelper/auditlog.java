package DBHelper;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class auditlog extends DBHelper {
	private final String TABLE_NAME = "auditlog";
	public static final String employid = "employid";
	public static final String role = "role";
	public static final String action = "action";
	public static final String user = "user";
	public static final String timestamp = "timestamp";
	public static final String records = "records";

	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}

	public void insert(Integer employid, String role, String action, String user, String timestamp, String records) {
		role = role != null ? "\"" + role + "\"" : null;
		action = action != null ? "\"" + action + "\"" : null;
		user = user != null ? "\"" + user + "\"" : null;
		timestamp = timestamp != null ? "\"" + timestamp + "\"" : null;
		records = records != null ? "\"" + records + "\"" : null;
		
		Object[] values_ar = {employid, role, action, user, timestamp, records};
		String[] fields_ar = {auditlog.employid, auditlog.role, auditlog.action, auditlog.user, auditlog.timestamp, auditlog.records};
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
		}

		// Sort by timestamp descending
		query.append(" ORDER BY timestamp DESC");

		return super.executeQueryToTable(query.toString());
	}

	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}

	public void execute(String query) {
		super.execute(query);
	}

	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort, String string) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

}