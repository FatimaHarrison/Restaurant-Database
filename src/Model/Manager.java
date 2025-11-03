package Model;

import java.time.LocalDateTime;

public class Manager {
    private final Integer employId;       // Employee or customer ID
    private final String role;            // Employee role
    private final String actionType;      // Action type (e.g., point host, floor manager)
    private String employName = "";       // Optional name or merch ID
    private final LocalDateTime timestamp;// Timestamp of action
    private final String records;         // Ratings or comments

    public Manager(Integer employId, String role, String actionType, String employName, LocalDateTime timestamp, String records) {
        this.employId = employId;
        this.role = role;
        this.actionType = actionType;
        this.employName = employName;
        this.timestamp = timestamp;
        this.records = records;
    }

    // Getters
    public Integer getEmployId() { return employId; }
    public String getRole() { return role; }
    public String getActionType() { return actionType; }
    public String getEmployName() { return employName; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getRecords() { return records; }

    // Setter for optional name
    public void setEmployName(String employName) {
        this.employName = employName;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Role: %s | Action: %s | Name: %s | Time: %s | Notes: %s",
                employId, role, actionType, employName, timestamp.toString(), records);
    }
}
