package Frontend.Compoent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchTextField extends JTextField {
    private String placeholder;
    
    public SearchTextField() {
        this("");
    }
    
    public SearchTextField(String placeholder) {
        this.placeholder = placeholder;
        initComponent();
    }
    
    private void initComponent() {
        setPreferredSize(new Dimension(250, 35));
        putClientProperty("JTextField.placeholderText", placeholder);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 35, 5, 10)
        ));
        setBackground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    // Thêm phương thức setPlaceholder
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        putClientProperty("JTextField.placeholderText", placeholder);
        repaint(); // Vẽ lại để hiển thị placeholder mới
    }
    
    public String getPlaceholder() {
        return placeholder;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Chỉ vẽ icon khi không có text và có placeholder
        if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Vẽ icon search (🔍) hoặc icon tùy chọn
            g2d.setColor(new Color(107, 114, 128));
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            // Vẽ icon ở bên trái
            g2d.drawString("🔍", 10, getHeight() / 2 + 5);
            
            // Nếu muốn vẽ cả placeholder text (optional)
            // g2d.setColor(new Color(156, 163, 175));
            // g2d.drawString(placeholder, 35, getHeight() / 2 + 5);
            
            g2d.dispose();
        }
    }
}