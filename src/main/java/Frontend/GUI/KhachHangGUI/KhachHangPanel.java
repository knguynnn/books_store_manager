package Frontend.GUI.KhachHangGUI;

import Backend.BUS.KhachHang_BanHang.KhachHangBUS;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;
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
import java.util.ArrayList;

public class KhachHangPanel extends JPanel {
    private KhachHangBUS bus = new KhachHangBUS();
    private JTextField txtMa, txtHo, txtTen, txtSdt, txtEmail, txtDiaChi;
    private JTable table;
    private DefaultTableModel model;
    private ButtonAdd btnAdd;
    private ButtonFix btnUpdate;
    private ButtonDele btnDelete;
    private ButtonRefresh btnReset;
    private ButtonNhapExcel btnNhapExcel;
    private ButtonXuatExcel btnXuatExcel;
    private JCheckBox chkTrangThai;
    private SearchTextField searchField;
    private boolean isEditing = false;

    public KhachHangPanel() {
        initComponent();
        initEvent();
        loadData();
        generateNewMaKH();
    }

    public void loadData() {
        ArrayList<KhachHangDTO> list = bus.getAll(); 
        model.setRowCount(0); 
        
        if (list != null) {
            for (KhachHangDTO kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKH(), 
                    kh.getHoKH(), 
                    kh.getTenKH(), 
                    kh.getSoDienThoai(), 
                    kh.getEmail(), 
                    kh.getDiaChi(),
                    kh.isTrangThai() ? "Hoạt động" : "Khóa"
                });
            }
        }
    }

    private void generateNewMaKH() {
        String newMaKH = bus.generateNewMaKH();
        txtMa.setText(newMaKH);
        txtMa.setEnabled(false);
        txtMa.setBackground(new Color(240, 240, 240));
        txtMa.setForeground(new Color(75, 85, 99));
        txtMa.setCaretColor(new Color(75, 85, 99));
    }

    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        this.setLayout(new MigLayout("fill, insets 15", "[320!][grow]", "[fill]"));

        JPanel pnlForm = createFormPanel();
        JPanel pnlTable = createTablePanel();
        
        this.add(pnlForm);
        this.add(pnlTable, "grow");
    }

    private JPanel createFormPanel() {
        JPanel pnlForm = new JPanel(new MigLayout("wrap, fill, insets 20", "[fill]", "[][][][][][][grow]"));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));

        JLabel lbTitle = new JLabel("THÔNG TIN KHÁCH HÀNG");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setForeground(Theme.PRIMARY);
        pnlForm.add(lbTitle, "align center, gapbottom 15, span");

        txtMa = createTextField("Mã khách hàng");
        txtHo = createTextField("Họ");
        txtTen = createTextField("Tên");
        txtSdt = createTextField("Số điện thoại");
        txtEmail = createTextField("Email");
        txtDiaChi = createTextField("Địa chỉ");

        JLabel lblMaKH = createLabel("Mã KH:");
        JLabel lblAuto = new JLabel("(Tự động)");
        lblAuto.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblAuto.setForeground(new Color(107, 114, 128));
        
        JPanel pnlMaLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlMaLabel.setOpaque(false);
        pnlMaLabel.add(lblMaKH);
        pnlMaLabel.add(lblAuto);
        
        pnlForm.add(pnlMaLabel);
        pnlForm.add(txtMa, "h 40!, gapbottom 5");
        
        pnlForm.add(createLabel("Họ và tên:"));
        JPanel pnlHoTen = new JPanel(new MigLayout("fill, insets 0", "[100!][grow]", "[]"));
        pnlHoTen.setOpaque(false);
        pnlHoTen.add(txtHo, "w 100!");
        pnlHoTen.add(txtTen, "grow");
        pnlForm.add(pnlHoTen, "h 40!, gapbottom 5");
        
        pnlForm.add(createLabel("Số điện thoại:"));
        pnlForm.add(txtSdt, "h 40!, gapbottom 5");
        
        pnlForm.add(createLabel("Email:"));
        pnlForm.add(txtEmail, "h 40!, gapbottom 5");
        
        pnlForm.add(createLabel("Địa chỉ:"));
        pnlForm.add(txtDiaChi, "h 40!, gapbottom 10");

        chkTrangThai = new JCheckBox("Đang hoạt động");
        chkTrangThai.setSelected(true);
        chkTrangThai.setOpaque(false);
        chkTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkTrangThai.setForeground(Theme.TEXT);
        pnlForm.add(chkTrangThai, "gaptop 5, gapbottom 10");

        JPanel pnlButtons = createButtonPanel();
        pnlForm.add(pnlButtons, "grow, gaptop 15");

        return pnlForm;
    }

    private JPanel createTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1", "[fill]", "[][grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        // Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new MigLayout("insets 0", "[][grow][]", "[]"));
        pnlSearch.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSearch.setForeground(Theme.TEXT);
        pnlSearch.add(lblSearch);
        
        searchField = new SearchTextField();
        searchField.setPlaceholder("Nhập mã, tên hoặc số điện thoại...");
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
        model = new DefaultTableModel(new String[]{"Mã KH", "Họ", "Tên", "SĐT", "Email", "Địa chỉ", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setDefaultRenderer(Object.class, new CustomerTableRenderer());
        
        // Định dạng header
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Điều chỉnh độ rộng cột
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // Mã KH
        columnModel.getColumn(1).setPreferredWidth(80);  // Họ
        columnModel.getColumn(2).setPreferredWidth(100); // Tên
        columnModel.getColumn(3).setPreferredWidth(120); // SĐT
        columnModel.getColumn(4).setPreferredWidth(180); // Email
        columnModel.getColumn(5).setPreferredWidth(200); // Địa chỉ
        columnModel.getColumn(6).setPreferredWidth(100); // Trạng thái
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        pnlTable.add(scrollPane, "grow, h 100%");

        return pnlTable;
    }

    // Custom Table Renderer để phân biệt màu theo trạng thái
    private class CustomerTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            // Lấy giá trị trạng thái từ cột 6
            String trangThai = (String) table.getValueAt(row, 6);
            boolean isActive = trangThai.equals("Hoạt động");
            
            if (!isSelected) {
                if (!isActive) {
                    // Khách hàng đã khóa - màu xám nhạt
                    c.setBackground(new Color(248, 249, 250));
                    c.setForeground(new Color(108, 117, 125));
                } else {
                    // Khách hàng hoạt động - màu trắng
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            } else {
                // Khi được chọn, dùng màu selection
                c.setBackground(new Color(59, 130, 246, 50));
                c.setForeground(Color.BLACK);
            }
            
            // Căn giữa cho một số cột
            if (column == 0 || column == 3 || column == 6) {
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            } else if (column == 1 || column == 2) {
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
            }
            
            // Thêm icon/strikethrough cho khách hàng đã khóa
            if (column == 2 && !isActive) { // Cột tên
                setText("<html><strike>" + value + "</strike></html>");
            }
            
            // Thay đổi màu chữ cột trạng thái
            if (column == 6) {
                if (isActive) {
                    c.setForeground(new Color(34, 197, 94)); // Xanh lá cho "Hoạt động"
                    setText("✓ " + value);
                } else {
                    c.setForeground(new Color(239, 68, 68)); // Đỏ cho "Khóa"
                    setText("✗ " + value);
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            }
            
            // Đặt border nhẹ
            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            
            return c;
        }
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new MigLayout("wrap 2, gap 10", "[grow]", "[][][][]"));
        pnlButtons.setOpaque(false);
        
        btnAdd = new ButtonAdd("Thêm");
        btnUpdate = new ButtonFix("Sửa");
        btnDelete = new ButtonDele("Xóa");
        btnReset = new ButtonRefresh("Làm mới");
        btnNhapExcel = new ButtonNhapExcel("Nhập Excel");
        btnXuatExcel = new ButtonXuatExcel("Xuất Excel");
        
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Component[] buttons = {btnAdd, btnUpdate, btnDelete, btnReset, btnNhapExcel, btnXuatExcel};
        for (Component btn : buttons) {
            if (btn instanceof JButton) {
                ((JButton) btn).setFont(buttonFont);
                ((JButton) btn).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
        
        pnlButtons.add(btnAdd, "grow");
        pnlButtons.add(btnUpdate, "grow");
        pnlButtons.add(btnDelete, "grow");
        pnlButtons.add(btnReset, "grow");
        pnlButtons.add(btnNhapExcel, "span, grow");
        pnlButtons.add(btnXuatExcel, "span, grow");

        return pnlButtons;
    }

    private void initEvent() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    txtMa.setText(model.getValueAt(row, 0).toString());
                    txtHo.setText(model.getValueAt(row, 1).toString());
                    txtTen.setText(model.getValueAt(row, 2).toString());
                    txtSdt.setText(model.getValueAt(row, 3).toString());
                    txtEmail.setText(model.getValueAt(row, 4).toString());
                    txtDiaChi.setText(model.getValueAt(row, 5).toString());
                    
                    String trangThai = model.getValueAt(row, 6).toString();
                    chkTrangThai.setSelected(trangThai.equals("Hoạt động"));
                    
                    txtMa.setEnabled(false);
                    txtMa.setBackground(new Color(240, 240, 240));
                    txtMa.setForeground(new Color(75, 85, 99));
                    
                    btnAdd.setText("Lưu");
                }
            }
        });

        btnAdd.addActionListener(e -> {
            if (isEditing) {
                suaKhachHang();
            } else {
                themKhachHang();
            }
        });
        
        btnUpdate.addActionListener(e -> {
            if (table.getSelectedRow() >= 0) {
                suaKhachHang();
            } else {
                showWarning("Vui lòng chọn khách hàng cần sửa!");
            }
        });
        
        btnDelete.addActionListener(e -> xoaKhachHang());
        btnReset.addActionListener(e -> resetForm());
        btnNhapExcel.addActionListener(e -> nhapExcel());
        btnXuatExcel.addActionListener(e -> xuatExcel());
        
        // Sự kiện tìm kiếm realtime
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiemRealtime();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiemRealtime();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiemRealtime();
            }
        });

        setupEnterNavigation();
    }

    private void themKhachHang() {
        if (!validateInput()) return;
        
        KhachHangDTO kh = createKhachHangDTO();
        if (kh == null) return;
        
        if (bus.addKhachHang(kh)) {
            showSuccess("Thêm khách hàng thành công!");
            loadData();
            resetForm();
        } else {
            showError("Thêm khách hàng thất bại!");
        }
    }

    private void suaKhachHang() {
        if (!isEditing) {
            showWarning("Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        
        if (!validateInput()) return;
        
        KhachHangDTO kh = createKhachHangDTO();
        if (kh == null) return;
        
        if (bus.updateKhachHang(kh)) {
            showSuccess("Cập nhật khách hàng thành công!");
            loadData();
            resetForm();
        } else {
            showError("Cập nhật khách hàng thất bại!");
        }
    }

    private void xoaKhachHang() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showWarning("Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        
        String maKH = model.getValueAt(row, 0).toString();
        String tenKH = model.getValueAt(row, 2).toString();
        String trangThai = model.getValueAt(row, 6).toString();
        
        // Thay đổi thông báo tùy theo trạng thái
        String action = trangThai.equals("Hoạt động") ? "khóa" : "kích hoạt lại";
        String message = trangThai.equals("Hoạt động") ? 
            "Bạn có chắc chắn muốn khóa khách hàng này?\nKhách hàng sẽ không xuất hiện trong các giao dịch mới." :
            "Bạn có chắc chắn muốn kích hoạt lại khách hàng này?";
        
        int confirm = JOptionPane.showConfirmDialog(this,
            message + "\n\n" +
            "Mã: " + maKH + "\n" +
            "Tên: " + tenKH,
            "Xác nhận " + action,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Thực hiện xóa (thực chất là đổi trạng thái)
            if (bus.deleteKhachHang(maKH)) {
                showSuccess((trangThai.equals("Hoạt động") ? "Khóa" : "Kích hoạt") + " khách hàng thành công!");
                loadData();
                resetForm();
            } else {
                showError((trangThai.equals("Hoạt động") ? "Khóa" : "Kích hoạt") + " khách hàng thất bại!");
            }
        }
    }

    private void timKiemRealtime() {
        String keyword = searchField.getText().trim();
        
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        ArrayList<KhachHangDTO> list = bus.search(keyword);
        model.setRowCount(0);
        
        if (list != null && !list.isEmpty()) {
            for (KhachHangDTO kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKH(), 
                    kh.getHoKH(), 
                    kh.getTenKH(), 
                    kh.getSoDienThoai(), 
                    kh.getEmail(), 
                    kh.getDiaChi(),
                    kh.isTrangThai() ? "Hoạt động" : "Khóa"
                });
            }
        }
    }

    private void resetForm() {
        txtHo.setText("");
        txtTen.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        chkTrangThai.setSelected(true);
        
        searchField.setText("");
        table.clearSelection();
        isEditing = false;
        btnAdd.setText("Thêm");
        
        generateNewMaKH();
        txtHo.requestFocus();
    }

    private void nhapExcel() {
        showInfo("Chức năng nhập Excel đang được phát triển...");
    }

    private void xuatExcel() {
        showInfo("Chức năng xuất Excel đang được phát triển...");
    }

    private boolean validateInput() {
        if (txtTen.getText().trim().isEmpty()) {
            showError("Tên khách hàng không được để trống!");
            txtTen.requestFocus();
            return false;
        }
        
        if (txtSdt.getText().trim().isEmpty()) {
            showError("Số điện thoại không được để trống!");
            txtSdt.requestFocus();
            return false;
        }
        
        String sdt = txtSdt.getText().trim();
        if (!sdt.matches("^(0|\\+84)[3|5|7|8|9][0-9]{8}$")) {
            showError("Số điện thoại không hợp lệ! Ví dụ: 0912345678");
            txtSdt.requestFocus();
            return false;
        }
        
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Email không hợp lệ! Ví dụ: email@example.com");
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }

    private KhachHangDTO createKhachHangDTO() {
        try {
            return new KhachHangDTO(
                txtMa.getText().trim(),
                txtHo.getText().trim(),
                txtTen.getText().trim(),
                txtDiaChi.getText().trim(),
                txtEmail.getText().trim(),
                txtSdt.getText().trim(),
                chkTrangThai.isSelected()
            );
        } catch (Exception e) {
            showError("Có lỗi xảy ra khi tạo đối tượng khách hàng!");
            return null;
        }
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

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Theme.TEXT);
        return label;
    }

    private void setupEnterNavigation() {
        Component[] fields = {txtHo, txtTen, txtSdt, txtEmail, txtDiaChi};
        
        for (Component field : fields) {
            if (field instanceof JTextField) {
                ((JTextField) field).addActionListener(e -> {
                    for (int i = 0; i < fields.length - 1; i++) {
                        if (fields[i] == e.getSource()) {
                            fields[i + 1].requestFocus();
                            break;
                        }
                    }
                });
            }
        }
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông tin", JOptionPane.INFORMATION_MESSAGE);
    }
}