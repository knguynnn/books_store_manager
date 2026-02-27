package Frontend.GUI.ThongKeGUI;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Date;

public class ThongKePanel extends JPanel {

    private final Color PRIMARY = new Color(15, 32, 65);

    private JComboBox<String> cboDoiTuong;
    private JComboBox<String> cboKieu;
    private JSpinner spTuNgay;
    private JSpinner spDenNgay;
    private JButton btnThongKe;

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblValueDoanhThu;
    private JLabel lblValueSachBan;
    private JLabel lblValueDonHang;
    private JLabel lblValueKhachHang;

    private JLabel lblTongThu;
    private JLabel lblTongChi;
    private JLabel lblLoiNhuan;

    public ThongKePanel() {
        initComponent();
        initEvent();
    }

    private void initComponent() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THỐNG KÊ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(10,15,10,10));

        add(lblTitle, BorderLayout.NORTH);

        JPanel main = new JPanel(new MigLayout("fill, wrap 1, insets 10", "[fill]", "[][][][grow]"));
        main.setBackground(Color.WHITE);

        main.add(createFilterPanel(), "growx");
        main.add(createDashboardPanel(), "growx");
        main.add(createSummaryPanel(), "growx");
        main.add(createTablePanel(), "grow");

        add(main, BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {

        JPanel pnl = new JPanel(new MigLayout("wrap 6, fill", "[][150!][][150!][][150!]"));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY,2),
                "Bộ lọc thống kê",
                0,0,
                new Font("Segoe UI",Font.BOLD,14),
                PRIMARY
        ));

        pnl.add(new JLabel("Theo đối tượng:"));
        cboDoiTuong = new JComboBox<>(new String[]{"Nhân viên","Khách hàng","Sản phẩm"});
        pnl.add(cboDoiTuong);

        pnl.add(new JLabel("Kiểu thống kê:"));
        cboKieu = new JComboBox<>(new String[]{"Theo ngày","Theo tháng","Theo quý","Theo năm"});
        pnl.add(cboKieu);

        pnl.add(new JLabel("Từ ngày:"));
        spTuNgay = new JSpinner(new SpinnerDateModel());
        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay,"yyyy-MM-dd"));
        pnl.add(spTuNgay);

        pnl.add(new JLabel("Đến ngày:"));
        spDenNgay = new JSpinner(new SpinnerDateModel());
        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay,"yyyy-MM-dd"));
        pnl.add(spDenNgay);

        btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI",Font.BOLD,14));
        btnThongKe.setBackground(PRIMARY);
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFocusPainted(false);

        pnl.add(btnThongKe,"span 6, right, w 150!, h 40!");

        return pnl;
    }

    private JPanel createDashboardPanel() {

        JPanel pnl = new JPanel(new MigLayout("fill","[grow][grow][grow][grow]"));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY,2),
                "Tổng quan",
                0,0,
                new Font("Segoe UI",Font.BOLD,14),
                PRIMARY
        ));

        pnl.add(createCard("Doanh thu"));
        pnl.add(createCard("Sách đã bán"));
        pnl.add(createCard("Đơn hàng"));
        pnl.add(createCard("Khách hàng"));

        return pnl;
    }

    private JPanel createCard(String title){

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(PRIMARY,2));
        card.setPreferredSize(new Dimension(260,120));

        JLabel lblTitle = new JLabel(title,SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI",Font.BOLD,16));
        lblTitle.setForeground(PRIMARY);

        JLabel lblValue = new JLabel("0",SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI",Font.BOLD,26));

        card.add(lblTitle,BorderLayout.NORTH);
        card.add(lblValue,BorderLayout.CENTER);

        if(title.contains("Doanh")) lblValueDoanhThu = lblValue;
        if(title.contains("Sách")) lblValueSachBan = lblValue;
        if(title.contains("Đơn")) lblValueDonHang = lblValue;
        if(title.contains("Khách")) lblValueKhachHang = lblValue;

        return card;
    }

    private JPanel createSummaryPanel(){

        JPanel pnl = new JPanel(new MigLayout("fill","[grow][grow][grow]"));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY,2),
                "Lợi nhuận",
                0,0,
                new Font("Segoe UI",Font.BOLD,14),
                PRIMARY
        ));

        lblTongThu = createSummaryLabel("Tổng thu: 0 VNĐ");
        lblTongChi = createSummaryLabel("Tổng chi: 0 VNĐ");
        lblLoiNhuan = createSummaryLabel("Lợi nhuận: 0 VNĐ");

        pnl.add(lblTongThu);
        pnl.add(lblTongChi);
        pnl.add(lblLoiNhuan);

        return pnl;
    }

    private JLabel createSummaryLabel(String text){
        JLabel lbl = new JLabel(text,SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,15));
        lbl.setBorder(BorderFactory.createLineBorder(PRIMARY,2));
        return lbl;
    }

    private JPanel createTablePanel(){

        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY,2),
                "Chi tiết thống kê",
                0,0,
                new Font("Segoe UI",Font.BOLD,14),
                PRIMARY
        ));

        model = new DefaultTableModel(
                new String[]{"Tên","Q1","Q2","Q3","Q4","Tổng cộng"},0){
            public boolean isCellEditable(int r,int c){ return false;}
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI",Font.PLAIN,14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI",Font.BOLD,15));

        pnl.add(new JScrollPane(table),BorderLayout.CENTER);
        return pnl;
    }

    private void initEvent(){
        btnThongKe.addActionListener(e->xuLyThongKe());
    }

    private void xuLyThongKe(){

        Date tuNgay = (Date) spTuNgay.getValue();
        Date denNgay = (Date) spDenNgay.getValue();

        if(tuNgay.after(denNgay)){
            JOptionPane.showMessageDialog(this,"Từ ngày phải nhỏ hơn Đến ngày!");
            return;
        }

        model.setRowCount(0);

        model.addRow(new Object[]{"NV01",1000000,2000000,1500000,3000000,7500000});
        model.addRow(new Object[]{"NV02",2000000,1000000,2500000,1000000,6500000});

        lblValueDoanhThu.setText("14,000,000 VNĐ");
        lblValueSachBan.setText("320");
        lblValueDonHang.setText("120");
        lblValueKhachHang.setText("85");

        lblTongThu.setText("Tổng thu: 14,000,000 VNĐ");
        lblTongChi.setText("Tổng chi: 5,000,000 VNĐ");
        lblLoiNhuan.setText("Lợi nhuận: 9,000,000 VNĐ");
    }
}