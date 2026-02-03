package Frontend.Compoent;

import javax.swing.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.*;

public class ButtonXuatPdf extends JButton{
    public ButtonXuatPdf(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(Color.WHITE);
        setBackground(new Color(44, 62, 80));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Loại bỏ viền mặc định khi focus
        setFocusPainted(false);
        setBorderPainted(false);
        try {
            FlatSVGIcon PdfIcon = new FlatSVGIcon("images/icon/file-text.svg", 16, 16);
            PdfIcon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.WHITE));
            setIcon(PdfIcon);
        } catch (Exception ex) {
            System.out.println("LỖI: Không tìm thấy file icon! " + ex.getMessage());
        }
        setIconTextGap(8);
        setMargin(new Insets(5, 5, 5, 5));
    }
    
}
