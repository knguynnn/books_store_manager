package Frontend.Compoent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Table extends JTable{
    public Table() {
        setRowHeight(35);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionBackground(new Color(232, 240, 254)); // Màu xanh rất nhẹ khi click
        setSelectionForeground(Color.BLACK);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Cấu hình Header
        JTableHeader header = getTableHeader();
        header.setBackground(Theme.SECONDARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));

        // Căn giữa nội dung
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, centerRenderer);
    }
}
