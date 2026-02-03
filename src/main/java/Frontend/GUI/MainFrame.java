package Frontend.GUI;

import Frontend.Compoent.Theme;
import javax.swing.*;
import java.awt.*;
import Frontend.GUI.TrangChuGUI.TrangChuPanel;

public class MainFrame extends JFrame {
    private CardLayout rootLayout;
    private JPanel rootPanel;
    private Sidebar sidebar;
    private Header header;
    private JPanel contentArea;
    private CardLayout contentLayout;

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

        // --- MÀN HÌNH 1: PORTAL ---
        TrangChuPanel homeView = new TrangChuPanel(this);
        rootPanel.add(homeView, "HOME_VIEW");

        // --- MÀN HÌNH 2: GIAO DIỆN CHÍNH ---
        JPanel pnlApp = new JPanel(new BorderLayout());
        sidebar = new Sidebar(this);
        pnlApp.add(sidebar, BorderLayout.WEST);

        JPanel pnlRight = new JPanel(new BorderLayout());
        header = new Header();
        pnlRight.add(header, BorderLayout.NORTH);

        contentLayout = new CardLayout();
        contentArea = new JPanel(contentLayout);
        contentArea.setBackground(Theme.BACKGROUND);
        
        // Đăng ký các trang nghiệp vụ (Khớp chính xác với text ở Sidebar)
        String[] pages = {"Bán hàng", "Sản phẩm", "Nhập hàng", "Khách hàng", "Nhà cung cấp", "Nhân viên", "Thống kê", "Phân quyền"};
        for (String page : pages) {
            JPanel pnlPlaceholder = new JPanel(new GridBagLayout());
            pnlPlaceholder.add(new JLabel("GIAO DIỆN TRANG: " + page.toUpperCase()));
            contentArea.add(pnlPlaceholder, page);
        }
        
        pnlRight.add(contentArea, BorderLayout.CENTER);
        pnlApp.add(pnlRight, BorderLayout.CENTER);

        rootPanel.add(pnlApp, "MAIN_VIEW");
        add(rootPanel);
        rootLayout.show(rootPanel, "HOME_VIEW");
    }

    public void switchView(String businessName) {
        rootLayout.show(rootPanel, "MAIN_VIEW");
        showCard(businessName);
        sidebar.setMenuSelected(businessName); 
    }

    public void backToHome() {
        rootLayout.show(rootPanel, "HOME_VIEW");
    }

    public void showCard(String name) {
        contentLayout.show(contentArea, name);
        header.setTitle(name);
    }
}