package Frontend.GUI.PhanQuyenGUI;
import javax.swing.*;
import java.awt.*;

public class PhanQuyenPanel extends JPanel{
    public PhanQuyenPanel(){
        setLayout(new BorderLayout());
        add(new JLabel("MÀN HÌNH PHÂN QUYỀN", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}