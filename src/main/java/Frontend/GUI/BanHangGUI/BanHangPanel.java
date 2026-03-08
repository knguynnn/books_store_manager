package Frontend.GUI.BanHangGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import Backend.BUS.KhachHang_BanHang.CTHoaDonBUS;
import Backend.BUS.KhachHang_BanHang.HoaDonBUS;
import Backend.BUS.KhachHang_BanHang.KhachHangBUS;
import Backend.BUS.KhuyenMai.KhuyenMaiBUS;
import Backend.BUS.NhanVien_TaiKhoan.NhanVienBUS;
import Backend.BUS.SanPham_DanhMuc.SanPhamBUS;
import Backend.DTO.KhachHang_BanHang.CTHoaDonDTO;
import Backend.DTO.KhachHang_BanHang.HoaDonDTO;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;
import Backend.DTO.KhuyenMai.KhuyenMaiDTO;
import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import Frontend.Compoent.ButtonAdd;
import Frontend.Compoent.ButtonDele;
import Frontend.Compoent.ButtonFix;
import Frontend.Compoent.ButtonRefresh;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.ButtonXuatPdf;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;


public class BanHangPanel extends JPanel {

    // ======= Bảng Hóa đơn =======
    private JTable tableHoaDon;
    private DefaultTableModel modelHoaDon;

    // ======= Bảng Chi tiết Hóa đơn =======
    private JTable tableCTHD;
    private DefaultTableModel modelCTHD;

    // ======= Form Hóa đơn =======
    private JTextField txtMaHD;
    private JTextField txtThoiGian;
    private JComboBox<String> cboMaNV;
    private JButton btnChonNV;
    private JComboBox<String> cboMaKH;
    private JButton btnChonKH;
    private JComboBox<String> cboMaKM;
    private JTextField txtTongTienTruocKM;
    private JTextField txtGiamGiaHD;
    private JTextField txtTongTienThanhToan;

    // ======= Form Chi tiết (thêm SP vào HĐ) =======
    private JComboBox<String> cboMaSP;
    private JButton btnChonSP;
    private JTextField txtSoLuong;
    private JTextField txtDonGiaBan;
    private JTextField txtThanhTienSP;

    // ======= Nút thao tác =======
    private ButtonAdd btnLuu;
    private ButtonFix btnSua;
    private ButtonDele btnXoa;
    private ButtonRefresh btnMoi;
    private ButtonXuatExcel btnXuat;
    private ButtonXuatPdf btnXuatPDF;
    private CustomButton btnThemSP;
    private CustomButton btnXoaSP;

    // ======= Tìm kiếm =======
    private SearchTextField searchField;
    private JComboBox<String> cboSearchField;
    private JComboBox<String> cboSearchOp;

    private final DecimalFormat currencyFormat = new DecimalFormat("#,### đ");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private boolean isEditing = false;

    // ======= BUS =======
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private final CTHoaDonBUS ctHoaDonBUS = new CTHoaDonBUS();
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();
    private final NhanVienBUS nhanVienBUS = new NhanVienBUS();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private final KhuyenMaiBUS khuyenMaiBUS = new KhuyenMaiBUS();

    public BanHangPanel() {
        initComponent();
        initEvent();
        loadData();
    }

    // ============================ GIAO DIỆN ============================

    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        this.setLayout(new MigLayout("fill, insets 10, gap 10", "[290!][grow]", "[fill]"));

        JPanel pnlForm = createFormPanel();
        JScrollPane scrollForm = new JScrollPane(pnlForm);
        scrollForm.setBorder(BorderFactory.createEmptyBorder());
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        scrollForm.setBackground(Color.WHITE);

        JPanel pnlRight = createRightPanel();

