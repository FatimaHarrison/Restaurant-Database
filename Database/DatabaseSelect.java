package Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class DatabaseSelect {

    public static String promptForDatabasePath(ActionListener parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select SQLite Database File");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = chooser.showOpenDialog((Component) parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        } else {
            JOptionPane.showMessageDialog((Component) parent, "No database selected. Please try again.");
            return null;
        }
    }
}
