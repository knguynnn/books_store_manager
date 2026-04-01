import Frontend.Compoent.Theme;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        Theme.setup(false);
        SwingUtilities.invokeLater(() -> {
            new Frontend.GUI.LoginGUI().setVisible(true);
        });
    }
}