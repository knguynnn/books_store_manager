package Frontend.GUI;

import Frontend.Compoent.Theme;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel sidebar;
    private Header header;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public MainFrame() {
        initComponent();
    }

    public void showCard(String name) {
        cardLayout.show(contentArea, name);
        header.setTitle(name);
    }

    private void initComponent() {
    setTitle("QUẢN LÝ CỬA HÀNG SÁCH");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1300, 850);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // 1. Sidebar
    sidebar = new Sidebar(this);
    add(sidebar, BorderLayout.WEST);

    // 2. Panel chính chứa Header và ContentArea
    JPanel pnlMain = new JPanel(new BorderLayout());
    header = new Header();
    pnlMain.add(header, BorderLayout.NORTH);

    // CHỈ KHỞI TẠO 1 LẦN DUY NHẤT Ở ĐÂY
    cardLayout = new CardLayout();
    contentArea = new JPanel(cardLayout);
    contentArea.setBackground(Theme.BACKGROUND);

    // Tạo màn hình chào mừng
    JPanel pnlWelcome = new JPanel(new GridBagLayout());
    pnlWelcome.add(new JLabel("HỆ THỐNG QUẢN LÝ BÁN SÁCH TRỰC TUYẾN"));
    contentArea.add(pnlWelcome, "WELCOME");

    pnlMain.add(contentArea, BorderLayout.CENTER);
    add(pnlMain, BorderLayout.CENTER); // Thêm pnlMain duy nhất vào Frame
    }
}