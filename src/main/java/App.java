import Frontend.GUI.MainFrame;
import Frontend.Compoent.Theme;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // 1. Cài đặt giao diện FlatLaf (Giữ nguyên cấu hình Theme của bạn)
        Theme.setup(false); // false = Giao diện sáng (Light Mode)

        // 2. Chạy giao diện trên luồng Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Thay vì mở thẳng MainFrame, ta mở LoginGUI để thực hiện đăng nhập
            new Frontend.GUI.LoginGUI().setVisible(true);
        });
    }
}