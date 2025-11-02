package Model;

public class Reservation {
    private int id;
    private String name;
    private String email;
    private String phone;
    private int partySize;
    private String date;
    private String time;
    private String notes;

    // Constructor for new inserts (no ID yet)
    public Reservation(String name, String email, String phone, int partySize, String date, String time, String notes) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.partySize = partySize;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    // Constructor with ID (for reads and updates)
    public Reservation(int id, String name, String email, String phone, int partySize, String date, String time, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.partySize = partySize;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getPartySize() { return partySize; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPartySize(int partySize) { this.partySize = partySize; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Email: %s | Phone: %s | Party Size: %d | Date: %s | Time: %s | Notes: %s",
                id, name, email, phone, partySize, date, time, notes);
    }
}
