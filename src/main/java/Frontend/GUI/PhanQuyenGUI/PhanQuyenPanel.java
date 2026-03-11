package Frontend.GUI.PhanQuyenGUI;

import Backend.BUS.NhanVien_TaiKhoan.PhanQuyenBUS;
import Backend.DTO.NhanVien_TaiKhoan.PhanQuyenDTO;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PhanQuyenPanel extends JPanel {

    private PhanQuyenBUS bus = new PhanQuyenBUS();
    private JTable tblNhom, tblChucNang;
    private DefaultTableModel modelNhom, modelChucNang;
    private JButton btnThem, btnSua, btnXoa, btnLuu;
    private JTextField txtMaQuyen, txtTenQuyen;

    public PhanQuyenPanel() {
        initUI();
        loadDataToTableNhom();
        setupEvents();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- TITLE ---
        JLabel lblTitle = new JLabel("QUẢN LÝ PHÂN QUYỀN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(15, 32, 65));
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: SPLIT PANE ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);

        // 1. Bên trái: Danh sách nhóm quyền & Input
        JPanel pnlLeft = new JPanel(new BorderLayout(5, 5));
        pnlLeft.setBackground(Color.WHITE);

        // Form nhập liệu nhanh
        JPanel pnlInput = new JPanel(new MigLayout("wrap 2", "[][grow]"));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin nhóm quyền"));
        pnlInput.setBackground(Color.WHITE);
        
        txtMaQuyen = new JTextField();
        txtTenQuyen = new JTextField();
        pnlInput.add(new JLabel("Mã quyền:")); pnlInput.add(txtMaQuyen, "pushx, growx");
        pnlInput.add(new JLabel("Tên quyền:")); pnlInput.add(txtTenQuyen, "pushx, growx");

        modelNhom = new DefaultTableModel(new String[]{"Mã Quyền", "Tên Nhóm Quyền"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblNhom = new JTable(modelNhom);
        styleTable(tblNhom);

        pnlLeft.add(pnlInput, BorderLayout.NORTH);
        pnlLeft.add(new JScrollPane(tblNhom), BorderLayout.CENTER);

        // 2. Bên phải: Chi tiết các quyền (Checkbox)
        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createTitledBorder("Chi tiết quyền hạn"));

        // Cột 0: Tên chức năng | Cột 1: Có quyền (Boolean -> hiện Checkbox)
        modelChucNang = new DefaultTableModel(new Object[]{"Chức năng", "Cho phép"}, 0) {
            @Override 
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int r, int c) { return c == 1; }
        };
        tblChucNang = new JTable(modelChucNang);
        styleTable(tblChucNang);
        
        pnlRight.add(new JScrollPane(tblChucNang), BorderLayout.CENTER);

        // --- BUTTONS ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(Color.WHITE);
        btnThem = createBtn("Thêm mới");
        btnSua = createBtn("Sửa");
        btnXoa = createBtn("Xóa");
        btnLuu = createBtn("Lưu thay đổi");
        btnLuu.setBackground(new Color(40, 167, 69)); // Màu xanh lá cho nút Lưu
        
        pnlButtons.add(btnThem); pnlButtons.add(btnSua); pnlButtons.add(btnXoa); pnlButtons.add(btnLuu);
        pnlRight.add(pnlButtons, BorderLayout.SOUTH);

        splitPane.setLeftComponent(pnlLeft);
        splitPane.setRightComponent(pnlRight);
        add(splitPane, BorderLayout.CENTER);
    }

    private void setupEvents() {
        // Khi click vào 1 dòng bên bảng Nhóm quyền
        tblNhom.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblNhom.getSelectedRow();
                if (row != -1) {
                    String maQuyen = tblNhom.getValueAt(row, 0).toString();
                    PhanQuyenDTO pq = bus.getById(maQuyen);
                    displayChiTiet(pq);
                }
            }
        });

        // Nút Lưu
        btnLuu.addActionListener(e -> saveAction());

        // Nút Thêm
        btnThem.addActionListener(e -> {
            PhanQuyenDTO pq = getDTOFromTable();
            String msg = bus.add(pq);
            JOptionPane.showMessageDialog(this, msg);
            loadDataToTableNhom();
        });

        // Nút Xóa
        btnXoa.addActionListener(e -> {
            int row = tblNhom.getSelectedRow();
            if (row != -1) {
                String ma = tblNhom.getValueAt(row, 0).toString();
                String msg = bus.delete(ma);
                JOptionPane.showMessageDialog(this, msg);
                loadDataToTableNhom();
            }
        });
    }

    private void loadDataToTableNhom() {
        modelNhom.setRowCount(0);
        ArrayList<PhanQuyenDTO> list = bus.getAll();
        for (PhanQuyenDTO pq : list) {
            modelNhom.addRow(new Object[]{pq.getMaQuyen(), pq.getTenQuyen()});
        }
    }

    private void displayChiTiet(PhanQuyenDTO pq) {
        txtMaQuyen.setText(pq.getMaQuyen());
        txtTenQuyen.setText(pq.getTenQuyen());
        txtMaQuyen.setEditable(false); // Không cho sửa mã khi đang xem chi tiết

        modelChucNang.setRowCount(0);
        modelChucNang.addRow(new Object[]{"Quản lý Khách hàng", pq.getQlKhachHang() == 1});
        modelChucNang.addRow(new Object[]{"Quản lý Nhân viên", pq.getQlNhanVien() == 1});
        modelChucNang.addRow(new Object[]{"Quản lý Sản phẩm", pq.getQlSanPham() == 1});
        modelChucNang.addRow(new Object[]{"Quản lý Nhập hàng", pq.getQlNhapHang() == 1});
        modelChucNang.addRow(new Object[]{"Quản lý Bán hàng", pq.getQlBanHang() == 1});
        modelChucNang.addRow(new Object[]{"Quản lý Nhà cung cấp", pq.getQlNhaCungCap() == 1});
        modelChucNang.addRow(new Object[]{"Quản lý Thống kê", pq.getQlThongKe() == 1});
        modelChucNang.addRow(new Object[]{"Quản lý Phân quyền", pq.getQlPhanQuyen() == 1});
    }

    private void saveAction() {
        PhanQuyenDTO pq = getDTOFromTable();
        String msg = bus.update(pq);
        JOptionPane.showMessageDialog(this, msg);
        loadDataToTableNhom();
    }

    private PhanQuyenDTO getDTOFromTable() {
        String ma = txtMaQuyen.getText();
        String ten = txtTenQuyen.getText();
        
        // Chuyển đổi từ Checkbox (Boolean) về int (0/1) để lưu vào DB
        int[] q = new int[8];
        for (int i = 0; i < 8; i++) {
            q[i] = (boolean) modelChucNang.getValueAt(i, 1) ? 1 : 0;
        }
        
        return new PhanQuyenDTO(ma, ten, q[0], q[1], q[2], q[3], q[4], q[5], q[6], q[7]);
    }

    private JButton createBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(15, 32, 65));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}