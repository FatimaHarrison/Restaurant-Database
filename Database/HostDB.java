package Database;
import Model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HostDB {
    private static String dbPath = "Terralina.db";

    public static void setDatabasePath(String path) {
        dbPath = path;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    private Reservation mapResultSet(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getInt("party_size"),
                rs.getString("date"),
                rs.getString("time"),
                rs.getString("notes")
        );
    }

    public List<Reservation> getAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching reservations: " + e.getMessage());
        }
        return list;
    }

    public boolean save(Reservation r) {
        String sql = "INSERT INTO reservations (name, email, phoneNumber, partySize, date, time, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getName());
            stmt.setString(2, r.getEmail());
            stmt.setString(3, r.getPhone());
            stmt.setInt(4, r.getPartySize());
            stmt.setString(5, r.getDate());
            stmt.setString(6, r.getTime());
            stmt.setString(7, r.getNotes());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error saving reservation: " + e.getMessage());
            return false;
        }
    }

    public Reservation getById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching reservation: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Reservation r) {
        String sql = "UPDATE reservations SET name = ?, email = ?, phoneNumber = ?, partySize = ?, date = ?, time = ?, notes = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getName());
            stmt.setString(2, r.getEmail());
            stmt.setString(3, r.getPhone());
            stmt.setInt(4, r.getPartySize());
            stmt.setString(5, r.getDate());
            stmt.setString(6, r.getTime());
            stmt.setString(7, r.getNotes());
            stmt.setInt(8, r.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating reservation: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting reservation: " + e.getMessage());
            return false;
        }
    }
}
