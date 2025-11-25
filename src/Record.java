//Declaring GUI imports
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
/**
 * The {@code Record} class represents a reservation entry for a customer.
 * <p>
 * It includes customer details, reservation metadata, and validation logic
 * for each field to ensure data integrity before insertion or display.
 */
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
    /**
     * Constructs a new {@code Record} with validated reservation details.
     *
     * @param id          7-digit customer ID
     * @param name        customer name
     * @param email       customer email
     * @param phoneNumber customer phone number
     * @param partySize   number of guests
     * @param date        reservation date
     * @param time        reservation time
     * @param notes       optional notes
     */
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
    /**
     * Sets and validates the customer ID.
     * Must be a 7-digit number between 1000000 and 9999999.
     *
     * @param id customer ID
     */
    public void setId(Integer id) { //Restriction numbers to be 7 digits and in between given numbers.
        if (id == null || id < 1000000 || id > 9999999) {
            throw new IllegalArgumentException("ID must be a 7-digit number."); //Error message
        }
        this.id = id;//Constructor.
    }
    /**
     * Sets and validates the customer name.
     * Must be 1–25 characters and non-empty.
     *
     * @param name customer name
     */
//Exceptions validations for customer name
    public void setName(String name) { //Restriction for name length.
        if (name == null || name.trim().isEmpty() || name.length() > 25) {
            throw new IllegalArgumentException("Name must be 1–25 characters.");//Error message
        }
        this.name = name.trim();//Constructor.
    }
    /**
     * Sets and validates the customer email.
     * Must contain an "@" symbol.
     *
     * @param email customer email
     */
    //Exceptions validations for customer email
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email.trim();//Constructor.
    }  /**
     * Sets and validates the customer phone number.
     * Must be exactly 10 digits.
     *
     * @param phoneNumber customer phone number
     */
    //Exceptions validations for customer phone number
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;//Constructor.
    }
    /**
     * Sets and validates the party size.
     * Must be between 1 and 13.
     *
     * @param partySize number of guests
     */
    //Exceptions validations for customer reservation party size
    public void setPartySize(Integer partySize) {
        if (partySize == null || partySize < 1 || partySize > 13) {//Checks if user input match restriction.
            throw new IllegalArgumentException("Party size must be between 1 and 13.");//Error message.
        }
        this.partySize = partySize;//Constructor.
    }

    /**
     * Sets and validates the reservation date.
     * Must match {@code YYYY/MM/DD} format.
     *
     * @param date reservation date
     */
    //Exceptions validations for entering reservation date
    public void setDate(String date) {
        if (date == null || !date.matches("\\d{4}/\\d{2}/\\d{2}")) { //Error message if date is empty or incorrect format.
            throw new IllegalArgumentException("Date must be in YYYY/MM/DD format.");
        }
        this.date = date;//Constructor.
    }

    /**
     * Sets and validates the reservation time.
     * Must match {@code hh:mm AM/PM} format.
     *
     * @param time reservation time
     */
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
    /**
     * Sets and validates optional notes.
     * Notes must not contain unsafe characters like {@code < > "} and will be trimmed.
     *
     * @param notes reservation notes
     */
    //Exceptions validations for entering reservation notes
    public void setNotes(String notes) {
        if (notes == null || notes.matches(".*[<>\"].*")) { //Gives restrictions on notes
            throw new IllegalArgumentException("Notes contain unsafe characters."); //Error message
        }
        this.notes = notes.trim();//Constructor.
    }
    /**
     * Returns a formatted string representation of the reservation record.
     * <p>
     * Used for logging, debugging, or GUI display.
     *
     * @return formatted reservation details
     */
    @Override //Wired to Host GUI
    public String toString() {//Displaying format
        return String.format("ID: %d | Name: %s | Email: %s | Phone: %s | Party Size: %d | Date: %s | Time: %s | Notes: %s", //Print out format
                id, name, email, phoneNumber, partySize, date, time, notes); //Gets stored data
    }
}
