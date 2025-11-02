import Database.DatabaseSelect;
import Database.HostDB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatabaseGUI {
    private JButton DBbtn;
    private JLabel Title;
    private JPanel DatabaseGUI;
    private JPanel mainPanel;

    public DatabaseGUI() {
        JFrame frame = new JFrame("Select Database");
        frame.setContentPane(null);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        DBbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Use the frame as the parent component
                String dbPath = DatabaseSelect.promptForDatabasePath((ActionListener) frame);
                if (dbPath != null) {
                    HostDB.setDatabasePath(dbPath); // or Employeedb.setDatabasePath(dbPath)
                    JOptionPane.showMessageDialog(frame, "Database loaded:\n" + dbPath);
                }
            }
        });
    }
}
