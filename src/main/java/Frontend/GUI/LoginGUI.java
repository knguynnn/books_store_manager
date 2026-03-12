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
        setSize(1100, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); 

        JPanel pnlRoot = new JPanel(new BorderLayout());
        pnlRoot.setBackground(new Color(191, 219, 237)); 
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(15, 25, 20, 25));

        // --- HEADER ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        JLabel lblTitle = new JLabel("Books_store_manager");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 150, 80)); 

        JButton btnClose = new JButton("X");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(new Color(220, 38, 38));
        btnClose.setPreferredSize(new Dimension(28, 28));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.addActionListener(e -> System.exit(0));

        pnlTop.add(lblTitle, BorderLayout.WEST);
        pnlTop.add(btnClose, BorderLayout.EAST);
        pnlRoot.add(pnlTop, BorderLayout.NORTH);

        // --- KHU VỰC CHÍNH ---
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setOpaque(false);

        JPanel pnlMain = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        pnlMain.setPreferredSize(new Dimension(1020, 530));
        pnlMain.setOpaque(false);

        // **SLIDESHOW ẢNH**
        List<String> imagePaths = new ArrayList<>();
        String baseDir = "src/main/resources/images/product/";
        imagePaths.add(baseDir + "congtruong.jpg"); 
        imagePaths.add(baseDir + "khuC.jpg");
        imagePaths.add(baseDir + "khuB.jpg");
        imagePaths.add(baseDir + "anhNen2.jpg");
        
        slideshowPanel = new ImageSlideshowPanel(imagePaths, 4000);
        slideshowPanel.setPreferredSize(new Dimension(650, 530)); 
        pnlMain.add(slideshowPanel, BorderLayout.WEST);

        // **FORM ĐĂNG NHẬP**
        JPanel pnlForm = new JPanel(new MigLayout("wrap, insets 40 40 40 40, fillx", "[fill]", "[]10[]30[]5[]20[]30[]"));
        pnlForm.setOpaque(false);

        JLabel lbLoginIcon = new JLabel(new FlatSVGIcon("images/icon/user-black.svg", 45, 45));
        lbLoginIcon.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lbLoginTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lbLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lbLoginTitle.setForeground(Theme.PRIMARY);

        pnlForm.add(lbLoginIcon, "center");
        pnlForm.add(lbLoginTitle, "center");

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.putClientProperty("JTextField.placeholderText", "Tên đăng nhập");
        txtUsername.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("images/icon/square-user-black.svg", 20, 20));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.putClientProperty("JTextField.placeholderText", "Mật khẩu");
        txtPassword.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("images/icon/shield-check-black.svg", 20, 20));
        txtPassword.putClientProperty("JPasswordField.revealIcon", true);

        pnlForm.add(new JLabel("Username"), "gapleft 5");
        pnlForm.add(txtUsername, "h 45!");
        pnlForm.add(new JLabel("Password"), "gapleft 5, gaptop 10");
        pnlForm.add(txtPassword, "h 45!");

        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(Theme.PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.putClientProperty("JButton.buttonType", "roundRect");
        
        btnLogin.addActionListener(this::handleLogin);
        getRootPane().setDefaultButton(btnLogin);

        pnlForm.add(btnLogin, "h 50!, gaptop 10");

        pnlMain.add(pnlForm, BorderLayout.CENTER);
        pnlCenter.add(pnlMain);
        pnlRoot.add(pnlCenter, BorderLayout.CENTER);

        add(pnlRoot);
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
            JOptionPane.showMessageDialog(this, "Lỗi kết nối Database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- CLASS SLIDESHOW VỚI HIỆU ỨNG CHẠY TỪ PHẢI SANG TRÁI ---
    private static class ImageSlideshowPanel extends JPanel {
        private List<Image> images = new ArrayList<>();
        private int currentIndex = 0;
        private int nextIndex = 1;
        private int xOffset = 0; // Vị trí dịch chuyển của ảnh
        private Timer timer;
        private Timer slideTimer;

        public ImageSlideshowPanel(List<String> imagePaths, int delay) {
            setOpaque(false);
            for (String path : imagePaths) {
                File f = new File(path);
                if (f.exists()) images.add(new ImageIcon(f.getAbsolutePath()).getImage());
            }
            if (images.size() < 2) return;

            // Timer chính chờ để bắt đầu trượt
            timer = new Timer(delay, e -> startSlide());
            timer.start();
        }

        private void startSlide() {
            if (slideTimer != null && slideTimer.isRunning()) return;
            xOffset = 0;
            nextIndex = (currentIndex + 1) % images.size();

            // slideTimer thực hiện việc dịch chuyển tọa độ x
            slideTimer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    xOffset -= 15; // Tốc độ trượt (có thể tăng/giảm số này)
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
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            // Bo góc mượt cho khu vực hiển thị ảnh
            Shape clip = new RoundRectangle2D.Double(0, 0, getWidth() + 50, getHeight(), 30, 30);
            g2.setClip(clip);

            if (!images.isEmpty()) {
                // Vẽ ảnh hiện tại
                g2.drawImage(images.get(currentIndex), xOffset, 0, getWidth(), getHeight(), this);
                
                // Vẽ ảnh tiếp theo đang tràn vào từ bên phải
                if (xOffset != 0) {
                    g2.drawImage(images.get(nextIndex), getWidth() + xOffset, 0, getWidth(), getHeight(), this);
                }
            }
            g2.dispose();
        }

        public void stop() {
            if (timer != null) timer.stop();
            if (slideTimer != null) slideTimer.stop();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}