import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * The {@code AuditLog} class provides static methods for writing audit entries to a log file.
 * <p>
 * It supports both structured logging (with employee metadata) and simple message-based logging.
 * Entries are timestamped and appended to a persistent file for traceability and compliance.
 */
//declaring class
public class AuditLog {
    // Default path where audit logs will be saved
    private static final String DEFAULT_DATA_PATH  = "Terralina GUI/Textual/Data";
    // Formatter for timestamp entries
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    /**
     * Writes a structured audit entry to the log file.
     * <p>
     * The entry includes employee ID, role, action type, timestamp, name, and additional record notes.
     * Fields are separated by hyphens for easy parsing.
     *
     * @param employId   employee ID performing the action
     * @param role       role of the employee (e.g., Manager, Host)
     * @param actionType type of action performed (e.g., Login, Insert, Delete)
     * @param employName name of the employee
     * @param records    additional notes or context for the action
     */
    // Log structured audit entry
    public static void log(int employId, String role, String actionType, String employName, String records) {
        LocalDateTime timestamp = LocalDateTime.now();
        // Construct a hyphen-separated log entry
        String entry = String.join("-",
                String.valueOf(employId),
                role,
                actionType,
                timestamp.format(FORMATTER),
                employName,
                records);
        // Append the entry to the audit log file
        try (PrintWriter writer = new PrintWriter(new FileWriter(DEFAULT_DATA_PATH , true))) {
            writer.println(entry);
        } catch (Exception e) {
            //Error message
            System.out.println("Audit log error: " + e.getMessage());
        }
    }
    /**
     * Writes a simple message-based audit entry to the log file.
     * <p>
     * The message is prefixed with a timestamp and used for general system events or errors.
     *
     * @param message the message to log
     */
    // message-only log
    public static void log(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        try (PrintWriter writer = new PrintWriter(new FileWriter(DEFAULT_DATA_PATH , true))) {
            writer.println(timestamp.format(FORMATTER) + " - " + message);
        } catch (Exception e) {
            System.out.println("Audit log error: " + e.getMessage());
        }
    }
}
