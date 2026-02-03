package Frontend.GUI;

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

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel sidebar;
    private Header header;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public MainFrame() {
        initComponent();
    }

    public void showCard(String name) {
        cardLayout.show(contentArea, name);
        header.setTitle(name);
    }

    private void initComponent() {
    setTitle("QUẢN LÝ CỬA HÀNG SÁCH");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1300, 850);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // 1. Sidebar
    sidebar = new Sidebar(this);
    add(sidebar, BorderLayout.WEST);

    // 2. Panel chính chứa Header và ContentArea
    JPanel pnlMain = new JPanel(new BorderLayout());
    header = new Header();
    pnlMain.add(header, BorderLayout.NORTH);

    // CHỈ KHỞI TẠO 1 LẦN DUY NHẤT Ở ĐÂY
    cardLayout = new CardLayout();
    contentArea = new JPanel(cardLayout);
    contentArea.setBackground(Theme.BACKGROUND);

    // --- KHỞI TẠO CÁC MÀN HÌNH TƯƠNG ỨNG VỚI SIDEBAR ---
    // (Tạm thời dùng JPanel rỗng để test nút, sau này bạn replace bằng GUI thật)
    contentArea.add(new TrangChuPanel(), "Trang chủ");
    contentArea.add(new BanHangPanel(), "Bán hàng");
    contentArea.add(new SanPhamPanel(), "Sản phẩm");
    contentArea.add(new NhapHangPanel(), "Nhập hàng");
    contentArea.add(new KhachHangPanel(), "Khách hàng");
    contentArea.add(new NhaCungCapPanel(), "Nhà cung cấp"); // Mới thêm
    contentArea.add(new NhanVienPanel(), "Nhân viên");
    contentArea.add(new ThongKePanel(), "Thống kê");
    contentArea.add(new PhanQuyenPanel(), "Phân quyền");   // Mới thêm

    // Tạo màn hình chào mừng
    JPanel pnlWelcome = new JPanel(new GridBagLayout());
    pnlWelcome.add(new JLabel("HỆ THỐNG QUẢN LÝ BÁN SÁCH TRỰC TUYẾN"));
    contentArea.add(pnlWelcome, "WELCOME");

    pnlMain.add(contentArea, BorderLayout.CENTER);
    add(pnlMain, BorderLayout.CENTER); // Thêm pnlMain duy nhất vào Frame
    }
}