package Database;
import Model.Manager;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ManagerDB {
    private static String dbPath = "Terralina.db";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void setDatabasePath(String path) {
        dbPath = path;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    private Manager mapResultSet(ResultSet rs) throws SQLException {
        return new Manager(
                rs.getInt("employid"),
                rs.getString("role"),
                rs.getString("action"),
                rs.getString("user"),
                LocalDateTime.parse(rs.getString("timestamp"), formatter),
                rs.getString("records")
        );
    }

    public List<Manager> getAll() {
        List<Manager> list = new ArrayList<>();
        String sql = "SELECT * FROM auditLog";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching audit logs: " + e.getMessage());
        }
        return list;
    }

    public boolean save(Manager m) {
        String sql = "INSERT INTO auditLog (employid, role, action, user, timestamp, records) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getEmployId());
            stmt.setString(2, m.getRole());
            stmt.setString(3, m.getActionType());
            stmt.setString(4, m.getEmployName());
            stmt.setString(5, m.getTimestamp().format(formatter));
            stmt.setString(6, m.getRecords());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error saving audit log: " + ex.getMessage());
            return false;
        }
    }

    public Manager getById(int id) {
        String sql = "SELECT * FROM auditLog WHERE employid = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching audit log: " + e.getMessage());
        }
        return null;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM auditLog WHERE employid = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error deleting audit log: " + ex.getMessage());
            return false;
        }
    }
}
