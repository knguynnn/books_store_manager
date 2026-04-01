package Frontend.GUI.KhachHangGUI;

import Backend.BUS.KhachHang_BanHang.KhachHangBUS;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;
import Frontend.Compoent.*;
import net.miginfocom.swing.MigLayout;

// Import Apache POI cho Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

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

    // --- CÁC HÀM UI & LOGIC CƠ BẢN (Giữ nguyên cấu trúc của bạn) ---

    public void loadData() {
        ArrayList<KhachHangDTO> list = bus.getAll(); 
        model.setRowCount(0); 
        if (list != null) {
            for (KhachHangDTO kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKH(), kh.getHoKH(), kh.getTenKH(), 
                    kh.getSoDienThoai(), kh.getEmail(), kh.getDiaChi(),
                    kh.isTrangThai() ? "Hoạt động" : "Khóa"
                });
            }
        }
    }

    private void generateNewMaKH() {
        txtMa.setText(bus.generateNewMaKH());
        txtMa.setEnabled(false);
        txtMa.setBackground(new Color(240, 240, 240));
    }

    private void initComponent() {
        this.setBackground(Theme.BACKGROUND);
        this.setLayout(new MigLayout("fill, insets 15", "[320!][grow]", "[fill]"));
        this.add(createFormPanel());
        this.add(createTablePanel(), "grow");
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

        pnlForm.add(createLabel("Mã KH:"));
        pnlForm.add(txtMa, "h 40!, gapbottom 5");
        pnlForm.add(createLabel("Họ và tên:"));
        JPanel pnlHoTen = new JPanel(new MigLayout("fill, insets 0", "[100!][grow]", "[]"));
        pnlHoTen.setOpaque(false);
        pnlHoTen.add(txtHo, "w 100!"); pnlHoTen.add(txtTen, "grow");
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
        pnlForm.add(chkTrangThai, "gaptop 5, gapbottom 10");
        pnlForm.add(createButtonPanel(), "grow, gaptop 15");

        return pnlForm;
    }

    private JPanel createTablePanel() {
        JPanel pnlTable = new JPanel(new MigLayout("fill, wrap 1", "[fill]", "[][grow]"));
        pnlTable.setBackground(Theme.BACKGROUND);

        searchField = new SearchTextField();
        searchField.setPlaceholder("Nhập mã, tên hoặc số điện thoại...");
        pnlTable.add(searchField, "h 35!, gaptop 0");

        model = new DefaultTableModel(new String[]{"Mã KH", "Họ", "Tên", "SĐT", "Email", "Địa chỉ", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(40);
        table.setDefaultRenderer(Object.class, new CustomerTableRenderer());
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        pnlTable.add(new JScrollPane(table), "grow, h 100%");
        return pnlTable;
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
        
        pnlButtons.add(btnAdd, "grow"); pnlButtons.add(btnUpdate, "grow");
        pnlButtons.add(btnDelete, "grow"); pnlButtons.add(btnReset, "grow");
        pnlButtons.add(btnNhapExcel, "span, grow");
        pnlButtons.add(btnXuatExcel, "span, grow");

        return pnlButtons;
    }

    private void initEvent() {
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    txtMa.setText(model.getValueAt(row, 0).toString());
                    txtHo.setText(model.getValueAt(row, 1).toString());
                    txtTen.setText(model.getValueAt(row, 2).toString());
                    txtSdt.setText(model.getValueAt(row, 3).toString());
                    txtEmail.setText(model.getValueAt(row, 4).toString());
                    txtDiaChi.setText(model.getValueAt(row, 5).toString());
                    chkTrangThai.setSelected(model.getValueAt(row, 6).toString().equals("Hoạt động"));
                    btnAdd.setText("Lưu");
                }
            }
        });

        btnAdd.addActionListener(e -> { if (isEditing) suaKhachHang(); else themKhachHang(); });
        btnUpdate.addActionListener(e -> suaKhachHang());
        btnDelete.addActionListener(e -> xoaKhachHang());
        btnReset.addActionListener(e -> resetForm());
        
        // --- SỰ KIỆN EXCEL ---
        btnNhapExcel.addActionListener(e -> nhapExcel());
        btnXuatExcel.addActionListener(e -> xuatExcel());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timKiemRealtime(); }
            public void removeUpdate(DocumentEvent e) { timKiemRealtime(); }
            public void changedUpdate(DocumentEvent e) { timKiemRealtime(); }
        });
    }

    // --- CHỨC NĂNG XUẤT EXCEL ---
    private void xuatExcel() {
        if (table.getRowCount() == 0) {
            showWarning("Không có dữ liệu để xuất!");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file khách hàng");
        fileChooser.setSelectedFile(new File("DanhSachKhachHang.xlsx"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            if (!saveFile.getName().endsWith(".xlsx")) {
                saveFile = new File(saveFile.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Khách Hàng");

                // Style Header
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // Ghi Header
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Ghi Data
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        row.createCell(j).setCellValue(table.getValueAt(i, j).toString());
                    }
                }

                for (int i = 0; i < table.getColumnCount(); i++) sheet.autoSizeColumn(i);

                try (FileOutputStream out = new FileOutputStream(saveFile)) {
                    workbook.write(out);
                    showSuccess("Xuất file Excel thành công!");
                }
            } catch (Exception ex) {
                showError("Lỗi xuất file: " + ex.getMessage());
            }
        }
    }

    // --- CHỨC NĂNG NHẬP EXCEL ---
    private void nhapExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel khách hàng");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File excelFile = fileChooser.getSelectedFile();
            
            try (FileInputStream fis = new FileInputStream(excelFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rows = sheet.iterator();
                
                // Bỏ qua dòng tiêu đề (Header)
                if (rows.hasNext()) rows.next();
                
                int countSuccess = 0;
                int countFail = 0;

                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    try {
                        // Giả sử file Excel có các cột: Họ, Tên, SĐT, Email, Địa chỉ
                        // Lưu ý: Mã KH sẽ tự sinh trong BUS
                        String ho = getCellValue(currentRow.getCell(1));
                        String ten = getCellValue(currentRow.getCell(2));
                        String sdt = getCellValue(currentRow.getCell(3));
                        String email = getCellValue(currentRow.getCell(4));
                        String diaChi = getCellValue(currentRow.getCell(5));

                        if (ten.isEmpty() || sdt.isEmpty()) continue;

                        KhachHangDTO kh = new KhachHangDTO(
                            bus.generateNewMaKH(), // Tự sinh mã mới cho mỗi dòng
                            ho, ten, diaChi, email, sdt, true
                        );

                        if (bus.addKhachHang(kh)) countSuccess++;
                        else countFail++;

                    } catch (Exception ex) {
                        countFail++;
                    }
                }
                loadData();
                showSuccess("Đã nhập xong! Thành công: " + countSuccess + ", Thất bại: " + countFail);
            } catch (Exception ex) {
                showError("Lỗi đọc file Excel: " + ex.getMessage());
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long)cell.getNumericCellValue());
            default: return "";
        }
    }

    // --- CÁC HÀM HỖ TRỢ KHÁC (Giữ nguyên từ bài của bạn) ---

    private void themKhachHang() {
        if (!validateInput()) return;
        if (bus.addKhachHang(createKhachHangDTO())) {
            showSuccess("Thêm khách hàng thành công!");
            
            // PHÁT TÍN HIỆU CHO TOÀN HỆ THỐNG
            Backend.BUS.EventBus.publish("KHACH_HANG_CHANGED");
            
            loadData();
            resetForm();
        }
    }

    private void suaKhachHang() {
        if (!isEditing) return;
        if (bus.updateKhachHang(createKhachHangDTO())) {
            showSuccess("Cập nhật thành công!");
            
            // PHÁT TÍN HIỆU CHO TOÀN HỆ THỐNG
            Backend.BUS.EventBus.publish("KHACH_HANG_CHANGED");
            
            loadData();
            resetForm();
        }
    }

    private void xoaKhachHang() {
        if (JOptionPane.showConfirmDialog(this, "Xóa khách hàng này?") == 0) {
            if (bus.deleteKhachHang(txtMa.getText())) {
                
                // PHÁT TÍN HIỆU CHO TOÀN HỆ THỐNG
                Backend.BUS.EventBus.publish("KHACH_HANG_CHANGED");
                
                loadData();
                resetForm();
            }
        }
    }

    private void timKiemRealtime() {
        ArrayList<KhachHangDTO> list = bus.search(searchField.getText().trim());
        model.setRowCount(0);
        if (list != null) {
            for (KhachHangDTO kh : list) {
                model.addRow(new Object[]{ kh.getMaKH(), kh.getHoKH(), kh.getTenKH(), kh.getSoDienThoai(), kh.getEmail(), kh.getDiaChi(), kh.isTrangThai() ? "Hoạt động" : "Khóa" });
            }
        }
    }

    private void resetForm() {
        txtHo.setText(""); txtTen.setText(""); txtSdt.setText(""); txtEmail.setText(""); txtDiaChi.setText("");
        chkTrangThai.setSelected(true); isEditing = false; btnAdd.setText("Thêm"); generateNewMaKH();
    }

    private boolean validateInput() {
        if (txtTen.getText().trim().isEmpty() || txtSdt.getText().trim().isEmpty()) {
            showError("Tên và SĐT không được để trống!"); return false;
        }
        return true;
    }

    private KhachHangDTO createKhachHangDTO() {
        return new KhachHangDTO(txtMa.getText().trim(), txtHo.getText().trim(), txtTen.getText().trim(), txtDiaChi.getText().trim(), txtEmail.getText().trim(), txtSdt.getText().trim(), chkTrangThai.isSelected());
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.putClientProperty("JTextField.placeholderText", placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return textField;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Theme.TEXT);
        return label;
    }

    private void showSuccess(String m) { JOptionPane.showMessageDialog(this, m, "Thông báo", 1); }
    private void showError(String m) { JOptionPane.showMessageDialog(this, m, "Lỗi", 0); }
    private void showWarning(String m) { JOptionPane.showMessageDialog(this, m, "Cảnh báo", 2); }

    class CustomerTableRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            // reset màu mặc định
            c.setForeground(Color.BLACK);
            c.setBackground(Color.WHITE);

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