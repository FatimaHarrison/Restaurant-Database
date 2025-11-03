//Declaring location of the database.
package Database;
//Importing the files functions
import Model.Reservation; //Importing reservation file from models.
import java.sql.*; //Importing the GUI functions.
import java.util.ArrayList;
import java.util.List;
//Declaring primary class "HostDB"
public class HostDB {
    private static String dbPath;
//Setting the database path.
    public static void setDatabasePath(String path) {
        dbPath = path;
    }
//Gives user an error message if the path is not successfully set.
    private static Connection connect() throws SQLException {
        if (dbPath == null || dbPath.isEmpty()) {
            throw new SQLException("Database path not set.");
        } //Returns the path connection to file.
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }
   //Map method is used to convert SQL result set in a reservation object.
    //Getter to retrieve each results sets to be converted.
    private static Reservation mapResultSet(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phoneNumber"),
                rs.getInt("partySize"),
                rs.getString("date"),
                rs.getString("time"),
                rs.getString("notes")
        );
    }
//Method to retrieve all data within the reservation's table.
    public static List<Reservation> getAll() {
        List<Reservation> list = new ArrayList<>();//The array list of reservations
        String sql = "SELECT * FROM reservations"; //SQL schema
//Method to execute a SQL query and populate a list with Reservation objects.
        try (Connection conn = connect(); //Connects with database
             Statement stmt = conn.createStatement();//Generate statement to get the rs.
             ResultSet rs = stmt.executeQuery(sql)) { //from the Result sst.

            while (rs.next()) {
                list.add(mapResultSet(rs));
            } //Give the user an error message.
        } catch (SQLException e) {//Cannot get reservations data.
            System.out.println("Error fetching reservations: " + e.getMessage());
        }
        return list;
    }
//SQL schema to allow user to insert new data onto the table.
    public boolean save(Reservation r) {//Method to save the data onto table.
        String sql = "INSERT INTO reservations (name, email, phoneNumber, partySize, date, time, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        //Connection to database.
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Statements to set set data onto reservations table.
            stmt.setString(1, r.getName());
            stmt.setString(2, r.getEmail());
            stmt.setString(3, r.getPhoneNumber());
            stmt.setInt(4, r.getPartySize());
            stmt.setString(5, r.getDate());
            stmt.setString(6, r.getTime());
            stmt.setString(7, r.getNotes());


            return stmt.executeUpdate() > 0;//No updating data
        } catch (SQLException e) { //Notify user if an error occurs.
            System.out.println("Error saving reservation: " + e.getMessage());
            return false;
        }
    }

//SQL schema for useer to search a reservation by ID.
    public Reservation getById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
//Connects to the database
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {//Generate statement
            //Get statement from first of the parameterindex
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery(); //Using the result set to execute the query.

            if (rs.next()) { //If the results set exist it will pass.
                return mapResultSet(rs);
            }
        } catch (SQLException e) { //Otherwise notify user of error.
            System.out.println("Error fetching reservation: " + e.getMessage());
        }
        return null;
    }
//SQL query to update data from the table.
    public boolean update(Reservation r) {
        String sql = "UPDATE reservations SET name = ?, email = ?, phoneNumber = ?, partySize = ?, date = ?, time = ?, notes = ? WHERE id = ?";
//Connects to the database
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //Generates a statement to get the result set.
            //ParameterIndex to choose to update from
            stmt.setString(1, r.getName());
            stmt.setString(2, r.getEmail());
            stmt.setString(3, r.getPhoneNumber());
            stmt.setInt(4, r.getPartySize());
            stmt.setString(5, r.getDate());
            stmt.setString(6, r.getTime());
            stmt.setString(7, r.getNotes());
            stmt.setInt(8, r.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { //Notify user if an error occurred.
            System.out.println("Error updating reservation: " + e.getMessage());
            return false;
        }
    }
//SQL query to remove data from table
    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
//Connects to the database
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
//Fetching and setting the parameterindex
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; //deletes the data
        } catch (SQLException e) {//Notify if error has occurred.
            System.out.println("Error deleting reservation: " + e.getMessage());
            return false;
        }
    }
}
