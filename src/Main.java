import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CampusWayGUI gui = new CampusWayGUI();
            gui.setVisible(true);
        });
    }
}
