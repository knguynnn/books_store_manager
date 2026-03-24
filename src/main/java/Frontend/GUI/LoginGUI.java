package Frontend.GUI;

import Backend.BUS.NhanVien_TaiKhoan.DangNhapBUS;
import Backend.BUS.SharedData;
import Frontend.Compoent.Theme;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoginGUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private DangNhapBUS bus = new DangNhapBUS();
    private ImageSlideshowPanel slideshowPanel;

    public LoginGUI() {
        initComponent();
    }

    private void initComponent() {
        setTitle("Books_store_manager");
        // 1. TĂNG KÍCH THƯỚC TỔNG THỂ (1150x600)
        setSize(1150, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); 

        // 2. Panel nội dung chính (pnlMain)
        JPanel pnlMain = new JPanel(null) { // Dùng null layout để quản lý chính xác tọa độ
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(235, 235, 235)); // Màu nền form sáng hơn một chút
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        pnlMain.setOpaque(false);

        // 3. Nút đóng (X)
        JButton btnClose = new JButton("X");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(new Color(220, 38, 38));
        btnClose.setBounds(1110, 10, 30, 30); 
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> System.exit(0));
        pnlMain.add(btnClose);

        // --- KHU VỰC SLIDESHOW (Rộng 750px) ---
        List<String> imagePaths = new ArrayList<>();
        String baseDir = "src/main/resources/images/product/";
        imagePaths.add(baseDir + "congtruong.jpg"); 
        imagePaths.add(baseDir + "khuC.jpg");
        imagePaths.add(baseDir + "khuB.jpg");
        imagePaths.add(baseDir + "anhNen2.jpg");
        
        slideshowPanel = new ImageSlideshowPanel(imagePaths, 4500);
        slideshowPanel.setBounds(0, 0, 750, 600); // Tăng chiều rộng ảnh
        pnlMain.add(slideshowPanel);

        // --- KHU VỰC FORM (Rộng 400px) ---
        JPanel pnlForm = new JPanel(new MigLayout("wrap, insets 60 50 40 50, fillx", "[fill]", "[]10[]40[]10[]20[]10[]40[]"));
        pnlForm.setOpaque(false);
        pnlForm.setBounds(750, 0, 400, 600); // Đặt chính xác sau phần ảnh

        JLabel lbLoginIcon = new JLabel(new FlatSVGIcon("images/icon/user-black.svg", 60, 60));
        lbLoginIcon.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lbLoginTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lbLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lbLoginTitle.setForeground(Theme.PRIMARY);

        pnlForm.add(lbLoginIcon, "center");
        pnlForm.add(lbLoginTitle, "center");

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.putClientProperty("JTextField.placeholderText", "Tên đăng nhập");
        txtUsername.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("images/icon/square-user-black.svg", 22, 22));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.putClientProperty("JTextField.placeholderText", "Mật khẩu");
        txtPassword.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("images/icon/shield-check-black.svg", 22, 22));
        txtPassword.putClientProperty("JPasswordField.revealIcon", true);

        pnlForm.add(new JLabel("Username"), "gapleft 5");
        pnlForm.add(txtUsername, "h 48!");
        pnlForm.add(new JLabel("Password"), "gapleft 5, gaptop 10");
        pnlForm.add(txtPassword, "h 48!");

        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(Theme.PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.putClientProperty("JButton.buttonType", "roundRect");
        
        btnLogin.addActionListener(this::handleLogin);
        pnlForm.add(btnLogin, "h 55!, gaptop 20");

        pnlMain.add(pnlForm);
        this.setContentPane(pnlMain);
        getRootPane().setDefaultButton(btnLogin);
        setBackground(new Color(0, 0, 0, 0));
    }

    private void handleLogin(ActionEvent e) {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        try {
            String result = bus.login(user, pass); 
            if ("SUCCESS".equals(result)) {
                MainFrame main = new MainFrame();
                main.applyPermissions();
                main.setVisible(true);
                if (slideshowPanel != null) slideshowPanel.stop();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ImageSlideshowPanel extends JPanel {
        private List<Image> images = new ArrayList<>();
        private int currentIndex = 0;
        private int nextIndex = 1;
        private int xOffset = 0;
        private Timer timer, slideTimer;

        public ImageSlideshowPanel(List<String> imagePaths, int delay) {
            setOpaque(false);
            for (String path : imagePaths) {
                File f = new File(path);
                if (f.exists()) images.add(new ImageIcon(f.getAbsolutePath()).getImage());
            }
            if (images.size() < 2) return;
            timer = new Timer(delay, e -> startSlide());
            timer.start();
        }

        private void startSlide() {
            if (slideTimer != null && slideTimer.isRunning()) return;
            xOffset = 0;
            nextIndex = (currentIndex + 1) % images.size();
            slideTimer = new Timer(15, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    xOffset -= 20; // Tốc độ trượt
                    if (Math.abs(xOffset) >= getWidth()) {
                        currentIndex = nextIndex;
                        xOffset = 0;
                        slideTimer.stop();
                    }
                    repaint();
                }
            });
            slideTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // CẮT BO GÓC BÊN TRÁI (Chỉ bo góc trái của ảnh)
            Shape clip = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setClip(clip);

            if (!images.isEmpty()) {
                drawScaledImage(g2, images.get(currentIndex), xOffset);
                if (xOffset != 0) {
                    drawScaledImage(g2, images.get(nextIndex), getWidth() + xOffset);
                }
            }
            g2.dispose();
        }

        private void drawScaledImage(Graphics2D g2, Image img, int x) {
            // Logic Center Crop: Đảm bảo ảnh luôn lấp đầy vùng hiển thị mà không bị méo
            int imgW = img.getWidth(null);
            int imgH = img.getHeight(null);
            double s = Math.max((double) getWidth() / imgW, (double) getHeight() / imgH);
            int w = (int) (s * imgW);
            int h = (int) (s * imgH);
            int y = (getHeight() - h) / 2;
            g2.drawImage(img, x, y, w, h, null);
        }

        public void stop() {
            if (timer != null) timer.stop();
            if (slideTimer != null) slideTimer.stop();
        }
    }
}