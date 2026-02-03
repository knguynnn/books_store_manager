package Frontend.Compoent;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton{
    public CustomButton(String text, Color bg) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(bg);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Loại bỏ viền mặc định khi focus
        setFocusPainted(false);
        setBorderPainted(false);
    }
    
}
