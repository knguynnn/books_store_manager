package Frontend.GUI.KhachHangGUI;
import javax.swing.*;
import java.awt.*;

public class KhachHangPanel extends JPanel{
    public KhachHangPanel(){
        setLayout(new BorderLayout());
        add(new JLabel("MÀN HÌNH KHÁCH HÀNG", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
