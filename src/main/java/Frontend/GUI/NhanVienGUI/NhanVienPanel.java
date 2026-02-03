package Frontend.GUI.NhanVienGUI;
import javax.swing.*;
import java.awt.*;

public class NhanVienPanel extends JPanel{
    public NhanVienPanel(){
        setLayout(new BorderLayout());
        add(new JLabel("MÀN HÌNH NHÂN VIÊN", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}