package Frontend.GUI.NhaCungCapGUI;
import javax.swing.*;
import java.awt.*;

public class NhaCungCapPanel extends JPanel{
    public NhaCungCapPanel(){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(new JLabel("MÀN HÌNH NHÀ CUNG CẤP", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}