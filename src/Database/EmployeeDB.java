package Database;

import Model.EmployeeRec;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDB {
    private static String dbPath;
    private LocalDate hireDate;

    public static void setDatabasePath(String path) {
        dbPath = path;
    }

    private Connection connect() throws SQLException {
        if (dbPath == null || dbPath.isEmpty()) {
            throw new SQLException("Database path not set.");
        }
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    private EmployeeRec mapResultSet(ResultSet rs) throws SQLException {
        return new EmployeeRec(
                rs.getInt("employeeID"),
                rs.getString("employName"),
                rs.getString("employGen"),
                rs.getString("employRole"),
                rs.getString("employStat"),
                rs.getString("hireDate")
        );
    }

    public List<EmployeeRec> getAll() {
        List<EmployeeRec> list = new ArrayList<>();
        String sql = "SELECT * FROM employeeRecords";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
        return list;
    }

    public boolean save(EmployeeRec e) {
        String sql = "INSERT INTO employeeRecords (employeeID, employName, employGen, employRole, employStat, hireDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, e.getEmployID()); // ✅ Explicit ID
            stmt.setString(2, e.getEmployName());
            stmt.setString(3, e.getEmployGen());
            stmt.setString(4, e.getEmployRole());
            stmt.setString(5, e.getEmployStat());
            stmt.setString(6, e.getHireDate()); // Assuming hireDate is a String

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error saving employee: " + ex.getMessage());
            return false;
        }
    }

    public EmployeeRec getById(int id) {
        String sql = "SELECT * FROM employeeRecords WHERE employeeID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching employee: " + e.getMessage());
        }
        return null;
    }

    public boolean update(EmployeeRec e) {
        String sql = "UPDATE employeeRecords SET employName = ?, employGen = ?, employRole = ?, employStat = ?, hireDate = ? WHERE employeeID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getEmployName());
            stmt.setString(2, e.getEmployGen());
            stmt.setString(3, e.getEmployRole());
            stmt.setString(4, e.getEmployStat());
            stmt.setString(5, hireDate.toString());
            stmt.setInt(6, e.getEmployID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating employee: " + ex.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM employeeRecords WHERE employeeID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error deleting employee: " + ex.getMessage());
            return false;
        }
    }
}
