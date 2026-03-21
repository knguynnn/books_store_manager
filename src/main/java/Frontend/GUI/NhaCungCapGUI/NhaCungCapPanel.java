package Frontend.GUI.NhaCungCapGUI;

import Frontend.Compoent.*;
import Backend.BUS.NCC_NhapHang.NhaCungCapBUS;
import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;

// Thư viện Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*; // Dùng cho Layout và Dimension
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class NhaCungCapPanel extends JPanel {
    private NhaCungCapBUS bus = new NhaCungCapBUS();
    private boolean isEditing = false; 
    
    // Gọi đích danh package để tránh lỗi "Table is ambiguous"
    private Frontend.Compoent.Table tableNCC;
    private DefaultTableModel modelNCC;

    private SearchTextField txtTimKiem;
    private CustomButton btnTim;

    private InfoField txtMaNCC, txtTenNCC, txtSDT, txtDiaChi, txtEmail;

    private ButtonAdd btnThem;
    private ButtonFix btnSua;
    private ButtonDele btnXoa;
    private ButtonRefresh btnMoi;
    private ButtonNhapExcel btnNhap;
    private ButtonXuatExcel btnXuat;

    public NhaCungCapPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Gọi đích danh java.awt.Font để tránh lỗi
        JLabel lblTitle = new JLabel("  QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTitle.setForeground(Theme.TEXT);
        lblTitle.setPreferredSize(new Dimension(0, 45));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Theme.BACKGROUND);
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(450); 
        mainSplit.setResizeWeight(0.2); 
        mainSplit.setBorder(null);

        mainSplit.setLeftComponent(createLeftFormPanel());
        mainSplit.setRightComponent(createRightTablePanel());

        add(mainSplit, BorderLayout.CENTER);

        initEvents();
        loadData();
        setNewMaNCC();
    }

    private JPanel createLeftFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        // Gọi đích danh java.awt.Color để tránh lỗi
        wrapper.setBackground(java.awt.Color.WHITE);
        wrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));

        JLabel lblFormTitle = new JLabel("  THÔNG TIN NHÀ CUNG CẤP");
        lblFormTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblFormTitle.setForeground(Theme.TEXT);
        lblFormTitle.setPreferredSize(new Dimension(0, 35));
        lblFormTitle.setOpaque(true);
        lblFormTitle.setBackground(java.awt.Color.WHITE);
        wrapper.add(lblFormTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(java.awt.Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        int row = 0;

        addFormRow(formPanel, gbc, row++, "Mã NCC:", txtMaNCC = new InfoField(""));
        txtMaNCC.setEditable(false);
        addFormRow(formPanel, gbc, row++, "Tên NCC:", txtTenNCC = new InfoField(""));
        addFormRow(formPanel, gbc, row++, "SĐT:", txtSDT = new InfoField(""));
        addFormRow(formPanel, gbc, row++, "Địa chỉ:", txtDiaChi = new InfoField(""));
        addFormRow(formPanel, gbc, row++, "Email:", txtEmail = new InfoField(""));

        gbc.gridx = 0; gbc.gridy = row; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(Box.createVerticalGlue(), gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        wrapper.add(formScroll, BorderLayout.CENTER);

        wrapper.add(createButtonPanel(), BorderLayout.SOUTH);
        return wrapper;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, InfoField field) {
        gbc.gridy = row;
        gbc.insets = new Insets(10, 0, 10, 5);

        gbc.gridx = 0; 
        gbc.weightx = 0.0; 
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createFormLabel(labelText), gbc);

        gbc.gridx = 1; 
        gbc.weightx = 1.0; // Quan trọng: Cho phép component chiếm hết không gian ngang
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        field.setPreferredSize(new Dimension(250, 40)); 
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 8, 8));
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));

        btnThem = new ButtonAdd("Thêm");
        btnSua = new ButtonFix("Sửa");
        btnXoa = new ButtonDele("Xóa");
        btnMoi = new ButtonRefresh("Mới");
        btnNhap = new ButtonNhapExcel("Nhập");
        btnXuat = new ButtonXuatExcel("Xuất");

        panel.add(btnThem); panel.add(btnSua); panel.add(btnXoa);
        panel.add(btnMoi); panel.add(btnNhap); panel.add(btnXuat);
        return panel;
    }

    private JPanel createRightTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 5, 5, 5));

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setBackground(java.awt.Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(8, 10, 8, 10));

        txtTimKiem = new SearchTextField("Nhập mã, tên NCC...");
        searchPanel.add(new JLabel("Tìm kiếm:"), BorderLayout.WEST);
        searchPanel.add(txtTimKiem, BorderLayout.CENTER);

        btnTim = new CustomButton("Tìm", Theme.PRIMARY);
        btnTim.setPreferredSize(new Dimension(70, 35));
        searchPanel.add(btnTim, BorderLayout.EAST);

        panel.add(searchPanel, BorderLayout.NORTH);

        tableNCC = new Frontend.Compoent.Table();
        String[] cols = {"Mã NCC", "Tên NCC", "SĐT", "Địa chỉ", "Email"};
        modelNCC = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNCC.setModel(modelNCC);

        JScrollPane sp = new JScrollPane(tableNCC);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private void initEvents() {
        tableNCC.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableNCC.getSelectedRow();
                if (row >= 0) {
                    isEditing = true;
                    btnThem.setText("Lưu");
                    txtMaNCC.setText(val(row, 0));
                    txtTenNCC.setText(val(row, 1));
                    txtSDT.setText(val(row, 2));
                    txtDiaChi.setText(val(row, 3));
                    txtEmail.setText(val(row, 4));
                }
            }
        });

        btnThem.addActionListener(e -> {
            if (!validateNCC()) return;
            NhaCungCapDTO ncc = new NhaCungCapDTO(txtMaNCC.getText(), txtTenNCC.getText(), txtSDT.getText(), txtDiaChi.getText(), txtEmail.getText());
            if (isEditing) {
                if (bus.updateNCC(ncc)) { loadData(); resetForm(); setNewMaNCC(); }
            } else {
                if (bus.addNCC(ncc)) { loadData(); resetForm(); setNewMaNCC(); }
            }
        });

        btnMoi.addActionListener(e -> { resetForm(); setNewMaNCC(); });

        btnXuat.addActionListener(e -> {
            XuatExcel.xuat(tableNCC, "Danh Sach Nha Cung Cap");
        });

        btnNhap.addActionListener(e -> {
            importExcel();
        });
    }

    private void importExcel() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rows = sheet.iterator();
                if (rows.hasNext()) rows.next(); // Skip header row

                int success = 0;
                while (rows.hasNext()) {
                    Row row = rows.next();
                    NhaCungCapDTO ncc = new NhaCungCapDTO(
                        getCellValue(row.getCell(0)),
                        getCellValue(row.getCell(1)),
                        getCellValue(row.getCell(2)),
                        getCellValue(row.getCell(3)),
                        getCellValue(row.getCell(4))
                    );
                    if (bus.addNCC(ncc)) success++;
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + success + " NCC.");
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file: " + ex.getMessage());
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long)cell.getNumericCellValue());
        return cell.getStringCellValue();
    }

    private void loadData() {
        modelNCC.setRowCount(0);
        for (NhaCungCapDTO ncc : bus.getAll()) {
            modelNCC.addRow(new Object[]{ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSdt(), ncc.getDiaChi(), ncc.getEmail()});
        }
    }

    private void setNewMaNCC() {
        txtMaNCC.setText(bus.generateNewMaNCC());
        isEditing = false;
        btnThem.setText("Thêm");
    }

    private String val(int row, int col) {
        Object v = modelNCC.getValueAt(row, col);
        return v != null ? v.toString() : "";
    }

    private void resetForm() {
        txtMaNCC.setText(""); txtTenNCC.setText(""); txtSDT.setText("");
        txtDiaChi.setText(""); txtEmail.setText("");
        tableNCC.clearSelection();
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        label.setForeground(Theme.TEXT);
        label.setPreferredSize(new Dimension(100, 35));
        return label;
    }

    public boolean validateNCC() {
        if (txtTenNCC.getText().trim().isEmpty()) return false;
        return true;
    }
}