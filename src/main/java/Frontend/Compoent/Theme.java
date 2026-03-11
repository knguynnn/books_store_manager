package Frontend.Compoent;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color PRIMARY = new Color(15, 23, 42);    // Navy
    public static final Color BACKGROUND = new Color(248, 250, 252); // Ivory
    public static final Color ACCENT = new Color(201, 162, 39);    // Gold
    public static final Color TEXT = new Color(31, 41, 55);       // Dark gray
    public static final Color BORDER = new Color(229, 231, 235);  // Light gray
    public static final Color SECONDARY_COLOR = new Color(17, 24, 39);

    // Màu chức năng
    public static final Color ACCENT_COLOR = new Color(42, 187, 155);   // Xanh lá (Thêm/Lưu)
    public static final Color DANGER_COLOR = new Color(235, 87, 87);   // Đỏ (Xóa/Đăng xuất)
    public static final Color WARNING_COLOR = new Color(243, 156, 18); // Vàng (Cảnh báo/Sửa)
    public static final Color PURPLE_COLOR = new Color(155, 89, 182); // Tím (Làm mới)

    // Thêm các màu cho table
    public static final Color TABLE_HEADER_BG = PRIMARY;
    public static final Color TABLE_HEADER_FG = Color.WHITE;
    public static final Color TABLE_SELECTION_BG = new Color(59, 130, 246, 50);
    public static final Color TABLE_GRID_COLOR = new Color(229, 231, 235);
    public static final Color TABLE_INACTIVE_BG = new Color(248, 249, 250);
    public static final Color TABLE_INACTIVE_FG = new Color(108, 117, 125);
    public static final Color TABLE_ACTIVE_COLOR = new Color(34, 197, 94);  // Xanh lá cho Active
    public static final Color TABLE_INACTIVE_COLOR = new Color(239, 68, 68); // Đỏ cho Inactive
    
    // Thêm các màu cho button states
    public static final Color BUTTON_HOVER_ADD = new Color(34, 197, 94).darker();
    public static final Color BUTTON_HOVER_EDIT = new Color(245, 158, 11).darker();
    public static final Color BUTTON_HOVER_DELETE = new Color(239, 68, 68).darker();
    public static final Color BUTTON_HOVER_REFRESH = new Color(59, 130, 246).darker();

    public static final int ROUNDING_ARC = 15;

    public static void setup(boolean isDark) {
        try {
            if(isDark) FlatDarkLaf.setup();
            else FlatLightLaf.setup();

            // Cấu hình UI Defaults
            UIManager.put("Button.arc", ROUNDING_ARC);
            UIManager.put("Component.arc", ROUNDING_ARC);
            UIManager.put("TextComponent.arc", ROUNDING_ARC);
            UIManager.put("CheckBox.arc", 5);
            UIManager.put("ScrollBar.thumbArc", 999);

            // Tinh chỉnh Table hiện đại
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            UIManager.put("Table.selectionBackground", new Color(232, 236, 241));
            UIManager.put("Table.selectionForeground", Color.BLACK);

            // Chỉnh màu sắc focus cho hiện đại
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Button.innerFocusWidth", 0);
        } catch (Exception ex) {
            System.err.println("Không thể thiết lập Theme!");
        }
    }
}