        this.add(scrollForm);
        this.add(pnlRight, "grow");
    }

    // -------------------- Panel trái: Form nhập liệu --------------------

    private JPanel createFormPanel() {
        JPanel pnlForm = new JPanel(new MigLayout("wrap, fill, insets 12 14 12 14", "[fill]", ""));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));

        // Tiêu đề
        JLabel lbTitle = new JLabel("BÁN HÀNG");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lbTitle.setForeground(Theme.PRIMARY);
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlForm.add(lbTitle, "align center, gapbottom 6");

        // --- Mã HĐ ---
        JPanel pnlMaLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlMaLabel.setOpaque(false);
        pnlMaLabel.add(createLabel("Mã HĐ:"));
        JLabel lblAuto = new JLabel("  (Tự động)");
        lblAuto.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblAuto.setForeground(new Color(156, 163, 175));
        pnlMaLabel.add(lblAuto);
        pnlForm.add(pnlMaLabel);
        txtMaHD = createReadOnlyField("Mã hóa đơn");
        pnlForm.add(txtMaHD, "h 30!, gapbottom 2");

        // --- Thời gian ---
        pnlForm.add(createLabel("Thời gian:"));
        txtThoiGian = createReadOnlyField("dd/MM/yyyy HH:mm");
        txtThoiGian.setText(dateFormat.format(new Timestamp(System.currentTimeMillis())));
        pnlForm.add(txtThoiGian, "h 30!, gapbottom 2");

        // --- Nhân viên ---
        pnlForm.add(createLabel("Nhân viên:"));
        JPanel pnlNV = new JPanel(new MigLayout("fill, insets 0", "[grow][]", "[]"));
        pnlNV.setOpaque(false);
        cboMaNV = createComboBox();
        btnChonNV = createBrowseButton();
        pnlNV.add(cboMaNV, "grow, h 30!");
        pnlNV.add(btnChonNV, "w 30!, h 30!");
        pnlForm.add(pnlNV, "gapbottom 2");

        // --- Khách hàng ---
        pnlForm.add(createLabel("Khách hàng:"));
        JPanel pnlKH = new JPanel(new MigLayout("fill, insets 0", "[grow][]", "[]"));
        pnlKH.setOpaque(false);
        cboMaKH = createComboBox();
        btnChonKH = createBrowseButton();
        pnlKH.add(cboMaKH, "grow, h 30!");
        pnlKH.add(btnChonKH, "w 30!, h 30!");
        pnlForm.add(pnlKH, "gapbottom 2");

        // --- Khuyến mãi ---
        pnlForm.add(createLabel("Khuyến mãi:"));
        cboMaKM = createComboBox();
        cboMaKM.addItem("");
        pnlForm.add(cboMaKM, "h 30!, gapbottom 4");

        // --- Separator ---
        pnlForm.add(createSeparator(), "growx, gapbottom 3");

        // --- Tổng tiền ---
        pnlForm.add(createLabel("Tổng trước KM:"));
        txtTongTienTruocKM = createReadOnlyField("0 đ");
        txtTongTienTruocKM.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlForm.add(txtTongTienTruocKM, "h 28!, gapbottom 2");

        pnlForm.add(createLabel("Giảm giá HĐ:"));
        txtGiamGiaHD = createReadOnlyField("0 đ");
        txtGiamGiaHD.setForeground(Theme.DANGER_COLOR);
        txtGiamGiaHD.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlForm.add(txtGiamGiaHD, "h 28!, gapbottom 2");

        // --- Tổng thanh toán (nổi bật) ---
        JLabel lblTongTT = new JLabel("TỔNG THANH TOÁN:");
        lblTongTT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTongTT.setForeground(Theme.PRIMARY);
        pnlForm.add(lblTongTT);
        txtTongTienThanhToan = createReadOnlyField("0 đ");
        txtTongTienThanhToan.setBackground(new Color(255, 250, 230));
        txtTongTienThanhToan.setForeground(Theme.ACCENT_COLOR);
        txtTongTienThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTongTienThanhToan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT, 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        pnlForm.add(txtTongTienThanhToan, "h 36!, gapbottom 4");

        // --- Separator ---
        pnlForm.add(createSeparator(), "growx, gapbottom 3");

        // --- Khu vực thêm SP ---
        JLabel lblCTTitle = new JLabel("THÊM SẢN PHẨM VÀO HĐ");
        lblCTTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCTTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lblCTTitle, "gapbottom 2");

        pnlForm.add(createLabel("Sản phẩm:"));
        JPanel pnlSP = new JPanel(new MigLayout("fill, insets 0", "[grow][]", "[]"));
        pnlSP.setOpaque(false);
        cboMaSP = createComboBox();
        btnChonSP = createBrowseButton();
        pnlSP.add(cboMaSP, "grow, h 28!");
        pnlSP.add(btnChonSP, "w 28!, h 28!");
        pnlForm.add(pnlSP, "gapbottom 2");

        pnlForm.add(createLabel("Số lượng:"));
        txtSoLuong = createTextField("1");
        pnlForm.add(txtSoLuong, "h 28!, gapbottom 2");

        pnlForm.add(createLabel("Đơn giá bán:"));
        txtDonGiaBan = createReadOnlyField("Tự lấy từ SP");
        pnlForm.add(txtDonGiaBan, "h 28!, gapbottom 2");

        pnlForm.add(createLabel("Thành tiền:"));
        txtThanhTienSP = createReadOnlyField("0");
        txtThanhTienSP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlForm.add(txtThanhTienSP, "h 28!, gapbottom 4");

        // Nút thêm/xóa SP
        JPanel pnlSPButtons = new JPanel(new MigLayout("insets 0, gap 6", "[grow][grow]", "[]"));
        pnlSPButtons.setOpaque(false);
        btnThemSP = new CustomButton("+ Thêm SP", Theme.ACCENT_COLOR);
        btnThemSP.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnXoaSP = new CustomButton("- Xóa SP", Theme.DANGER_COLOR);
        btnXoaSP.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pnlSPButtons.add(btnThemSP, "grow, h 28!");
        pnlSPButtons.add(btnXoaSP, "grow, h 28!");
        pnlForm.add(pnlSPButtons, "gapbottom 6");

        // --- Nút chức năng ---
        JPanel pnlButtons = createButtonPanel();
        pnlForm.add(pnlButtons, "grow, gaptop 0");

        return pnlForm;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new MigLayout("wrap 2, gap 6, insets 0", "[grow][grow]", ""));
        pnlButtons.setOpaque(false);

        btnLuu = new ButtonAdd("Lưu HĐ");
        btnSua = new ButtonFix("Sửa HĐ");
        btnXoa = new ButtonDele("Xóa HĐ");
        btnMoi = new ButtonRefresh("Làm mới");
        btnXuat = new ButtonXuatExcel("Xuất Excel");
        btnXuatPDF = new ButtonXuatPdf("Xuất hóa đơn PDF");

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 12);
        Component[] buttons = {btnLuu, btnSua, btnXoa, btnMoi, btnXuat, btnXuatPDF};
        for (Component btn : buttons) {
            if (btn instanceof JButton) {
                ((JButton) btn).setFont(buttonFont);
                ((JButton) btn).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        pnlButtons.add(btnLuu, "grow, h 30!");
        pnlButtons.add(btnSua, "grow, h 30!");
        pnlButtons.add(btnXoa, "grow, h 30!");
        pnlButtons.add(btnMoi, "grow, h 30!");
        pnlButtons.add(btnXuat, "span, grow, h 30!");
        pnlButtons.add(btnXuatPDF, "span, grow, h 30!");

        return pnlButtons;
    }

    // -------------------- Panel phải: Tìm kiếm + 2 bảng --------------------

    private JPanel createRightPanel() {
        JPanel pnlRight = new JPanel(new MigLayout(
                "fill, wrap 1, insets 0", "[fill]",
                "[]10[grow 55]8[]2[grow 45]"
        ));
        pnlRight.setBackground(Theme.BACKGROUND);

        // Thanh tìm kiếm
        JPanel pnlSearch = createSearchPanel();
        pnlRight.add(pnlSearch);

        // Bảng hóa đơn
        JPanel pnlHoaDon = createHoaDonTablePanel();
        pnlRight.add(pnlHoaDon, "grow");

        // Tiêu đề bảng chi tiết
        JLabel lblCT = new JLabel("  CHI TIẾT HÓA ĐƠN");
        lblCT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCT.setForeground(new Color(30, 58, 95));
        lblCT.setIcon(null);
        pnlRight.add(lblCT);

        // Bảng chi tiết hóa đơn
        JPanel pnlChiTiet = createCTHDTablePanel();
        pnlRight.add(pnlChiTiet, "grow");

        return pnlRight;
    }

    private JPanel createSearchPanel() {
        JPanel pnlSearch = new JPanel(new MigLayout("insets 5 0 5 0", "[][][][grow][]", "[]"));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(Theme.TEXT);
        pnlSearch.add(lblSearch);

        cboSearchField = new JComboBox<>(new String[]{
                "Tất cả", "Mã HĐ", "Mã NV", "Mã KH", "Mã KM"
        });
        cboSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboSearchField.setPreferredSize(new Dimension(100, 34));
        pnlSearch.add(cboSearchField);

        cboSearchOp = new JComboBox<>(new String[]{
                "Chứa", "Bằng", ">", ">=", "<", "<=", "<>"
        });
        cboSearchOp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboSearchOp.setPreferredSize(new Dimension(70, 34));
        pnlSearch.add(cboSearchOp);

        searchField = new SearchTextField("Nhập giá trị tìm kiếm...");
        searchField.setPreferredSize(new Dimension(0, 34));
        pnlSearch.add(searchField, "grow");

        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(Theme.PRIMARY);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.addActionListener(e -> timKiemHoaDon());
        pnlSearch.add(btnTimKiem, "w 80!, h 34!");

        return pnlSearch;
    }

    // -------------------- Bảng Hóa đơn (trên) --------------------

    private JPanel createHoaDonTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1, insets 0", "[fill]", "[grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        modelHoaDon = new DefaultTableModel(
                new String[]{"Mã HĐ", "Thời gian", "Mã NV", "Mã KH", "Mã KM",
                        "Tổng trước KM", "Giảm giá", "Thanh toán"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        tableHoaDon = new JTable(modelHoaDon);
        tableHoaDon.setRowHeight(38);
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableHoaDon.setShowGrid(false);
        tableHoaDon.setIntercellSpacing(new Dimension(0, 0));
        tableHoaDon.setDefaultRenderer(Object.class, new HoaDonTableRenderer());

        JTableHeader header = tableHoaDon.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        TableColumnModel colModel = tableHoaDon.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(80);   // Mã HĐ
        colModel.getColumn(1).setPreferredWidth(140);  // Thời gian
        colModel.getColumn(2).setPreferredWidth(65);   // Mã NV
        colModel.getColumn(3).setPreferredWidth(65);   // Mã KH
        colModel.getColumn(4).setPreferredWidth(65);   // Mã KM
        colModel.getColumn(5).setPreferredWidth(120);  // Tổng trước KM
        colModel.getColumn(6).setPreferredWidth(100);  // Giảm giá
        colModel.getColumn(7).setPreferredWidth(130);  // Thanh toán

        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTable.add(scrollPane, "grow");

        return pnlTable;
    }

    // -------------------- Bảng Chi tiết Hóa đơn (dưới) --------------------

    private JPanel createCTHDTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1, insets 0", "[fill]", "[grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        modelCTHD = new DefaultTableModel(
                new String[]{"Mã HĐ", "Mã SP", "Tên SP", "Số lượng", "Đơn giá bán", "Thành tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        tableCTHD = new JTable(modelCTHD);
        tableCTHD.setRowHeight(38);
        tableCTHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCTHD.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableCTHD.setShowGrid(false);
        tableCTHD.setIntercellSpacing(new Dimension(0, 0));
        tableCTHD.setDefaultRenderer(Object.class, new CTHDTableRenderer());

        JTableHeader header = tableCTHD.getTableHeader();
        header.setBackground(new Color(30, 58, 95));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        TableColumnModel colModel = tableCTHD.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(80);   // Mã HĐ
        colModel.getColumn(1).setPreferredWidth(80);   // Mã SP
        colModel.getColumn(2).setPreferredWidth(160);  // Tên SP
        colModel.getColumn(3).setPreferredWidth(70);   // Số lượng
        colModel.getColumn(4).setPreferredWidth(120);  // Đơn giá bán
        colModel.getColumn(5).setPreferredWidth(130);  // Thành tiền

        JScrollPane scrollPane = new JScrollPane(tableCTHD);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTable.add(scrollPane, "grow");

        return pnlTable;
    }

    // -------------------- Custom Renderer: Bảng Hóa đơn --------------------

    private class HoaDonTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            // Màu nền xen kẽ
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                c.setForeground(Theme.TEXT);
            } else {
                c.setBackground(new Color(59, 130, 246, 40));
                c.setForeground(Theme.TEXT);
            }

            // Căn chỉnh cột
            if (column == 0 || column == 2 || column == 3 || column == 4) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else if (column == 1) {
                setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            // Cột Thanh toán: nổi bật xanh lá
            if (column == 7) {
                c.setForeground(Theme.ACCENT_COLOR);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }

            // Cột Giảm giá: đỏ nếu > 0
            if (column == 6 && value != null && !value.toString().equals("0") && !value.toString().equals("0 đ")) {
                c.setForeground(Theme.DANGER_COLOR);
            }

            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return c;
        }
    }

    // -------------------- Custom Renderer: Bảng Chi tiết --------------------

    private class CTHDTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                c.setForeground(Theme.TEXT);
            } else {
                c.setBackground(new Color(59, 130, 246, 40));
                c.setForeground(Theme.TEXT);
            }

            // Căn chỉnh
            if (column == 0 || column == 1 || column == 3) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else if (column == 2) {
                setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            // Cột Thành tiền: bold
            if (column == 5) {
                c.setForeground(Theme.ACCENT_COLOR);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }

            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return c;
        }
    }

    // ============================ SỰ KIỆN ============================

    private void initEvent() {
        // Click bảng Hóa đơn → fill form + load chi tiết
        tableHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableHoaDon.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    txtMaHD.setText(safeGet(modelHoaDon, row, 0));
                    txtThoiGian.setText(safeGet(modelHoaDon, row, 1));
                    setComboValue(cboMaNV, safeGet(modelHoaDon, row, 2));
                    setComboValue(cboMaKH, safeGet(modelHoaDon, row, 3));
                    setComboValue(cboMaKM, safeGet(modelHoaDon, row, 4));
                    txtTongTienTruocKM.setText(safeGet(modelHoaDon, row, 5));
                    txtGiamGiaHD.setText(safeGet(modelHoaDon, row, 6));
                    txtTongTienThanhToan.setText(safeGet(modelHoaDon, row, 7));

                    loadChiTietHoaDon(safeGet(modelHoaDon, row, 0));
                }
            }
        });

        // Click bảng Chi tiết → fill form SP
        tableCTHD.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCTHD.getSelectedRow();
                if (row >= 0) {
                    setComboValue(cboMaSP, safeGet(modelCTHD, row, 1));
                    txtSoLuong.setText(safeGet(modelCTHD, row, 3));
                    txtDonGiaBan.setText(safeGet(modelCTHD, row, 4));
                    txtThanhTienSP.setText(safeGet(modelCTHD, row, 5));
                }
            }
        });

        // Nút chọn qua dialog
        btnChonNV.addActionListener(e -> moDialogTimKiem("NHÂN VIÊN", cboMaNV));
        btnChonKH.addActionListener(e -> moDialogTimKiem("KHÁCH HÀNG", cboMaKH));
        btnChonSP.addActionListener(e -> moDialogTimKiem("SẢN PHẨM", cboMaSP));

        // Khi chọn SP từ combo → tự fill đơn giá
        cboMaSP.addActionListener(e -> {
            Object sel = cboMaSP.getSelectedItem();
            if (sel != null && !sel.toString().trim().isEmpty()) {
                SanPhamDTO sp = sanPhamBUS.getById(sel.toString().trim());
                if (sp != null) {
                    txtDonGiaBan.setText(String.valueOf(sp.getDonGia()));
                    tinhThanhTienSP();
                }
            }
        });

        // Khi thay đổi số lượng → tính lại thành tiền SP
        txtSoLuong.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { tinhThanhTienSP(); }
            @Override public void removeUpdate(DocumentEvent e) { tinhThanhTienSP(); }
            @Override public void changedUpdate(DocumentEvent e) { tinhThanhTienSP(); }
        });

        // Khi chọn khuyến mãi → tính lại giảm giá
        cboMaKM.addActionListener(e -> capNhatTongTienUI());

        // Thêm / Xóa SP khỏi chi tiết
        btnThemSP.addActionListener(e -> themSanPhamVaoChiTiet());
        btnXoaSP.addActionListener(e -> xoaSanPhamKhoiChiTiet());

        // Các nút thao tác chính
        btnLuu.addActionListener(e -> {
            if (isEditing) {
                suaHoaDon();
            } else {
                taoHoaDon();
            }
        });

        btnSua.addActionListener(e -> {
            if (tableHoaDon.getSelectedRow() >= 0) {
                suaHoaDon();
            } else {
                showWarning("Vui lòng chọn hóa đơn cần sửa!");
            }
        });

        btnXoa.addActionListener(e -> xoaHoaDon());
        btnMoi.addActionListener(e -> resetForm());
        btnXuat.addActionListener(e -> Frontend.Compoent.XuatExcel.xuat(tableHoaDon, "DanhSachHoaDon"));
        btnXuatPDF.addActionListener(e -> xuatPDFHoaDon());

        // Tìm kiếm realtime
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { timKiemHoaDon(); }
            @Override public void removeUpdate(DocumentEvent e) { timKiemHoaDon(); }
            @Override public void changedUpdate(DocumentEvent e) { timKiemHoaDon(); }
        });

        setupEnterNavigation();
    }

    private void setupEnterNavigation() {
        Component[] fields = {cboMaNV, cboMaKH, cboMaKM, cboMaSP, txtSoLuong};
        for (int i = 0; i < fields.length - 1; i++) {
            final int next = i + 1;
            if (fields[i] instanceof JTextField) {
                ((JTextField) fields[i]).addActionListener(e -> fields[next].requestFocus());
            } else if (fields[i] instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) fields[i];
                Component editor = combo.getEditor().getEditorComponent();
                if (editor instanceof JTextField) {
                    ((JTextField) editor).addActionListener(e -> fields[next].requestFocus());
                }
            }
        }
    }

    // ============================ NGHIỆP VỤ ============================

    private void moDialogTimKiem(String loai, JComboBox<String> targetCombo) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Tìm chọn " + loai, true);
        dialog.setSize(520, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new MigLayout("fill, insets 15", "[fill]", "[][grow][]"));

        // Thanh tìm kiếm trong dialog
        JPanel pnlTop = new JPanel(new MigLayout("insets 0", "[][grow]", "[]"));
        pnlTop.setOpaque(false);
        JLabel lblTim = new JLabel("Tìm:");
        lblTim.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlTop.add(lblTim);
        JTextField txtTimDialog = createTextField("Nhập mã hoặc tên...");
        pnlTop.add(txtTimDialog, "grow, h 34!");
        dialog.add(pnlTop);

        // Bảng kết quả
        String[] cols;
        if ("NHÂN VIÊN".equals(loai)) {
            cols = new String[]{"Mã", "Họ", "Tên", "Chức vụ"};
        } else if ("KHÁCH HÀNG".equals(loai)) {
            cols = new String[]{"Mã", "Họ", "Tên", "SĐT"};
        } else {
            cols = new String[]{"Mã", "Tên", "Tồn kho", "Đơn giá"};
        }
        DefaultTableModel modelDialog = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableDialog = new JTable(modelDialog);
        tableDialog.setRowHeight(34);
        tableDialog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDialog.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader hd = tableDialog.getTableHeader();
        hd.setBackground(Theme.PRIMARY);
        hd.setForeground(Color.WHITE);
        hd.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Load dữ liệu vào dialog
        Runnable loadDialogData = () -> {
            modelDialog.setRowCount(0);
            String kw = txtTimDialog.getText().trim();
            if ("NHÂN VIÊN".equals(loai)) {
                ArrayList<NhanVienDTO> ds = kw.isEmpty() ? nhanVienBUS.getAll() : nhanVienBUS.search(kw);
                for (NhanVienDTO nv : ds) {
                    modelDialog.addRow(new Object[]{nv.getMaNV(), nv.getHoNV(), nv.getTenNV(), nv.getChucVu()});
                }
            } else if ("KHÁCH HÀNG".equals(loai)) {
                ArrayList<KhachHangDTO> ds = kw.isEmpty() ? khachHangBUS.getAll() : khachHangBUS.search(kw);
                for (KhachHangDTO kh : ds) {
                    modelDialog.addRow(new Object[]{kh.getMaKH(), kh.getHoKH(), kh.getTenKH(), kh.getSoDienThoai()});
                }
            } else {
                ArrayList<SanPhamDTO> ds = kw.isEmpty() ? sanPhamBUS.getAll() : sanPhamBUS.search(kw);
                for (SanPhamDTO sp : ds) {
                    modelDialog.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(),
                            String.valueOf(sp.getSoLuongTon()), currencyFormat.format(sp.getDonGia())});
                }
            }
        };
        loadDialogData.run();

        // Tìm kiếm realtime trong dialog
        txtTimDialog.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { loadDialogData.run(); }
            @Override public void removeUpdate(DocumentEvent e) { loadDialogData.run(); }
            @Override public void changedUpdate(DocumentEvent e) { loadDialogData.run(); }
        });

        JScrollPane sp = new JScrollPane(tableDialog);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        dialog.add(sp, "grow");

        // Nút chọn
        JButton btnChon = new JButton("Chọn");
        btnChon.setBackground(Theme.ACCENT_COLOR);
        btnChon.setForeground(Color.WHITE);
        btnChon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnChon.setFocusPainted(false);
        btnChon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChon.addActionListener(ev -> {
            int r = tableDialog.getSelectedRow();
            if (r >= 0) {
                String ma = modelDialog.getValueAt(r, 0).toString();
                setComboValue(targetCombo, ma);

                // Nếu chọn SP thì fill giá vào form
                if ("SẢN PHẨM".equals(loai)) {
                    SanPhamDTO spDTO = sanPhamBUS.getById(ma);
                    if (spDTO != null) {
                        txtDonGiaBan.setText(String.valueOf(spDTO.getDonGia()));
                        tinhThanhTienSP();
                    }
                }

                dialog.dispose();
            } else {
                showWarning("Vui lòng chọn một dòng!");
            }
        });

        // Double-click để chọn nhanh
        tableDialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    btnChon.doClick();
                }
            }
        });

        dialog.add(btnChon, "align right, w 100!, h 34!");
        dialog.setVisible(true);
    }

    private void tinhThanhTienSP() {
        try {
            long donGia = Long.parseLong(txtDonGiaBan.getText().trim().replaceAll("[^0-9]", ""));
            int sl = Integer.parseInt(txtSoLuong.getText().trim());
            long thanhTien = donGia * sl;
            txtThanhTienSP.setText(currencyFormat.format(thanhTien));
        } catch (NumberFormatException ex) {
            txtThanhTienSP.setText("0");
        }
    }

    private void xuatPDFHoaDon() {
        int row = tableHoaDon.getSelectedRow();
        if (row < 0) {
            showWarning("Vui lòng chọn hóa đơn cần xuất PDF!");
            return;
        }
        String maHD = modelHoaDon.getValueAt(row, 0).toString();

        // Lấy thông tin hóa đơn từ bảng
        String thoiGian = safeGet(modelHoaDon, row, 1);
        String maNV = safeGet(modelHoaDon, row, 2);
        String maKH = safeGet(modelHoaDon, row, 3);
        String maKM = safeGet(modelHoaDon, row, 4);
        String tongTruocKM = safeGet(modelHoaDon, row, 5);
        String giamGia = safeGet(modelHoaDon, row, 6);
        String tongThanhToan = safeGet(modelHoaDon, row, 7);

        // Lấy chi tiết hóa đơn
        ArrayList<CTHoaDonDTO> dsCT = ctHoaDonBUS.getByMaHD(maHD);

        // Chọn nơi lưu file
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu hóa đơn PDF");
        chooser.setSelectedFile(new java.io.File("HoaDon_" + maHD + ".pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String filePath = chooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) filePath += ".pdf";

        try {
            com.lowagie.text.Document doc = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
            com.lowagie.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(filePath));
            doc.open();

            com.lowagie.text.pdf.BaseFont bf = com.lowagie.text.pdf.BaseFont.createFont(
                    "c:/windows/fonts/arial.ttf",
                    com.lowagie.text.pdf.BaseFont.IDENTITY_H,
                    com.lowagie.text.pdf.BaseFont.EMBEDDED);
            com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(bf, 18, com.lowagie.text.Font.BOLD);
            com.lowagie.text.Font fontSubtitle = new com.lowagie.text.Font(bf, 11, com.lowagie.text.Font.NORMAL, java.awt.Color.GRAY);
            com.lowagie.text.Font fontLabel = new com.lowagie.text.Font(bf, 11, com.lowagie.text.Font.BOLD);
            com.lowagie.text.Font fontValue = new com.lowagie.text.Font(bf, 11, com.lowagie.text.Font.NORMAL);
            com.lowagie.text.Font fontHeader = new com.lowagie.text.Font(bf, 10, com.lowagie.text.Font.BOLD, java.awt.Color.WHITE);
            com.lowagie.text.Font fontCell = new com.lowagie.text.Font(bf, 10, com.lowagie.text.Font.NORMAL);
            com.lowagie.text.Font fontTotal = new com.lowagie.text.Font(bf, 12, com.lowagie.text.Font.BOLD, new java.awt.Color(41, 128, 185));

            // === Header cửa hàng ===
            com.lowagie.text.Paragraph pTitle = new com.lowagie.text.Paragraph("CỬA HÀNG SÁCH", fontTitle);
            pTitle.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            doc.add(pTitle);

            com.lowagie.text.Paragraph pSub = new com.lowagie.text.Paragraph("HÓA ĐƠN BÁN HÀNG", fontSubtitle);
            pSub.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            pSub.setSpacingAfter(15);
            doc.add(pSub);

            // === Thông tin hóa đơn ===
            com.lowagie.text.pdf.PdfPTable infoTable = new com.lowagie.text.pdf.PdfPTable(4);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{20, 30, 20, 30});

            addInfoCell(infoTable, "Mã hóa đơn:", fontLabel);
            addInfoCell(infoTable, maHD, fontValue);
            addInfoCell(infoTable, "Thời gian:", fontLabel);
            addInfoCell(infoTable, thoiGian, fontValue);

            addInfoCell(infoTable, "Nhân viên:", fontLabel);
            addInfoCell(infoTable, maNV, fontValue);
            addInfoCell(infoTable, "Khách hàng:", fontLabel);
            addInfoCell(infoTable, maKH, fontValue);

            addInfoCell(infoTable, "Khuyến mãi:", fontLabel);
            addInfoCell(infoTable, maKM.isEmpty() ? "Không" : maKM, fontValue);
            addInfoCell(infoTable, "", fontLabel);
            addInfoCell(infoTable, "", fontValue);

            infoTable.setSpacingAfter(15);
            doc.add(infoTable);

            // === Bảng chi tiết hóa đơn ===
            com.lowagie.text.pdf.PdfPTable detailTable = new com.lowagie.text.pdf.PdfPTable(5);
            detailTable.setWidthPercentage(100);
            detailTable.setWidths(new float[]{10, 35, 15, 20, 20});

            String[] headers = {"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            for (String h : headers) {
                com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(
                        new com.lowagie.text.Phrase(h, fontHeader));
                cell.setBackgroundColor(new java.awt.Color(41, 128, 185));
                cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                cell.setPadding(6);
                detailTable.addCell(cell);
            }

            for (CTHoaDonDTO ct : dsCT) {
                String tenSP = ctHoaDonBUS.getTenSP(ct.getMaSP());
                addDetailCell(detailTable, ct.getMaSP(), fontCell, false);
                addDetailCell(detailTable, tenSP, fontCell, false);
                addDetailCell(detailTable, String.valueOf(ct.getSoLuong()), fontCell, true);
                addDetailCell(detailTable, currencyFormat.format(ct.getDonGiaBan()), fontCell, true);
                addDetailCell(detailTable, currencyFormat.format(ct.getThanhTien()), fontCell, true);
            }

            detailTable.setSpacingAfter(15);
            doc.add(detailTable);

            // === Tổng tiền ===
            com.lowagie.text.pdf.PdfPTable totalTable = new com.lowagie.text.pdf.PdfPTable(2);
            totalTable.setWidthPercentage(50);
            totalTable.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            totalTable.setWidths(new float[]{50, 50});

            addTotalRow(totalTable, "Tổng tiền trước KM:", tongTruocKM, fontLabel, fontValue);
            addTotalRow(totalTable, "Giảm giá:", giamGia, fontLabel, fontValue);
            addTotalRow(totalTable, "TỔNG THANH TOÁN:", tongThanhToan, fontTotal, fontTotal);

            doc.add(totalTable);

            // === Chân trang ===
            com.lowagie.text.Paragraph pFoot = new com.lowagie.text.Paragraph(
                    "\nCảm ơn quý khách đã mua hàng!", fontSubtitle);
            pFoot.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            pFoot.setSpacingBefore(20);
            doc.add(pFoot);

            doc.close();
            showSuccess("Xuất hóa đơn PDF thành công!\n" + filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Lỗi khi xuất PDF: " + ex.getMessage());
        }
    }

    private void addInfoCell(com.lowagie.text.pdf.PdfPTable table, String text,
                             com.lowagie.text.Font font) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(
                new com.lowagie.text.Phrase(text, font));
        cell.setBorder(com.lowagie.text.pdf.PdfPCell.NO_BORDER);
        cell.setPadding(4);
        table.addCell(cell);
    }

    private void addDetailCell(com.lowagie.text.pdf.PdfPTable table, String text,
                               com.lowagie.text.Font font, boolean alignRight) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(
                new com.lowagie.text.Phrase(text, font));
        cell.setPadding(5);
        if (alignRight) cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
        table.addCell(cell);
    }

    private void addTotalRow(com.lowagie.text.pdf.PdfPTable table, String label, String value,
                             com.lowagie.text.Font labelFont, com.lowagie.text.Font valueFont) {
        com.lowagie.text.pdf.PdfPCell c1 = new com.lowagie.text.pdf.PdfPCell(
                new com.lowagie.text.Phrase(label, labelFont));
        c1.setBorder(com.lowagie.text.pdf.PdfPCell.NO_BORDER);
        c1.setPadding(4);
        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
        table.addCell(c1);

        com.lowagie.text.pdf.PdfPCell c2 = new com.lowagie.text.pdf.PdfPCell(
                new com.lowagie.text.Phrase(value, valueFont));
        c2.setBorder(com.lowagie.text.pdf.PdfPCell.NO_BORDER);
        c2.setPadding(4);
        c2.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
        table.addCell(c2);
    }

    public void loadData() {
        modelHoaDon.setRowCount(0);
        modelCTHD.setRowCount(0);
        ArrayList<HoaDonDTO> dsHD = hoaDonBUS.getAll();
        for (HoaDonDTO hd : dsHD) {
            modelHoaDon.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getThoiGian() != null ? dateFormat.format(hd.getThoiGian()) : "",
                    hd.getMaNV(),
                    hd.getMaKH(),
                    hd.getMaKM() != null ? hd.getMaKM() : "",
                    currencyFormat.format(hd.getTongTienTruocKM()),
                    currencyFormat.format(hd.getGiamGiaHD()),
                    currencyFormat.format(hd.getTongTienThanhToan())
            });
        }
        loadComboData();
    }

    private void loadComboData() {
        // Nhân viên
        cboMaNV.removeAllItems();
        cboMaNV.addItem("");
        for (NhanVienDTO nv : nhanVienBUS.getAll()) {
            cboMaNV.addItem(nv.getMaNV());
        }

        // Khách hàng
        cboMaKH.removeAllItems();
        cboMaKH.addItem("");
        for (KhachHangDTO kh : khachHangBUS.getAll()) {
            cboMaKH.addItem(kh.getMaKH());
        }

        // Khuyến mãi (chỉ lấy đang hiệu lực)
        cboMaKM.removeAllItems();
        cboMaKM.addItem("");
        for (KhuyenMaiDTO km : khuyenMaiBUS.getAll()) {
            cboMaKM.addItem(km.getMaKM());
        }

        // Sản phẩm
        cboMaSP.removeAllItems();
        cboMaSP.addItem("");
        for (SanPhamDTO sp : sanPhamBUS.getAll()) {
            cboMaSP.addItem(sp.getMaSP());
        }
    }

    private void loadChiTietHoaDon(String maHD) {
        modelCTHD.setRowCount(0);
        ArrayList<CTHoaDonDTO> dsCT = ctHoaDonBUS.getByMaHD(maHD);
        for (CTHoaDonDTO ct : dsCT) {
            String tenSP = ctHoaDonBUS.getTenSP(ct.getMaSP());
            modelCTHD.addRow(new Object[]{
                    ct.getMaHD(),
                    ct.getMaSP(),
                    tenSP,
                    String.valueOf(ct.getSoLuong()),
                    currencyFormat.format(ct.getDonGiaBan()),
                    currencyFormat.format(ct.getThanhTien())
            });
        }
    }

    private void themSanPhamVaoChiTiet() {
        Object selectedSP = cboMaSP.getSelectedItem();
        if (selectedSP == null || selectedSP.toString().trim().isEmpty()) {
            showWarning("Vui lòng chọn sản phẩm!");
            cboMaSP.requestFocus();
            return;
        }

        String maSP = selectedSP.toString().trim();
        String slStr = txtSoLuong.getText().trim();

        if (slStr.isEmpty()) {
            showWarning("Vui lòng nhập số lượng!");
            txtSoLuong.requestFocus();
            return;
        }

        int sl;
        try {
            sl = Integer.parseInt(slStr);
            if (sl <= 0) {
                showWarning("Số lượng phải lớn hơn 0!");
                txtSoLuong.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            showWarning("Số lượng không hợp lệ!");
            txtSoLuong.requestFocus();
            return;
        }

        // Kiểm tra SP tồn tại + đủ tồn kho
        SanPhamDTO spDTO = sanPhamBUS.getById(maSP);
        if (spDTO == null) {
            showWarning("Không tìm thấy sản phẩm " + maSP + "!");
            return;
        }
        if (spDTO.getSoLuongTon() < sl) {
            showWarning("Sản phẩm " + maSP + " chỉ còn " + spDTO.getSoLuongTon() + " trong kho!");
            return;
        }

        // Kiểm tra trùng SP
        for (int i = 0; i < modelCTHD.getRowCount(); i++) {
            if (modelCTHD.getValueAt(i, 1).toString().equals(maSP)) {
                showWarning("Sản phẩm " + maSP + " đã có trong chi tiết!");
                return;
            }
        }

        String maHD = txtMaHD.getText().trim();
        if (maHD.isEmpty()) maHD = "(Mới)";

        long donGia = spDTO.getDonGia();
        long thanhTien = donGia * sl;

        modelCTHD.addRow(new Object[]{
                maHD, maSP, spDTO.getTenSP(),
                String.valueOf(sl),
                currencyFormat.format(donGia),
                currencyFormat.format(thanhTien)
        });

        capNhatTongTienUI();

        // Reset form SP
        cboMaSP.setSelectedItem(null);
        txtSoLuong.setText("1");
        txtDonGiaBan.setText("");
        txtThanhTienSP.setText("0");
        cboMaSP.requestFocus();
    }

    private void xoaSanPhamKhoiChiTiet() {
        int row = tableCTHD.getSelectedRow();
        if (row < 0) {
            showWarning("Vui lòng chọn sản phẩm cần xóa!");
            return;
        }

        String maSP = modelCTHD.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa sản phẩm " + maSP + " khỏi hóa đơn?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            modelCTHD.removeRow(row);
            capNhatTongTienUI();
            tableCTHD.clearSelection();
            cboMaSP.setSelectedItem(null);
            txtSoLuong.setText("1");
            txtDonGiaBan.setText("");
            txtThanhTienSP.setText("0");
        }
    }

    private void capNhatTongTienUI() {
        long tongTien = 0;
        for (int i = 0; i < modelCTHD.getRowCount(); i++) {
            String ttStr = modelCTHD.getValueAt(i, 5).toString().replaceAll("[^0-9]", "");
            if (!ttStr.isEmpty()) {
                tongTien += Long.parseLong(ttStr);
            }
        }
        txtTongTienTruocKM.setText(currencyFormat.format(tongTien));

        // Tạo danh sách CTHD tạm để tính giảm giá chính xác (hỗ trợ GIAM_SP)
        Object kmSelected = cboMaKM.getSelectedItem();
        String maKM = kmSelected != null ? kmSelected.toString().trim() : "";
        ArrayList<CTHoaDonDTO> dsCTTam = new ArrayList<>();
        for (int i = 0; i < modelCTHD.getRowCount(); i++) {
            CTHoaDonDTO ct = new CTHoaDonDTO();
            ct.setMaSP(modelCTHD.getValueAt(i, 1).toString());
            ct.setSoLuong(Integer.parseInt(modelCTHD.getValueAt(i, 3).toString().replaceAll("[^0-9]", "")));
            dsCTTam.add(ct);
        }
        long giamGia = khuyenMaiBUS.tinhGiamGiaChiTiet(maKM, tongTien, dsCTTam);
        txtGiamGiaHD.setText(currencyFormat.format(giamGia));
        txtTongTienThanhToan.setText(currencyFormat.format(tongTien - giamGia));
    }

    private void taoHoaDon() {
        Object nvSelected = cboMaNV.getSelectedItem();
        if (nvSelected == null || nvSelected.toString().trim().isEmpty()) {
            showWarning("Vui lòng chọn nhân viên!");
            cboMaNV.requestFocus();
            return;
        }
        Object khSelected = cboMaKH.getSelectedItem();
        if (khSelected == null || khSelected.toString().trim().isEmpty()) {
            showWarning("Vui lòng chọn khách hàng!");
            cboMaKH.requestFocus();
            return;
        }
        if (modelCTHD.getRowCount() == 0) {
            showWarning("Hóa đơn chưa có sản phẩm nào!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận tạo hóa đơn mới?\n\n"
                + "Nhân viên: " + nvSelected + "\n"
                + "Khách hàng: " + khSelected + "\n"
                + "Số sản phẩm: " + modelCTHD.getRowCount() + "\n"
                + "Tổng thanh toán: " + txtTongTienThanhToan.getText(),
                "Xác nhận tạo hóa đơn",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Tạo DTO hóa đơn
            HoaDonDTO hd = new HoaDonDTO();
            hd.setMaNV(nvSelected.toString().trim());
            hd.setMaKH(khSelected.toString().trim());
            Object kmSel = cboMaKM.getSelectedItem();
            hd.setMaKM(kmSel != null ? kmSel.toString().trim() : "");

            // Tính giảm giá
            long tongTruocKM = parseCurrency(txtTongTienTruocKM.getText());
            long giamGia = parseCurrency(txtGiamGiaHD.getText());
            hd.setTongTienTruocKM(tongTruocKM);
            hd.setGiamGiaHD(giamGia);
            hd.setTongTienThanhToan(tongTruocKM - giamGia);

            // Tạo danh sách chi tiết
            ArrayList<CTHoaDonDTO> dsCTHD = new ArrayList<>();
            for (int i = 0; i < modelCTHD.getRowCount(); i++) {
                CTHoaDonDTO ct = new CTHoaDonDTO();
                ct.setMaSP(modelCTHD.getValueAt(i, 1).toString());
                ct.setSoLuong(Integer.parseInt(modelCTHD.getValueAt(i, 3).toString().replaceAll("[^0-9]", "")));
                ct.setDonGiaBan(parseCurrency(modelCTHD.getValueAt(i, 4).toString()));
                ct.setThanhTien(parseCurrency(modelCTHD.getValueAt(i, 5).toString()));
                dsCTHD.add(ct);
            }

            boolean ok = hoaDonBUS.taoHoaDon(hd, dsCTHD);
            if (ok) {
                showSuccess("Tạo hóa đơn " + hd.getMaHD() + " thành công!");
                resetForm();
                loadData();
            } else {
                showError("Tạo hóa đơn thất bại!\nCó thể sản phẩm không đủ tồn kho.");
            }
        }
    }

    private void suaHoaDon() {
        if (!isEditing) {
            showWarning("Vui lòng chọn hóa đơn cần sửa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận cập nhật hóa đơn " + txtMaHD.getText().trim() + "?",
                "Xác nhận sửa",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            HoaDonDTO hd = new HoaDonDTO();
            hd.setMaHD(txtMaHD.getText().trim());
            Object nvSel = cboMaNV.getSelectedItem();
            hd.setMaNV(nvSel != null ? nvSel.toString().trim() : "");
            Object khSel = cboMaKH.getSelectedItem();
            hd.setMaKH(khSel != null ? khSel.toString().trim() : "");
            Object kmSel = cboMaKM.getSelectedItem();
            hd.setMaKM(kmSel != null ? kmSel.toString().trim() : "");

            long tongTruocKM = parseCurrency(txtTongTienTruocKM.getText());
            long giamGia = parseCurrency(txtGiamGiaHD.getText());
            hd.setTongTienTruocKM(tongTruocKM);
            hd.setGiamGiaHD(giamGia);
            hd.setTongTienThanhToan(tongTruocKM - giamGia);

            ArrayList<CTHoaDonDTO> dsCTHDMoi = new ArrayList<>();
            for (int i = 0; i < modelCTHD.getRowCount(); i++) {
                CTHoaDonDTO ct = new CTHoaDonDTO();
                ct.setMaSP(modelCTHD.getValueAt(i, 1).toString());
                ct.setSoLuong(Integer.parseInt(modelCTHD.getValueAt(i, 3).toString().replaceAll("[^0-9]", "")));
                ct.setDonGiaBan(parseCurrency(modelCTHD.getValueAt(i, 4).toString()));
                ct.setThanhTien(parseCurrency(modelCTHD.getValueAt(i, 5).toString()));
                dsCTHDMoi.add(ct);
            }

            boolean ok = hoaDonBUS.suaHoaDon(hd, dsCTHDMoi);
            if (ok) {
                showSuccess("Cập nhật hóa đơn thành công!");
                resetForm();
                loadData();
            } else {
                showError("Cập nhật hóa đơn thất bại!");
            }
        }
    }

    private void xoaHoaDon() {
        int row = tableHoaDon.getSelectedRow();
        if (row < 0) {
            showWarning("Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        String maHD = modelHoaDon.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa hóa đơn " + maHD + "?\nTất cả chi tiết sẽ bị xóa theo.\nTồn kho sẽ được hoàn lại.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = hoaDonBUS.xoaHoaDon(maHD);
            if (ok) {
                showSuccess("Xóa hóa đơn thành công!");
                resetForm();
                loadData();
            } else {
                showError("Xóa hóa đơn thất bại!");
            }
        }
    }

    private void timKiemHoaDon() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        String column = cboSearchField.getSelectedItem().toString();
        String operator = cboSearchOp.getSelectedItem().toString();

        modelHoaDon.setRowCount(0);
        modelCTHD.setRowCount(0);
        ArrayList<HoaDonDTO> dsHD = hoaDonBUS.search(column, operator, keyword);
        for (HoaDonDTO hd : dsHD) {
            modelHoaDon.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getThoiGian() != null ? dateFormat.format(hd.getThoiGian()) : "",
                    hd.getMaNV(),
                    hd.getMaKH(),
                    hd.getMaKM() != null ? hd.getMaKM() : "",
                    currencyFormat.format(hd.getTongTienTruocKM()),
                    currencyFormat.format(hd.getGiamGiaHD()),
                    currencyFormat.format(hd.getTongTienThanhToan())
            });
        }
    }

    private void resetForm() {
        txtMaHD.setText("");
        txtThoiGian.setText(dateFormat.format(new Timestamp(System.currentTimeMillis())));
        cboMaNV.setSelectedItem(null);
        cboMaKH.setSelectedItem(null);
        if (cboMaKM.getItemCount() > 0) cboMaKM.setSelectedIndex(0);
        txtTongTienTruocKM.setText("0 đ");
        txtGiamGiaHD.setText("0 đ");
        txtTongTienThanhToan.setText("0 đ");

        cboMaSP.setSelectedItem(null);
        txtSoLuong.setText("1");
        txtDonGiaBan.setText("");
        txtThanhTienSP.setText("0");

        searchField.setText("");
        modelCTHD.setRowCount(0);
        tableHoaDon.clearSelection();
        tableCTHD.clearSelection();

        isEditing = false;
        btnLuu.setText("Lưu HĐ");
        cboMaNV.requestFocus();
    }

    // ============================ HELPER ============================

    private String safeGet(DefaultTableModel model, int row, int col) {
        Object val = model.getValueAt(row, col);
        return val != null ? val.toString() : "";
    }

    private void setComboValue(JComboBox<String> combo, String value) {
        boolean found = false;
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(value)) {
                found = true;
                break;
            }
        }
        if (!found && value != null && !value.isEmpty()) {
            combo.addItem(value);
        }
        combo.setSelectedItem(value);
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.putClientProperty("JTextField.placeholderText", placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return textField;
    }

    private JTextField createReadOnlyField(String placeholder) {
        JTextField textField = createTextField(placeholder);
        textField.setEnabled(false);
        textField.setBackground(new Color(243, 244, 246));
        textField.setDisabledTextColor(new Color(75, 85, 99));
        return textField;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setEditable(true);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JButton createBrowseButton() {
        JButton btn = new JButton("...");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Theme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0));
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(Theme.TEXT);
        return label;
    }

    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        return sep;
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông tin", JOptionPane.INFORMATION_MESSAGE);
    }

    private long parseCurrency(String text) {
        String digits = text.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        return Long.parseLong(digits);
    }
}