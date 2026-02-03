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

        // --- PHẦN TRÊN: LOGO & MENU ---
        pnlMenu = new JPanel();
        pnlMenu.setOpaque(false);
        pnlMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        // 1. Logo
        JLabel lbLogo = new JLabel("BOOK STORE", SwingConstants.CENTER);
        lbLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbLogo.setForeground(Theme.ACCENT);
        lbLogo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        lbLogo.setPreferredSize(new Dimension(250, 100));
        pnlMenu.add(lbLogo);

        // 2. Danh sách Menu (Cập nhật theo yêu cầu)
        // Cấu trúc: { "Tên hiển thị", "Tên icon.svg" }
        String[][] menuItems = {
            {"Trang chủ", "home.svg"},         // Dashboard tổng quan
            {"Bán hàng", "shopping-basket.svg"}, // Nghiệp vụ xuất (thay cho Phiếu xuất)
            {"Sản phẩm", "dock.svg"},          // Quản lý sách
            {"Nhập hàng", "circle-plus.svg"},  // Quản lý phiếu nhập
            {"Khách hàng", "file-user.svg"},  // Quản lý khách hàng
            {"Nhà cung cấp", "ncc.svg"},     // Quản lý NCC (Khớp với bảng ncc)
            {"Nhân viên", "square-user.svg"},  // Quản lý nhân viên
            {"Thống kê", "thongke.svg"},    // Báo cáo doanh thu
            {"Phân quyền", "shield-check.svg"}         // Quản lý tài khoản/quyền
        };
        
        // Lưu ý: Hãy đảm bảo bạn có đủ file .svg trong thư mục images/icon/
        // Nếu thiếu icon nào, hãy báo tôi để tôi gợi ý tên icon thay thế có sẵn.

        for (String[] item : menuItems) {
            String name = item[0];
            String icon = item[1];
            JButton btn = createMenuButton(name, icon);
            
            btn.addActionListener(e -> {
                setActiveButton(btn);
                mainFrame.showCard(name); // Chuyển màn hình
            });
            
            pnlMenu.add(btn);
            listButtons.add(btn);
        }

        this.add(pnlMenu, BorderLayout.CENTER);

        // --- PHẦN DƯỚI: ĐĂNG XUẤT (Giữ nguyên vị trí cũ) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        pnlBottom.setOpaque(false);

        JButton btnLogout = new JButton(" Đăng xuất");
        try {
            btnLogout.setIcon(new FlatSVGIcon("images/icon/log-out.svg", 20, 20));
        } catch (Exception e) {
            // Icon lỗi thì thôi, không crash app
        }
        btnLogout.setPreferredSize(new Dimension(210, 50));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(Theme.DANGER_COLOR);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(mainFrame, 
                "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        pnlBottom.add(btnLogout);
        this.add(pnlBottom, BorderLayout.SOUTH);

        // Mặc định chọn nút đầu tiên (Trang chủ)
        if (!listButtons.isEmpty()) {
            setActiveButton(listButtons.get(0));
        }
    }

    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(" " + text);
        try {
            btn.setIcon(new FlatSVGIcon("images/icon/" + iconName, 20, 20));
        } catch (Exception e) {
            // Nếu không tìm thấy icon, vẫn hiện nút bình thường
            System.err.println("Chưa có icon: " + iconName);
        }
        
        btn.setPreferredSize(new Dimension(230, 50));
        // Đã sửa lỗi Font.MEDIUM thành Font.BOLD để tránh lỗi
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.PRIMARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0)); // Căn lề trái icon
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setActiveButton(JButton activeBtn) {
        for (JButton btn : listButtons) {
            btn.setBackground(Theme.PRIMARY);
            btn.setForeground(Color.WHITE);
        }
        // Khi nút được chọn, đổi màu nền sáng hơn và chữ màu chính
        activeBtn.setBackground(Theme.ACCENT); 
        activeBtn.setForeground(Theme.PRIMARY);
    }
}