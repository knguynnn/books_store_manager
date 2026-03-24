package Frontend.Compoent;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;

public class XuatExcel {

    public static void xuat(JTable table) {
        xuat(table, "Dữ liệu");
    }

    public static void xuat(JTable table, String sheetName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
        chooser.setSelectedFile(new File(sheetName.replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".xlsx"));
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        String filePath = chooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".xlsx")) filePath += ".xlsx";

        DefaultTableModel tblModel = (DefaultTableModel) table.getModel();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // Style header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Style data
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < tblModel.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(tblModel.getColumnName(i));
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            for (int i = 0; i < tblModel.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < tblModel.getColumnCount(); j++) {
                    Cell cell = row.createCell(j);
                    Object val = tblModel.getValueAt(i, j);
                    cell.setCellValue(val != null ? val.toString() : "");
                    cell.setCellStyle(dataStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < tblModel.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(new File(filePath))) {
                workbook.write(out);
                JOptionPane.showMessageDialog(null, "Xuất file Excel thành công!\n" + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}