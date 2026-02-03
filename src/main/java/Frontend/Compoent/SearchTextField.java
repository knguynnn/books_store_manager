package Frontend.Compoent;

import javax.swing.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;

public class SearchTextField extends JTextField{
    public SearchTextField(String placeholder) {
        setBackground(new Color(240, 242, 245));
        putClientProperty("JTextField.placeholderText", placeholder);
        putClientProperty("JTextField.showClearButton", true);
        putClientProperty("FlatLaf.style", "arc: 20");
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        try {
            FlatSVGIcon searchIcon = new FlatSVGIcon("images/icon/search.svg", 16, 16);
            searchIcon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> new Color(150, 150, 150)));
            putClientProperty("JTextField.leadingIcon", searchIcon);
        } catch (Exception ex) {
            System.out.println("LỖI: Không tìm thấy file icon! " + ex.getMessage());
        }
        setMargin(new Insets(5, 10, 5, 5));
    }
    
}
