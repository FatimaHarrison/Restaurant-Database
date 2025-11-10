//Importing local date for formatting
import java.time.LocalDate;
//Declaring primary class along with class attributes.
/**
 * The {@code EmployeeRecord} class represents a single employee's profile within the system.
 * <p>
 * It stores key attributes such as ID, name, gender, role, status, and hire date.
 * This class is commonly used in GUI workflows and database interactions.
 */
public class EmployeeRecord {
    private final int employID;//Employee id #
    private String employName;//Employee name
    private String employGen;//Employee Gender
    private String employRole;//Employee Role
    private String employStat;//Employee Status
    private String hireDate;//Hiredate
    /**
     * Constructs a new {@code EmployeeRecord} with the specified attributes.
     *
     * @param employID   employee ID
     * @param employName employee name
     * @param employGen  employee gender
     * @param employRole employee role
     * @param employStat employment status
     * @param hireDate   hire date as a string
     */
    public EmployeeRecord(int employID, String employName, String employGen,
                          String employRole, String employStat, String hireDate) {
        //Parameterized contractors
        this.employID = employID;
        this.employName = employName;
        this.employGen = employGen;
        this.employRole = employRole;
        this.employStat = employStat;
        this.hireDate = hireDate;

    }
    // Getters methods
    /** @return employee ID
     *  @return employee name
     *  @return employee gender
     *  @return employee role
     *  @return employee status
     * */
    public int getEmployID() {
        return employID;
    }
    public String getEmployName() {
        return employName;
    }
    public String getEmployGen() {
        return employGen;
    }
    public String getEmployRole() {
        return employRole;
    }
    public String getEmployStat() {
        return employStat;
    }
    public String getHireDate() { return hireDate;
    }
    //Gets each stored in data from file.

    // Setters for parametrized constructors.
    public void setEmployName(String employName) {
        this.employName = employName;
    }
    public void setEmployGen(String employGen) {
        this.employGen = employGen;
    }
    public void setEmployRole(String employRole) {
        this.employRole = employRole;
    }
    public void setEmployStat(String employStat) {
        this.employStat = employStat;
    }
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
    /**
     * Returns a formatted string representation of the employee record.
     * <p>
     * Used for logging, debugging, or GUI display.
     *
     * @return formatted employee details
     */
    @Override //Wired to employee record GUI file
    public String toString() { //Displaying format
        return String.format("ID: %d | Name: %s | Gender: %s | Role: %s | Status: %s | Hire Date: %s",
                employID, employName, employGen, employRole, employStat, hireDate.toString());//Gets stored in data.
    }
}
