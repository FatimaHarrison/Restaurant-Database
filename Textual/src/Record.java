//Declaring GUI imports
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//Declaring primary class along with class attributes.
public class Record {
    private Integer id;//Used for customer ID number
    private String name;//Customer name
    private String email;//Customer email
    private String phoneNumber;//Customer phone number
    private Integer partySize;//Desired party size
    private String date;//date of reso
    private String time;//tike of reso
    private String notes;//Additional notes optional
//Setter methods
    public Record(Integer id, String name, String email, String phoneNumber,
                  Integer partySize, String date, String time, String notes) {
        setId(id); //Sets each class attributes as constructors.
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPartySize(partySize);
        setDate(date);
        setTime(time);
        setNotes(notes);
    }
//Getter methods
    //Gets each store in data from file and or table
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Integer getPartySize() { return partySize; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getNotes() { return notes; }
//Exceptions validations for ID number
    public void setId(Integer id) { //Restriction numbers to be 7 digits and in between given numbers.
        if (id == null || id < 1000000 || id > 9999999) {
            throw new IllegalArgumentException("ID must be a 7-digit number."); //Error message
        }
        this.id = id;//Constructor.
    }
//Exceptions validations for customer name
    public void setName(String name) { //Restriction for name length.
        if (name == null || name.trim().isEmpty() || name.length() > 25) {
            throw new IllegalArgumentException("Name must be 1–25 characters.");//Error message
        }
        this.name = name.trim();//Constructor.
    }
    //Exceptions validations for customer email
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email.trim();//Constructor.
    }
    //Exceptions validations for customer phone number
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;//Constructor.
    }
    //Exceptions validations for customer reservation party size
    public void setPartySize(Integer partySize) {
        if (partySize == null || partySize < 1 || partySize > 13) {//Checks if user input match restriction.
            throw new IllegalArgumentException("Party size must be between 1 and 13.");//Error message.
        }
        this.partySize = partySize;//Constructor.
    }
    //Exceptions validations for entering reservation date
    public void setDate(String date) {
        if (date == null || !date.matches("\\d{4}/\\d{2}/\\d{2}")) { //Error message if date is empty or incorrect format.
            throw new IllegalArgumentException("Date must be in YYYY/MM/DD format.");
        }
        this.date = date;//Constructor.
    }
    //Exceptions validations for entering reservation time
    public void setTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty.");//Error message
        }
        try {
            //Restrictions on time format.
            time = time.trim().toUpperCase().replaceAll("(?<=\\d)(AM|PM)", " $1");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");//PM or AM lowercase or uppercase
            LocalTime parsedTime = LocalTime.parse(time, formatter);
            this.time = parsedTime.format(formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Time must be in hh:mm AM/PM format (e.g., 02:30 PM).");//Error message
        }
    }
    //Exceptions validations for entering reservation notes
    public void setNotes(String notes) {
        if (notes == null || notes.matches(".*[<>\"].*")) { //Gives restrictions on notes
            throw new IllegalArgumentException("Notes contain unsafe characters."); //Error message
        }
        this.notes = notes.trim();//Constructor.
    }

    @Override //Wired to Host GUI
    public String toString() {//Displaying format
        return String.format("ID: %d | Name: %s | Email: %s | Phone: %s | Party Size: %d | Date: %s | Time: %s | Notes: %s", //Print out format
                id, name, email, phoneNumber, partySize, date, time, notes); //Gets stored data
    }
}
