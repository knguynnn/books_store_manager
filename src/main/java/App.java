import Frontend.GUI.MainFrame;
import Frontend.Compoent.Theme;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Cài đặt giao diện FlatLaf (Sáng hoặc Tối)
        Theme.setup(false); // false = Giao diện sáng (Light Mode)

        // Chạy giao diện trên luồng Event Dispatch Thread (luồng an toàn của Swing)
        SwingUtilities.invokeLater(() -> {
            MainFrame main = new MainFrame();
            main.setVisible(true);
        });
    }
}