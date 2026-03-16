package Frontend.GUI.NhapHangGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

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

public class NhapHangPanel extends JPanel {

    private static final int FORM_COLUMN_WIDTH = 360;
    private static final int FIELD_HEIGHT = 32;
    private static final int TOTAL_FIELD_HEIGHT = 40;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BROWSE_BUTTON_WIDTH = 38;
    private static final int INLINE_LABEL_WIDTH = 70;

    // ======= Bảng Phiếu nhập =======
    private JTable tablePhieuNhap;
    private DefaultTableModel modelPhieuNhap;

    // ======= Bảng Chi tiết phiếu nhập =======
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;

    // ======= Form Phiếu nhập =======
    private JTextField txtMaPhieuNhap;
    private JTextField txtNgayNhap;
    private JComboBox<String> cboMaNV;
    private JButton btnChonNV;
    private JComboBox<String> cboMaNCC;
    private JButton btnChonNCC;
    private JTextField txtTongTienNhap;

    // ======= Form Chi tiết (thêm SP vào phiếu) =======
    private JComboBox<String> cboMaSP;
    private JButton btnChonSP;
    private JSpinner spinSoLuong;
    private JTextField txtDonGia;

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
    private JButton btnTimKiem;

    private final DecimalFormat currencyFormat = new DecimalFormat("#,### đ");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public NhapHangPanel() {
        initComponent();
        new NhapHangController(this);
    }

    // ============================ GIAO DIỆN ============================

    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        this.setLayout(new MigLayout("fill, insets 10, gap 12", "[" + FORM_COLUMN_WIDTH + "!][grow]", "[fill]"));

        JPanel pnlForm = createFormPanel();
        JScrollPane scrollForm = new JScrollPane(pnlForm);
        scrollForm.setBorder(BorderFactory.createEmptyBorder());
        scrollForm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        scrollForm.setBackground(Color.WHITE);
        scrollForm.getViewport().setBackground(Color.WHITE);

        JPanel pnlRight = createRightPanel();

