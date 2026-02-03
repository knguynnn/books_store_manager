package Frontend.Compoent;

import javax.swing.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.*;

public class ButtonDele extends JButton{
    public ButtonDele(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(Theme.DANGER_COLOR);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
        setBorderPainted(false);

        try {
            FlatSVGIcon PdfIcon = new FlatSVGIcon("images/icon/circle-minus.svg", 16, 16);
            PdfIcon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.WHITE));
            setIcon(PdfIcon);
        } catch (Exception ex) {
            System.out.println("LỖI: Không tìm thấy file icon! " + ex.getMessage());
        }
        setIconTextGap(8);
    }
}
