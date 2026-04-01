package Frontend.GUI.NhaCungCapGUI;

import Frontend.Compoent.*;
import Backend.BUS.NCC_NhapHang.NhaCungCapBUS;
import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class NhaCungCapPanel extends JPanel {
    private NhaCungCapBUS bus = new NhaCungCapBUS();
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

    // Định nghĩa màu đồng nhất với nút Tìm
    private final Color PRIMARY_COLOR = new Color(15, 25, 45);

    public NhaCungCapPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        JLabel lblTitle = new JLabel("  QUẢN LÝ NHÀ CUNG CẤP", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setPreferredSize(new Dimension(0, 50));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(350);
        mainSplit.setLeftComponent(createLeftFormPanel());
        mainSplit.setRightComponent(createRightTablePanel());
        add(mainSplit, BorderLayout.CENTER);

        initEvents();
        loadData(bus.getAll());
        setNewMaNCC();
    }

    private JPanel createLeftFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 15, 20, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        
        int r = 0;
        addFormRow(formPanel, gbc, r++, "Mã NCC:", txtMaNCC = new InfoField(""), 15);
        txtMaNCC.setEditable(false); 
        addFormRow(formPanel, gbc, r++, "Tên NCC:", txtTenNCC = new InfoField(""), 15);
        addFormRow(formPanel, gbc, r++, "SĐT:", txtSDT = new InfoField(""), 15);
        addFormRow(formPanel, gbc, r++, "Địa chỉ:", txtDiaChi = new InfoField(""), 15);
        addFormRow(formPanel, gbc, r++, "Email:", txtEmail = new InfoField(""), 15);

        gbc.gridy = r++; gbc.weighty = 1.0;
        formPanel.add(new JLabel(""), gbc);
        wrapper.add(formPanel, BorderLayout.CENTER);
        wrapper.add(createButtonPanel(), BorderLayout.SOUTH);
        return wrapper;
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String lbl, InfoField field, int bottom) {
        gbc.gridy = row; gbc.insets = new Insets(0, 0, bottom, 0);
        gbc.gridx = 0; gbc.weightx = 0.3; p.add(new JLabel(lbl), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; field.setEditable(true); p.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(3, 2, 8, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnThem = new ButtonAdd("Thêm");
        btnSua = new ButtonFix("Sửa");
        btnXoa = new ButtonDele("Xóa");
        btnMoi = new ButtonRefresh("Mới");
        btnNhap = new ButtonNhapExcel("Nhập Excel");
        btnXuat = new ButtonXuatExcel("Xuất Excel");
        p.add(btnThem); p.add(btnSua); p.add(btnXoa); p.add(btnMoi); p.add(btnNhap); p.add(btnXuat);
        return p;
    }

    private JPanel createRightTablePanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        
        JPanel searchBox = new JPanel(new BorderLayout(5, 5));
        searchBox.setBackground(Color.WHITE);
        searchBox.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        txtTimKiem = new SearchTextField("Nhập mã, tên hoặc SĐT để tìm...");
        btnTim = new CustomButton("Tìm", PRIMARY_COLOR); 
        btnTim.setForeground(Color.WHITE);
        btnTim.setPreferredSize(new Dimension(100, 35));
        
        searchBox.add(txtTimKiem, BorderLayout.CENTER);
        searchBox.add(btnTim, BorderLayout.EAST);
        p.add(searchBox, BorderLayout.NORTH);
        
        tableNCC = new Frontend.Compoent.Table();
        modelNCC = new DefaultTableModel(new String[]{"Mã NCC", "Tên NCC", "SĐT", "Địa chỉ", "Email"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNCC.setModel(modelNCC);

        // --- PHẦN THÊM MÀU CHO DÒNG TIÊU ĐỀ (HEADER) ---
        JTableHeader header = tableNCC.getTableHeader();
        header.setBackground(PRIMARY_COLOR); // Màu xanh đen giống nút Tìm
        header.setForeground(Color.WHITE);   // Chữ trắng cho dễ đọc
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);  // Không cho kéo đổi thứ tự cột
        // -----------------------------------------------

        p.add(new JScrollPane(tableNCC), BorderLayout.CENTER);
        return p;
    }

    private void initEvents() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { performSearch(); }
            @Override public void removeUpdate(DocumentEvent e) { performSearch(); }
            @Override public void changedUpdate(DocumentEvent e) { performSearch(); }
        });

        tableNCC.getSelectionModel().addListSelectionListener(e -> {
            int row = tableNCC.getSelectedRow();
            if (row >= 0) {
                txtMaNCC.setText(val(row, 0)); txtTenNCC.setText(val(row, 1));
                txtSDT.setText(val(row, 2)); txtDiaChi.setText(val(row, 3));
                txtEmail.setText(val(row, 4)); btnThem.setEnabled(false); 
            }
        });

        btnThem.addActionListener(e -> {
            if (bus.addNCC(getFormData())) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                refreshUI();
            }
        });

        btnSua.addActionListener(e -> {
            if (tableNCC.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Chọn NCC cần sửa!");
                return;
            }
            if (bus.updateNCC(getFormData())) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                refreshUI();
            }
        });

        btnXoa.addActionListener(e -> {
            int row = tableNCC.getSelectedRow();
            if (row < 0) return;
            if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xác nhận", 0) == 0) {
                if (bus.deleteNCC(val(row, 0))) refreshUI();
            }
        });

        btnMoi.addActionListener(e -> refreshUI());

        btnXuat.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!f.getName().endsWith(".xlsx")) f = new File(f.getAbsolutePath() + ".xlsx");
                if (bus.exportExcel(f)) JOptionPane.showMessageDialog(this, "Xuất thành công!");
            }
        });

        btnNhap.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                int count = bus.importExcel(chooser.getSelectedFile());
                if (count >= 0) {
                    JOptionPane.showMessageDialog(this, "Đã nhập thành công " + count + " NCC!");
                    refreshUI();
                }
            }
        });
    }

    private void performSearch() { loadData(bus.search(txtTimKiem.getText().trim())); }
    private void loadData(ArrayList<NhaCungCapDTO> list) {
        modelNCC.setRowCount(0);
        for (NhaCungCapDTO ncc : list) modelNCC.addRow(new Object[]{ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSdt(), ncc.getDiaChi(), ncc.getEmail()});
    }
    private void refreshUI() {
        bus.refreshData(); loadData(bus.getAll());
        resetForm(); setNewMaNCC(); btnThem.setEnabled(true);
    }
    private void resetForm() {
        txtTenNCC.setText(""); txtSDT.setText(""); txtDiaChi.setText(""); txtEmail.setText("");
        tableNCC.clearSelection();
    }
    private void setNewMaNCC() { txtMaNCC.setText(bus.generateNewMaNCC()); }
    private NhaCungCapDTO getFormData() { return new NhaCungCapDTO(txtMaNCC.getText(), txtTenNCC.getText(), txtSDT.getText(), txtDiaChi.getText(), txtEmail.getText()); }
    private String val(int r, int c) { return modelNCC.getValueAt(r, c).toString(); }
}