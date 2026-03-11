package Frontend.GUI.TrangChuGUI;

import Frontend.Compoent.Theme;
import Frontend.GUI.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrangChuPanel extends JPanel {
    private MainFrame mainFrame;
    private Image backgroundImage;

    public TrangChuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        loadBackgroundImage();
        initComponent();
    }

    private void loadBackgroundImage() {
    try {
        // CÁCH 1: Dùng ClassLoader (Cách chuẩn nhất cho Maven/Resources)
        // Lưu ý: Không có chữ "src/main/resources", chỉ bắt đầu từ sau đó
        String resourcePath = "/images/product/anhNen.jpg";
        URL imgUrl = getClass().getResource(resourcePath);
        
        if (imgUrl != null) {
            backgroundImage = new ImageIcon(imgUrl).getImage();
            System.out.println(">>> [QUÁ NGON] Đã tìm thấy ảnh bằng ClassLoader!");
        } else {
            // CÁCH 2: Nếu cách trên fail, dùng đường dẫn tuyệt đối mà Console vừa báo
            File file = new File("C:/CODE/JAVA/books_store_manager/src/main/resources/images/product/anhNen.jpg");
            if (file.exists()) {
                backgroundImage = javax.imageio.ImageIO.read(file);
                System.out.println(">>> [OK] Đã tìm thấy ảnh bằng đường dẫn tuyệt đối!");
            } else {
                System.err.println(">>> [CẠN LỜI] File thực sự không tồn tại tại: " + file.getAbsolutePath());
                System.err.println("Hãy kiểm tra lại: 1. Tên file có viết hoa không? 2. Đuôi file có đúng .jpg không?");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            // Vẽ ảnh phủ kín
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            
            // Lớp phủ tối mờ (Overlay) - Chỉnh số 180 để tăng/giảm độ đậm nhạt
            g2d.setColor(new Color(15, 23, 42, 180)); 
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Nếu vẫn không có ảnh, vẽ màu Đỏ để bạn biết là code vẽ đang chạy nhưng ảnh lỗi
            g2d.setColor(Color.RED); 
            g2d.fillRect(0, 0, getWidth(), getHeight());
            System.err.println(">>> Đang vẽ nền Đỏ vì backgroundImage bị NULL");
        }
    }

    private void initComponent() {
        // Giữ nguyên logic layout và nút như cũ
        this.setLayout(new MigLayout("fill, insets 40 80 40 80", "[center]", "[]40[]"));

        // --- PHẦN 1: HEADER ---
        JPanel pnlHeader = new JPanel(new MigLayout("wrap 1, ins 0", "[center]"));
        pnlHeader.setOpaque(false);

        JLabel lbWelcome = new JLabel("CHÀO BUỔI SÁNG, QUẢN TRỊ VIÊN!");
        lbWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbWelcome.setForeground(Theme.ACCENT);
        
        JLabel lbTitle = new JLabel("HỆ THỐNG QUẢN LÝ BOOK STORE");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lbTitle.setForeground(Color.WHITE);

        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy"));
        JLabel lbDate = new JLabel("Hôm nay là " + today);
        lbDate.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lbDate.setForeground(new Color(220, 220, 220));

        pnlHeader.add(lbWelcome);
        pnlHeader.add(lbTitle);
        pnlHeader.add(lbDate);

        // --- PHẦN 2: GRID CÁC NÚT ---
        JPanel pnlGrid = new JPanel(new MigLayout("wrap 4, gap 25", "[220!][220!][220!][220!]", "[200!][200!]"));
        pnlGrid.setOpaque(false);

        pnlGrid.add(createModernCard("Bán hàng", "shopping-basket.svg", new Color(45, 212, 191)));
        pnlGrid.add(createModernCard("Sản phẩm", "dock.svg", new Color(129, 140, 248)));
        pnlGrid.add(createModernCard("Nhập hàng", "circle-plus.svg", new Color(251, 146, 60)));
        pnlGrid.add(createModernCard("Khách hàng", "file-user.svg", new Color(56, 189, 248)));
        pnlGrid.add(createModernCard("Nhà cung cấp", "ncc.svg", new Color(167, 139, 250)));
        pnlGrid.add(createModernCard("Nhân viên", "square-user.svg", new Color(248, 113, 113)));
        pnlGrid.add(createModernCard("Thống kê", "thongke.svg", new Color(232, 121, 249)));
        pnlGrid.add(createModernCard("Phân quyền", "shield-check.svg", new Color(148, 163, 184)));

        this.add(pnlHeader, "wrap");
        this.add(pnlGrid, "wrap");
    }

    private JButton createModernCard(String text, String iconPath, Color accentColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Hiệu ứng Glassmorphism (Kính mờ) sẽ cực đẹp trên nền ảnh
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 50)); // Sáng hơn khi hover
                } else {
                    g2.setColor(new Color(255, 255, 255, 25)); // Trong suốt nhẹ
                }
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Vẽ viền nhẹ
                g2.setColor(new Color(255, 255, 255, 40));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 20, 20));
                
                g2.dispose();
                super.paintComponent(g);
            }
        };

        try {
            btn.setIcon(new FlatSVGIcon("images/icon/" + iconPath, 56, 56));
        } catch (Exception e) {}

        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setIconTextGap(20);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setForeground(accentColor); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setForeground(Color.WHITE); }
        });

        btn.addActionListener(e -> mainFrame.switchView(text));
        return btn;
    }
}