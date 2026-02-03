package Frontend.GUI.NhapHangGUI;
import javax.swing.*;
import java.awt.*;

public class NhapHangPanel extends JPanel {
    public NhapHangPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(new JLabel("MÀN HÌNH PHIẾU NHẬP & NHÀ CUNG CẤP ", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}