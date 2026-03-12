package Frontend.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Backend.BUS.SharedData;
import Backend.DTO.NhanVien_TaiKhoan.PhanQuyenDTO;
import Frontend.Compoent.Theme;
import Frontend.GUI.BanHangGUI.BanHangPanel;
import Frontend.GUI.KhachHangGUI.KhachHangPanel;
import Frontend.GUI.NhaCungCapGUI.NhaCungCapPanel;
import Frontend.GUI.NhanVienGUI.NhanVienPanel;
import Frontend.GUI.NhapHangGUI.NhapHangPanel;
import Frontend.GUI.PhanQuyenGUI.PhanQuyenPanel;
import Frontend.GUI.SanPhamGUI.SanPhamPanel;
import Frontend.GUI.ThongKeGUI.ThongKePanel;
import Frontend.GUI.TrangChuGUI.TrangChuPanel;

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

    public void showCard(String name) {
        contentLayout.show(contentArea, name);
        header.setTitle(name);
    }

    public void showMainApp() {
        rootLayout.show(rootPanel, "APP_VIEW");
    }

    public void showHome() {
        rootLayout.show(rootPanel, "HOME_VIEW");
    }

    /**
     * Chuyển sang màn hình quản lý và hiện đúng tab
     */
    public void switchView(String name) {
        showMainApp();
        showCard(name);
        sidebar.setMenuSelected(name);
    }

    /**
     * Quay về trang chủ (HOME_VIEW)
     */
    public void backToHome() {
        rootLayout.show(rootPanel, "HOME_VIEW");
    }

    /**
     * Áp dụng phân quyền từ SharedData sau khi đăng nhập
     */
    public void applyPermissions() {
        PhanQuyenDTO quyen = SharedData.quyenHienTai;
        if (quyen != null && sidebar != null) {
            sidebar.applyPermissions(quyen);
        }
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

        // --- ĐĂNG KÝ TẤT CẢ CÁC TRANG NGHIỆP VỤ THỰC TẾ ---
        contentArea.add(new BanHangPanel(), "Bán hàng");
        contentArea.add(new SanPhamPanel(), "Sản phẩm");
        contentArea.add(new NhapHangPanel(), "Nhập hàng");
        contentArea.add(new KhachHangPanel(), "Khách hàng");
        contentArea.add(new NhaCungCapPanel(), "Nhà cung cấp");
        contentArea.add(new NhanVienPanel(), "Nhân viên");
        contentArea.add(new ThongKePanel(), "Thống kê");
        contentArea.add(new PhanQuyenPanel(), "Phân quyền");

        pnlRight.add(contentArea, BorderLayout.CENTER);
        pnlApp.add(pnlRight, BorderLayout.CENTER);

        rootPanel.add(pnlApp, "APP_VIEW");

        setContentPane(rootPanel);
        rootLayout.show(rootPanel, "HOME_VIEW");
    }
}