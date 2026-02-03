package Frontend.GUI;

import Frontend.Compoent.Theme;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Sidebar extends JPanel {
    private MainFrame mainFrame;
    private ArrayList<JButton> listButtons = new ArrayList<>();
    private JPanel pnlMenu; // Chứa logo và các nút menu

    public Sidebar(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
    }

    private void initComponent() {
        this.setPreferredSize(new Dimension(250, 800));
        this.setBackground(Theme.PRIMARY);
        this.setLayout(new BorderLayout()); // Dùng BorderLayout để đẩy nút xuống dưới

        // --- PHẦN TRÊN: LOGO VÀ MENU ---
        pnlMenu = new JPanel();
        pnlMenu.setOpaque(false);
        pnlMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        // 1. Logo ứng dụng
        JLabel lbLogo = new JLabel("BOOK STORE", SwingConstants.CENTER);
        lbLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbLogo.setForeground(Theme.ACCENT);
        lbLogo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        lbLogo.setPreferredSize(new Dimension(250, 100));
        pnlMenu.add(lbLogo);

        // 2. Danh sách menu kèm Icon
        String[][] menuItems = {
            {"Bán Hàng", "shield-minus.svg"},
            {"Sản Phẩm", "shopping-basket.svg"},
            {"Nhân Viên", "square-user.svg"},
            {"Khách Hàng", "file-user.svg"},
            {"Nhập Hàng", "shield-plus.svg"},
            {"Hóa đơn", "ticket.svg"},
            {"Thống Kê", "refresh.svg"}
        };
        
        for (String[] item : menuItems) {
            JButton btn = createMenuButton(item[0], item[1]);
            pnlMenu.add(btn);
            listButtons.add(btn);
            
            btn.addActionListener(e -> {
                setActiveButton(btn);
                mainFrame.showCard(item[0]);
            });
        }
        this.add(pnlMenu, BorderLayout.CENTER);

        // --- PHẦN DƯỚI: NÚT ĐĂNG XUẤT ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        pnlBottom.setOpaque(false);

        JButton btnLogout = new JButton(" Đăng xuất");
        btnLogout.setIcon(new FlatSVGIcon("images/icon/log-out.svg", 20, 20));
        btnLogout.setPreferredSize(new Dimension(210, 50));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(Theme.DANGER_COLOR); // Màu đỏ từ Theme
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bo tròn nút (FlatLaf tự động bo theo UIManager trong Theme.java)
        btnLogout.addActionListener(e -> System.exit(0));
        
        pnlBottom.add(btnLogout);
        this.add(pnlBottom, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(" " + text);
        try {
            btn.setIcon(new FlatSVGIcon("images/icon/" + iconName, 20, 20));
        } catch (Exception e) {
            System.err.println("Không tìm thấy icon: " + iconName);
        }
        
        btn.setPreferredSize(new Dimension(230, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.PRIMARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setActiveButton(JButton activeBtn) {
        for (JButton btn : listButtons) {
            btn.setBackground(Theme.PRIMARY);
            btn.setForeground(Color.WHITE);
        }
        activeBtn.setBackground(Theme.ACCENT);
        activeBtn.setForeground(Theme.PRIMARY);
    }
}