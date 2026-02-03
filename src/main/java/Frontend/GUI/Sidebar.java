package Frontend.GUI;

import Frontend.Compoent.Theme;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Sidebar extends JPanel {
    private MainFrame mainFrame;
    private ArrayList<JButton> listButtons = new ArrayList<>();
    private JPanel pnlMenu;

    public Sidebar(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
    }

    private void initComponent() {
        this.setPreferredSize(new Dimension(250, 800));
        this.setBackground(Theme.PRIMARY);
        this.setLayout(new BorderLayout());

        pnlMenu = new JPanel();
        pnlMenu.setOpaque(false);
        pnlMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        // Logo
        JLabel lbLogo = new JLabel("BOOK STORE", SwingConstants.CENTER);
        lbLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbLogo.setForeground(Theme.ACCENT);
        lbLogo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        lbLogo.setPreferredSize(new Dimension(250, 100));
        pnlMenu.add(lbLogo);

        // --- DANH SÁCH MENU CHÍNH XÁC ---
        String[][] menuItems = {
            {"Trang chủ", "home.svg"},
            {"Bán hàng", "shopping-basket.svg"},
            {"Sản phẩm", "dock.svg"},
            {"Nhập hàng", "circle-plus.svg"},
            {"Khách hàng", "file-user.svg"},
            {"Nhà cung cấp", "ncc.svg"},
            {"Nhân viên", "square-user.svg"},
            {"Thống kê", "thongke.svg"},
            {"Phân quyền", "shield-check.svg"}
        };

        for (String[] item : menuItems) {
            JButton btn = createMenuButton(item[0], item[1]);
            pnlMenu.add(btn);
            listButtons.add(btn);
            
            btn.addActionListener(e -> {
                setActiveButton(btn);
                if (item[0].equals("Trang chủ")) {
                    mainFrame.backToHome(); // Quay lại HOME_VIEW (Portal)
                } else {
                    mainFrame.showCard(item[0]); // Chuyển Card nghiệp vụ
                }
            });
        }
        this.add(pnlMenu, BorderLayout.CENTER);

        // Nút Đăng xuất
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        pnlBottom.setOpaque(false);
        JButton btnLogout = new JButton(" Đăng xuất");
        btnLogout.setIcon(new FlatSVGIcon("images/icon/log-out.svg", 20, 20));
        btnLogout.setPreferredSize(new Dimension(230, 50));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogout.setForeground(Theme.DANGER_COLOR);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> System.exit(0));
        pnlBottom.add(btnLogout);
        this.add(pnlBottom, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(" " + text);
        try {
            btn.setIcon(new FlatSVGIcon("images/icon/" + iconName, 20, 20));
        } catch (Exception e) {}
        btn.setPreferredSize(new Dimension(230, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.PRIMARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void setActiveButton(JButton activeBtn) {
        for (JButton btn : listButtons) {
            btn.setBackground(Theme.PRIMARY);
        }
        activeBtn.setBackground(new Color(30, 41, 59));
    }

    public void setMenuSelected(String name) {
        for (JButton btn : listButtons) {
            if (btn.getText().trim().equals(name)) {
                setActiveButton(btn);
                break;
            }
        }
    }
}