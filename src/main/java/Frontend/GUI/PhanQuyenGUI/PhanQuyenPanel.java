package Frontend.GUI.PhanQuyenGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class PhanQuyenPanel extends JPanel {

    private JTable tblNhom;
    private JTable tblChucNang;

    private DefaultTableModel modelNhom;
    private DefaultTableModel modelChucNang;

    private JButton btnThem, btnSua, btnXoa, btnLuu;

    private final Color PRIMARY = new Color(15, 32, 65);

    public PhanQuyenPanel() {
        initUI();
        fakeData();
    }

    private void initUI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("PHÂN QUYỀN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createNhomPanel(),
                createChucNangPanel()
        );

        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.3);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createNhomPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                "Danh sách nhóm quyền",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY
        ));

        modelNhom = new DefaultTableModel(
                new String[]{"Mã nhóm", "Tên nhóm"}, 0
        );

        tblNhom = new JTable(modelNhom);
        styleTable(tblNhom);

        panel.add(new JScrollPane(tblNhom), BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlBtn.setBackground(Color.WHITE);

        btnThem = createButton("Thêm");
        btnSua = createButton("Sửa");
        btnXoa = createButton("Xóa");

        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);

        panel.add(pnlBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createChucNangPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                "Phân quyền chức năng",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY
        ));

        modelChucNang = new DefaultTableModel(
                new String[]{"Chọn", "Mã chức năng", "Tên chức năng"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                return String.class;
            }
        };

        tblChucNang = new JTable(modelChucNang);
        styleTable(tblChucNang);

        panel.add(new JScrollPane(tblChucNang), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottom.setBackground(Color.WHITE);

        btnLuu = new JButton("Lưu phân quyền");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBackground(PRIMARY);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setPreferredSize(new Dimension(180, 40));

        bottom.add(btnLuu);

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(90, 35));
        return btn;
    }

    private void styleTable(JTable table) {

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220, 230, 250));

        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 35));
    }

    private void fakeData() {

        modelNhom.addRow(new Object[]{"ADMIN", "Quản trị viên"});
        modelNhom.addRow(new Object[]{"NV", "Nhân viên"});
        modelNhom.addRow(new Object[]{"QL", "Quản lý"});

        modelChucNang.addRow(new Object[]{true, "BANHANG", "Bán hàng"});
        modelChucNang.addRow(new Object[]{true, "SANPHAM", "Sản phẩm"});
        modelChucNang.addRow(new Object[]{true, "NHAPHANG", "Nhập hàng"});
        modelChucNang.addRow(new Object[]{true, "KHACHHANG", "Khách hàng"});
        modelChucNang.addRow(new Object[]{true, "NHANVIEN", "Nhân viên"});
        modelChucNang.addRow(new Object[]{true, "THONGKE", "Thống kê"});
        modelChucNang.addRow(new Object[]{true, "PHANQUYEN", "Phân quyền"});
    }
}