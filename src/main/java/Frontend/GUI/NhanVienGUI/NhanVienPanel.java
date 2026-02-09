package Frontend.GUI.NhanVienGUI;

// --- SỬA IMPORT ĐÚNG THEO CODE TRƯỚC ---
import Backend.BUS.NhanVien_TaiKhoan.NhanVienBUS; 
import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO; // <-- Quan trọng: Import đúng package
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
import java.sql.Date; // <-- Import thư viện ngày tháng SQL
import java.text.DecimalFormat;
import java.util.ArrayList;

public class NhanVienPanel extends JPanel {
    private NhanVienBUS bus = new NhanVienBUS();
    // Các trường nhập liệu
    private JTextField txtMa, txtHo, txtTen, txtSdt, txtEmail, txtDiaChi, txtCccd, txtNgSinh, txtNgayVaoLam, txtLuong;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    private JCheckBox chkTrangThai;
    
    // Table
    private JTable table;
    private DefaultTableModel model;
    
    // Buttons
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

    public void loadData() {
        ArrayList<NhanVienDTO> list = bus.getAll(); 
        model.setRowCount(0); 
        
        if (list != null) {
            for (NhanVienDTO nv : list) {
                model.addRow(new Object[]{
                    nv.getMaNV(), 
                    nv.getHoNV() + " " + nv.getTenNV(), // Gộp họ tên hiển thị
                    nv.getGioiTinh(),
                    nv.getSoDienThoai(),
                    nv.getChucVu(),
                    moneyFormat.format(nv.getLuongCoBan()), 
                    nv.isTrangThai() ? "Hoạt động" : "Đã nghỉ"
                });
            }
        }
    }

    private void generateNewMaNV() {
        String newMa = bus.generateNewMaNV(); 
        txtMa.setText(newMa);
        txtMa.setEnabled(false);
        txtMa.setBackground(new Color(240, 240, 240));
    }

    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        // SỬA: Tăng kích thước cột Form lên 400px để không bị chật
        this.setLayout(new MigLayout("fill, insets 10", "[400!][grow]", "[fill]"));

        JPanel pnlForm = createFormPanel();
        JPanel pnlTable = createTablePanel();
        
