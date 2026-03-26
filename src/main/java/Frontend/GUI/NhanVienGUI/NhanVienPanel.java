package Frontend.GUI.NhanVienGUI;

import Backend.BUS.NhanVien_TaiKhoan.NhanVienBUS;
import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;
import Frontend.Compoent.*;
import net.miginfocom.swing.MigLayout;

// Import thư viện Apache POI (Chỉ định đích danh để tránh xung đột Color/Font)
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
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
        generateNewMaNV();
    }

    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        this.setLayout(new MigLayout("fill, insets 10", "[450!][grow]", "[fill]"));

        JPanel pnlForm = createFormPanel();
        JPanel pnlTable = createTablePanel();
        
        this.add(pnlForm, "growy");
        this.add(pnlTable, "grow");
    }

    private JPanel createFormPanel() {
        JPanel pnlForm = new JPanel(new MigLayout("wrap 2, fill, insets 10", "[120!][grow]", "[]10[]"));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lbTitle = new JLabel("THÔNG TIN NHÂN VIÊN");
        lbTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        lbTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lbTitle, "span 2, align center, gapbottom 20");

        txtMa = createTextField("Mã nhân viên (Tự động)");
        pnlForm.add(createLabel("Mã nhân viên:"));
        pnlForm.add(txtMa, "grow, h 35!");

        pnlForm.add(createLabel("Họ tên:"));
        JPanel pnlHoTen = new JPanel(new MigLayout("insets 0", "[70%][30%]", "[]"));
        pnlHoTen.setOpaque(false);
        txtHo = createTextField("Họ và tên lót");
        txtTen = createTextField("Tên");
        pnlHoTen.add(txtHo, "grow, h 35!");
        pnlHoTen.add(txtTen, "grow, h 35!");
        pnlForm.add(pnlHoTen, "grow");

        pnlForm.add(createLabel("Ngày sinh/GT:"));
        JPanel pnlNgaySinh = new JPanel(new MigLayout("insets 0", "[grow][100!]", "[]"));
        pnlNgaySinh.setOpaque(false);
        txtNgSinh = createTextField("yyyy-mm-dd");
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        pnlNgaySinh.add(txtNgSinh, "grow, h 35!");
        pnlNgaySinh.add(cboGioiTinh, "grow, h 35!");
        pnlForm.add(pnlNgaySinh, "grow");

        txtSdt = createTextField("Nhập 10 chữ số...");
        pnlForm.add(createLabel("Số điện thoại:"));
        pnlForm.add(txtSdt, "grow, h 35!");

        txtCccd = createTextField("Nhập 12 chữ số...");
        pnlForm.add(createLabel("CCCD:"));
        pnlForm.add(txtCccd, "grow, h 35!");

        txtEmail = createTextField("example@gmail.com");
        pnlForm.add(createLabel("Email:"));
        pnlForm.add(txtEmail, "grow, h 35!");

        txtDiaChi = createTextField("Địa chỉ...");
        pnlForm.add(createLabel("Địa chỉ:"));
        pnlForm.add(txtDiaChi, "grow, h 35!");

        cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên bán hàng", "Thủ kho", "Bảo vệ"});
        cboChucVu.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        pnlForm.add(createLabel("Chức vụ:"));
        pnlForm.add(cboChucVu, "grow, h 35!");
        
        txtNgayVaoLam = createTextField("yyyy-mm-dd");
        pnlForm.add(createLabel("Ngày vào làm:"));
        pnlForm.add(txtNgayVaoLam, "grow, h 35!");

        txtLuong = createTextField("Lương...");
        pnlForm.add(createLabel("Lương cơ bản:"));
        pnlForm.add(txtLuong, "grow, h 35!");

        chkTrangThai = new JCheckBox("Đang làm việc");
        chkTrangThai.setSelected(true);
        pnlForm.add(createLabel("Trạng thái:"));
        pnlForm.add(chkTrangThai, "h 35!");

        JPanel pnlButtons = createButtonPanel();
        pnlForm.add(pnlButtons, "span 2, grow, pushy, aligny bottom");

        return pnlForm;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new MigLayout("wrap 3, fill, insets 0, gap 5", "[grow][grow][grow]", "[]"));
        pnlButtons.setOpaque(false);
        
        btnAdd = new ButtonAdd("Thêm");
        btnUpdate = new ButtonFix("Sửa");
        btnDelete = new ButtonDele("Xóa");
        btnReset = new ButtonRefresh("Mới");
        btnNhapExcel = new ButtonNhapExcel("Nhập");
        btnXuatExcel = new ButtonXuatExcel("Xuất");
        
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

        JPanel pnlSearch = new JPanel(new MigLayout("insets 0", "[][grow][]", "[]"));
        pnlSearch.setOpaque(false);
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        
        searchField = new SearchTextField();
        pnlSearch.add(searchField, "grow");
        
        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(Theme.PRIMARY);
        btnTimKiem.setForeground(Color.WHITE);
        pnlSearch.add(btnTimKiem, "w 80!, h 35!");
        
        pnlTable.add(pnlSearch, "gaptop 0");

        model = new DefaultTableModel(new String[]{"Mã NV", "Họ tên", "Giới tính", "SĐT", "Chức vụ", "Lương", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(40);
        table.getTableHeader().setBackground(Theme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setDefaultRenderer(Object.class, new EmployeeTableRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        pnlTable.add(scrollPane, "grow");

        return pnlTable;
    }

    private void initEvent() {
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    fillForm(model.getValueAt(row, 0).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> { if (isEditing) suaNhanVien(); else themNhanVien(); });
        btnUpdate.addActionListener(e -> suaNhanVien());
        btnDelete.addActionListener(e -> xoaNhanVien());
        btnReset.addActionListener(e -> resetForm());
        
        // SỰ KIỆN XUẤT EXCEL
        btnXuatExcel.addActionListener(e -> exportToExcel());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timKiem(); }
            public void removeUpdate(DocumentEvent e) { timKiem(); }
            public void changedUpdate(DocumentEvent e) { timKiem(); }
        });
    }

    // --- HÀM XUẤT EXCEL CHO NHÂN VIÊN ---
    private void exportToExcel() {
        if (table.getRowCount() == 0) {
            showWarning("Không có dữ liệu nhân viên để xuất!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu danh sách nhân viên");
        fileChooser.setSelectedFile(new File("DanhSachNhanVien.xlsx"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) filePath += ".xlsx";

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Nhân Viên");

                // Style cho Header
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // Ghi Header
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Ghi dữ liệu từ table model
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Object val = table.getValueAt(i, j);
                        row.createCell(j).setCellValue(val != null ? val.toString() : "");
                    }
                }

                // Auto size cột
                for (int i = 0; i < table.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(new File(filePath))) {
                    workbook.write(out);
                    showSuccess("Xuất danh sách nhân viên thành công!");
                }
            } catch (Exception ex) {
                showError("Lỗi xuất file: " + ex.getMessage());
            }
        }
    }

    // --- CÁC HÀM LOGIC KHÁC (GIỮ NGUYÊN) ---
    private boolean validateInput() {
        if (txtHo.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
            showError("Họ tên không được rỗng!"); return false;
        }
        if (!txtSdt.getText().trim().matches("^0\\d{9}$")) {
            showError("SĐT phải 10 số!"); return false;
        }
        return true;
    }

    private void themNhanVien() {
        if (!validateInput()) return;
        if (bus.addNhanVien(createNhanVienDTO())) {
            showSuccess("Thêm thành công!"); loadData(); resetForm();
        }
    }

    private void suaNhanVien() {
        if (!isEditing) return;
        if (bus.updateNhanVien(createNhanVienDTO())) {
            showSuccess("Sửa thành công!"); loadData(); resetForm();
        }
    }

    private void xoaNhanVien() {
        if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?") == 0) {
            if (bus.deleteNhanVien(txtMa.getText())) { loadData(); resetForm(); }
        }
    }

    private void fillForm(String maNV) {
        NhanVienDTO nv = bus.getNhanVienByID(maNV);
        if (nv != null) {
            txtMa.setText(nv.getMaNV()); txtHo.setText(nv.getHoNV()); txtTen.setText(nv.getTenNV());
            cboGioiTinh.setSelectedItem(nv.getGioiTinh()); txtNgSinh.setText(nv.getNgSinh().toString());
            txtSdt.setText(nv.getSoDienThoai()); txtCccd.setText(nv.getCccd()); txtEmail.setText(nv.getEmail());
            txtDiaChi.setText(nv.getDiaChi()); cboChucVu.setSelectedItem(nv.getChucVu());
            txtNgayVaoLam.setText(nv.getNgayVaoLam().toString()); txtLuong.setText(String.valueOf(nv.getLuongCoBan()));
            chkTrangThai.setSelected(nv.isTrangThai());
            btnAdd.setText("Lưu");
        }
    }

    private NhanVienDTO createNhanVienDTO() {
        NhanVienDTO nv = new NhanVienDTO();
        nv.setMaNV(txtMa.getText()); nv.setHoNV(txtHo.getText()); nv.setTenNV(txtTen.getText());
        nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
        nv.setSoDienThoai(txtSdt.getText()); nv.setEmail(txtEmail.getText());
        nv.setDiaChi(txtDiaChi.getText()); nv.setCccd(txtCccd.getText());
        nv.setChucVu(cboChucVu.getSelectedItem().toString()); nv.setTrangThai(chkTrangThai.isSelected());
        try {
            nv.setLuongCoBan(Long.parseLong(txtLuong.getText().replace(",", "")));
            nv.setNgSinh(Date.valueOf(txtNgSinh.getText()));
            nv.setNgayVaoLam(Date.valueOf(txtNgayVaoLam.getText()));
        } catch(Exception e) {}
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

    private void generateNewMaNV() { txtMa.setText(bus.generateNewMaNV()); txtMa.setEnabled(false); }

    private void resetForm() {
        txtHo.setText(""); txtTen.setText(""); txtSdt.setText(""); txtEmail.setText("");
        txtDiaChi.setText(""); txtCccd.setText(""); txtLuong.setText(""); txtNgSinh.setText(""); txtNgayVaoLam.setText("");
        isEditing = false; btnAdd.setText("Thêm"); generateNewMaNV();
    }

    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.putClientProperty("JTextField.placeholderText", placeholder);
        return txt;
    }

    private JLabel createLabel(String text) { return new JLabel(text); }
    private void showSuccess(String msg) { JOptionPane.showMessageDialog(this, msg); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Lỗi", 0); }
    private void showWarning(String msg) { JOptionPane.showMessageDialog(this, msg, "Cảnh báo", 2); }

    class EmployeeTableRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            // reset màu mặc định
            c.setForeground(Color.BLACK);

            String trangThai = table.getValueAt(row, 6).toString();

            if (column == 6) {
                if (trangThai.equals("Hoạt động")) {
                    c.setForeground(new Color(0,150,0));
                } else {
                    c.setForeground(Color.RED);
                }

                setHorizontalAlignment(CENTER);
            }

            return c;
        }
    }
}