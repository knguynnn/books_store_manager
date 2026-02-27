package Frontend.GUI;

import Frontend.Compoent.Theme;
import javax.swing.*;
import java.awt.*;

import Frontend.GUI.KhachHangGUI.KhachHangPanel;
import Frontend.GUI.NhanVienGUI.NhanVienPanel;
import Frontend.GUI.TrangChuGUI.TrangChuPanel;
import Frontend.GUI.ThongKeGUI.ThongKePanel;
import Frontend.GUI.PhanQuyenGUI.PhanQuyenPanel;

public class MainFrame extends JFrame {
    private CardLayout rootLayout;
    private JPanel rootPanel;
    private Sidebar sidebar;
    private Header header;
    private JPanel contentArea;
    private CardLayout contentLayout;
    private TrangChuPanel homeView;

    public MainFrame() {
        initComponent();
    }

    private void initComponent() {
        setTitle("HỆ THỐNG QUẢN LÝ CỬA HÀNG SÁCH");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        rootLayout = new CardLayout();
        rootPanel = new JPanel(rootLayout);

        // --- MÀN HÌNH 1: TRANG CHỦ (PORTAL) ---
        homeView = new TrangChuPanel(this);
        rootPanel.add(homeView, "HOME_VIEW");

        // --- MÀN HÌNH 2: GIAO DIỆN QUẢN LÝ CHÍNH ---
        JPanel pnlApp = new JPanel(new BorderLayout());
        
        // Sidebar bên trái
        sidebar = new Sidebar(this);
        pnlApp.add(sidebar, BorderLayout.WEST);

        // Phần nội dung bên phải (Header + Content Area)
        JPanel pnlRight = new JPanel(new BorderLayout());
        header = new Header();
        pnlRight.add(header, BorderLayout.NORTH);

        contentLayout = new CardLayout();
        contentArea = new JPanel(contentLayout);
        contentArea.setBackground(Theme.BACKGROUND);
        
        // --- ĐĂNG KÝ CÁC TRANG NGHIỆP VỤ ---
        
        // 1. Trang Khách hàng (Sử dụng Panel thực tế bạn đang làm)
        contentArea.add(new KhachHangPanel(), "Khách hàng");
        
        // 1.2 Trang Nhân viên
        contentArea.add(new NhanVienPanel(), "Nhân viên");
        
        //1.5 thống kê 
        contentArea.add(new ThongKePanel(), "Thống kê");

        //1.6 Trang Phân Quyền 
        contentArea.add(new PhanQuyenPanel(), "Phân quyền");
        
        // 2. Đăng ký các trang khác dưới dạng Placeholder
        String[] pages = {"Bán hàng", "Sản phẩm", "Nhập hàng", "Nhà cung cấp"};
        for (String page : pages) {
            JPanel pnlPlaceholder = new JPanel(new GridBagLayout());
            pnlPlaceholder.setBackground(Theme.BACKGROUND);
            JLabel lbl = new JLabel("GIAO DIỆN TRANG: " + page.toUpperCase());
            lbl.setFont(new Font("Segoe UI", Font.ITALIC, 20));
            pnlPlaceholder.add(lbl);
            contentArea.add(pnlPlaceholder, page);
        }
        
        pnlRight.add(contentArea, BorderLayout.CENTER);
        pnlApp.add(pnlRight, BorderLayout.CENTER);

        // Thêm giao diện chính vào Root
        rootPanel.add(pnlApp, "MAIN_VIEW");
        
        add(rootPanel);
        
        // Luôn hiển thị Trang chủ đầu tiên khi mở máy
        rootLayout.show(rootPanel, "HOME_VIEW");
    }

    /**
     * Chuyển từ Trang chủ vào các trang nghiệp vụ (không hiệu ứng)
     */
    public void switchView(String businessName) {
        rootLayout.show(rootPanel, "MAIN_VIEW");
        showCard(businessName);
        sidebar.setMenuSelected(businessName);
    }

    /**
     * Chuyển đổi giữa các trang con trong vùng Content
     */
    public void showCard(String name) {
        contentLayout.show(contentArea, name);
        header.setTitle(name.toUpperCase());
    }

    /**
     * Quay lại màn hình Trang chủ (Portal)
     */
    public void backToHome() {
        rootLayout.show(rootPanel, "HOME_VIEW");
    }
}