        this.add(pnlForm);
        this.add(pnlTable, "grow"); // Table bên phải tự giãn
    }

    private JPanel createFormPanel() {
        // Form để chiều rộng 400, insets giảm xuống 10 cho gọn
        JPanel pnlForm = new JPanel(new MigLayout("wrap 2, fill, insets 10", "[100!][grow]", "[]"));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lbTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lbTitle, "span 2, align center, gapbottom 10");

        // --- 1. Mã NV ---
        txtMa = createTextField("Mã nhân viên");
        pnlForm.add(createLabel("Mã NV:"));
        pnlForm.add(txtMa, "grow, h 35!");

        // --- 2. Họ và Tên (Gộp 1 dòng) ---
        // Logic: Label chiếm cột 1, Panel con chiếm cột 2 (chứa 2 textfield)
        pnlForm.add(createLabel("Họ tên:"));
        
        JPanel pnlHoTen = new JPanel(new MigLayout("insets 0", "[35%!][grow]", "[]"));
        pnlHoTen.setOpaque(false);
        txtHo = createTextField("Họ đệm");
        txtTen = createTextField("Tên");
        pnlHoTen.add(txtHo, "grow, h 35!");
        pnlHoTen.add(txtTen, "grow, h 35!");
        
        pnlForm.add(pnlHoTen, "grow");

        // --- 3. Giới tính & Ngày sinh (Gộp 1 dòng) ---
        pnlForm.add(createLabel("Sinh nhật/GT:"));
        
        JPanel pnlNgaySinh = new JPanel(new MigLayout("insets 0", "[grow][90!]", "[]"));
        pnlNgaySinh.setOpaque(false);
        
        txtNgSinh = createTextField("yyyy-mm-dd");
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboGioiTinh.setBackground(Color.WHITE);
        
        pnlNgaySinh.add(txtNgSinh, "grow, h 35!");
        pnlNgaySinh.add(cboGioiTinh, "grow, h 35!");
        
        pnlForm.add(pnlNgaySinh, "grow");

        // --- 4. SĐT ---
        txtSdt = createTextField("Số điện thoại");
        pnlForm.add(createLabel("SĐT:"));
        pnlForm.add(txtSdt, "grow, h 35!");

        // --- 5. CCCD ---
        txtCccd = createTextField("Căn cước công dân");
        pnlForm.add(createLabel("CCCD:"));
        pnlForm.add(txtCccd, "grow, h 35!");

        // --- 6. Email ---
        txtEmail = createTextField("Email");
        pnlForm.add(createLabel("Email:"));
        pnlForm.add(txtEmail, "grow, h 35!");

        // --- 7. Địa chỉ ---
        txtDiaChi = createTextField("Địa chỉ");
        pnlForm.add(createLabel("Địa chỉ:"));
        pnlForm.add(txtDiaChi, "grow, h 35!");

        // --- 8. Chức vụ ---
        cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên bán hàng", "Kho"});
        cboChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboChucVu.setBackground(Color.WHITE);
        pnlForm.add(createLabel("Chức vụ:"));
        pnlForm.add(cboChucVu, "grow, h 35!");
        
        // --- 9. Ngày vào làm ---
        txtNgayVaoLam = createTextField("yyyy-mm-dd");
        pnlForm.add(createLabel("Ngày vào làm:"));
        pnlForm.add(txtNgayVaoLam, "grow, h 35!");

        // --- 10. Lương ---
        txtLuong = createTextField("Nhập lương cơ bản...");
        pnlForm.add(createLabel("Lương:"));
        pnlForm.add(txtLuong, "grow, h 35!");

        // --- 11. Trạng thái ---
        chkTrangThai = new JCheckBox("Đang làm việc");
        chkTrangThai.setSelected(true);
        chkTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkTrangThai.setOpaque(false);
        pnlForm.add(createLabel("Trạng thái:")); 
        pnlForm.add(chkTrangThai, "h 35!");

        // --- BUTTONS ---
        JPanel pnlButtons = createButtonPanel();
        pnlForm.add(pnlButtons, "span 2, grow, gaptop 10"); // Đẩy xuống một chút cho thoáng

        return pnlForm;
    }

    private JPanel createButtonPanel() {
        // SỬA: Chia thành lưới 3 cột, 2 dòng để tiết kiệm diện tích dọc
        // [Thêm] [Sửa] [Xóa]
        // [Mới] [Nhập] [Xuất]
        JPanel pnlButtons = new JPanel(new MigLayout("wrap 3, fill, insets 0, gap 5", "[grow][grow][grow]", "[]"));
        pnlButtons.setOpaque(false);
        
        btnAdd = new ButtonAdd("Thêm");
        btnUpdate = new ButtonFix("Sửa");
        btnDelete = new ButtonDele("Xóa");
        btnReset = new ButtonRefresh("Mới"); // Tên ngắn gọn cho đẹp
        btnNhapExcel = new ButtonNhapExcel("Nhập");
        btnXuatExcel = new ButtonXuatExcel("Xuất");
        
        Component[] comps = {btnAdd, btnUpdate, btnDelete, btnReset, btnNhapExcel, btnXuatExcel};
        for(Component c : comps) {
            if(c instanceof JButton) {
                ((JButton)c).setFont(new Font("Segoe UI", Font.BOLD, 13)); // Giảm font chút cho vừa
                ((JButton)c).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        // Dòng 1
        pnlButtons.add(btnAdd, "grow");
        pnlButtons.add(btnUpdate, "grow");
        pnlButtons.add(btnDelete, "grow");
        
        // Dòng 2
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
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(lblSearch);
        
        searchField = new SearchTextField();
        searchField.setPlaceholder("Nhập mã, tên hoặc SĐT...");
        searchField.setPreferredSize(new Dimension(0, 35));
        pnlSearch.add(searchField, "grow");
        
        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(Theme.PRIMARY);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlSearch.add(btnTimKiem, "w 80!");
        
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setDefaultRenderer(Object.class, new EmployeeTableRenderer());
        
        // Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Độ rộng cột
        TableColumnModel col = table.getColumnModel();
        col.getColumn(0).setPreferredWidth(80);  
        col.getColumn(1).setPreferredWidth(150); 
        col.getColumn(2).setPreferredWidth(60);  
        col.getColumn(3).setPreferredWidth(100); 
        col.getColumn(4).setPreferredWidth(100); 
        col.getColumn(5).setPreferredWidth(100); 
        col.getColumn(6).setPreferredWidth(100); 
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        pnlTable.add(scrollPane, "grow");

        return pnlTable;
    }

    private void initEvent() {
        // Sự kiện click vào bảng
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    // Lấy MaNV từ bảng
                    String maNV = model.getValueAt(row, 0).toString();
                    
                    // Gọi BUS để lấy thông tin chi tiết
                    NhanVienDTO nv = bus.getNhanVienByID(maNV);
                    
                    if (nv != null) {
                        txtMa.setText(nv.getMaNV());
                        txtHo.setText(nv.getHoNV());
                        txtTen.setText(nv.getTenNV());
                        cboGioiTinh.setSelectedItem(nv.getGioiTinh());
                        
                        // Hiển thị ngày tháng (Nếu null thì để trống)
                        txtNgSinh.setText(nv.getNgSinh() != null ? nv.getNgSinh().toString() : "");
                        
                        txtSdt.setText(nv.getSoDienThoai());
                        txtCccd.setText(nv.getCccd());
                        txtEmail.setText(nv.getEmail());
                        txtDiaChi.setText(nv.getDiaChi());
                        cboChucVu.setSelectedItem(nv.getChucVu());
                        
                        txtNgayVaoLam.setText(nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().toString() : "");
                        
                        txtLuong.setText(String.valueOf(nv.getLuongCoBan()));
                        chkTrangThai.setSelected(nv.isTrangThai());
                    }
                    
                    txtMa.setEnabled(false);
                    btnAdd.setText("Lưu");
                }
            }
        });

        // Các nút chức năng
        btnAdd.addActionListener(e -> {
            if (isEditing) suaNhanVien(); else themNhanVien();
        });
        
        btnUpdate.addActionListener(e -> suaNhanVien());
        btnDelete.addActionListener(e -> xoaNhanVien());
        btnReset.addActionListener(e -> resetForm());
        
        // Tìm kiếm
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timKiem(); }
            public void removeUpdate(DocumentEvent e) { timKiem(); }
            public void changedUpdate(DocumentEvent e) { timKiem(); }
        });
    }

    // --- CÁC HÀM XỬ LÝ LOGIC ---

    private void themNhanVien() {
        if (!validateInput()) return;
        NhanVienDTO nv = createNhanVienDTO();
        if (nv == null) return; // Nếu tạo lỗi thì dừng
        
        if (bus.addNhanVien(nv)) {
            showSuccess("Thêm nhân viên thành công!");
            loadData();
            resetForm();
        } else {
            showError("Thêm thất bại!");
        }
    }

    private void suaNhanVien() {
        if (!isEditing) {
            showWarning("Chọn nhân viên cần sửa!");
            return;
        }
        if (!validateInput()) return;
        NhanVienDTO nv = createNhanVienDTO();
        if (nv == null) return;
        
        if (bus.updateNhanVien(nv)) {
            showSuccess("Cập nhật thành công!");
            loadData();
            resetForm();
        } else {
            showError("Cập nhật thất bại!");
        }
    }

    private void xoaNhanVien() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showWarning("Chọn nhân viên để xóa/khóa!");
            return;
        }
        String maNV = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa/khóa nhân viên " + maNV + "?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (bus.deleteNhanVien(maNV)) {
                showSuccess("Đã xử lý thành công!");
                loadData();
                resetForm();
            } else {
                showError("Xử lý thất bại!");
            }
        }
    }
    
    private void timKiem() {
        String key = searchField.getText().trim();
        if(key.isEmpty()) {
            loadData();
        } else {
            ArrayList<NhanVienDTO> list = bus.search(key);
            model.setRowCount(0);
            for(NhanVienDTO nv : list) {
                model.addRow(new Object[]{
                    nv.getMaNV(), nv.getHoNV() + " " + nv.getTenNV(), nv.getGioiTinh(),
                    nv.getSoDienThoai(), nv.getChucVu(), moneyFormat.format(nv.getLuongCoBan()),
                    nv.isTrangThai() ? "Hoạt động" : "Đã nghỉ"
                });
            }
        }
    }

    private void resetForm() {
        txtHo.setText(""); txtTen.setText(""); 
        txtSdt.setText(""); txtEmail.setText(""); txtDiaChi.setText("");
        txtCccd.setText(""); txtLuong.setText("");
        txtNgSinh.setText(""); txtNgayVaoLam.setText("");
        cboGioiTinh.setSelectedIndex(0); cboChucVu.setSelectedIndex(0);
        chkTrangThai.setSelected(true);
        
        isEditing = false;
        btnAdd.setText("Thêm");
        txtMa.setEnabled(false);
        generateNewMaNV();
        table.clearSelection();
    }
    
    // --- UTILS ---

    private boolean validateInput() {
        if(txtHo.getText().isEmpty() || txtTen.getText().isEmpty()) {
            showError("Họ tên không được để trống"); return false;
        }
        if(txtSdt.getText().isEmpty()) {
             showError("SĐT không được để trống"); return false;
        }
        
        // Validate Lương
        try {
            Long.parseLong(txtLuong.getText().replace(",", ""));
        } catch(Exception e) {
             showError("Lương phải là số nguyên"); return false;
        }

        // Validate Ngày (Quan trọng để không bị lỗi SQL Date)
        if (!isValidDate(txtNgSinh.getText())) {
            showError("Ngày sinh sai định dạng! Vui lòng nhập: yyyy-mm-dd (Ví dụ: 1999-01-30)");
            return false;
        }
        if (!isValidDate(txtNgayVaoLam.getText())) {
            showError("Ngày vào làm sai định dạng! Vui lòng nhập: yyyy-mm-dd");
            return false;
        }
        
        return true;
    }
    
    // Hàm kiểm tra định dạng ngày đơn giản
    private boolean isValidDate(String dateStr) {
        return dateStr.matches("\\d{4}-\\d{2}-\\d{2}");
    }
    
    private NhanVienDTO createNhanVienDTO() {
        // Chuyển đổi dữ liệu form sang DTO
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
            // Xử lý Lương
            String luongStr = txtLuong.getText().replace(",", "").replace(".", "");
            nv.setLuongCoBan(Long.parseLong(luongStr));
            
            // Xử lý Ngày (Đã validate ở trên nên yên tâm convert)
            nv.setNgSinh(Date.valueOf(txtNgSinh.getText().trim()));
            nv.setNgayVaoLam(Date.valueOf(txtNgayVaoLam.getText().trim()));
            
        } catch (Exception e) {
            showError("Lỗi tạo dữ liệu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return nv;
    }
    
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
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(Theme.TEXT);
        return lbl;
    }
    
    private void showSuccess(String msg) { JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE); }
    private void showWarning(String msg) { JOptionPane.showMessageDialog(this, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE); }
    
    // Renderer cho bảng Nhân viên (Tô màu trạng thái)
    private class EmployeeTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) table.getValueAt(row, 6);
            
            if (!isSelected) {
                if ("Đã nghỉ".equals(status)) {
                    c.setBackground(new Color(240, 240, 240));
                    c.setForeground(Color.GRAY);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            }
            
            if (column == 6) { // Cột trạng thái
                if ("Hoạt động".equals(status)) c.setForeground(new Color(0, 150, 0));
                else c.setForeground(Color.RED);
                setHorizontalAlignment(CENTER);
            } else {
                setHorizontalAlignment(LEFT);
            }
            return c;
        }
    }
    
    // Thêm hàm main này để test chạy riêng Panel
    /*
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Nhan Vien Panel");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        // Add Panel vào Frame
        NhanVienPanel panel = new NhanVienPanel();
        frame.add(panel);
        
        frame.setVisible(true);
    }
    */
}