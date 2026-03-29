package Frontend.GUI.SanPhamGUI;

import Backend.BUS.SanPham_DanhMuc.*;
import Backend.DTO.SanPham_DanhMuc.*;
import Frontend.Compoent.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.Year;
import java.util.ArrayList;

public class SanPhamPanel extends JPanel {
    private SanPhamBUS spBUS = new SanPhamBUS();
    private TacGiaBUS tgBUS = new TacGiaBUS();
    private TheLoaiBUS tlBUS = new TheLoaiBUS();
    private NhaXuatBanBUS nxbBUS = new NhaXuatBanBUS();

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbMode;
    private SearchTextField searchField;
    private JButton btnSearch;
    private JButton btnAdvSearch; // nút ⋯ tìm kiếm nâng cao

    private JLabel lbTitle;
    private JPanel pnlFields;

    private JTextField txtMa, txtTen, txtMoTa, txtSoLuong;
    private JTextField txtSDT, txtDiaChi, txtEmail;

    private JComboBox<String> cbMaTL, cbMaTG, cbMaNXB, cbNamXB;
    private JButton btnAddTL, btnAddTG, btnAddNXB;

    private JLabel lbMa, lbTen, lbMoTa, lbNamXB, lbMaTL, lbMaTG, lbMaNXB, lbGia, lbGiaBan, lbSoLuong;
    private JLabel lbSDT, lbDiaChi, lbEmail;

    private JLabel lbHinhAnh;

    // --- Giá nhập + lợi nhuận + giá bán ---
    private JTextField txtGiaNhap;  // readonly, lấy từ phiếu nhập mới nhất
    private JComboBox<Integer> cbLoiNhuan;
    private JTextField txtGiaBan;   // readonly, = giaNhap * (1 + %)
    private JPanel pnlGiaNhapGroup;
    private JPanel pnlGiaBanGroup;

    private ButtonAdd btnAdd;
    private ButtonFix btnUpdate;
    private ButtonDele btnDelete;
    private ButtonRefresh btnReset;
    private ButtonNhapExcel btnNhapExcel;
    private ButtonXuatExcel btnXuatExcel;
    private JButton btnChonAnh;
    private File selectedFile;

    public SanPhamPanel() {
        initComponents();
        initEvent();
        refreshComboBoxData();
        switchMode("Sản phẩm");
        Backend.BUS.EventBus.subscribe(eventName -> {
            if (eventName.equals("SAN_PHAM_CHANGED")) {
                // ✅ refreshTable() gọi spBUS.refreshData() rồi reload bảng
                // ✅ invokeLater đảm bảo cập nhật UI trên đúng Swing EDT thread
                SwingUtilities.invokeLater(() -> refreshTable());
            }
        });
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        GridBagConstraints gMain = new GridBagConstraints();

        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        lbTitle = new JLabel("THÔNG TIN", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlLeft.add(lbTitle, BorderLayout.NORTH);

        JPanel pnlContentLeft = new JPanel();
        pnlContentLeft.setLayout(new BoxLayout(pnlContentLeft, BoxLayout.Y_AXIS));
        pnlContentLeft.setBackground(Color.WHITE);

        JPanel pnlImageSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
        pnlImageSection.setBackground(Color.WHITE);
        lbHinhAnh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lbHinhAnh.setPreferredSize(new Dimension(120, 160));
        lbHinhAnh.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));

        btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonAnh.setBackground(new Color(33, 37, 41));
        btnChonAnh.setForeground(Color.WHITE);

        JPanel pnlButtonWrapper = new JPanel(new BorderLayout());
        pnlButtonWrapper.setBackground(Color.WHITE);
        pnlButtonWrapper.add(btnChonAnh, BorderLayout.SOUTH);

        pnlImageSection.add(lbHinhAnh);
        pnlImageSection.add(pnlButtonWrapper);
        pnlContentLeft.add(pnlImageSection);

        pnlFields = new JPanel(new GridBagLayout());
        pnlFields.setBackground(Color.WHITE);
        pnlFields.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        txtMa = createStyledField(); txtMa.setEditable(false); txtMa.setBackground(new Color(240, 240, 240));
        txtTen = createStyledField();
        txtMoTa = createStyledField();
        txtSoLuong = createStyledField(); txtSoLuong.setEditable(false); txtSoLuong.setBackground(new Color(240, 240, 240));
        txtSDT = createStyledField();
        txtDiaChi = createStyledField();
        txtEmail = createStyledField();

        cbMaTL = createStyledComboBox();
        cbMaTG = createStyledComboBox();
        cbMaNXB = createStyledComboBox();
        cbNamXB = createStyledComboBox();

        btnAddTL = createQuickButton();
        btnAddTG = createQuickButton();
        btnAddNXB = createQuickButton();

        int currentYear = Year.now().getValue();
        cbNamXB.addItem("-- Chọn năm --");
        for (int i = currentYear; i >= currentYear - 100; i--) cbNamXB.addItem(String.valueOf(i));

