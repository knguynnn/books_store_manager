package Frontend.GUI.NhanVienGUI;

import Backend.BUS.NhanVien_TaiKhoan.NhanVienBUS;
import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;
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
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class NhanVienPanel extends JPanel {
    private NhanVienBUS bus = new NhanVienBUS();
    
    // --- Components ---
    private JTextField txtMa, txtHo, txtTen, txtSdt, txtEmail, txtDiaChi, txtCccd, txtNgSinh, txtNgayVaoLam, txtLuong;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    private JCheckBox chkTrangThai;
    private JTable table;
    private DefaultTableModel model;
    
    // --- Buttons ---
    private ButtonAdd btnAdd;
    private ButtonFix btnUpdate;
    private ButtonDele btnDelete;
    private ButtonRefresh btnReset;
    private ButtonNhapExcel btnNhapExcel;
    private ButtonXuatExcel btnXuatExcel;
    
    private SearchTextField searchField;
    private boolean isEditing = false;
    private DecimalFormat moneyFormat = new DecimalFormat("###,###");

    public NhanVienPanel() {
        initComponent();
        initEvent();
        loadData();
        generateNewMaNV(); // Tự động sinh mã khi mở
    }

    // --- 1. SETUP GIAO DIỆN (UI) ---
    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        
        // SỬA: Tăng độ rộng cột Form lên 450px để tận dụng màn hình lớn
        // Cột Table (grow) sẽ chiếm phần còn lại
        this.setLayout(new MigLayout("fill, insets 10", "[450!][grow]", "[fill]"));

        JPanel pnlForm = createFormPanel();
        JPanel pnlTable = createTablePanel();
        
        this.add(pnlForm, "growy"); // Form giãn chiều dọc
        this.add(pnlTable, "grow"); // Table giãn toàn bộ
    }

    private JPanel createFormPanel() {
        // Form layout: Wrap 2 cột, nhưng cột nhãn (Label) cố định 120px
        JPanel pnlForm = new JPanel(new MigLayout("wrap 2, fill, insets 10", "[120!][grow]", "[]10[]"));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lbTitle = new JLabel("THÔNG TIN NHÂN VIÊN");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lbTitle, "span 2, align center, gapbottom 20");

        // --- ROW 1: Mã NV ---
        txtMa = createTextField("Mã nhân viên (Tự động)");
        pnlForm.add(createLabel("Mã nhân viên:"));
        pnlForm.add(txtMa, "grow, h 35!");

        // --- ROW 2: Họ và Tên (Chia tỷ lệ 70% - 30%) ---
        pnlForm.add(createLabel("Họ tên:"));
        
        // Panel con chứa Họ và Tên
        JPanel pnlHoTen = new JPanel(new MigLayout("insets 0", "[70%][30%]", "[]"));
        pnlHoTen.setOpaque(false);
        
        txtHo = createTextField("Họ và tên lót");
        txtTen = createTextField("Tên");
        
        pnlHoTen.add(txtHo, "grow, h 35!");
        pnlHoTen.add(txtTen, "grow, h 35!");
        
        pnlForm.add(pnlHoTen, "grow");

        // --- ROW 3: Ngày sinh & Giới tính ---
        pnlForm.add(createLabel("Ngày sinh/GT:"));
        
        JPanel pnlNgaySinh = new JPanel(new MigLayout("insets 0", "[grow][100!]", "[]"));
        pnlNgaySinh.setOpaque(false);
        
        txtNgSinh = createTextField("yyyy-mm-dd (VD: 1990-01-01)");
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboGioiTinh.setBackground(Color.WHITE);
        
        pnlNgaySinh.add(txtNgSinh, "grow, h 35!");
        pnlNgaySinh.add(cboGioiTinh, "grow, h 35!");
        
        pnlForm.add(pnlNgaySinh, "grow");

        // --- ROW 4: SĐT ---
        txtSdt = createTextField("Nhập 10 chữ số...");
        pnlForm.add(createLabel("Số điện thoại:"));
        pnlForm.add(txtSdt, "grow, h 35!");

        // --- ROW 5: CCCD ---
        txtCccd = createTextField("Nhập 12 chữ số...");
        pnlForm.add(createLabel("CCCD:"));
        pnlForm.add(txtCccd, "grow, h 35!");

        // --- ROW 6: Email ---
        txtEmail = createTextField("example@gmail.com");
        pnlForm.add(createLabel("Email:"));
        pnlForm.add(txtEmail, "grow, h 35!");

        // --- ROW 7: Địa chỉ ---
        txtDiaChi = createTextField("Số nhà, đường, quận/huyện...");
        pnlForm.add(createLabel("Địa chỉ:"));
        pnlForm.add(txtDiaChi, "grow, h 35!");

        // --- ROW 8: Chức vụ ---
        cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên bán hàng", "Thủ kho", "Bảo vệ"});
        cboChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboChucVu.setBackground(Color.WHITE);
        pnlForm.add(createLabel("Chức vụ:"));
        pnlForm.add(cboChucVu, "grow, h 35!");
        
        // --- ROW 9: Ngày vào làm ---
        txtNgayVaoLam = createTextField("yyyy-mm-dd");
        pnlForm.add(createLabel("Ngày vào làm:"));
        pnlForm.add(txtNgayVaoLam, "grow, h 35!");

        // --- ROW 10: Lương ---
        txtLuong = createTextField("Nhập số tiền...");
        pnlForm.add(createLabel("Lương cơ bản:"));
        pnlForm.add(txtLuong, "grow, h 35!");

        // --- ROW 11: Trạng thái ---
        chkTrangThai = new JCheckBox("Đang làm việc");
        chkTrangThai.setSelected(true);
        chkTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkTrangThai.setOpaque(false);
        pnlForm.add(createLabel("Trạng thái:"));
        pnlForm.add(chkTrangThai, "h 35!");

        // --- BUTTONS ---
        JPanel pnlButtons = createButtonPanel();
        pnlForm.add(pnlButtons, "span 2, grow, pushy, aligny bottom"); // Đẩy xuống đáy

        return pnlForm;
    }

    private JPanel createButtonPanel() {
        // Grid 2 dòng, 3 cột
        JPanel pnlButtons = new JPanel(new MigLayout("wrap 3, fill, insets 0, gap 5", "[grow][grow][grow]", "[]"));
        pnlButtons.setOpaque(false);
        
        btnAdd = new ButtonAdd("Thêm");
        btnUpdate = new ButtonFix("Sửa");
        btnDelete = new ButtonDele("Xóa");
        btnReset = new ButtonRefresh("Mới");
        btnNhapExcel = new ButtonNhapExcel("Nhập");
        btnXuatExcel = new ButtonXuatExcel("Xuất");
        
        Component[] comps = {btnAdd, btnUpdate, btnDelete, btnReset, btnNhapExcel, btnXuatExcel};
        for(Component c : comps) {
            if(c instanceof JButton) {
                ((JButton)c).setFont(new Font("Segoe UI", Font.BOLD, 13));
                ((JButton)c).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        pnlButtons.add(btnAdd, "grow");
        pnlButtons.add(btnUpdate, "grow");
        pnlButtons.add(btnDelete, "grow");
        pnlButtons.add(btnReset, "grow");
        pnlButtons.add(btnNhapExcel, "grow");
        pnlButtons.add(btnXuatExcel, "grow");
        
        return pnlButtons;
    }
    
    private JPanel createTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1", "[fill]", "[][grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        // Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new MigLayout("insets 0", "[][grow][]", "[]"));
        pnlSearch.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlSearch.add(lblSearch);
        
        searchField = new SearchTextField();
        searchField.setPlaceholder("Nhập mã, tên hoặc SĐT...");
        searchField.setPreferredSize(new Dimension(0, 35));
        pnlSearch.add(searchField, "grow");
        
        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(Theme.PRIMARY);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlSearch.add(btnTimKiem, "w 80!, h 35!");
        
        pnlTable.add(pnlSearch, "gaptop 0");

        // Bảng dữ liệu
        model = new DefaultTableModel(new String[]{"Mã NV", "Họ tên", "Giới tính", "SĐT", "Chức vụ", "Lương", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Theme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setDefaultRenderer(Object.class, new EmployeeTableRenderer());
        
        // Chỉnh độ rộng cột
        TableColumnModel col = table.getColumnModel();
        col.getColumn(0).setPreferredWidth(80);  // Mã
        col.getColumn(1).setPreferredWidth(180); // Tên
        col.getColumn(2).setPreferredWidth(60);  // GT
        col.getColumn(3).setPreferredWidth(100); // SĐT
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlTable.add(scrollPane, "grow");

        return pnlTable;
    }

    // --- 2. LOGIC & EVENT ---
    private void initEvent() {
        // Sự kiện click bảng
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    String maNV = model.getValueAt(row, 0).toString();
                    fillForm(maNV);
                }
            }
        });

        // Nút chức năng
        btnAdd.addActionListener(e -> {
            if (isEditing) suaNhanVien(); else themNhanVien();
        });
        btnUpdate.addActionListener(e -> suaNhanVien());
        btnDelete.addActionListener(e -> xoaNhanVien());
        btnReset.addActionListener(e -> resetForm());
        
        // Tìm kiếm realtime
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timKiem(); }
            public void removeUpdate(DocumentEvent e) { timKiem(); }
            public void changedUpdate(DocumentEvent e) { timKiem(); }
        });
    }

    // --- 3. VALIDATION (QUAN TRỌNG) ---
    private boolean validateInput() {
        // 1. Kiểm tra rỗng
        if (txtHo.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
            showError("Họ tên không được để trống!"); txtHo.requestFocus(); return false;
        }
        if (txtSdt.getText().trim().isEmpty()) {
            showError("Số điện thoại không được để trống!"); txtSdt.requestFocus(); return false;
        }
        if (txtCccd.getText().trim().isEmpty()) {
            showError("CCCD không được để trống!"); txtCccd.requestFocus(); return false;
        }
        
        // 2. Validate SĐT (10 số, bắt đầu bằng 0)
        if (!txtSdt.getText().trim().matches("^0\\d{9}$")) {
            showError("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0!"); txtSdt.requestFocus(); return false;
        }

        // 3. Validate CCCD (12 số)
        if (!txtCccd.getText().trim().matches("^\\d{12}$")) {
            showError("CCCD phải có đúng 12 chữ số!"); txtCccd.requestFocus(); return false;
        }
        
        // 4. Validate Email (đơn giản)
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Email không hợp lệ!"); txtEmail.requestFocus(); return false;
        }

        // 5. Validate Lương (Số dương)
        try {
            long luong = Long.parseLong(txtLuong.getText().replace(",", "").replace(".", "").trim());
            if (luong <= 0) {
                showError("Lương phải lớn hơn 0!"); txtLuong.requestFocus(); return false;
            }
        } catch (NumberFormatException e) {
            showError("Lương phải là số nguyên hợp lệ!"); txtLuong.requestFocus(); return false;
        }

        // 6. Validate Ngày tháng & Tuổi
        if (!isValidDate(txtNgSinh.getText().trim(), "Ngày sinh")) return false;
        if (!isValidDate(txtNgayVaoLam.getText().trim(), "Ngày vào làm")) return false;

        LocalDate dob = LocalDate.parse(txtNgSinh.getText().trim());
        LocalDate workDate = LocalDate.parse(txtNgayVaoLam.getText().trim());
        LocalDate now = LocalDate.now();

        // Check tuổi >= 18
        if (Period.between(dob, now).getYears() < 18) {
            showError("Nhân viên chưa đủ 18 tuổi!"); txtNgSinh.requestFocus(); return false;
        }

        // Check ngày sinh không được ở tương lai
        if (dob.isAfter(now)) {
            showError("Ngày sinh không hợp lệ (vượt quá hiện tại)!"); txtNgSinh.requestFocus(); return false;
        }

        // Check ngày vào làm không được ở tương lai
        if (workDate.isAfter(now)) {
            showError("Ngày vào làm không được vượt quá ngày hiện tại!"); txtNgayVaoLam.requestFocus(); return false;
        }
        
        return true;
    }
    
    // Hàm check định dạng ngày
    private boolean isValidDate(String dateStr, String fieldName) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE); // yyyy-mm-dd
            return true;
        } catch (DateTimeParseException e) {
            showError(fieldName + " sai định dạng! Vui lòng nhập: yyyy-mm-dd (Ví dụ: 1999-12-30)");
            return false;
        }
    }

    // --- 4. DATA HANDLING ---
    private void themNhanVien() {
        if (!validateInput()) return;
        NhanVienDTO nv = createNhanVienDTO();
        if (bus.addNhanVien(nv)) {
            showSuccess("Thêm nhân viên thành công!");
            loadData();
            resetForm();
        } else {
            showError("Thêm thất bại (Có thể mã hoặc CCCD bị trùng)!");
        }
    }

    private void suaNhanVien() {
        if (!isEditing) { showWarning("Chọn nhân viên cần sửa!"); return; }
        if (!validateInput()) return;
        NhanVienDTO nv = createNhanVienDTO();
        if (bus.updateNhanVien(nv)) {
            showSuccess("Cập nhật thành công!");
            loadData();
            resetForm();
        } else {
            showError("Cập nhật thất bại!");
        }
    }
    
    private void xoaNhanVien() {
        if (!isEditing) { showWarning("Chọn nhân viên để xóa!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?") == JOptionPane.YES_OPTION) {
            if (bus.deleteNhanVien(txtMa.getText())) {
                showSuccess("Đã xóa thành công!");
                loadData();
                resetForm();
            }
        }
    }

    private void fillForm(String maNV) {
        NhanVienDTO nv = bus.getNhanVienByID(maNV);
        if (nv != null) {
            txtMa.setText(nv.getMaNV());
            txtHo.setText(nv.getHoNV());
            txtTen.setText(nv.getTenNV());
            cboGioiTinh.setSelectedItem(nv.getGioiTinh());
            txtNgSinh.setText(nv.getNgSinh() != null ? nv.getNgSinh().toString() : "");
            txtSdt.setText(nv.getSoDienThoai());
            txtCccd.setText(nv.getCccd());
            txtEmail.setText(nv.getEmail());
            txtDiaChi.setText(nv.getDiaChi());
            cboChucVu.setSelectedItem(nv.getChucVu());
            txtNgayVaoLam.setText(nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().toString() : "");
            txtLuong.setText(String.valueOf(nv.getLuongCoBan()));
            chkTrangThai.setSelected(nv.isTrangThai());
            
            txtMa.setEnabled(false);
            btnAdd.setText("Lưu");
        }
    }

    private NhanVienDTO createNhanVienDTO() {
        NhanVienDTO nv = new NhanVienDTO();
        nv.setMaNV(txtMa.getText().trim());
        nv.setHoNV(txtHo.getText().trim());
        nv.setTenNV(txtTen.getText().trim());
        nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
        nv.setSoDienThoai(txtSdt.getText().trim());
        nv.setEmail(txtEmail.getText().trim());
        nv.setDiaChi(txtDiaChi.getText().trim());
        nv.setCccd(txtCccd.getText().trim());
        nv.setChucVu(cboChucVu.getSelectedItem().toString());
        nv.setTrangThai(chkTrangThai.isSelected());
        try {
            nv.setLuongCoBan(Long.parseLong(txtLuong.getText().replace(",", "").replace(".", "").trim()));
            nv.setNgSinh(Date.valueOf(txtNgSinh.getText().trim()));
            nv.setNgayVaoLam(Date.valueOf(txtNgayVaoLam.getText().trim()));
        } catch (Exception e) {}
        return nv;
    }

    public void loadData() {
        ArrayList<NhanVienDTO> list = bus.getAll();
        model.setRowCount(0);
        for (NhanVienDTO nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoNV() + " " + nv.getTenNV(), nv.getGioiTinh(),
                nv.getSoDienThoai(), nv.getChucVu(), moneyFormat.format(nv.getLuongCoBan()),
                nv.isTrangThai() ? "Hoạt động" : "Đã nghỉ"
            });
        }
    }
    
    private void timKiem() {
        ArrayList<NhanVienDTO> list = bus.search(searchField.getText().trim());
        model.setRowCount(0);
        for (NhanVienDTO nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoNV() + " " + nv.getTenNV(), nv.getGioiTinh(),
                nv.getSoDienThoai(), nv.getChucVu(), moneyFormat.format(nv.getLuongCoBan()),
                nv.isTrangThai() ? "Hoạt động" : "Đã nghỉ"
            });
        }
    }

    private void generateNewMaNV() {
        txtMa.setText(bus.generateNewMaNV());
        txtMa.setEnabled(false);
        txtMa.setBackground(new Color(240, 240, 240));
    }

    private void resetForm() {
        txtHo.setText(""); txtTen.setText(""); txtSdt.setText(""); txtEmail.setText("");
        txtDiaChi.setText(""); txtCccd.setText(""); txtLuong.setText(""); txtNgSinh.setText(""); txtNgayVaoLam.setText("");
        cboGioiTinh.setSelectedIndex(0); cboChucVu.setSelectedIndex(0); chkTrangThai.setSelected(true);
        isEditing = false; btnAdd.setText("Thêm"); generateNewMaNV(); table.clearSelection();
    }

    // --- UTILS ---
    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.putClientProperty("JTextField.placeholderText", placeholder);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return txt;
    }
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(Theme.TEXT);
        return lbl;
    }
    private void showSuccess(String msg) { JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE); }
    private void showWarning(String msg) { JOptionPane.showMessageDialog(this, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE); }
    
    // Renderer tô màu
    private class EmployeeTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) table.getValueAt(row, 6);
            if (!isSelected) {
                if ("Đã nghỉ".equals(status)) { c.setBackground(new Color(245, 245, 245)); c.setForeground(Color.GRAY); }
                else { c.setBackground(Color.WHITE); c.setForeground(Color.BLACK); }
            }
            if (column == 6) {
                if ("Hoạt động".equals(status)) { c.setForeground(new Color(0, 150, 0)); setText("✓ Hoạt động"); }
                else { c.setForeground(Color.RED); setText("✗ Đã nghỉ"); }
                setHorizontalAlignment(CENTER);
            } else setHorizontalAlignment(LEFT);
            return c;
        }
    }
}