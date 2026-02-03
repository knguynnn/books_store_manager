package Frontend.GUI;

import Frontend.Compoent.Theme;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    private JLabel lbTitle;
    private JTextField txtSearch;
    private JLabel lbUser;

    public Header() {
        initComponent();
    }

    private void initComponent() {
        this.setPreferredSize(new Dimension(0, 70)); // Tăng độ cao một chút cho thoáng
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        this.setLayout(new BorderLayout());

        // --- 1. PHẦN BÊN TRÁI: TIÊU ĐỀ TRANG ---
        lbTitle = new JLabel(" TRANG CHỦ");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setForeground(Theme.TEXT);
        lbTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        this.add(lbTitle, BorderLayout.WEST);

        // --- 2. PHẦN Ở GIỮA: Ô TÌM KIẾM ---
        JPanel pnlCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        pnlCenter.setOpaque(false);

        // Tạo ô tìm kiếm
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(350, 40));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm sách, nhân viên, hóa đơn...");
        // Thêm icon kính lúp vào đầu ô tìm kiếm (Tính năng của FlatLaf)
        txtSearch.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("images/icon/search-black.svg", 18, 18)); 
        // Lưu ý: Bạn có thể thay "help-circle.svg" bằng icon search của bạn
        
        pnlCenter.add(txtSearch);
        this.add(pnlCenter, BorderLayout.CENTER);

        // --- 3. PHẦN BÊN PHẢI: THÔNG TIN TÀI KHOẢN ---
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        pnlRight.setOpaque(false);

        // Avatar hoặc Icon người dùng
        JLabel lbAvatar = new JLabel(new FlatSVGIcon("images/icon/user-black.svg", 30, 30));
        
        // Tên tài khoản
        lbUser = new JLabel("Quản trị viên");
        lbUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbUser.setForeground(Theme.TEXT);

        // Group lại cho đẹp
        JPanel pnlAccount = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAccount.setOpaque(false);
        pnlAccount.add(lbUser);
        pnlAccount.add(lbAvatar);
        pnlAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlRight.add(pnlAccount);
        this.add(pnlRight, BorderLayout.EAST);
    }

    public void setTitle(String title) {
        lbTitle.setText(" " + title.toUpperCase());
    }
}