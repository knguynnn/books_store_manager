package Frontend.GUI.ThongKeGUI;

import Backend.BUS.ThongKe_PhanQuyen.ThongKeBUS;
import Backend.DTO.ThongKe_PhanQuyen.ThongKeDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThongKePanel extends JPanel {

    private ThongKeBUS tkBUS = new ThongKeBUS();
    private JComboBox<String> cboKieuThoiGian;
    private JSpinner spTuNgay, spDenNgay;
    private JButton btnLayDuLieu;
    private JLabel lblValueDoanhThu, lblValueSachBan, lblValueDonHang, lblValueKhachHang, lblValueTongChi;

    private JComboBox<String> cboLoaiChiTiet;
    private JComboBox<Integer> cboNam; 
    private JTable table;
    private DefaultTableModel model;

    public ThongKePanel() {
        initComponent();
        addEvents();
        setDefaultDate();
        loadDataTongQuan();
        loadDataChiTiet();
    }

    private void initComponent() {
        // Tăng insets cạnh dưới (30) để tạo khoảng đệm an toàn với đáy màn hình
        setLayout(new MigLayout("fill, wrap 1, insets 25 25 30 25", "[fill]", "[]30[grow, 0::]"));
        setBackground(new Color(245, 247, 250)); 

        add(createUpperSection(), "growx");
        add(createLowerSection(), "grow, h 0::"); 
    }

    private void addEvents() {
        btnLayDuLieu.addActionListener(e -> loadDataTongQuan());
        cboLoaiChiTiet.addActionListener(e -> loadDataChiTiet());
        cboNam.addActionListener(e -> loadDataChiTiet());
    }

    private void setDefaultDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1); 
        spTuNgay.setValue(cal.getTime());
    }

    private void loadDataTongQuan() {
        Date tuNgay = (Date) spTuNgay.getValue();
        Date denNgay = (Date) spDenNgay.getValue();
        if (tuNgay.after(denNgay)) {
            JOptionPane.showMessageDialog(this, "\"Từ ngày\" không thể lớn hơn \"Đến ngày\"!", "Lỗi ngày tháng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ThongKeDTO.TongQuan data = tkBUS.getTongQuan(tuNgay, denNgay);
        lblValueDoanhThu.setText(tkBUS.formatMoney(data.getDoanhThu()));
        lblValueTongChi.setText(tkBUS.formatMoney(data.getTongChi()));
        lblValueSachBan.setText(String.valueOf(data.getSoSachDaBan()));
        lblValueDonHang.setText(String.valueOf(data.getSoDonHang()));
        lblValueKhachHang.setText(String.valueOf(data.getSoKhachHang()));
    }

    private void loadDataChiTiet() {
        int loaiIndex = cboLoaiChiTiet.getSelectedIndex(); 
        int namDuocChon = (Integer) cboNam.getSelectedItem();
        ArrayList<ThongKeDTO.ChiTiet> list = tkBUS.getChiTietTheoQuy(loaiIndex, namDuocChon);
        model.setRowCount(0); 
        for (ThongKeDTO.ChiTiet item : list) {
            model.addRow(new Object[]{
                item.getTenDoiTuong(),
                tkBUS.formatMoney(item.getQuy1()),
                tkBUS.formatMoney(item.getQuy2()),
                tkBUS.formatMoney(item.getQuy3()),
                tkBUS.formatMoney(item.getQuy4()),
                tkBUS.formatMoney(item.getTongCong())
            });
        }
    }

    // --- (Phần createUpperSection giữ nguyên như cũ) ---
    private JPanel createUpperSection() {
        JPanel pnl = new JPanel(new MigLayout("fill, wrap 1, insets 0", "[fill]", "[]20[]"));
        pnl.setOpaque(false);
        JPanel pnlFilter = new JPanel(new MigLayout("insets 10 15 10 15, gapx 10", "[]10[]25[]10[]25[]10[] push"));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        JLabel title = new JLabel("BỘ LỌC:");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(100, 100, 100));
        pnlFilter.add(title);
        pnlFilter.add(new JLabel("Từ:"));
        spTuNgay = createCustomSpinner();
        pnlFilter.add(spTuNgay, "width 130!");
        pnlFilter.add(new JLabel("Đến:"));
        spDenNgay = createCustomSpinner();
        pnlFilter.add(spDenNgay, "width 130!");
        btnLayDuLieu = new JButton("Lấy dữ liệu");
        btnLayDuLieu.setBackground(new Color(63, 81, 181));
        btnLayDuLieu.setForeground(Color.WHITE);
        btnLayDuLieu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlFilter.add(btnLayDuLieu, "width 120!, height 32!, gapleft 20");
        JPanel pnlCards = new JPanel(new MigLayout("fill, gapx 15, insets 0", "[grow][grow][grow][grow][grow]"));
        pnlCards.setOpaque(false);
        pnlCards.add(createRoundedCard("DOANH THU", "0 VNĐ", new Color(46, 204, 113), "revenue"));
        pnlCards.add(createRoundedCard("TỔNG CHI", "0 VNĐ", new Color(231, 76, 60), "cost"));
        pnlCards.add(createRoundedCard("SÁCH ĐÃ BÁN", "0", new Color(52, 152, 219), "books"));
        pnlCards.add(createRoundedCard("ĐƠN HÀNG", "0", new Color(241, 196, 15), "orders"));
        pnlCards.add(createRoundedCard("KHÁCH HÀNG", "0", new Color(155, 89, 182), "customers"));
        pnl.add(pnlFilter);
        pnl.add(pnlCards);
        return pnl;
    }

    private JSpinner createCustomSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        spinner.setPreferredSize(new Dimension(130, 30));
        return spinner;
    }

    private JPanel createRoundedCard(String title, String value, Color bgColor, String type) {
        JPanel card = new JPanel(new MigLayout("wrap 1, insets 20", "[center, fill]", "[]12[]")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); 
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(220, 140)); 
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(255, 255, 255, 200));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lblVal = new JLabel(value);
        lblVal.setForeground(Color.WHITE);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(lblTitle);
        card.add(lblVal);
        switch (type) {
            case "revenue" -> lblValueDoanhThu = lblVal;
            case "cost" -> lblValueTongChi = lblVal;
            case "books" -> lblValueSachBan = lblVal;
            case "orders" -> lblValueDonHang = lblVal;
            case "customers" -> lblValueKhachHang = lblVal;
        }
        return card;
    }

    private JPanel createLowerSection() {
        // Tăng khoảng cách tiêu đề và bảng (gap 30) để lộ rõ viền dưới của Combobox
        JPanel pnl = new JPanel(new MigLayout("fill, wrap 1, insets 20", "[fill]", "[]30[grow, 0::]"));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new LineBorder(new Color(225, 225, 225), 1, true));

        // Dùng MigLayout cho phần tiêu đề để kiểm soát khoảng cách tốt hơn
        JPanel pnlHeader = new JPanel(new MigLayout("insets 0, fillx", "[left]push[right]"));
        pnlHeader.setOpaque(false);
        
        JLabel title = new JLabel("CHI TIẾT DOANH THU THEO QUÝ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(title);

        // Panel chứa các nút điều khiển
        JPanel pnlControls = new JPanel(new MigLayout("insets 0, gapx 15"));
        pnlControls.setOpaque(false);

        // --- EASTER EGG TROLL: DANH SÁCH NĂM KÉO DÀI VỀ NĂM 1 ---
        int currentYear = tkBUS.getNamHienTai();
        // Tạo mảng chứa toàn bộ số năm từ hiện tại lùi về năm 1
        Integer[] years = new Integer[currentYear]; 
        for (int i = 0; i < currentYear; i++) {
            years[i] = currentYear - i; 
        }
        
        cboNam = new JComboBox<>(years);
        cboNam.setMaximumRowCount(15); // Hiện 15 dòng mỗi lần bung để nhìn cho choáng
        cboNam.setPreferredSize(new Dimension(110, 35));
        // Thêm Tooltip để tăng tính troll
        cboNam.setToolTipText("Kéo xuống nếu bạn muốn xem doanh thu từ thời Phục Hưng hoặc Đồ Đá...");

        cboLoaiChiTiet = new JComboBox<>(new String[]{"Nhân Viên", "Khách Hàng", "Sản Phẩm"});
        cboLoaiChiTiet.setPreferredSize(new Dimension(180, 35)); 

        pnlControls.add(new JLabel("Năm:"), "gapright 5");
        pnlControls.add(cboNam);
        pnlControls.add(cboLoaiChiTiet);

        pnlHeader.add(pnlControls);
        
        // Cấu hình bảng dữ liệu
        String[] columns = {"Tên đối tượng", "Quý 1 (VNĐ)", "Quý 2 (VNĐ)", "Quý 3 (VNĐ)", "Quý 4 (VNĐ)", "TỔNG CỘNG"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(40);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=1; i<table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        // Thêm các thành phần vào Panel chính
        pnl.add(pnlHeader, "growx");
        // h 0:: đảm bảo JScrollPane co giãn đúng và hiện thanh cuộn khi danh sách quá dài
        pnl.add(scroll, "grow, h 0::"); 
        
        return pnl;
    }
}