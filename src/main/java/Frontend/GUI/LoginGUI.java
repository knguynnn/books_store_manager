package Frontend.GUI;

import Backend.BUS.NhanVien_TaiKhoan.DangNhapBUS;
import Backend.BUS.SharedData;
import Frontend.Compoent.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginGUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private DangNhapBUS bus = new DangNhapBUS();

    public LoginGUI() {
        initComponent();
    }

    private void initComponent() {
        setTitle("Đăng nhập hệ thống");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Theme.PRIMARY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel pnlForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(new JLabel("Tài khoản:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtUsername = new JTextField(15);
        txtUsername.setPreferredSize(new Dimension(200, 35));
        pnlForm.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnlForm.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtPassword = new JPasswordField(15);
        txtPassword.setPreferredSize(new Dimension(200, 35));
        pnlForm.add(txtPassword, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Nút đăng nhập
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(Theme.PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(150, 40));
        
        btnLogin.addActionListener(this::handleLogin);
        
        // Thêm sự kiện Enter để đăng nhập
        getRootPane().setDefaultButton(btnLogin);

        pnlBottom.add(btnLogin);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void handleLogin(ActionEvent e) {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        
        String result = bus.login(user, pass); // DangNhapBUS đã có sẵn logic lưu SharedData

        if (result.equals("SUCCESS")) {
            MainFrame main = new MainFrame(); // Khởi tạo giao diện chính
            main.applyPermissions();          // Lọc menu ngay lập tức
            main.setVisible(true);            // Hiển thị
            this.dispose();                   // Đóng form đăng nhập
        } else {
            JOptionPane.showMessageDialog(this, result);
        }
    }

    public static void main(String[] args) {
        // Hàm main để chạy thử form đăng nhập
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}