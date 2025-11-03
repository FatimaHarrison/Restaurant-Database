package Model;

public class Reservation {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private int partySize;
    private String date;
    private String time;
    private String notes;

    // Constructor for new inserts (no ID yet)
    public Reservation(String name, String email, String phoneNumber, int partySize, String date, String time, String notes) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.partySize = partySize;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    // Constructor with ID (for reads and updates)
    public Reservation(int id, String name, String email, String phoneNumber, int partySize, String date, String time, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.partySize = partySize;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getPartySize() { return partySize; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPartySize(int partySize) { this.partySize = partySize; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return String.format(
                "ID: %d\nName: %s\nEmail: %s\nPhone: %s\nParty Size: %d\nDate: %s\nTime: %s\nNotes: %s\n",
                id, name, email, phoneNumber, partySize, date, time, notes
        );

    }
}
