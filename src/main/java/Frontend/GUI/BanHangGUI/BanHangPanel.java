package Frontend.GUI.BanHangGUI;

import Backend.DTO.KhachHang_BanHang.HoaDonDTO;
import Backend.DTO.KhachHang_BanHang.CTHoaDonDTO;
import Frontend.Compoent.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BanHangPanel extends JPanel {

    private JTable tableHoaDon;
    private DefaultTableModel modelHoaDon;

    private JTable tableCTHD;
    private DefaultTableModel modelCTHD;

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

    private JComboBox<String> cboMaSP;
    private JButton btnChonSP;
    private JTextField txtSoLuong;
    private JTextField txtDonGiaBan;
    private JTextField txtThanhTienSP;

    private ButtonAdd btnLuu;
    private ButtonFix btnSua;
    private ButtonDele btnXoa;
    private ButtonRefresh btnMoi;
    private ButtonNhapExcel btnNhap;
    private ButtonXuatExcel btnXuat;
    private ButtonXuatPdf btnXuatPDF;
    private CustomButton btnThemSP;
    private CustomButton btnXoaSP;

    private SearchTextField searchField;
    private JComboBox<String> cboSearchField;
    private JComboBox<String> cboSearchOp;

    private final DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private boolean isEditing = false;

    public BanHangPanel() {
        initComponent();
        initEvent();
        loadData();
    }

    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        this.setLayout(new MigLayout("fill, insets 15", "[320!][grow]", "[fill]"));

        JPanel pnlForm = createFormPanel();
        JPanel pnlRight = createRightPanel();

        this.add(pnlForm);
        this.add(pnlRight, "grow");
    }

    private JPanel createFormPanel() {
        JPanel pnlForm = new JPanel(new MigLayout("wrap, fill, insets 20", "[fill]", ""));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));

        JLabel lbTitle = new JLabel("QUẢN LÝ BÁN HÀNG");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lbTitle, "align center, gapbottom 15, span");

        JLabel lblMaHD = createLabel("Mã HD:");
        JLabel lblAuto = new JLabel("(Tự động)");
        lblAuto.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblAuto.setForeground(new Color(107, 114, 128));
        JPanel pnlMaLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlMaLabel.setOpaque(false);
        pnlMaLabel.add(lblMaHD);
        pnlMaLabel.add(lblAuto);
        pnlForm.add(pnlMaLabel);
        txtMaHD = createTextField("Mã hóa đơn");
        txtMaHD.setEnabled(false);
        txtMaHD.setBackground(new Color(240, 240, 240));
        txtMaHD.setForeground(new Color(75, 85, 99));
        pnlForm.add(txtMaHD, "h 40!, gapbottom 5");

        pnlForm.add(createLabel("Thời gian:"));
        txtThoiGian = createTextField("yyyy-MM-dd HH:mm");
        txtThoiGian.setText(dateFormat.format(new Timestamp(System.currentTimeMillis())));
        txtThoiGian.setEnabled(false);
        txtThoiGian.setBackground(new Color(240, 240, 240));
        pnlForm.add(txtThoiGian, "h 40!, gapbottom 5");

        pnlForm.add(createLabel("Nhân viên:"));
        JPanel pnlNV = new JPanel(new MigLayout("fill, insets 0", "[grow][]", "[]"));
        pnlNV.setOpaque(false);
        cboMaNV = createComboBox();
        btnChonNV = createBrowseButton();
        pnlNV.add(cboMaNV, "grow");
        pnlNV.add(btnChonNV, "w 40!, h 40!");
        pnlForm.add(pnlNV, "h 40!, gapbottom 5");

        pnlForm.add(createLabel("Khách hàng:"));
        JPanel pnlKH = new JPanel(new MigLayout("fill, insets 0", "[grow][]", "[]"));
        pnlKH.setOpaque(false);
        cboMaKH = createComboBox();
        btnChonKH = createBrowseButton();
        pnlKH.add(cboMaKH, "grow");
        pnlKH.add(btnChonKH, "w 40!, h 40!");
        pnlForm.add(pnlKH, "h 40!, gapbottom 5");

        pnlForm.add(createLabel("Khuyến mãi:"));
        cboMaKM = createComboBox();
        cboMaKM.addItem("");
        pnlForm.add(cboMaKM, "h 40!, gapbottom 10");

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(Theme.BORDER);
        pnlForm.add(sep1, "growx, gapbottom 8");

        pnlForm.add(createLabel("Tổng trước KM:"));
        txtTongTienTruocKM = createTextField("0");
        txtTongTienTruocKM.setEnabled(false);
        txtTongTienTruocKM.setBackground(new Color(240, 240, 240));
        txtTongTienTruocKM.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlForm.add(txtTongTienTruocKM, "h 40!, gapbottom 5");

        pnlForm.add(createLabel("Giảm giá HĐ:"));
        txtGiamGiaHD = createTextField("0");
        txtGiamGiaHD.setEnabled(false);
        txtGiamGiaHD.setBackground(new Color(240, 240, 240));
        txtGiamGiaHD.setForeground(Theme.DANGER_COLOR);
        txtGiamGiaHD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlForm.add(txtGiamGiaHD, "h 40!, gapbottom 5");

        pnlForm.add(createLabel("TỔNG THANH TOÁN:"));
        txtTongTienThanhToan = createTextField("0");
        txtTongTienThanhToan.setEnabled(false);
        txtTongTienThanhToan.setBackground(new Color(255, 250, 230));
        txtTongTienThanhToan.setForeground(Theme.PRIMARY);
        txtTongTienThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTongTienThanhToan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT, 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        pnlForm.add(txtTongTienThanhToan, "h 42!, gapbottom 10");

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(Theme.BORDER);
        pnlForm.add(sep2, "growx, gapbottom 8");

        JLabel lblCTTitle = new JLabel("THÊM SẢN PHẨM VÀO HĐ");
        lblCTTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCTTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lblCTTitle, "gapbottom 5");

        pnlForm.add(createLabel("Sản phẩm:"));
        JPanel pnlSP = new JPanel(new MigLayout("fill, insets 0", "[grow][]", "[]"));
        pnlSP.setOpaque(false);
        cboMaSP = createComboBox();
        btnChonSP = createBrowseButton();
        pnlSP.add(cboMaSP, "grow");
        pnlSP.add(btnChonSP, "w 38!, h 38!");
        pnlForm.add(pnlSP, "h 38!, gapbottom 5");

        pnlForm.add(createLabel("Số lượng:"));
        txtSoLuong = createTextField("1");
        pnlForm.add(txtSoLuong, "h 38!, gapbottom 5");

        pnlForm.add(createLabel("Đơn giá bán:"));
        txtDonGiaBan = createTextField("Tự lấy từ SP");
        txtDonGiaBan.setEnabled(false);
        txtDonGiaBan.setBackground(new Color(240, 240, 240));
        pnlForm.add(txtDonGiaBan, "h 38!, gapbottom 5");

        pnlForm.add(createLabel("Thành tiền:"));
        txtThanhTienSP = createTextField("0");
        txtThanhTienSP.setEnabled(false);
        txtThanhTienSP.setBackground(new Color(240, 240, 240));
        txtThanhTienSP.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlForm.add(txtThanhTienSP, "h 38!, gapbottom 5");

        JPanel pnlSPButtons = new JPanel(new MigLayout("insets 0, gap 8", "[grow][grow]", "[]"));
        pnlSPButtons.setOpaque(false);
        btnThemSP = new CustomButton("+ Thêm SP", Theme.ACCENT_COLOR);
        btnThemSP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnXoaSP = new CustomButton("- Xóa SP", Theme.DANGER_COLOR);
        btnXoaSP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlSPButtons.add(btnThemSP, "grow, h 34!");
        pnlSPButtons.add(btnXoaSP, "grow, h 34!");
        pnlForm.add(pnlSPButtons, "gapbottom 10");

        JPanel pnlButtons = createButtonPanel();
        pnlForm.add(pnlButtons, "grow, gaptop 5");

        return pnlForm;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new MigLayout("wrap 2, gap 10", "[grow]", ""));
        pnlButtons.setOpaque(false);

        btnLuu = new ButtonAdd("Lưu");
        btnSua = new ButtonFix("Sửa");
        btnXoa = new ButtonDele("Xóa");
        btnMoi = new ButtonRefresh("Mới");
        btnNhap = new ButtonNhapExcel("Nhập Excel");
        btnXuat = new ButtonXuatExcel("Xuất Excel");
        btnXuatPDF = new ButtonXuatPdf("In hóa đơn PDF");

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Component[] buttons = {btnLuu, btnSua, btnXoa, btnMoi, btnNhap, btnXuat, btnXuatPDF};
        for (Component btn : buttons) {
            if (btn instanceof JButton) {
                ((JButton) btn).setFont(buttonFont);
                ((JButton) btn).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        pnlButtons.add(btnLuu, "grow");
        pnlButtons.add(btnSua, "grow");
        pnlButtons.add(btnXoa, "grow");
        pnlButtons.add(btnMoi, "grow");
        pnlButtons.add(btnNhap, "span, grow");
        pnlButtons.add(btnXuat, "span, grow");
        pnlButtons.add(btnXuatPDF, "span, grow");

        return pnlButtons;
    }

    private JPanel createRightPanel() {
        JPanel pnlRight = new JPanel(new MigLayout("fill, wrap 1", "[fill]", "[][grow 55][][grow 45]"));
        pnlRight.setBackground(Theme.BACKGROUND);

        JPanel pnlSearch = createSearchPanel();
        pnlRight.add(pnlSearch, "gaptop 0");

        JPanel pnlHoaDon = createHoaDonTablePanel();
        pnlRight.add(pnlHoaDon, "grow");

        JLabel lblCT = new JLabel("CHI TIẾT HÓA ĐƠN");
        lblCT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCT.setForeground(Theme.PRIMARY);
        pnlRight.add(lblCT, "gaptop 10, gapbottom 5");

        JPanel pnlChiTiet = createCTHDTablePanel();
        pnlRight.add(pnlChiTiet, "grow");

        return pnlRight;
    }

    private JPanel createSearchPanel() {
        JPanel pnlSearch = new JPanel(new MigLayout("insets 0", "[][][][grow][]", "[]"));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSearch.setForeground(Theme.TEXT);
        pnlSearch.add(lblSearch);

        cboSearchField = new JComboBox<>(new String[]{
                "Tất cả", "Mã HĐ", "Mã NV", "Mã KH", "Mã KM"
        });
        cboSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboSearchField.setPreferredSize(new Dimension(100, 35));
        pnlSearch.add(cboSearchField);

        cboSearchOp = new JComboBox<>(new String[]{
                "Chứa", "Bằng", ">", ">=", "<", "<=", "<>"
        });
        cboSearchOp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboSearchOp.setPreferredSize(new Dimension(70, 35));
        pnlSearch.add(cboSearchOp);

        searchField = new SearchTextField();
        searchField.setPlaceholder("Nhập giá trị tìm kiếm...");
        searchField.setPreferredSize(new Dimension(0, 35));
        pnlSearch.add(searchField, "grow");

        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(Theme.PRIMARY);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlSearch.add(btnTimKiem, "w 80!");

        return pnlSearch;
    }

    private JPanel createHoaDonTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1", "[fill]", "[grow]"));
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
        tableHoaDon.setRowHeight(40);
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableHoaDon.setDefaultRenderer(Object.class, new HoaDonTableRenderer());

        JTableHeader header = tableHoaDon.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        TableColumnModel colModel = tableHoaDon.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(80);
        colModel.getColumn(1).setPreferredWidth(140);
        colModel.getColumn(2).setPreferredWidth(70);
        colModel.getColumn(3).setPreferredWidth(70);
        colModel.getColumn(4).setPreferredWidth(70);
        colModel.getColumn(5).setPreferredWidth(120);
        colModel.getColumn(6).setPreferredWidth(100);
        colModel.getColumn(7).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTable.add(scrollPane, "grow, h 100%");

        return pnlTable;
    }

    private JPanel createCTHDTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1", "[fill]", "[grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        modelCTHD = new DefaultTableModel(
                new String[]{"Mã HĐ", "Mã SP", "Số lượng", "Đơn giá bán", "Thành tiền"}, 0) {
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
        tableCTHD.setRowHeight(40);
        tableCTHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCTHD.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableCTHD.setDefaultRenderer(Object.class, new CTHDTableRenderer());

        JTableHeader header = tableCTHD.getTableHeader();
        header.setBackground(new Color(30, 58, 95));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        TableColumnModel colModel = tableCTHD.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(100);
        colModel.getColumn(1).setPreferredWidth(100);
        colModel.getColumn(2).setPreferredWidth(80);
        colModel.getColumn(3).setPreferredWidth(130);
        colModel.getColumn(4).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(tableCTHD);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTable.add(scrollPane, "grow, h 100%");

        return pnlTable;
    }

    private class HoaDonTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(new Color(59, 130, 246, 50));
                c.setForeground(Color.BLACK);
            }

            if (column == 0 || column == 2 || column == 3 || column == 4) {
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            } else if (column == 1) {
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
            } else if (column >= 5) {
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
            }

            if (column == 7 && !isSelected) {
                c.setForeground(Theme.ACCENT_COLOR);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }

            if (column == 6 && value != null && !value.toString().equals("0") && !isSelected) {
                c.setForeground(Theme.DANGER_COLOR);
            }

            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return c;
        }
    }

    private class CTHDTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(new Color(59, 130, 246, 50));
                c.setForeground(Color.BLACK);
            }

            if (column == 0 || column == 1 || column == 2) {
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            }

            if (column == 3 || column == 4) {
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
            }

            if (column == 4) {
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }

            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return c;
        }
    }

    private void initEvent() {
        tableHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableHoaDon.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    txtMaHD.setText(safeGet(modelHoaDon, row, 0));
                    txtThoiGian.setText(safeGet(modelHoaDon, row, 1));
                    cboMaNV.setSelectedItem(safeGet(modelHoaDon, row, 2));
                    cboMaKH.setSelectedItem(safeGet(modelHoaDon, row, 3));
                    cboMaKM.setSelectedItem(safeGet(modelHoaDon, row, 4));
                    txtTongTienTruocKM.setText(safeGet(modelHoaDon, row, 5));
                    txtGiamGiaHD.setText(safeGet(modelHoaDon, row, 6));
                    txtTongTienThanhToan.setText(safeGet(modelHoaDon, row, 7));

                    txtMaHD.setEnabled(false);
                    txtMaHD.setBackground(new Color(240, 240, 240));
                    txtMaHD.setForeground(new Color(75, 85, 99));

                    loadChiTietHoaDon(safeGet(modelHoaDon, row, 0));
                    btnLuu.setText("Lưu");
                }
            }
        });

        tableCTHD.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCTHD.getSelectedRow();
                if (row >= 0) {
                    cboMaSP.setSelectedItem(safeGet(modelCTHD, row, 1));
                    txtSoLuong.setText(safeGet(modelCTHD, row, 2));
                    txtDonGiaBan.setText(safeGet(modelCTHD, row, 3));
                    txtThanhTienSP.setText(safeGet(modelCTHD, row, 4));
                }
            }
        });

        btnChonNV.addActionListener(e -> moDialogTimKiem("NHÂN VIÊN", cboMaNV));
        btnChonKH.addActionListener(e -> moDialogTimKiem("KHÁCH HÀNG", cboMaKH));
        btnChonSP.addActionListener(e -> moDialogTimKiem("SẢN PHẨM", cboMaSP));

        btnThemSP.addActionListener(e -> themSanPhamVaoChiTiet());
        btnXoaSP.addActionListener(e -> xoaSanPhamKhoiChiTiet());

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
        btnNhap.addActionListener(e -> showInfo("Chức năng nhập Excel đang được phát triển..."));
        btnXuat.addActionListener(e -> showInfo("Chức năng xuất Excel đang được phát triển..."));
        btnXuatPDF.addActionListener(e -> xuatPDFHoaDon());

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

    private void moDialogTimKiem(String loai, JComboBox<String> targetCombo) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Tìm chọn " + loai, true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new MigLayout("fill, insets 15", "[fill]", "[][grow][]"));

        JPanel pnlTop = new JPanel(new MigLayout("insets 0", "[][grow]", "[]"));
        pnlTop.setOpaque(false);
        pnlTop.add(new JLabel("Tìm:"));
        JTextField txtTimDialog = createTextField("Nhập mã hoặc tên...");
        pnlTop.add(txtTimDialog, "grow, h 35!");
        dialog.add(pnlTop);

        DefaultTableModel modelDialog = new DefaultTableModel(new String[]{"Mã", "Tên"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableDialog = new JTable(modelDialog);
        tableDialog.setRowHeight(35);
        tableDialog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDialog.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader hd = tableDialog.getTableHeader();
        hd.setBackground(Theme.PRIMARY);
        hd.setForeground(Color.WHITE);
        hd.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane sp = new JScrollPane(tableDialog);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        dialog.add(sp, "grow");

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
                boolean found = false;
                for (int i = 0; i < targetCombo.getItemCount(); i++) {
                    if (targetCombo.getItemAt(i).equals(ma)) {
                        found = true;
                        break;
                    }
                }
                if (!found) targetCombo.addItem(ma);
                targetCombo.setSelectedItem(ma);
                dialog.dispose();
            } else {
                showWarning("Vui lòng chọn một dòng!");
            }
        });
        dialog.add(btnChon, "align right, w 100!, h 35!");

        dialog.setVisible(true);
    }

    private void xuatPDFHoaDon() {
        int row = tableHoaDon.getSelectedRow();
        if (row < 0) {
            showWarning("Vui lòng chọn hóa đơn cần in!");
            return;
        }
        String maHD = modelHoaDon.getValueAt(row, 0).toString();
        showInfo("Xuất PDF hóa đơn " + maHD + " đang được phát triển...");
    }

    public void loadData() {
        modelHoaDon.setRowCount(0);
        modelCTHD.setRowCount(0);
    }

    private void loadChiTietHoaDon(String maHD) {
        modelCTHD.setRowCount(0);
    }

    private void themSanPhamVaoChiTiet() {
        Object selectedSP = cboMaSP.getSelectedItem();
        if (selectedSP == null || selectedSP.toString().trim().isEmpty()) {
            showWarning("Vui lòng chọn Mã sản phẩm!");
            cboMaSP.requestFocus();
            return;
        }

        String maSP = selectedSP.toString().trim();
        String slStr = txtSoLuong.getText().trim();

        if (slStr.isEmpty()) {
            showWarning("Vui lòng nhập Số lượng!");
            txtSoLuong.requestFocus();
            return;
        }

        for (int i = 0; i < modelCTHD.getRowCount(); i++) {
            if (modelCTHD.getValueAt(i, 1).toString().equals(maSP)) {
                showWarning("Sản phẩm " + maSP + " đã có trong chi tiết!");
                return;
            }
        }

        String maHD = txtMaHD.getText().trim();
        if (maHD.isEmpty()) maHD = "(Mới)";

        modelCTHD.addRow(new Object[]{
                maHD, maSP, slStr,
                txtDonGiaBan.getText().trim(),
                txtThanhTienSP.getText().trim()
        });

        capNhatTongTienUI();

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
            String ttStr = modelCTHD.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
            if (!ttStr.isEmpty()) {
                tongTien += Long.parseLong(ttStr);
            }
        }
        txtTongTienTruocKM.setText(currencyFormat.format(tongTien));
        txtGiamGiaHD.setText("0");
        txtTongTienThanhToan.setText(currencyFormat.format(tongTien));
    }

    private void taoHoaDon() {
        Object nvSelected = cboMaNV.getSelectedItem();
        if (nvSelected == null || nvSelected.toString().trim().isEmpty()) {
            showWarning("Vui lòng chọn Nhân viên!");
            cboMaNV.requestFocus();
            return;
        }
        Object khSelected = cboMaKH.getSelectedItem();
        if (khSelected == null || khSelected.toString().trim().isEmpty()) {
            showWarning("Vui lòng chọn Khách hàng!");
            cboMaKH.requestFocus();
            return;
        }
        if (modelCTHD.getRowCount() == 0) {
            showWarning("Hóa đơn chưa có sản phẩm nào!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận tạo hóa đơn mới?\n\n" +
                "NV: " + nvSelected.toString() + "\n" +
                "KH: " + khSelected.toString() + "\n" +
                "Số SP: " + modelCTHD.getRowCount() + "\n" +
                "Tổng thanh toán: " + txtTongTienThanhToan.getText(),
                "Xác nhận tạo hóa đơn",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            showSuccess("Tạo hóa đơn thành công!");
            resetForm();
            loadData();
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
            showSuccess("Cập nhật hóa đơn thành công!");
            resetForm();
            loadData();
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
                "Xóa hóa đơn " + maHD + "?\nTất cả chi tiết sẽ bị xóa theo.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            showSuccess("Xóa hóa đơn thành công!");
            resetForm();
            loadData();
        }
    }

    private void timKiemHoaDon() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
        }
    }

    private void resetForm() {
        txtMaHD.setText("");
        txtThoiGian.setText(dateFormat.format(new Timestamp(System.currentTimeMillis())));
        cboMaNV.setSelectedItem(null);
        cboMaKH.setSelectedItem(null);
        cboMaKM.setSelectedIndex(0);
        txtTongTienTruocKM.setText("0");
        txtGiamGiaHD.setText("0");
        txtTongTienThanhToan.setText("0");

        cboMaSP.setSelectedItem(null);
        txtSoLuong.setText("1");
        txtDonGiaBan.setText("");
        txtThanhTienSP.setText("0");

        searchField.setText("");
        modelCTHD.setRowCount(0);
        tableHoaDon.clearSelection();
        tableCTHD.clearSelection();

        isEditing = false;
        btnLuu.setText("Lưu");
        cboMaNV.requestFocus();
    }

    private String safeGet(DefaultTableModel model, int row, int col) {
        Object val = model.getValueAt(row, col);
        return val != null ? val.toString() : "";
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.putClientProperty("JTextField.placeholderText", placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setEditable(true);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JButton createBrowseButton() {
        JButton btn = new JButton("...");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Theme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0));
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Theme.TEXT);
        return label;
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
}