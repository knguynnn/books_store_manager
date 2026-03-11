package Frontend.GUI.SanPhamGUI;

import Backend.BUS.SanPham_DanhMuc.*;
import Backend.DTO.SanPham_DanhMuc.*;
import Frontend.Compoent.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SanPhamPanel extends JPanel {
    private SanPhamBUS spBUS = new SanPhamBUS();
    private TacGiaBUS tgBUS = new TacGiaBUS();
    private TheLoaiBUS tlBUS = new TheLoaiBUS();
    private NhaXuatBanBUS nxbBUS = new NhaXuatBanBUS();

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbMode;
    private SearchTextField searchField;
    private JButton btnSearch;

    private JLabel lbTitle;
    private JPanel pnlFields;
    
    // Khai báo các JTextField riêng biệt
    private JTextField txtMa, txtTen, txtMoTa, txtNamXB, txtMaTL, txtMaTG, txtMaNXB, txtMaNCC, txtGiaDVT, txtSoLuong;
    private JLabel[] labels = new JLabel[10];
    private JLabel lbTrangThai;
    private JCheckBox chkStatus;

    private ButtonAdd btnAdd;
    private ButtonFix btnUpdate;
    private ButtonDele btnDelete;
    private ButtonRefresh btnReset;

    public SanPhamPanel() {
        initComponents();
        initEvent();
        switchMode("Sản phẩm");
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        GridBagConstraints gMain = new GridBagConstraints();

        // --- BÊN TRÁI: FORM CHI TIẾT ---
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        lbTitle = new JLabel("THÔNG TIN", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlLeft.add(lbTitle, BorderLayout.NORTH);

        pnlFields = new JPanel(new GridBagLayout());
        pnlFields.setBackground(Color.WHITE);
        pnlFields.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Khởi tạo các JTextField
        txtMa = createStyledField(); txtMa.setEditable(false); txtMa.setBackground(new Color(230, 230, 230));
        txtTen = createStyledField();
        txtMoTa = createStyledField();
        txtNamXB = createStyledField();
        txtMaTL = createStyledField();
        txtMaTG = createStyledField();
        txtMaNXB = createStyledField();
        txtMaNCC = createStyledField();
        txtGiaDVT = createStyledField();
        txtSoLuong = createStyledField();

        // Add các field vào panel thông qua hàm helper
        setupField(0, "Mã:", txtMa);
        setupField(1, "Tên:", txtTen);
        setupField(2, "Mô tả:", txtMoTa);
        setupField(3, "Năm XB:", txtNamXB);
        setupField(4, "Mã TL:", txtMaTL);
        setupField(5, "Mã TG:", txtMaTG);
        setupField(6, "Mã NXB:", txtMaNXB);
        setupField(7, "Mã NCC:", txtMaNCC);
        setupField(8, "Giá/DVT:", txtGiaDVT);
        setupField(9, "Số lượng:", txtSoLuong);

        // Trạng thái
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);

        lbTrangThai = new JLabel("Trạng thái:"); 
        lbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0; gbc.gridy = 10; gbc.weightx = 0.3;
        pnlFields.add(lbTrangThai, gbc); // Add vào panel

        chkStatus = new JCheckBox("Đang bán");
        chkStatus.setBackground(Color.WHITE);
        chkStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlFields.add(chkStatus, gbc);

        pnlLeft.add(pnlFields, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlBtn.setOpaque(false);
        pnlBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlBtn.add(btnAdd = new ButtonAdd("Thêm"));
        pnlBtn.add(btnUpdate = new ButtonFix("Sửa"));
        pnlBtn.add(btnDelete = new ButtonDele("Xóa"));
        pnlBtn.add(btnReset = new ButtonRefresh("Làm mới"));
        pnlLeft.add(pnlBtn, BorderLayout.SOUTH);

        gMain.gridx = 0; gMain.weightx = 0.35; gMain.fill = GridBagConstraints.BOTH; gMain.weighty = 1.0;
        add(pnlLeft, gMain);

        // --- BÊN PHẢI: DANH SÁCH ---
        JPanel pnlRight = new JPanel(new BorderLayout(0, 15));
        pnlRight.setOpaque(false);
        pnlRight.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlTool = new JPanel(new BorderLayout(10, 0));
        pnlTool.setOpaque(false);
        cbMode = new JComboBox<>(new String[]{"Sản phẩm", "Tác giả", "Thể loại", "Nhà xuất bản"});
        cbMode.setPreferredSize(new Dimension(130, 40));
        searchField = new SearchTextField();
        btnSearch = new JButton("🔍");
        btnSearch.setBackground(new Color(33, 37, 41));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(50, 40));

        pnlTool.add(cbMode, BorderLayout.WEST);
        pnlTool.add(searchField, BorderLayout.CENTER);
        pnlTool.add(btnSearch, BorderLayout.EAST);
        pnlRight.add(pnlTool, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(33, 37, 41));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        pnlRight.add(new JScrollPane(table), BorderLayout.CENTER);

        gMain.gridx = 1; gMain.weightx = 0.65;
        add(pnlRight, gMain);
    }

    private void setupField(int row, String labelText, JTextField field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        
        labels[row] = new JLabel(labelText);
        labels[row].setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        pnlFields.add(labels[row], gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlFields.add(field, gbc);
    }

    private JTextField createStyledField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(248, 249, 250));
        tf.setPreferredSize(new Dimension(0, 30));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        return tf;
    }

    private void switchMode(String mode) {
        lbTitle.setText("THÔNG TIN " + mode.toUpperCase());
        model.setRowCount(0);
        resetForm();

        if (mode.equals("Sản phẩm")) {
            model.setColumnIdentifiers(new String[]{"Mã SP", "Tên Sản Phẩm", "Tác giả", "Thể loại", "Đơn Giá", "Số lượng"});
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Cho phép tự động co giãn các cột còn lại
            // Lấy ColumnModel để chỉnh từng cột
            var columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(70);  // Mã SP (Hẹp)
            columnModel.getColumn(1).setPreferredWidth(150); // Tên Sản Phẩm (Rộng nhất)
            columnModel.getColumn(2).setPreferredWidth(180); // Tác giả (Rộng vừa)
            columnModel.getColumn(3).setPreferredWidth(140); // Thể loại
            columnModel.getColumn(4).setPreferredWidth(100); // Đơn Giá (Vừa)
            columnModel.getColumn(5).setPreferredWidth(100);  // Số lượng (Hẹp)

            updateFormVisibility(new String[]{"Mã SP:", "Tên SP:", "Mô tả:", "Năm XB:", "Mã TL:", "Mã TG:", "Mã NXB:", "Mã NCC:", "Giá/DVT:", "Số lượng:"});
            lbTrangThai.setVisible(true);
            chkStatus.setVisible(true);
            loadDataSanPham();
        } else if (mode.equals("Tác giả")) {
            model.setColumnIdentifiers(new String[]{"Mã TG", "Họ Tác Giả", "Tên Tác Giả"});
            updateFormVisibility(new String[]{"Mã TG:", "Họ TG:", "Tên TG:", "", "", "", "", "", "", ""});
            lbTrangThai.setVisible(false);
            chkStatus.setVisible(false);
            loadDataTacGia();
        } else if (mode.equals("Thể loại")) {
            model.setColumnIdentifiers(new String[]{"Mã TL", "Tên Thể Loại"});
            updateFormVisibility(new String[]{"Mã TL:", "Tên TL:", "", "", "", "", "", "", "", ""});
            lbTrangThai.setVisible(false);
            chkStatus.setVisible(false);
            loadDataTheLoai();
        } else if (mode.equals("Nhà xuất bản")) {
            model.setColumnIdentifiers(new String[]{"Mã NXB", "Tên NXB", "SĐT", "Địa chỉ"});
            updateFormVisibility(new String[]{"Mã NXB:", "Tên NXB:", "SĐT:", "Địa chỉ:", "Email:", "", "", "", "", ""});
            lbTrangThai.setVisible(false);
            chkStatus.setVisible(false);
            loadDataNXB();
        }
    }

    private void updateFormVisibility(String[] labelsText) {
        JTextField[] allFields = {txtMa, txtTen, txtMoTa, txtNamXB, txtMaTL, txtMaTG, txtMaNXB, txtMaNCC, txtGiaDVT, txtSoLuong};
        for (int i = 0; i < 10; i++) {
            boolean visible = (i < labelsText.length && !labelsText[i].isEmpty());
            labels[i].setText(labelsText[i]);
            labels[i].setVisible(visible);
            allFields[i].setVisible(visible);
        }
    }

    private void loadDataSanPham() {
        model.setRowCount(0);
        for (SanPhamDTO sp : spBUS.getAll()) {
            model.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), tgBUS.getTenById(sp.getMaTG()), tlBUS.getTenById(sp.getMaTL()), sp.getDonGia(), sp.getSoLuongTon()});
        }
    }

    private void loadDataTacGia() {
        model.setRowCount(0);
        for (TacGiaDTO tg : tgBUS.getAll()) model.addRow(new Object[]{tg.getMaTG(), tg.getHoTG(), tg.getTenTG()});
    }

    private void loadDataTheLoai() {
        model.setRowCount(0);
        for (TheLoaiDTO tl : tlBUS.getAll()) model.addRow(new Object[]{tl.getMaTL(), tl.getTenTL()});
    }

    private void loadDataNXB() {
        model.setRowCount(0);
        for (NhaXuatBanDTO nxb : nxbBUS.getAll()) {
            model.addRow(new Object[]{nxb.getMaNXB(), nxb.getTenNXB(), nxb.getSoDienThoai(), nxb.getDiaChi()});
        }
    }

    private void fillForm(int row) {
        String mode = cbMode.getSelectedItem().toString();
        String id = model.getValueAt(row, 0).toString();

        if (mode.equals("Sản phẩm")) {
            SanPhamDTO sp = spBUS.getById(id);
            if (sp != null) {
                txtMa.setText(sp.getMaSP()); txtTen.setText(sp.getTenSP()); txtMoTa.setText(sp.getMoTa());
                txtNamXB.setText(String.valueOf(sp.getNamXuatBan())); txtMaTL.setText(sp.getMaTL());
                txtMaTG.setText(sp.getMaTG()); txtMaNXB.setText(sp.getMaNXB()); txtMaNCC.setText(sp.getMaNCC());
                txtGiaDVT.setText(sp.getDonGia() + " / " + sp.getDonViTinh()); txtSoLuong.setText(String.valueOf(sp.getSoLuongTon()));
                chkStatus.setSelected(sp.isTrangThai());
            }
        } else if (mode.equals("Nhà xuất bản")) {
            for(NhaXuatBanDTO nxb : nxbBUS.getAll()) {
                if(nxb.getMaNXB().equals(id)) {
                    txtMa.setText(nxb.getMaNXB()); txtTen.setText(nxb.getTenNXB());
                    txtMoTa.setText(nxb.getSoDienThoai()); txtNamXB.setText(nxb.getDiaChi());
                    txtMaTL.setText(nxb.getEmail());
                    break;
                }
            }
        } else {
            txtMa.setText(id);
            txtTen.setText(model.getValueAt(row, 1).toString());
            if (mode.equals("Tác giả")) txtMoTa.setText(model.getValueAt(row, 2).toString());
        }
    }

    private void initEvent() {
        cbMode.addActionListener(e -> switchMode(cbMode.getSelectedItem().toString()));
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) fillForm(row);
            }
        });

        btnAdd.addActionListener(e -> {
            String mode = cbMode.getSelectedItem().toString();
            if (!validateInput(mode)) return;
            
            if (mode.equals("Sản phẩm")) {
                SanPhamDTO sp = getSanPhamFromFields();
                sp.setMaSP(spBUS.generateNewMaSP());
                if (spBUS.addSanPham(sp)) loadDataSanPham();
            } else if (mode.equals("Nhà xuất bản")) {
                NhaXuatBanDTO nxb = new NhaXuatBanDTO(nxbBUS.generateNewMaNXB(), txtTen.getText(), txtMoTa.getText(), txtNamXB.getText(), txtMaTL.getText());
                if (nxbBUS.addNXB(nxb)) loadDataNXB();
            } else if (mode.equals("Tác giả")) {
                if (tgBUS.addTacGia(new TacGiaDTO(tgBUS.generateNewMaTG(), txtTen.getText(), txtMoTa.getText()))) loadDataTacGia();
            } else {
                if (tlBUS.addTheLoai(new TheLoaiDTO(tlBUS.generateNewMaTL(), txtTen.getText()))) loadDataTheLoai();
            }
            resetForm();
        });

        btnUpdate.addActionListener(e -> {
            String mode = cbMode.getSelectedItem().toString();
            String id = txtMa.getText();
            if (id.isEmpty()) return;

            if (mode.equals("Sản phẩm")) {
                if (spBUS.updateSanPham(getSanPhamFromFields())) loadDataSanPham();
            } else if (mode.equals("Nhà xuất bản")) {
                if (nxbBUS.updateNXB(new NhaXuatBanDTO(id, txtTen.getText(), txtMoTa.getText(), txtNamXB.getText(), txtMaTL.getText()))) loadDataNXB();
            } else if (mode.equals("Tác giả")) {
                if (tgBUS.updateTacGia(new TacGiaDTO(id, txtTen.getText(), txtMoTa.getText()))) loadDataTacGia();
            } else {
                if (tlBUS.updateTheLoai(new TheLoaiDTO(id, txtTen.getText()))) loadDataTheLoai();
            }
        });

        btnDelete.addActionListener(e -> {
            String mode = cbMode.getSelectedItem().toString();
            String id = txtMa.getText();
            if (id.isEmpty()) return;

            if (JOptionPane.showConfirmDialog(this, "Xóa " + mode + " " + id + "?", "Xác nhận", 0) == 0) {
                boolean success = false;
                if (mode.equals("Sản phẩm")) success = spBUS.deleteSanPham(id);
                else if (mode.equals("Nhà xuất bản")) success = nxbBUS.deleteNXB(id);
                else if (mode.equals("Tác giả")) success = tgBUS.deleteTacGia(id);
                else success = tlBUS.deleteTheLoai(id);

                if (success) {
                    switchMode(mode);
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                }
            }
        });

        btnReset.addActionListener(e -> resetForm());
    }

    private SanPhamDTO getSanPhamFromFields() {
        SanPhamDTO sp = new SanPhamDTO();
        sp.setMaSP(txtMa.getText()); sp.setTenSP(txtTen.getText()); sp.setMoTa(txtMoTa.getText());
        sp.setNamXuatBan(Integer.parseInt(txtNamXB.getText().trim()));
        sp.setMaTL(txtMaTL.getText()); sp.setMaTG(txtMaTG.getText());
        sp.setMaNXB(txtMaNXB.getText()); sp.setMaNCC(txtMaNCC.getText());
        String giaDVT = txtGiaDVT.getText();
        if(giaDVT.contains("/")) {
            sp.setDonGia(Long.parseLong(giaDVT.split("/")[0].trim()));
            sp.setDonViTinh(giaDVT.split("/")[1].trim());
        } else { sp.setDonGia(Long.parseLong(giaDVT.trim())); }
        sp.setSoLuongTon(Integer.parseInt(txtSoLuong.getText().trim()));
        sp.setTrangThai(chkStatus.isSelected());
        return sp;
    }

    private boolean validateInput(String mode) {
        try {
            if (mode.equals("Sản phẩm")) {
                Integer.parseInt(txtNamXB.getText().trim());
                Integer.parseInt(txtSoLuong.getText().trim());
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra định dạng số!");
            return false;
        }
    }

    private void resetForm() {
        txtMa.setText(""); txtTen.setText(""); txtMoTa.setText("");
        txtNamXB.setText(""); txtMaTL.setText(""); txtMaTG.setText("");
        txtMaNXB.setText(""); txtMaNCC.setText(""); txtGiaDVT.setText("");
        txtSoLuong.setText("");
        
        table.clearSelection();
        chkStatus.setSelected(true);
        String mode = cbMode.getSelectedItem().toString();
        if(mode.equals("Sản phẩm")) {
            txtMa.setText(spBUS.generateNewMaSP());
            chkStatus.setSelected(true);
        }
        else if(mode.equals("Tác giả")) txtMa.setText(tgBUS.generateNewMaTG());
        else if(mode.equals("Thể loại")) txtMa.setText(tlBUS.generateNewMaTL());
        else txtMa.setText(nxbBUS.generateNewMaNXB());
    }
}