        // ---- Giá nhập (readonly, lấy từ phiếu nhập mới nhất) ----
        pnlGiaNhapGroup = new JPanel(new BorderLayout(5, 0));
        pnlGiaNhapGroup.setOpaque(false);
        txtGiaNhap = createStyledField();
        txtGiaNhap.setEditable(false);
        txtGiaNhap.setBackground(new Color(240, 240, 240));
        txtGiaNhap.setText("0");

        txtGiaNhap.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { tinhGiaBan(); }
            @Override public void removeUpdate(DocumentEvent e) { tinhGiaBan(); }
            @Override public void changedUpdate(DocumentEvent e) { tinhGiaBan(); }
        });

        // Combo % lợi nhuận: 0 (mặc định), 10, 20, 30, 40, 50
        cbLoiNhuan = new JComboBox<>(new Integer[]{0, 10, 20, 30, 40, 50});
        cbLoiNhuan.setSelectedIndex(0);
        cbLoiNhuan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbLoiNhuan.setPreferredSize(new Dimension(90, 30));
        cbLoiNhuan.setToolTipText("Chọn % lợi nhuận để tính giá bán");
        cbLoiNhuan.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value.equals(0) ? "-- Chọn --" : value + "%");
                return this;
            }
        });
        JLabel lbLoiNhuan = new JLabel("  +Lợi Nhuận:");
        lbLoiNhuan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbLoiNhuan.setForeground(new Color(0, 120, 60));
        JPanel pnlLoiNhuan = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pnlLoiNhuan.setOpaque(false);
        pnlLoiNhuan.add(lbLoiNhuan);
        pnlLoiNhuan.add(cbLoiNhuan);
        pnlGiaNhapGroup.add(txtGiaNhap, BorderLayout.CENTER);
        pnlGiaNhapGroup.add(pnlLoiNhuan, BorderLayout.EAST);

        // ---- Giá bán (readonly, tự tính) ----
        pnlGiaBanGroup = new JPanel(new BorderLayout(5, 0));
        pnlGiaBanGroup.setOpaque(false);
        txtGiaBan = createStyledField();
        txtGiaBan.setEditable(false);
        txtGiaBan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtGiaBan.setForeground(new Color(180, 0, 0));
        txtGiaBan.setBackground(new Color(255, 245, 245));
        txtGiaBan.setText("0");
        JLabel lbDVT_Fixed = new JLabel("/ Cuốn");
        lbDVT_Fixed.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlGiaBanGroup.add(txtGiaBan, BorderLayout.CENTER);
        pnlGiaBanGroup.add(lbDVT_Fixed, BorderLayout.EAST);

        lbMa = createStyledLabel("Mã:");
        lbTen = createStyledLabel("Tên:");
        lbMoTa = createStyledLabel("Mô tả:");
        lbNamXB = createStyledLabel("Năm XB:");
        lbMaTL = createStyledLabel("Thể loại:");
        lbMaTG = createStyledLabel("Tác giả:");
        lbMaNXB = createStyledLabel("Nhà XB:");
        lbSoLuong = createStyledLabel("Số lượng:");
        lbSDT = createStyledLabel("SĐT:");
        lbDiaChi = createStyledLabel("Địa chỉ:");
        lbEmail = createStyledLabel("Email:");

        int row = 0;
        setupDualFields(row++, lbMa, txtMa, lbMaTL, createComboPanel(cbMaTL, btnAddTL));
        setupFullRow(row++, lbTen, txtTen);
        setupFullRow(row++, lbMoTa, txtMoTa);
        setupDualFields(row++, lbMaTG, createComboPanel(cbMaTG, btnAddTG), lbMaNXB, createComboPanel(cbMaNXB, btnAddNXB));
        setupDualFields(row++, lbSoLuong, txtSoLuong, lbNamXB, cbNamXB);
        lbGia = createStyledLabel("Giá nhập:");
        setupFullRow(row++, lbGia, pnlGiaNhapGroup);
        lbGiaBan = createStyledLabel("Giá bán:");
        setupFullRow(row++, lbGiaBan, pnlGiaBanGroup);
        setupFullRow(row++, lbSDT, txtSDT);
        setupFullRow(row++, lbDiaChi, txtDiaChi);
        setupFullRow(row++, lbEmail, txtEmail);

        pnlContentLeft.add(pnlFields);
        JScrollPane scrollLeft = new JScrollPane(pnlContentLeft);
        scrollLeft.setBorder(null);
        pnlLeft.add(scrollLeft, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlBtn.setOpaque(false);
        pnlBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlBtn.add(btnAdd = new ButtonAdd("Thêm"));
        pnlBtn.add(btnUpdate = new ButtonFix("Sửa"));
        pnlBtn.add(btnDelete = new ButtonDele("Xóa"));
        pnlBtn.add(btnReset = new ButtonRefresh("Làm mới"));
        pnlBtn.add(btnNhapExcel = new ButtonNhapExcel("Nhập Excel"));
        pnlBtn.add(btnXuatExcel = new ButtonXuatExcel("Xuất Excel"));
        pnlLeft.add(pnlBtn, BorderLayout.SOUTH);

        gMain.gridx = 0; gMain.weightx = 0.35; gMain.fill = GridBagConstraints.BOTH; gMain.weighty = 1.0;
        add(pnlLeft, gMain);

        JPanel pnlRight = new JPanel(new BorderLayout(0, 15));
        pnlRight.setOpaque(false);
        pnlRight.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlTool = new JPanel(new BorderLayout(10, 0));
        pnlTool.setOpaque(false);
        cbMode = new JComboBox<>(new String[]{"Sản phẩm", "Tác giả", "Thể loại", "Nhà xuất bản"});
        cbMode.setPreferredSize(new Dimension(130, 40));
        searchField = new SearchTextField();

        btnSearch = new JButton("🔍");
        btnSearch.setBackground(new Color(33, 37, 41));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(50, 40));

        btnAdvSearch = new JButton("⋯");
        btnAdvSearch.setBackground(new Color(90, 90, 90));
        btnAdvSearch.setForeground(Color.WHITE);
        btnAdvSearch.setPreferredSize(new Dimension(45, 40));
        btnAdvSearch.setToolTipText("Tìm kiếm nâng cao");
        btnAdvSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel pnlSearchButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pnlSearchButtons.setOpaque(false);
        pnlSearchButtons.add(btnAdvSearch);
        pnlSearchButtons.add(btnSearch);

        pnlTool.add(cbMode, BorderLayout.WEST);
        pnlTool.add(searchField, BorderLayout.CENTER);
        pnlTool.add(pnlSearchButtons, BorderLayout.EAST);
        pnlRight.add(pnlTool, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(33, 37, 41));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        pnlRight.add(new JScrollPane(table), BorderLayout.CENTER);
        gMain.gridx = 1; gMain.weightx = 0.65;
        add(pnlRight, gMain);
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text); lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return lbl;
    }

    private JTextField createStyledField() {
        JTextField tf = new JTextField(); tf.setBackground(new Color(248, 249, 250));
        tf.setPreferredSize(new Dimension(0, 30)); tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)), BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        return tf;
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> cb = new JComboBox<>(); cb.setBackground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13)); cb.setPreferredSize(new Dimension(70, 30)); return cb;
    }

    private JButton createQuickButton() {
        JButton btn = new JButton("+");
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(33, 37, 41));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createComboPanel(JComboBox cb, JButton btn) {
        JPanel pnl = new JPanel(new BorderLayout(2, 0));
        pnl.setOpaque(false);
        pnl.add(cb, BorderLayout.CENTER);
        pnl.add(btn, BorderLayout.EAST);
        return pnl;
    }

    private void setupFullRow(int row, JComponent label, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(4, 0, 4, 5); gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.15; pnlFields.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 0.85; gbc.gridwidth = 3; pnlFields.add(field, gbc);
    }

    private void setupDualFields(int row, JComponent label1, JComponent field1, JComponent label2, JComponent field2) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 5);
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.01; pnlFields.add(label1, gbc);
        gbc.gridx = 1; gbc.weightx = 0.3; pnlFields.add(field1, gbc);
        gbc.gridx = 2; gbc.weightx = 0.01; gbc.insets = new Insets(4, 10, 4, 5); pnlFields.add(label2, gbc);
        gbc.gridx = 3; gbc.weightx = 0.3; gbc.insets = new Insets(4, 0, 4, 0); pnlFields.add(field2, gbc);
    }

    private void refreshComboBoxData() {
        cbMaTL.removeAllItems(); cbMaTL.addItem("-- Chọn --");
        for (TheLoaiDTO tl : tlBUS.getAll()) cbMaTL.addItem(tl.getMaTL());

        cbMaTG.removeAllItems(); cbMaTG.addItem("-- Chọn --");
        for (TacGiaDTO tg : tgBUS.getAll()) cbMaTG.addItem(tg.getMaTG());

        cbMaNXB.removeAllItems(); cbMaNXB.addItem("-- Chọn --");
        for (NhaXuatBanDTO nxb : nxbBUS.getAll()) cbMaNXB.addItem(nxb.getMaNXB());
    }

    private String getSelectedId(JComboBox<String> cb) {
        if (cb.getSelectedIndex() <= 0) return "";
        return (String) cb.getSelectedItem();
    }

    private void setAllFieldsVisible(boolean visible) {
        lbMa.setVisible(visible); txtMa.setVisible(visible);
        lbTen.setVisible(visible); txtTen.setVisible(visible);
        lbMoTa.setVisible(visible); txtMoTa.setVisible(visible);
        lbNamXB.setVisible(visible); cbNamXB.setVisible(visible);
        lbMaTL.setVisible(visible); cbMaTL.getParent().setVisible(visible);
        lbMaTG.setVisible(visible); cbMaTG.getParent().setVisible(visible);
        lbMaNXB.setVisible(visible); cbMaNXB.getParent().setVisible(visible);
        lbGia.setVisible(visible); pnlGiaNhapGroup.setVisible(visible);
        lbGiaBan.setVisible(visible); pnlGiaBanGroup.setVisible(visible);
        lbSoLuong.setVisible(visible); txtSoLuong.setVisible(visible);
        lbSDT.setVisible(visible); txtSDT.setVisible(visible);
        lbDiaChi.setVisible(visible); txtDiaChi.setVisible(visible);
        lbEmail.setVisible(visible); txtEmail.setVisible(visible);
    }

    private void initEvent() {
        cbMode.addActionListener(e -> switchMode(cbMode.getSelectedItem().toString()));
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) fillForm(row);
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { timKiem(); }
            @Override public void removeUpdate(DocumentEvent e) { timKiem(); }
            @Override public void changedUpdate(DocumentEvent e) { timKiem(); }
        });

        btnSearch.addActionListener(e -> timKiem());

        btnAdvSearch.addActionListener(e -> {
            if (!cbMode.getSelectedItem().toString().equals("Sản phẩm")) return;
            moDialogTimKiemNangCao();
        });

        btnChonAnh.addActionListener(e -> chonHinhAnh());
        btnReset.addActionListener(e -> resetForm());

        btnXuatExcel.addActionListener(e -> {
            XuatExcel.xuat(table, "Danh_Sach_San_Pham");
        });

        btnNhapExcel.addActionListener(e -> nhapDuLieuExcel());

        // Khi chọn % lợi nhuận → tự tính lại giá bán
        cbLoiNhuan.addActionListener(e -> tinhGiaBan());

        btnAdd.addActionListener(e -> themDuLieu());
        btnUpdate.addActionListener(e -> suaDuLieu());
        btnDelete.addActionListener(e -> xoaDuLieu());

        btnAddTL.addActionListener(e -> moDialogChonNhanh("Thể loại"));
        btnAddTG.addActionListener(e -> moDialogChonNhanh("Tác giả"));
        btnAddNXB.addActionListener(e -> moDialogChonNhanh("Nhà xuất bản"));
    }

    private void moDialogChonNhanh(String type) {
        QuickSelectDialog dialog = null;
        String[] cols = {"Mã", "Tên"};
        Object[][] data = null;

        switch (type) {
            case "Tác giả":
                data = tgBUS.getAll().stream().map(t -> new Object[]{t.getMaTG(), t.getHoTG() + " " + t.getTenTG()}).toArray(Object[][]::new);
                dialog = new QuickSelectDialog("Tác giả", "Mã TG:", "Tên:", data, cols);
                break;
            case "Thể loại":
                data = tlBUS.getAll().stream().map(t -> new Object[]{t.getMaTL(), t.getTenTL()}).toArray(Object[][]::new);
                dialog = new QuickSelectDialog("Thể loại", "Mã TL:", "Tên:", data, cols);
                break;
            case "Nhà xuất bản":
                data = nxbBUS.getAll().stream().map(t -> new Object[]{t.getMaNXB(), t.getTenNXB()}).toArray(Object[][]::new);
                dialog = new QuickSelectDialog("Nhà xuất bản", "Mã NXB:", "Tên:", data, cols);
                break;
        }

        if (dialog != null) {
            dialog.setVisible(true);
            String id = dialog.getSelectedId();
            if (!id.isEmpty()) {
                if (type.equals("Tác giả")) cbMaTG.setSelectedItem(id);
                else if (type.equals("Thể loại")) cbMaTL.setSelectedItem(id);
                else if (type.equals("Nhà xuất bản")) cbMaNXB.setSelectedItem(id);
            }
        }
    }

    private void switchMode(String mode) {
        lbTitle.setText("THÔNG TIN " + mode.toUpperCase());
        setAllFieldsVisible(false);
        lbHinhAnh.getParent().setVisible(mode.equals("Sản phẩm"));

        switch (mode) {
            case "Sản phẩm":
                // Đổi tên cột từ "Đơn Giá" thành "Giá Bán"
                model.setColumnIdentifiers(new String[]{"Mã SP", "Tên Sản Phẩm", "Tác giả", "Thể loại", "Giá Bán", "Số lượng"});
                
                lbMa.setText("Mã SP:"); lbMa.setVisible(true); txtMa.setVisible(true);
                lbTen.setText("Tên SP:"); lbTen.setVisible(true); txtTen.setVisible(true);
                lbMoTa.setText("Mô tả:"); lbMoTa.setVisible(true); txtMoTa.setVisible(true);
                lbMaTL.setVisible(true); cbMaTL.getParent().setVisible(true);
                lbMaTG.setVisible(true); cbMaTG.getParent().setVisible(true);
                lbMaNXB.setVisible(true); cbMaNXB.getParent().setVisible(true);
                lbNamXB.setVisible(true); cbNamXB.setVisible(true);
                lbGia.setVisible(true); pnlGiaNhapGroup.setVisible(true);
                lbGiaBan.setVisible(true); pnlGiaBanGroup.setVisible(true);
                lbSoLuong.setVisible(true); txtSoLuong.setVisible(true);
                break;
            case "Tác giả":
                model.setColumnIdentifiers(new String[]{"Mã TG", "Họ Tác Giả", "Tên Tác Giả"});
                lbMa.setText("Mã TG:"); lbMa.setVisible(true); txtMa.setVisible(true);
                lbTen.setText("Họ TG:"); lbTen.setVisible(true); txtTen.setVisible(true);
                lbMoTa.setText("Tên TG:"); lbMoTa.setVisible(true); txtMoTa.setVisible(true);
                break;
            case "Thể loại":
                model.setColumnIdentifiers(new String[]{"Mã TL", "Tên Thể Loại"});
                lbMa.setText("Mã TL:"); lbMa.setVisible(true); txtMa.setVisible(true);
                lbTen.setText("Tên TL:"); lbTen.setVisible(true); txtTen.setVisible(true);
                break;
            case "Nhà xuất bản":
                model.setColumnIdentifiers(new String[]{"Mã NXB", "Tên NXB", "SĐT", "Địa chỉ", "Email"});
                lbMa.setText("Mã NXB:"); lbMa.setVisible(true); txtMa.setVisible(true);
                lbTen.setText("Tên NXB:"); lbTen.setVisible(true); txtTen.setVisible(true);
                lbSDT.setVisible(true); txtSDT.setVisible(true);
                lbDiaChi.setVisible(true); txtDiaChi.setVisible(true);
                lbEmail.setVisible(true); txtEmail.setVisible(true);
                break;
        }
        resetForm(); timKiem(); setTableColumnWidths(); refreshTable();
    }

    private void resetForm() {
        txtMa.setText(""); txtTen.setText(""); txtMoTa.setText("");
        txtSoLuong.setText("0"); txtGiaNhap.setText("0"); txtGiaBan.setText("0");
        txtSDT.setText(""); txtDiaChi.setText(""); txtEmail.setText("");
        if (cbNamXB.getItemCount() > 0) cbNamXB.setSelectedIndex(0);
        if (cbMaTL.getItemCount() > 0) cbMaTL.setSelectedIndex(0);
        if (cbMaTG.getItemCount() > 0) cbMaTG.setSelectedIndex(0);
        if (cbMaNXB.getItemCount() > 0) cbMaNXB.setSelectedIndex(0);
        cbLoiNhuan.setSelectedIndex(0);
        lbHinhAnh.setIcon(null); lbHinhAnh.setText("Chưa có ảnh");
        selectedFile = null; table.clearSelection();
        String mode = cbMode.getSelectedItem().toString();
        switch (mode) {
            case "Sản phẩm": txtMa.setText(spBUS.generateNewMaSP()); break;
            case "Tác giả": txtMa.setText(tgBUS.generateNewMaTG()); break;
            case "Thể loại": txtMa.setText(tlBUS.generateNewMaTL()); break;
            case "Nhà xuất bản": txtMa.setText(nxbBUS.generateNewMaNXB()); break;
        }
    }

    private void fillForm(int row) {
        String mode = cbMode.getSelectedItem().toString();
        String id = model.getValueAt(row, 0).toString();
        switch (mode) {
            case "Sản phẩm":
                SanPhamDTO sp = spBUS.getById(id);
                if (sp != null) {
                    txtMa.setText(sp.getMaSP()); 
                    txtTen.setText(sp.getTenSP()); 
                    txtMoTa.setText(sp.getMoTa());
                    cbNamXB.setSelectedItem(String.valueOf(sp.getNamXuatBan()));
                    cbMaTL.setSelectedItem(sp.getMaTL()); 
                    cbMaTG.setSelectedItem(sp.getMaTG()); 
                    cbMaNXB.setSelectedItem(sp.getMaNXB());
                    txtSoLuong.setText(String.valueOf(sp.getSoLuongTon()));
                    displayImage(sp.getHinhAnh()); 
                    selectedFile = null;
                    int phanTram = sp.getPhanTram();
                    cbLoiNhuan.setSelectedItem(phanTram); 
                    capNhatGiaNhap(sp.getMaSP(), sp.getDonGia());
                }
                break;
            case "Tác giả":
                TacGiaDTO tg = tgBUS.getById(id);
                txtMa.setText(tg.getMaTG()); txtTen.setText(tg.getHoTG()); txtMoTa.setText(tg.getTenTG());
                break;
            case "Thể loại":
                TheLoaiDTO tl = tlBUS.getById(id);
                txtMa.setText(tl.getMaTL()); txtTen.setText(tl.getTenTL());
                break;
            case "Nhà xuất bản":
                NhaXuatBanDTO nxb = nxbBUS.getById(id);
                txtMa.setText(nxb.getMaNXB()); txtTen.setText(nxb.getTenNXB());
                txtSDT.setText(nxb.getSoDienThoai()); txtDiaChi.setText(nxb.getDiaChi()); txtEmail.setText(nxb.getEmail());
                break;
        }
    }

    private void timKiem() {
        String text = searchField.getText().trim();
        String mode = cbMode.getSelectedItem().toString();
        model.setRowCount(0);
        switch (mode) {
            case "Sản phẩm":
                for (SanPhamDTO sp : spBUS.search(text)) 
                    model.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), tgBUS.getTenById(sp.getMaTG()), tlBUS.getTenById(sp.getMaTL()), sp.getDonGia(), sp.getSoLuongTon()});
                break;
            case "Tác giả":
                for (TacGiaDTO tg : tgBUS.search(text)) model.addRow(new Object[]{tg.getMaTG(), tg.getHoTG(), tg.getTenTG()});
                break;
            case "Thể loại":
                for (TheLoaiDTO tl : tlBUS.search(text)) model.addRow(new Object[]{tl.getMaTL(), tl.getTenTL()});
                break;
            case "Nhà xuất bản":
                for (NhaXuatBanDTO nxb : nxbBUS.search(text)) model.addRow(new Object[]{nxb.getMaNXB(), nxb.getTenNXB(), nxb.getSoDienThoai(), nxb.getDiaChi(), nxb.getEmail()});
                break;
        }
    }

    private void nhapDuLieuExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx", "xls"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            String result = spBUS.nhapExcel(file.getAbsolutePath());
            
            if (result != null && result.startsWith("SUCCESS")) {
                String[] parts = result.split(":");
                int success = Integer.parseInt(parts[1]);
                int skip = Integer.parseInt(parts[2]);
                
                JOptionPane.showMessageDialog(this, 
                    "Nhập thành công: " + success + " dòng.\nBỏ qua: " + skip + " dòng (lỗi/trống).", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    
                refreshTable(); 
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi nhập Excel: " + result, 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void themDuLieu() {
        String mode = cbMode.getSelectedItem().toString();
        String result = "";
        switch (mode) {
            case "Sản phẩm":
                SanPhamDTO sp = new SanPhamDTO();
                sp.setTenSP(txtTen.getText());
                sp.setMoTa(txtMoTa.getText());
                sp.setMaTL(getSelectedId(cbMaTL));
                sp.setMaTG(getSelectedId(cbMaTG));
                sp.setMaNXB(getSelectedId(cbMaNXB));
                // Lợi nhuận
                int phanTramThem = (Integer) cbLoiNhuan.getSelectedItem();
                sp.setPhanTram(phanTramThem);
                // Năm xuất bản
                String namStrThem = (String) cbNamXB.getSelectedItem();
                int namThem = 0;
                if (namStrThem != null && !namStrThem.contains("--")) {
                    try { namThem = Integer.parseInt(namStrThem); } catch (NumberFormatException e) { namThem = 0; }
                }
                sp.setNamXuatBan(namThem);
                result = spBUS.validateAndAdd(sp, selectedFile);
                break;
            case "Tác giả":
                result = tgBUS.validateAndAdd(new TacGiaDTO("", txtTen.getText(), txtMoTa.getText()));
                break;
            case "Thể loại":
                result = tlBUS.validateAndAdd(new TheLoaiDTO("", txtTen.getText()));
                break;
            case "Nhà xuất bản":
                result = nxbBUS.validateAndAdd(new NhaXuatBanDTO("", txtTen.getText(), txtSDT.getText(), txtDiaChi.getText(), txtEmail.getText()));
                break;
        }
        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            refreshComboBoxData();
            resetForm();
            timKiem();
        } else {
            JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaDuLieu() {
        String mode = cbMode.getSelectedItem().toString();
        String result = "";
        switch (mode) {
            case "Sản phẩm":
                SanPhamDTO sp = spBUS.getById(txtMa.getText());
                if (sp != null) {
                    sp.setTenSP(txtTen.getText());
                    sp.setMoTa(txtMoTa.getText());
                    sp.setMaTL(getSelectedId(cbMaTL));
                    sp.setMaTG(getSelectedId(cbMaTG));
                    sp.setMaNXB(getSelectedId(cbMaNXB));
                    int phanTramSua = (Integer) cbLoiNhuan.getSelectedItem();
                    sp.setPhanTram(phanTramSua);
                    // Năm xuất bản
                    String namStrSua = (String) cbNamXB.getSelectedItem();
                    int namSua = 0;
                    if (namStrSua != null && !namStrSua.contains("--")) {
                        try { namSua = Integer.parseInt(namStrSua); } catch (NumberFormatException e) { namSua = 0; }
                    }
                    sp.setNamXuatBan(namSua);
                    result = spBUS.validateAndUpdate(sp, selectedFile);
                } else {
                    result = "Không tìm thấy sản phẩm để cập nhật!";
                }
                break;
            case "Tác giả":
                result = tgBUS.validateAndUpdate(new TacGiaDTO(txtMa.getText(), txtTen.getText(), txtMoTa.getText()));
                break;
            case "Thể loại":
                result = tlBUS.validateAndUpdate(new TheLoaiDTO(txtMa.getText(), txtTen.getText()));
                break;
            case "Nhà xuất bản":
                result = nxbBUS.validateAndUpdate(new NhaXuatBanDTO(txtMa.getText(), txtTen.getText(), txtSDT.getText(), txtDiaChi.getText(), txtEmail.getText()));
                break;
        }
        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            refreshComboBoxData();
            timKiem(); resetForm();
        } else {
            JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaDuLieu() {
        String mode = cbMode.getSelectedItem().toString();
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();

        if (ma.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa " + mode.toLowerCase() + ": '" + ten + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = false;
            switch (mode) {
                case "Sản phẩm": success = spBUS.deleteSanPham(ma); break;
                case "Tác giả": success = tgBUS.deleteTacGia(ma); break;
                case "Thể loại": success = tlBUS.deleteTheLoai(ma); break;
                case "Nhà xuất bản": success = nxbBUS.deleteNXB(ma); break;
            }
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                refreshTable();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    public void refreshTable() {
        String mode = cbMode.getSelectedItem().toString();
        switch (mode) {
            case "Sản phẩm": spBUS.refreshData(); break;
            case "Tác giả": tgBUS.refreshData(); break;
            case "Thể loại": tlBUS.refreshData(); break;
            case "Nhà xuất bản": nxbBUS.refreshData(); break;
        }
        timKiem();
    }

    /**
     * Lấy giá nhập mới nhất của sản phẩm từ CTPhieuNhapHang, hiển thị vào txtGiaNhap,
     * sau đó tính ngay giá bán theo % lợi nhuận đang chọn.
     * @param maSP  mã sản phẩm cần tra
     * @param donGiaCu  giá bán hiện tại trong DB (fallback nếu chưa có phiếu nhập)
     */
    private void capNhatGiaNhap(String maSP, long donGiaCu) {
        long giaNhap = spBUS.getGiaNhapMoiNhat(maSP); 
        if (giaNhap > 0) {
            txtGiaNhap.setText(String.valueOf(giaNhap));
            txtGiaNhap.setForeground(new Color(0, 100, 0));
        } else {
            txtGiaNhap.setText(donGiaCu > 0 ? String.valueOf(donGiaCu) : "0");
            txtGiaNhap.setForeground(Color.GRAY);
        }
        tinhGiaBan();
    }

    private void tinhGiaBan() {
        try {
            long giaNhap = Long.parseLong(txtGiaNhap.getText().trim());
            int phanTram = (Integer) cbLoiNhuan.getSelectedItem();
            
            // Gọi BUS tính toán
            long giaBan = spBUS.tinhGiaBan(giaNhap, phanTram);
            
            txtGiaBan.setText(String.valueOf(giaBan));
            txtGiaBan.setToolTipText("Giá bán = Giá nhập × (1 + " + phanTram + "%)");
        } catch (NumberFormatException ex) {
            txtGiaBan.setText("0");
        }
    }
    private void chonHinhAnh() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
            lbHinhAnh.setIcon(new ImageIcon(img)); lbHinhAnh.setText("");
        }
    }

    private void displayImage(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            lbHinhAnh.setIcon(null); lbHinhAnh.setText("Chưa có ảnh"); return;
        }
        File file = new File("src/main/resources/images/product/" + imageName);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
            lbHinhAnh.setIcon(new ImageIcon(img)); lbHinhAnh.setText("");
        }
    }

    private void setTableColumnWidths() {
        if (table.getColumnCount() == 0) return;
        TableColumnModel columnModel = table.getColumnModel();
        if (cbMode.getSelectedItem().toString().equals("Sản phẩm")) {
            columnModel.getColumn(0).setPreferredWidth(60);
            columnModel.getColumn(1).setPreferredWidth(200);
        }
    }

    private void moDialogTimKiemNangCao() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tìm kiếm nâng cao", true);
        dialog.setSize(420, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 5, 7, 5);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontComp  = new Font("Segoe UI", Font.PLAIN, 13);

        // --- Tác giả combobox ---
        JLabel lbTG = new JLabel("Tác giả:"); lbTG.setFont(fontLabel);
        JComboBox<String> cbTG = new JComboBox<>(); cbTG.setFont(fontComp);
        cbTG.addItem("-- Tất cả --");
        for (TacGiaDTO tg : tgBUS.getAll())
            cbTG.addItem(tg.getMaTG() + " - " + tg.getHoTG() + " " + tg.getTenTG());

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; pnlContent.add(lbTG, gbc);
        gbc.gridx = 1; gbc.weightx = 1; pnlContent.add(cbTG, gbc);

        // --- Thể loại combobox ---
        JLabel lbTL = new JLabel("Thể loại:"); lbTL.setFont(fontLabel);
        JComboBox<String> cbTL = new JComboBox<>(); cbTL.setFont(fontComp);
        cbTL.addItem("-- Tất cả --");
        for (TheLoaiDTO tl : tlBUS.getAll())
            cbTL.addItem(tl.getMaTL() + " - " + tl.getTenTL());

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; pnlContent.add(lbTL, gbc);
        gbc.gridx = 1; gbc.weightx = 1; pnlContent.add(cbTL, gbc);

        // --- Số lượng spinner + checkbox ---
        JLabel lbSL = new JLabel("Số lượng ≤:"); lbSL.setFont(fontLabel);
        JSpinner spnSL = new JSpinner(new SpinnerNumberModel(100, 0, 99999, 1));
        spnSL.setFont(fontComp); spnSL.setEnabled(false);
        JCheckBox chkSL = new JCheckBox("Bật lọc"); chkSL.setFont(fontComp);
        chkSL.addActionListener(e -> spnSL.setEnabled(chkSL.isSelected()));

        JPanel pnlSL = new JPanel(new BorderLayout(5, 0)); pnlSL.setOpaque(false);
        pnlSL.add(spnSL, BorderLayout.CENTER); pnlSL.add(chkSL, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; pnlContent.add(lbSL, gbc);
        gbc.gridx = 1; gbc.weightx = 1; pnlContent.add(pnlSL, gbc);

        // --- Giá spinner + checkbox ---
        JLabel lbGiaAdv = new JLabel("Giá ≤:"); lbGiaAdv.setFont(fontLabel);
        JSpinner spnGia = new JSpinner(new SpinnerNumberModel(200000, 0, 99999999, 1000));
        spnGia.setFont(fontComp); spnGia.setEnabled(false);
        JCheckBox chkGia = new JCheckBox("Bật lọc"); chkGia.setFont(fontComp);
        chkGia.addActionListener(e -> spnGia.setEnabled(chkGia.isSelected()));

        JPanel pnlGiaAdv = new JPanel(new BorderLayout(5, 0)); pnlGiaAdv.setOpaque(false);
        pnlGiaAdv.add(spnGia, BorderLayout.CENTER); pnlGiaAdv.add(chkGia, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; pnlContent.add(lbGiaAdv, gbc);
        gbc.gridx = 1; gbc.weightx = 1; pnlContent.add(pnlGiaAdv, gbc);

        dialog.add(pnlContent, BorderLayout.CENTER);

        // --- Nút ---
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnHuy = new JButton("Hủy"); btnHuy.setFont(fontComp);
        JButton btnTim = new JButton("🔍 Tìm kiếm");
        btnTim.setBackground(new Color(33, 37, 41)); btnTim.setForeground(Color.WHITE);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlBot.add(btnHuy); pnlBot.add(btnTim);
        dialog.add(pnlBot, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dialog.dispose());
        btnTim.addActionListener(e -> {
            String maTG = null, maTL = null;
            if (cbTG.getSelectedIndex() > 0)
                maTG = cbTG.getSelectedItem().toString().split(" - ")[0].trim();
            if (cbTL.getSelectedIndex() > 0)
                maTL = cbTL.getSelectedItem().toString().split(" - ")[0].trim();

            int targetSL   = chkSL.isSelected()  ? (int) spnSL.getValue() : -1;
            long targetGia = chkGia.isSelected() ? ((Number) spnGia.getValue()).longValue() : -1L;

            ArrayList<SanPhamDTO> ketQua = spBUS.searchAdvanced(maTG, maTL, targetSL, targetGia);
            model.setRowCount(0);
            for (SanPhamDTO sp : ketQua) {
                model.addRow(new Object[]{
                    sp.getMaSP(), 
                    sp.getTenSP(),
                    tgBUS.getTenById(sp.getMaTG()),
                    tlBUS.getTenById(sp.getMaTL()),
                    sp.getDonGia(), // Lấy Giá Bán từ DB
                    sp.getSoLuongTon()
                });
            }

            dialog.dispose();
        });
        dialog.setVisible(true);
    }
}

class QuickSelectDialog extends JDialog {
    private JTextField txtMa, txtTen, txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnOK, btnClose;
    private String selectedId = "";

    public QuickSelectDialog(String title, String labelMa, String labelTen, Object[][] data, String[] columns) {
        setTitle(title);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 5, 5);

        txtMa = new JTextField(); txtMa.setEditable(false);
        txtTen = new JTextField(); txtTen.setEditable(false);
        txtSearch = new JTextField();
        txtSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        gbc.gridx = 0; gbc.gridy = 0; pnlTop.add(new JLabel(labelMa), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; pnlTop.add(txtMa, gbc);
        gbc.gridx = 2; gbc.weightx = 0; pnlTop.add(new JLabel(labelTen), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; pnlTop.add(txtTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4; pnlTop.add(txtSearch, gbc);
        add(pnlTop, BorderLayout.NORTH);

        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : data) model.addRow(row);
        table = new JTable(model);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnOK = new JButton("Xác nhận");
        btnClose = new JButton("Thoát");
        btnOK.setBackground(new Color(33, 37, 41)); btnOK.setForeground(Color.WHITE);
        pnlBot.add(btnOK); pnlBot.add(btnClose);
        add(pnlBot, BorderLayout.SOUTH);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtMa.setText(table.getValueAt(row, 0).toString());
                    txtTen.setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                table.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        btnOK.addActionListener(e -> { selectedId = txtMa.getText(); dispose(); });
        btnClose.addActionListener(e -> { selectedId = ""; dispose(); });
    }

    public String getSelectedId() { return selectedId; }
}