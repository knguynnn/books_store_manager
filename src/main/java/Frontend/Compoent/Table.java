package Frontend.Compoent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Table extends JTable {
    public Table() {
        setShowGrid(true);
        setGridColor(new Color(229, 231, 235));
        setRowHeight(35);
        setSelectionBackground(new Color(59, 130, 246, 50));
        setSelectionForeground(Color.BLACK);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        getTableHeader().setReorderingAllowed(false);
        
        // Center align cho các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}