package Frontend.GUI.ThongKeGUI;
import javax.swing.*;
import java.awt.*;

public class ThongKePanel extends JPanel{
    public ThongKePanel(){
        setLayout(new BorderLayout());
        add(new JLabel("MÀN HÌNH THỐNG KÊ", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}