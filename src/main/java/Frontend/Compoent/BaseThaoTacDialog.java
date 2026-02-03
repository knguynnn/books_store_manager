package Frontend.Compoent;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public abstract class BaseThaoTacDialog extends JDialog{
    protected JPanel pnlContent;
    protected CustomButton btnHuy, btnXacNhan;

    public BaseThaoTacDialog(String title, int width, int height) {
        setTitle(title);
        setModal(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getRootPane().putClientProperty("FlatLaf.style", "arc: 20");

        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(Theme.PRIMARY);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        pnlContent = new JPanel(new MigLayout("wrap 2, fillx, insets 25", "[110!]10[grow]", "[]10[]"));
        pnlContent.setBackground(Color.WHITE);
        add(pnlContent, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnHuy = new CustomButton("HỦY", Theme.DANGER_COLOR);
        btnXacNhan = new CustomButton("LƯU THAY ĐỔI", Theme.ACCENT_COLOR);
        pnlFooter.add(btnHuy);
        pnlFooter.add(btnXacNhan);
        add(pnlFooter, BorderLayout.SOUTH);

        btnXacNhan.addActionListener(e -> logicXacNhan());
        btnHuy.addActionListener(e -> dispose());

        // Gọi initForm để lớp con vẽ giao diện
        initForm();
    }

    // Hàm để các lớp con (Thêm/Sửa) tự vẽ giao diện riêng của chúng
    protected abstract void initForm();
    protected abstract void logicXacNhan();
}
