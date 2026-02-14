package Frontend.GUI.NhapHangGUI;

import Frontend.Compoent.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class NhapHangPanel extends JPanel {
    private Table tablePhieuNhap;
    private DefaultTableModel modelPhieuNhap;

    private Table tableChiTiet;
    private DefaultTableModel modelChiTiet;

    private SearchTextField txtTimKiem;

    private InfoField txtMaPhieuNhap, txtNgayNhap, txtTongTienNhap, txtDonGia;
    private JComboBox<String> cboMaNCC, cboMaNV, cboMaSP;
    private JButton btnChonNCC, btnChonNV, btnChonSP;
    private JSpinner spinSoLuong;

    private ButtonAdd btnLuu;
    private ButtonFix btnSua;
    private ButtonDele btnXoa;
    private ButtonRefresh btnMoi;
    private ButtonNhapExcel btnNhap;
    private ButtonXuatExcel btnXuat;
    private CustomButton btnThemVaoPhieu;

    public NhapHangPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        JLabel lblTitle = new JLabel("PHIẾU NHẬP HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Theme.TEXT);
        lblTitle.setBorder(new EmptyBorder(10, 15, 10, 0));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Theme.BACKGROUND);
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
        wrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));

        JLabel lblFormTitle = new JLabel("  THÔNG TIN PHIẾU NHẬP");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFormTitle.setForeground(Theme.TEXT);
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
        txtMaPhieuNhap = new InfoField("");
        txtMaPhieuNhap.setEditable(false);
        formPanel.add(txtMaPhieuNhap, field);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Ngày nhập:"), lbl);
        row++;
        field.gridy = row;
        txtNgayNhap = new InfoField(java.time.LocalDate.now().toString());
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
        cboMaNV.setPreferredSize(new Dimension(0, 35));
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
        cboMaNCC.setPreferredSize(new Dimension(0, 35));
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
        txtTongTienNhap = new InfoField("0");
        txtTongTienNhap.setEditable(false);
        formPanel.add(txtTongTienNhap, field);

        row++;
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        GridBagConstraints sepC = new GridBagConstraints();
        sepC.gridx = 0; sepC.gridy = row; sepC.gridwidth = 2;
        sepC.fill = GridBagConstraints.HORIZONTAL;
        sepC.insets = new Insets(8, 0, 8, 0);
        formPanel.add(sep, sepC);

        row++;
        lbl.gridy = row;
        JLabel lblCTTitle = createFormLabel("THÊM SẢN PHẨM VÀO PHIẾU");
        lblCTTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCTTitle.setForeground(Theme.TEXT);
        formPanel.add(lblCTTitle, lbl);

        row++;
        lbl.gridy = row;
        formPanel.add(createFormLabel("Sản phẩm:"), lbl);
        row++;
        fieldWithBtn.gridy = row;
        fieldWithBtn.gridx = 0;
        cboMaSP = new JComboBox<>();
        cboMaSP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboMaSP.setPreferredSize(new Dimension(0, 35));
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
        txtDonGia = new InfoField("");
        formPanel.add(txtDonGia, field);

        row++;
        field.gridy = row;
        field.insets = new Insets(8, 0, 6, 0);
        btnThemVaoPhieu = new CustomButton("Thêm vào phiếu", Theme.ACCENT_COLOR);
        btnThemVaoPhieu.setPreferredSize(new Dimension(0, 35));
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

        btnLuu = new ButtonAdd("Lưu");
        btnSua = new ButtonFix("Sửa");
        btnXoa = new ButtonDele("Xóa");
        btnMoi = new ButtonRefresh("Mới");
        btnNhap = new ButtonNhapExcel("Nhập");
        btnXuat = new ButtonXuatExcel("Xuất");

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

        txtTimKiem = new SearchTextField("Tìm mã phiếu, mã NCC, mã NV...");
        searchPanel.add(txtTimKiem, BorderLayout.CENTER);

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
        tablePhieuNhap = new Table();
        tablePhieuNhap.setModel(modelPhieuNhap);

        JScrollPane sp = new JScrollPane(tablePhieuNhap);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createChiTietTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JLabel lblCT = new JLabel("  Chi tiết phiếu nhập");
        lblCT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCT.setForeground(Theme.PRIMARY);
        lblCT.setPreferredSize(new Dimension(0, 25));
        panel.add(lblCT, BorderLayout.NORTH);

        String[] cols = {"Mã phiếu nhập", "Mã SP", "Số lượng", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableChiTiet = new Table();
        tableChiTiet.setModel(modelChiTiet);

        JScrollPane sp = new JScrollPane(tableChiTiet);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
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
        label.setForeground(Theme.TEXT);
        return label;
    }

    private JButton createDotButton() {
        JButton btn = new JButton("...");
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(Theme.BACKGROUND);
        btn.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return btn;
    }

    public Table getTablePhieuNhap() { return tablePhieuNhap; }
    public Table getTableChiTiet() { return tableChiTiet; }
    public DefaultTableModel getModelPhieuNhap() { return modelPhieuNhap; }
    public DefaultTableModel getModelChiTiet() { return modelChiTiet; }
    public SearchTextField getTxtTimKiem() { return txtTimKiem; }
    public ButtonAdd getBtnLuu() { return btnLuu; }
    public ButtonFix getBtnSua() { return btnSua; }
    public ButtonDele getBtnXoa() { return btnXoa; }
    public ButtonRefresh getBtnMoi() { return btnMoi; }
    public ButtonNhapExcel getBtnNhap() { return btnNhap; }
    public ButtonXuatExcel getBtnXuat() { return btnXuat; }
    public InfoField getTxtMaPhieuNhap() { return txtMaPhieuNhap; }
    public InfoField getTxtNgayNhap() { return txtNgayNhap; }
    public InfoField getTxtTongTienNhap() { return txtTongTienNhap; }
    public JComboBox<String> getCboMaNCC() { return cboMaNCC; }
    public JComboBox<String> getCboMaNV() { return cboMaNV; }
    public JButton getBtnChonNCC() { return btnChonNCC; }
    public JButton getBtnChonNV() { return btnChonNV; }
    public JButton getBtnChonSP() { return btnChonSP; }
    public JComboBox<String> getCboMaSP() { return cboMaSP; }
    public JSpinner getSpinSoLuong() { return spinSoLuong; }
    public InfoField getTxtDonGia() { return txtDonGia; }
    public CustomButton getBtnThemVaoPhieu() { return btnThemVaoPhieu; }
}
