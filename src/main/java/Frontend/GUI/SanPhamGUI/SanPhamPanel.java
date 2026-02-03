package Frontend.GUI.SanPhamGUI;
import javax.swing.*;
import java.awt.*;

public class SanPhamPanel extends JPanel {
    public SanPhamPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("MÀN HÌNH QUẢN LÝ SẢN PHẨM - KHO ", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}