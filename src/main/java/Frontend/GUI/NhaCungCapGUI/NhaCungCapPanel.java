package Frontend.GUI.NhaCungCapGUI;

import Frontend.Compoent.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class NhaCungCapPanel extends JPanel {
    private Table tableNCC;
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

        JLabel lblTitle = new JLabel("  QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Theme.TEXT);
        lblTitle.setPreferredSize(new Dimension(0, 45));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Theme.BACKGROUND);
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(330);
        mainSplit.setResizeWeight(0.0);
        mainSplit.setBorder(null);

        mainSplit.setLeftComponent(createLeftFormPanel());
        mainSplit.setRightComponent(createRightTablePanel());

        add(mainSplit, BorderLayout.CENTER);

        initEvents();
    }

    private JPanel createLeftFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));

        JLabel lblFormTitle = new JLabel("  THÔNG TIN NHÀ CUNG CẤP");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFormTitle.setForeground(Theme.TEXT);
        lblFormTitle.setPreferredSize(new Dimension(0, 35));
        lblFormTitle.setOpaque(true);
        lblFormTitle.setBackground(Color.WHITE);
        wrapper.add(lblFormTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        addFormRow(formPanel, gbc, row++, "Mã NCC:", txtMaNCC = new InfoField(""));
        txtMaNCC.setEditable(false);

        addFormRow(formPanel, gbc, row++, "Tên NCC:", txtTenNCC = new InfoField(""));
        txtTenNCC.setEditable(true);

        addFormRow(formPanel, gbc, row++, "SĐT:", txtSDT = new InfoField(""));
        txtSDT.setEditable(true);

        addFormRow(formPanel, gbc, row++, "Địa chỉ:", txtDiaChi = new InfoField(""));
        txtDiaChi.setEditable(true);

        addFormRow(formPanel, gbc, row++, "Email:", txtEmail = new InfoField(""));
        txtEmail.setEditable(true);

        gbc.gridx = 0; gbc.gridy = row; gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(formScroll, BorderLayout.CENTER);

        wrapper.add(createButtonPanel(), BorderLayout.SOUTH);

        return wrapper;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, InfoField field) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(createFormLabel(labelText), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(new Dimension(0, 35));
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));

        btnThem = new ButtonAdd("Thêm");
        btnSua = new ButtonFix("Sửa");
        btnXoa = new ButtonDele("Xóa");
        btnMoi = new ButtonRefresh("Mới");
        btnNhap = new ButtonNhapExcel("Nhập");
        btnXuat = new ButtonXuatExcel("Xuất");

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnMoi);
        panel.add(btnNhap);
        panel.add(btnXuat);

        return panel;
    }

    private JPanel createRightTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 5, 5, 5));

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel lblTK = new JLabel("Tìm kiếm:");
        lblTK.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTK.setForeground(Theme.TEXT);
        searchPanel.add(lblTK, BorderLayout.WEST);

        txtTimKiem = new SearchTextField("Nhập mã, tên NCC, SĐT...");
        searchPanel.add(txtTimKiem, BorderLayout.CENTER);

        btnTim = new CustomButton("Tìm", Theme.PRIMARY);
        btnTim.setPreferredSize(new Dimension(70, 35));
        searchPanel.add(btnTim, BorderLayout.EAST);

        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        String[] cols = {"Mã NCC", "Tên NCC", "SĐT", "Địa chỉ", "Email"};
        modelNCC = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNCC = new Table();
        tableNCC.setModel(modelNCC);

        JScrollPane sp = new JScrollPane(tableNCC);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        tablePanel.add(sp, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private void initEvents() {
        tableNCC.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableNCC.getSelectedRow();
                if (row >= 0) {
                    txtMaNCC.setText(val(row, 0));
                    txtTenNCC.setText(val(row, 1));
                    txtSDT.setText(val(row, 2));
                    txtDiaChi.setText(val(row, 3));
                    txtEmail.setText(val(row, 4));
                }
            }
        });
    }

    private String val(int row, int col) {
        Object v = modelNCC.getValueAt(row, col);
        return v != null ? v.toString() : "";
    }

    public boolean validateNCC() {
        if (txtTenNCC.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên NCC không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenNCC.requestFocus();
            return false;
        }
        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "SĐT không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }
        String sdt = txtSDT.getText().trim();
        if (!sdt.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "SĐT phải là 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }
        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    public void resetForm() {
        txtMaNCC.setText("");
        txtTenNCC.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtTimKiem.setText("");
        tableNCC.clearSelection();
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(Theme.TEXT);
        label.setPreferredSize(new Dimension(80, 35));
        return label;
    }

    public Table getTableNCC() { return tableNCC; }
    public DefaultTableModel getModelNCC() { return modelNCC; }
    public SearchTextField getTxtTimKiem() { return txtTimKiem; }
    public CustomButton getBtnTim() { return btnTim; }
    public InfoField getTxtMaNCC() { return txtMaNCC; }
    public InfoField getTxtTenNCC() { return txtTenNCC; }
    public InfoField getTxtSDT() { return txtSDT; }
    public InfoField getTxtDiaChi() { return txtDiaChi; }
    public InfoField getTxtEmail() { return txtEmail; }
    public ButtonAdd getBtnThem() { return btnThem; }
    public ButtonFix getBtnSua() { return btnSua; }
    public ButtonDele getBtnXoa() { return btnXoa; }
    public ButtonRefresh getBtnMoi() { return btnMoi; }
    public ButtonNhapExcel getBtnNhap() { return btnNhap; }
    public ButtonXuatExcel getBtnXuat() { return btnXuat; }
}