        this.add(scrollForm, "w " + FORM_COLUMN_WIDTH + "!");
        this.add(pnlRight, "grow");
    }

    // -------------------- Panel trái: Form nhập liệu --------------------

    private JPanel createFormPanel() {
        JPanel pnlForm = new JPanel(new MigLayout("wrap, fillx, insets 12, gapy 6", "[grow,fill]", ""));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Tiêu đề
        JLabel lbTitle = new JLabel("NHẬP HÀNG");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbTitle.setForeground(Theme.PRIMARY);
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlForm.add(lbTitle, "align center, gapbottom 2");

        JLabel lbSubtitle = new JLabel("Quản lý thông tin phiếu nhập và thêm sản phẩm nhanh");
        lbSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbSubtitle.setForeground(new Color(100, 116, 139));
        lbSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlForm.add(lbSubtitle, "align center, gapbottom 8");

        // --- Mã phiếu ---
        JPanel pnlMaLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlMaLabel.setOpaque(false);
        pnlMaLabel.add(createLabel("Mã phiếu:"));
        JLabel lblAuto = new JLabel("  (Tự động)");
        lblAuto.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblAuto.setForeground(new Color(156, 163, 175));
        pnlMaLabel.add(lblAuto);
        pnlForm.add(pnlMaLabel);
        txtMaPhieuNhap = createReadOnlyField();
        pnlForm.add(txtMaPhieuNhap, "h " + FIELD_HEIGHT + "!");

        // --- Ngày nhập ---
        pnlForm.add(createLabel("Ngày nhập:"));
        txtNgayNhap = createReadOnlyField();
        txtNgayNhap.setText(dateFormat.format(new Timestamp(System.currentTimeMillis())));
        pnlForm.add(txtNgayNhap, "h " + FIELD_HEIGHT + "!");

        // --- Nhân viên ---
        pnlForm.add(createLabel("Nhân viên:"));
        cboMaNV = createComboBox();
        btnChonNV = createBrowseButton();
        pnlForm.add(createSelectionRow(cboMaNV, btnChonNV), "gapbottom 2");

        // --- Nhà cung cấp ---
        pnlForm.add(createLabel("Nhà cung cấp:"));
        cboMaNCC = createComboBox();
        btnChonNCC = createBrowseButton();
        pnlForm.add(createSelectionRow(cboMaNCC, btnChonNCC), "gapbottom 2");

        // --- Separator ---
        pnlForm.add(createSeparator(), "growx, gapy 2 4");

        // --- Tổng tiền nhập (nổi bật) ---
        JLabel lblTongTT = new JLabel("TỔNG TIỀN NHẬP:");
        lblTongTT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTongTT.setForeground(Theme.PRIMARY);
        pnlForm.add(lblTongTT);
        txtTongTienNhap = createReadOnlyField();
        txtTongTienNhap.setText("0 đ");
        txtTongTienNhap.setBackground(new Color(255, 250, 230));
        txtTongTienNhap.setForeground(Theme.ACCENT_COLOR);
        txtTongTienNhap.setFont(new Font("Segoe UI", Font.BOLD, 17));
        txtTongTienNhap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT, 2),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        pnlForm.add(txtTongTienNhap, "h " + TOTAL_FIELD_HEIGHT + "!, gapbottom 2");

        // --- Separator ---
        pnlForm.add(createSeparator(), "growx, gapy 4 4");

        // --- Khu vực thêm SP ---
        JLabel lblCTTitle = new JLabel("THÊM SẢN PHẨM VÀO PHIẾU");
        lblCTTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCTTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lblCTTitle, "gapbottom 0");

        JLabel lblCTHint = new JLabel("Chọn nhanh sản phẩm bằng danh sách hoặc nút ...");
        lblCTHint.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblCTHint.setForeground(new Color(100, 116, 139));
        pnlForm.add(lblCTHint, "gapbottom 2");

        pnlForm.add(createLabel("Sản phẩm:"));
        cboMaSP = createComboBox();
        btnChonSP = createBrowseButton();
        pnlForm.add(createSelectionRow(cboMaSP, btnChonSP), "gapbottom 4");

        JPanel pnlInfoSP = new JPanel(new MigLayout("wrap 1, fillx, insets 0, gapy 8", "[grow,fill]", ""));
        pnlInfoSP.setOpaque(false);

        spinSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        styleSpinner(spinSoLuong);
        ((JSpinner.DefaultEditor) spinSoLuong.getEditor()).getTextField().setColumns(10);

        txtDonGia = createTextField("");

        pnlInfoSP.add(createInlineFieldRow("Số lượng:", spinSoLuong), "growx");
        pnlInfoSP.add(createInlineFieldRow("Đơn giá nhập:", txtDonGia), "growx");
        pnlForm.add(pnlInfoSP, "gapbottom 4");

        // Nút thêm/xóa SP
        JPanel pnlSPButtons = new JPanel(new MigLayout("insets 0, gap 8", "[grow][grow]", "[]"));
        pnlSPButtons.setOpaque(false);
        btnThemSP = new CustomButton("+ Thêm SP", Theme.ACCENT_COLOR);
        btnThemSP.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnXoaSP = new CustomButton("- Xóa SP", Theme.DANGER_COLOR);
        btnXoaSP.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pnlSPButtons.add(btnThemSP, "grow, h " + BUTTON_HEIGHT + "!");
        pnlSPButtons.add(btnXoaSP, "grow, h " + BUTTON_HEIGHT + "!");
        pnlForm.add(pnlSPButtons, "gapbottom 6");

        // --- Separator ---
        pnlForm.add(createSeparator(), "growx, gapy 2 4");

        // --- Nút chức năng ---
        JPanel pnlButtons = createButtonPanel();
        pnlForm.add(pnlButtons, "grow, gaptop 0");

        return pnlForm;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new MigLayout("wrap 2, gap 6, insets 0", "[grow][grow]", ""));
        pnlButtons.setOpaque(false);

        btnLuu = new ButtonAdd("Lưu phiếu");
        btnSua = new ButtonFix("Sửa phiếu");
        btnXoa = new ButtonDele("Xóa phiếu");
        btnMoi = new ButtonRefresh("Làm mới");
        btnXuat = new ButtonXuatExcel("Xuất Excel");
        btnXuatPDF = new ButtonXuatPdf("Xuất PDF");

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 11);
        JButton[] buttons = {btnLuu, btnSua, btnXoa, btnMoi, btnXuat, btnXuatPDF};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        pnlButtons.add(btnLuu, "grow, h " + BUTTON_HEIGHT + "!");
        pnlButtons.add(btnSua, "grow, h " + BUTTON_HEIGHT + "!");
        pnlButtons.add(btnXoa, "grow, h " + BUTTON_HEIGHT + "!");
        pnlButtons.add(btnMoi, "grow, h " + BUTTON_HEIGHT + "!");
        pnlButtons.add(btnXuat, "span, grow, h " + BUTTON_HEIGHT + "!");
        pnlButtons.add(btnXuatPDF, "span, grow, h " + BUTTON_HEIGHT + "!");

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

        // Bảng phiếu nhập
        JPanel pnlPhieuNhap = createPhieuNhapTablePanel();
        pnlRight.add(pnlPhieuNhap, "grow");

        // Tiêu đề bảng chi tiết
        JLabel lblCT = new JLabel("  CHI TIẾT PHIẾU NHẬP");
        lblCT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCT.setForeground(new Color(30, 58, 95));
        pnlRight.add(lblCT);

        // Bảng chi tiết phiếu nhập
        JPanel pnlChiTiet = createChiTietTablePanel();
        pnlRight.add(pnlChiTiet, "grow");

        return pnlRight;
    }

    private JPanel createSearchPanel() {
        JPanel pnlSearch = new JPanel(new MigLayout("insets 5 0 5 0", "[][grow][]", "[]"));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(Theme.TEXT);
        pnlSearch.add(lblSearch);

        searchField = new SearchTextField("Nhập mã phiếu, mã NCC, mã NV...");
        searchField.setPreferredSize(new Dimension(0, 34));
        pnlSearch.add(searchField, "grow");

        btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(Theme.PRIMARY);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlSearch.add(btnTimKiem, "w 80!, h 34!");

        return pnlSearch;
    }

    // -------------------- Bảng Phiếu nhập (trên) --------------------

    private JPanel createPhieuNhapTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1, insets 0", "[fill]", "[grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        modelPhieuNhap = new DefaultTableModel(
                new String[]{"Mã phiếu", "Ngày nhập", "Mã NV", "Mã NCC", "Tổng tiền nhập"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) { return String.class; }
        };

        tablePhieuNhap = new JTable(modelPhieuNhap);
        tablePhieuNhap.setRowHeight(38);
        tablePhieuNhap.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePhieuNhap.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablePhieuNhap.setShowGrid(false);
        tablePhieuNhap.setIntercellSpacing(new Dimension(0, 0));
        tablePhieuNhap.setDefaultRenderer(Object.class, new PhieuNhapTableRenderer());

        JTableHeader header = tablePhieuNhap.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        TableColumnModel colModel = tablePhieuNhap.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(100);
        colModel.getColumn(1).setPreferredWidth(140);
        colModel.getColumn(2).setPreferredWidth(70);
        colModel.getColumn(3).setPreferredWidth(80);
        colModel.getColumn(4).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(tablePhieuNhap);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTable.add(scrollPane, "grow");
        return pnlTable;
    }

    // -------------------- Bảng Chi tiết phiếu nhập (dưới) --------------------

    private JPanel createChiTietTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1, insets 0", "[fill]", "[grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        modelChiTiet = new DefaultTableModel(
                new String[]{"Mã phiếu", "Mã SP", "Số lượng", "Đơn giá nhập", "Thành tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) { return String.class; }
        };

        tableChiTiet = new JTable(modelChiTiet);
        tableChiTiet.setRowHeight(38);
        tableChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableChiTiet.setShowGrid(false);
        tableChiTiet.setIntercellSpacing(new Dimension(0, 0));
        tableChiTiet.setDefaultRenderer(Object.class, new ChiTietTableRenderer());

        JTableHeader header = tableChiTiet.getTableHeader();
        header.setBackground(new Color(30, 58, 95));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        TableColumnModel colModel = tableChiTiet.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(100);
        colModel.getColumn(1).setPreferredWidth(80);
        colModel.getColumn(2).setPreferredWidth(70);
        colModel.getColumn(3).setPreferredWidth(120);
        colModel.getColumn(4).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTable.add(scrollPane, "grow");
        return pnlTable;
    }

    // -------------------- Custom Renderer: Bảng Phiếu nhập --------------------

    private class PhieuNhapTableRenderer extends DefaultTableCellRenderer {
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

            if (column == 0 || column == 2 || column == 3) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else if (column == 1) {
                setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            // Cột Tổng tiền nhập: nổi bật
            if (column == 4) {
                c.setForeground(Theme.ACCENT_COLOR);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }

            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return c;
        }
    }

    // -------------------- Custom Renderer: Bảng Chi tiết --------------------

    private class ChiTietTableRenderer extends DefaultTableCellRenderer {
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

            if (column == 0 || column == 1 || column == 2) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            // Cột Thành tiền: bold
            if (column == 4) {
                c.setForeground(Theme.ACCENT_COLOR);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }

            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return c;
        }
    }

    // ============================ HELPER ============================

    public boolean validatePhieuNhap() {
        if (txtNgayNhap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ngày nhập không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNgayNhap.requestFocus();
            return false;
        }
        if (cboMaNV.getSelectedItem() == null || cboMaNV.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboMaNV.requestFocus();
            return false;
        }
        if (cboMaNCC.getSelectedItem() == null || cboMaNCC.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboMaNCC.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateChiTiet() {
        if (cboMaSP.getSelectedItem() == null || cboMaSP.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboMaSP.requestFocus();
            return false;
        }
        if (txtDonGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đơn giá không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDonGia.requestFocus();
            return false;
        }
        try {
            long donGia = NhapHangActions.parseMoney(txtDonGia.getText().trim());
            if (donGia <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtDonGia.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDonGia.requestFocus();
            return false;
        }
        int soLuong = (int) spinSoLuong.getValue();
        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            spinSoLuong.requestFocus();
            return false;
        }
        return true;
    }

    public void resetForm() {
        txtMaPhieuNhap.setText("");
        txtNgayNhap.setText(dateFormat.format(new Timestamp(System.currentTimeMillis())));
        if (cboMaNV.getItemCount() > 0) cboMaNV.setSelectedIndex(-1);
        if (cboMaNCC.getItemCount() > 0) cboMaNCC.setSelectedIndex(-1);
        txtTongTienNhap.setText("0 đ");
        if (cboMaSP.getItemCount() > 0) cboMaSP.setSelectedIndex(-1);
        spinSoLuong.setValue(1);
        txtDonGia.setText("");
        searchField.setText("");
        tablePhieuNhap.clearSelection();
        modelChiTiet.setRowCount(0);
    }

    public void updateTongTienNhap() {
        long tongTien = 0;
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            Object value = modelChiTiet.getValueAt(i, 4);
            if (value != null) {
                String raw = value.toString().replaceAll("[^0-9]", "");
                if (!raw.isEmpty()) tongTien += Long.parseLong(raw);
            }
        }
        txtTongTienNhap.setText(currencyFormat.format(tongTien));
    }

    // -------------------- Factory helpers (giống BanHangPanel) --------------------

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(Theme.TEXT);
        return lbl;
    }

    private JTextField createReadOnlyField() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBackground(new Color(248, 250, 252));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    private JTextField createTextField(String text) {
        JTextField tf = new JTextField(text);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> cbo = new JComboBox<>();
        cbo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbo.setEditable(false);
        cbo.setMaximumRowCount(12);
        cbo.setMinimumSize(new Dimension(0, FIELD_HEIGHT));
        cbo.setPreferredSize(new Dimension(0, FIELD_HEIGHT));
        cbo.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        cbo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                renderer.setToolTipText(value == null ? null : value.toString());
                renderer.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return renderer;
            }
        });
        cbo.addActionListener(e -> {
            Object selectedItem = cbo.getSelectedItem();
            cbo.setToolTipText(selectedItem == null ? null : selectedItem.toString());
        });
        return cbo;
    }

    private JButton createBrowseButton() {
        JButton btn = new JButton("...");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Theme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(0, 0, 2, 0)
        ));
        return btn;
    }

    private JPanel createSelectionRow(JComboBox<String> comboBox, JButton browseButton) {
        JPanel pnlSelection = new JPanel(new MigLayout(
                "fillx, insets 0, gapx 6",
            "[grow,fill][" + BROWSE_BUTTON_WIDTH + "!]",
                "[" + FIELD_HEIGHT + "!]"
        ));
        pnlSelection.setOpaque(false);
        pnlSelection.add(comboBox, "growx, pushx, wmin 0");
        pnlSelection.add(browseButton, "growy");
        return pnlSelection;
    }

    private JPanel createInlineFieldRow(String labelText, JComponent field) {
        JPanel rowPanel = new JPanel(new MigLayout(
                "fillx, insets 0, gapx 8",
                "[" + INLINE_LABEL_WIDTH + "!][grow,fill]",
                "[" + FIELD_HEIGHT + "!]"
        ));
        rowPanel.setOpaque(false);
        rowPanel.add(createLabel(labelText), "aligny center");
        rowPanel.add(field, "grow");
        return rowPanel;
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spinner.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        editor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private JPanel createSeparator() {
        JPanel sep = new JPanel();
        sep.setPreferredSize(new Dimension(0, 1));
        sep.setBackground(Theme.BORDER);
        return sep;
    }

    // -------------------- Getters --------------------

    public JTable getTablePhieuNhap() { return tablePhieuNhap; }
    public JTable getTableChiTiet() { return tableChiTiet; }
    public DefaultTableModel getModelPhieuNhap() { return modelPhieuNhap; }
    public DefaultTableModel getModelChiTiet() { return modelChiTiet; }
    public SearchTextField getSearchField() { return searchField; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public ButtonAdd getBtnLuu() { return btnLuu; }
    public ButtonFix getBtnSua() { return btnSua; }
    public ButtonDele getBtnXoa() { return btnXoa; }
    public ButtonRefresh getBtnMoi() { return btnMoi; }
    public ButtonXuatExcel getBtnXuat() { return btnXuat; }
    public ButtonXuatPdf getBtnXuatPDF() { return btnXuatPDF; }
    public CustomButton getBtnThemSP() { return btnThemSP; }
    public CustomButton getBtnXoaSP() { return btnXoaSP; }
    public JTextField getTxtMaPhieuNhap() { return txtMaPhieuNhap; }
    public JTextField getTxtNgayNhap() { return txtNgayNhap; }
    public JTextField getTxtTongTienNhap() { return txtTongTienNhap; }
    public JTextField getTxtDonGia() { return txtDonGia; }
    public JComboBox<String> getCboMaNCC() { return cboMaNCC; }
    public JComboBox<String> getCboMaNV() { return cboMaNV; }
    public JComboBox<String> getCboMaSP() { return cboMaSP; }
    public JButton getBtnChonNCC() { return btnChonNCC; }
    public JButton getBtnChonNV() { return btnChonNV; }
    public JButton getBtnChonSP() { return btnChonSP; }
    public JSpinner getSpinSoLuong() { return spinSoLuong; }
}
