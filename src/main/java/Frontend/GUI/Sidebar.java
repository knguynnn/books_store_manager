package Frontend.GUI;

import Frontend.Compoent.Theme;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import Backend.DTO.NhanVien_TaiKhoan.PhanQuyenDTO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sidebar extends JPanel {
    private MainFrame mainFrame;
    private ArrayList<JButton> listButtons = new ArrayList<>();
    private Map<String, JButton> menuButtons = new HashMap<>(); 
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

        JLabel lbLogo = new JLabel("BOOK STORE", SwingConstants.CENTER);
        lbLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbLogo.setForeground(Theme.ACCENT);
        lbLogo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        lbLogo.setPreferredSize(new Dimension(250, 100));
        pnlMenu.add(lbLogo);

        String[][] menuItems = {
            {"Trang chủ", "home.svg"}, {"Bán hàng", "shopping-basket.svg"}, {"Sản phẩm", "dock.svg"},
            {"Nhập hàng", "circle-plus.svg"}, {"Khách hàng", "file-user.svg"}, {"Nhà cung cấp", "ncc.svg"},
            {"Nhân viên", "square-user.svg"}, {"Thống kê", "thongke.svg"}, {"Phân quyền", "shield-check.svg"}
        };

        for (String[] item : menuItems) {
            JButton btn = createMenuButton(item[0], item[1]);
            menuButtons.put(item[0], btn); 
            pnlMenu.add(btn);
            listButtons.add(btn);
            
            btn.addActionListener(e -> {
                setActiveButton(btn);
                if (item[0].equals("Trang chủ")) mainFrame.backToHome();
                else mainFrame.switchView(item[0]);
            });
        }
        this.add(pnlMenu, BorderLayout.CENTER);

        // --- NÚT ĐĂNG XUẤT (GIỮ NGUYÊN) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        pnlBottom.setOpaque(false);
        JButton btnLogout = new JButton(" Đăng xuất") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? Theme.DANGER_COLOR.darker() : (getModel().isRollover() ? new Color(220, 38, 38) : Theme.DANGER_COLOR));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Bo góc 15px
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.setIcon(new FlatSVGIcon("images/icon/log-out.svg", 20, 20));
        btnLogout.setPreferredSize(new Dimension(210, 45));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> System.exit(0));

        pnlBottom.add(btnLogout);
        this.add(pnlBottom, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(" " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Nếu nút đang được chọn (isOpaque = true), vẽ màu vàng Theme.ACCENT
                if (isOpaque()) {
                    g2.setColor(getBackground()); // Lấy màu từ setBackground(Theme.ACCENT)
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                } 
                // Hiệu ứng hover (tùy chọn): vẽ màu nhẹ khi di chuột vào nút chưa chọn
                else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 30)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        try { btn.setIcon(new FlatSVGIcon("images/icon/" + iconName, 20, 20)); } catch (Exception e) {}
        
        btn.setPreferredSize(new Dimension(230, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.PRIMARY); // Mặc định nền tối
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false); // Tắt nền mặc định để tự vẽ bo góc
        btn.setOpaque(false);            // Mặc định là false (chưa chọn)
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void setActiveButton(JButton activeBtn) {
        for (JButton btn : listButtons) {
            btn.setOpaque(false); 
            btn.setBackground(Theme.PRIMARY);
        }
        activeBtn.setOpaque(true); 
        activeBtn.setBackground(Theme.ACCENT); 
        activeBtn.repaint();
    }

    public void setMenuSelected(String name) {
        for (JButton btn : listButtons) {
            if (btn.getText().trim().equals(name)) {
                setActiveButton(btn);
                break;
            }
        }
    }

    public void applyPermissions(PhanQuyenDTO quyen) {
        if (quyen == null) return;
        setBtnVisible("Khách hàng", quyen.getQlKhachHang());
        setBtnVisible("Nhân viên", quyen.getQlNhanVien());
        setBtnVisible("Sản phẩm", quyen.getQlSanPham());
        setBtnVisible("Nhập hàng", quyen.getQlNhapHang());
        setBtnVisible("Bán hàng", quyen.getQlBanHang());
        setBtnVisible("Nhà cung cấp", quyen.getQlNhaCungCap());
        setBtnVisible("Thống kê", quyen.getQlThongKe());
        setBtnVisible("Phân quyền", quyen.getQlPhanQuyen());
        pnlMenu.revalidate();
        pnlMenu.repaint();
    }

    private void setBtnVisible(String name, int access) {
        JButton btn = menuButtons.get(name);
        if (btn != null) btn.setVisible(access == 1);
    }
}