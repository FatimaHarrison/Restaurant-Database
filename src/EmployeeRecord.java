//Importing local date for formatting
import java.time.LocalDate;
//Declaring primary class along with class attributes.
public class EmployeeRecord {
    private final int employID;//Employee id #
    private String employName;//Employee name
    private String employGen;//Employee Gender
    private String employRole;//Employee Role
    private String employStat;//Employee Status
    private String hireDate;//Hiredate
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

    @Override //Wired to employee record GUI file
    public String toString() { //Displaying format
        return String.format("ID: %d | Name: %s | Gender: %s | Role: %s | Status: %s | Hire Date: %s",
                employID, employName, employGen, employRole, employStat, hireDate.toString());//Gets stored in data.
    }
}
