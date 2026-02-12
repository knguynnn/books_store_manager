package Frontend.GUI.NhapHangGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class NhapHangPanel extends JPanel {
    private JTable tablePhieuNhap;
    private DefaultTableModel modelPhieuNhap;

    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;

    private JTextField txtTimKiem;
    private JButton btnTim;

    private JTextField txtMaPhieuNhap, txtNgayNhap, txtTongTienNhap;
    private JComboBox<String> cboMaNCC, cboMaNV;
    private JButton btnChonNCC, btnChonNV;

    private JComboBox<String> cboMaSP;
    private JButton btnChonSP;
    private JSpinner spinSoLuong;
    private JTextField txtDonGia;
    private JButton btnThemVaoPhieu;

    private JButton btnLuu, btnSua, btnXoa, btnMoi, btnNhap, btnXuat;

    public NhapHangPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        JLabel lblTitle = new JLabel("PHIẾU NHẬP HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(10, 15, 10, 0));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(240, 240, 240));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(320);
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
        wrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        JLabel lblFormTitle = new JLabel("  THÔNG TIN PHIẾU NHẬP");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFormTitle.setPreferredSize(new Dimension(0, 35));
        lblFormTitle.setOpaque(true);
        lblFormTitle.setBackground(Color.WHITE);
        wrapper.add(lblFormTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(5, 15, 5, 15));

        GridBagConstraints lbl = new GridBagConstraints();
        lbl.anchor = GridBagConstraints.WEST;
        lbl.insets = new Insets(6, 0, 2, 5);
        lbl.gridx = 0;
        lbl.weightx = 0;

        GridBagConstraints field = new GridBagConstraints();
        field.fill = GridBagConstraints.HORIZONTAL;
        field.insets = new Insets(2, 0, 6, 0);
        field.gridx = 0;
        field.gridwidth = 2;
        field.weightx = 1.0;

        GridBagConstraints fieldWithBtn = new GridBagConstraints();
        fieldWithBtn.fill = GridBagConstraints.HORIZONTAL;
        fieldWithBtn.insets = new Insets(2, 0, 6, 0);
        fieldWithBtn.weightx = 1.0;

        GridBagConstraints btnC = new GridBagConstraints();
        btnC.insets = new Insets(2, 3, 6, 0);
        btnC.weightx = 0;

        int row = 0;

        lbl.gridy = row;
        formPanel.add(createFormLabel("Mã phiếu:"), lbl);
        row++;
        field.gridy = row;
        txtMaPhieuNhap = createFormTextField();
        txtMaPhieuNhap.setEditable(false);
        txtMaPhieuNhap.setBackground(new Color(235, 235, 235));
        formPanel.add(txtMaPhieuNhap, field);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Ngày nhập:"), lbl);
        row++;
        field.gridy = row;
        txtNgayNhap = createFormTextField();
        txtNgayNhap.setText(java.time.LocalDate.now().toString());
        formPanel.add(txtNgayNhap, field);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Nhân viên:"), lbl);
        row++;
        fieldWithBtn.gridy = row;
        fieldWithBtn.gridx = 0;
        fieldWithBtn.gridwidth = 1;
        cboMaNV = new JComboBox<>();
        cboMaNV.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboMaNV.setPreferredSize(new Dimension(0, 30));
        formPanel.add(cboMaNV, fieldWithBtn);
        btnC.gridy = row;
        btnC.gridx = 1;
        btnChonNV = createDotButton();
        formPanel.add(btnChonNV, btnC);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Nhà cung cấp:"), lbl);
        row++;
        fieldWithBtn.gridy = row;
        fieldWithBtn.gridx = 0;
        cboMaNCC = new JComboBox<>();
        cboMaNCC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboMaNCC.setPreferredSize(new Dimension(0, 30));
        formPanel.add(cboMaNCC, fieldWithBtn);
        btnC.gridy = row;
        btnC.gridx = 1;
        btnChonNCC = createDotButton();
        formPanel.add(btnChonNCC, btnC);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Tổng tiền nhập:"), lbl);
        row++;
        field.gridy = row;
        txtTongTienNhap = createFormTextField();
        txtTongTienNhap.setEditable(false);
        txtTongTienNhap.setBackground(new Color(235, 235, 235));
        txtTongTienNhap.setText("0");
        formPanel.add(txtTongTienNhap, field);

        row++;
        JPanel sep1 = new JPanel();
        sep1.setBackground(new Color(200, 200, 200));
        sep1.setPreferredSize(new Dimension(0, 1));
        GridBagConstraints sepC = new GridBagConstraints();
        sepC.gridx = 0; sepC.gridy = row; sepC.gridwidth = 2;
        sepC.fill = GridBagConstraints.HORIZONTAL;
        sepC.insets = new Insets(8, 0, 8, 0);
        formPanel.add(sep1, sepC);

        row++;
        lbl.gridy = row;
        JLabel lblCTTitle = createFormLabel("THÊM SẢN PHẨM VÀO PHIẾU");
        lblCTTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblCTTitle, lbl);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Sản phẩm:"), lbl);
        row++;
        fieldWithBtn.gridy = row;
        fieldWithBtn.gridx = 0;
        cboMaSP = new JComboBox<>();
        cboMaSP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboMaSP.setPreferredSize(new Dimension(0, 30));
        formPanel.add(cboMaSP, fieldWithBtn);
        btnC.gridy = row;
        btnC.gridx = 1;
        btnChonSP = createDotButton();
        formPanel.add(btnChonSP, btnC);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Số lượng:"), lbl);
        row++;
        field.gridy = row;
        spinSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinSoLuong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ((JSpinner.DefaultEditor) spinSoLuong.getEditor()).getTextField().setColumns(10);
        formPanel.add(spinSoLuong, field);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Đơn giá:"), lbl);
        row++;
        field.gridy = row;
        txtDonGia = createFormTextField();
        formPanel.add(txtDonGia, field);

        row++;
        field.gridy = row;
        field.insets = new Insets(8, 0, 6, 0);
        btnThemVaoPhieu = createColorButton("Thêm vào phiếu", new Color(70, 130, 180));
        btnThemVaoPhieu.setPreferredSize(new Dimension(0, 32));
        formPanel.add(btnThemVaoPhieu, field);
        field.insets = new Insets(2, 0, 6, 0);

        row++;
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridx = 0; filler.gridy = row; filler.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), filler);

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(formScroll, BorderLayout.CENTER);

        JPanel btnPanel = createButtonPanel();
        wrapper.add(btnPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));

        btnLuu = createColorButton("⊕ Lưu", new Color(40, 167, 69));
        btnSua = createColorButton("⊙ Sửa", new Color(255, 165, 0));
        btnXoa = createColorButton("⊗ Xóa", new Color(220, 53, 69));
        btnMoi = createColorButton("↻ Mới", new Color(70, 130, 180));
        btnNhap = createColorButton("📁 Nhập", new Color(23, 162, 184));
        btnXuat = createColorButton("📊 Xuất", new Color(108, 117, 125));

        panel.add(btnLuu);
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

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel lblTK = new JLabel("Tìm kiếm:");
        lblTK.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchPanel.add(lblTK, BorderLayout.WEST);

        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(0, 30));
        txtTimKiem.setToolTipText("Nhập mã phiếu, mã NCC, mã NV...");
        searchPanel.add(txtTimKiem, BorderLayout.CENTER);

        btnTim = createColorButton("Tìm", new Color(52, 73, 94));
        btnTim.setPreferredSize(new Dimension(70, 30));
        searchPanel.add(btnTim, BorderLayout.EAST);

        panel.add(searchPanel, BorderLayout.NORTH);

        JSplitPane tableSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        tableSplit.setResizeWeight(0.55);
        tableSplit.setBorder(null);

        tableSplit.setTopComponent(createPhieuNhapTablePanel());
        tableSplit.setBottomComponent(createChiTietTablePanel());

        panel.add(tableSplit, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPhieuNhapTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] cols = {"Mã phiếu nhập", "Ngày nhập", "Mã NV", "Mã NCC", "Tổng tiền nhập"};
        modelPhieuNhap = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePhieuNhap = new JTable(modelPhieuNhap);
        setupTable(tablePhieuNhap);

        JScrollPane sp = new JScrollPane(tablePhieuNhap);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createChiTietTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JLabel lblCT = new JLabel("  Chi tiết phiếu nhập");
        lblCT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCT.setForeground(new Color(70, 130, 180));
        lblCT.setPreferredSize(new Dimension(0, 25));
        panel.add(lblCT, BorderLayout.NORTH);

        String[] cols = {"Mã phiếu nhập", "Mã SP", "Số lượng", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableChiTiet = new JTable(modelChiTiet);
        setupTable(tableChiTiet);

        JScrollPane sp = new JScrollPane(tableChiTiet);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private void initEvents() {
        tablePhieuNhap.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablePhieuNhap.getSelectedRow();
                if (row >= 0) {
                    txtMaPhieuNhap.setText(val(row, 0));
                    txtNgayNhap.setText(val(row, 1));
                    selectCombo(cboMaNV, val(row, 2));
                    selectCombo(cboMaNCC, val(row, 3));
                    txtTongTienNhap.setText(val(row, 4));
                    loadChiTietPhieuNhap(txtMaPhieuNhap.getText());
                }
            }
        });
    }

    private String val(int row, int col) {
        Object v = modelPhieuNhap.getValueAt(row, col);
        return v != null ? v.toString() : "";
    }

    private void selectCombo(JComboBox<String> cbo, String value) {
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (cbo.getItemAt(i).contains(value)) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
    }

    protected void loadChiTietPhieuNhap(String maPhieuNhap) {
    }

    private void setupTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(209, 232, 255));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 32));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
    }

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
            long donGia = Long.parseLong(txtDonGia.getText().trim());
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
        txtNgayNhap.setText(java.time.LocalDate.now().toString());
        cboMaNV.setSelectedIndex(-1);
        cboMaNCC.setSelectedIndex(-1);
        txtTongTienNhap.setText("0");
        cboMaSP.setSelectedIndex(-1);
        spinSoLuong.setValue(1);
        txtDonGia.setText("");
        txtTimKiem.setText("");
        tablePhieuNhap.clearSelection();
        modelChiTiet.setRowCount(0);
    }

    public void updateTongTienNhap() {
        long tongTien = 0;
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            Object value = modelChiTiet.getValueAt(i, 4);
            if (value != null) {
                tongTien += Long.parseLong(value.toString());
            }
        }
        txtTongTienNhap.setText(String.valueOf(tongTien));
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    private JTextField createFormTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(0, 30));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        return tf;
    }

    private JButton createDotButton() {
        JButton btn = new JButton("...");
        btn.setPreferredSize(new Dimension(35, 30));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(new Color(230, 230, 230));
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return btn;
    }

    private JButton createColorButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(0, 33));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });

        return button;
    }

    public JTable getTablePhieuNhap() { return tablePhieuNhap; }
    public JTable getTableChiTiet() { return tableChiTiet; }
    public DefaultTableModel getModelPhieuNhap() { return modelPhieuNhap; }
    public DefaultTableModel getModelChiTiet() { return modelChiTiet; }
    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JButton getBtnTim() { return btnTim; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnMoi() { return btnMoi; }
    public JButton getBtnNhap() { return btnNhap; }
    public JButton getBtnXuat() { return btnXuat; }
    public JTextField getTxtMaPhieuNhap() { return txtMaPhieuNhap; }
    public JTextField getTxtNgayNhap() { return txtNgayNhap; }
    public JTextField getTxtTongTienNhap() { return txtTongTienNhap; }
    public JComboBox<String> getCboMaNCC() { return cboMaNCC; }
    public JComboBox<String> getCboMaNV() { return cboMaNV; }
    public JButton getBtnChonNCC() { return btnChonNCC; }
    public JButton getBtnChonNV() { return btnChonNV; }
    public JButton getBtnChonSP() { return btnChonSP; }
    public JComboBox<String> getCboMaSP() { return cboMaSP; }
    public JSpinner getSpinSoLuong() { return spinSoLuong; }
    public JTextField getTxtDonGia() { return txtDonGia; }
    public JButton getBtnThemVaoPhieu() { return btnThemVaoPhieu; }
}
