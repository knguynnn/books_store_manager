package Frontend.Compoent;

import javax.swing.*;
import java.awt.*;

public class InfoField extends JTextField{
    public InfoField(String text) {
        super(text);
        setEditable(false);
        setBackground(new Color(245, 246, 247));
        setForeground(new Color(50, 50, 50));
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        putClientProperty("FlatLaf.style", 
            "arc: " + Theme.ROUNDING_ARC + "; " +
            "borderColor: #DCDCDC; " + 
            "focusedBorderColor: #2196F3");
        setMargin(new Insets(5, 12, 5, 12));
    }
}
