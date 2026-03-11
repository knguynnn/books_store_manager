package Frontend.Compoent;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

public class XuatPDF {

    public static void xuat(JTable jTable, String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu file PDF");
        chooser.setSelectedFile(new File(title.replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".pdf"));
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        String filePath = chooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) filePath += ".pdf";

        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(bf, 16, Font.BOLD);
            Font headerFont = new Font(bf, 10, Font.BOLD, Color.WHITE);
            Font cellFont = new Font(bf, 9, Font.NORMAL);

            // Tiêu đề
            Paragraph p = new Paragraph(title, titleFont);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(15);
            document.add(p);

            // Bảng dữ liệu
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            int colCount = model.getColumnCount();
            PdfPTable table = new PdfPTable(colCount);
            table.setWidthPercentage(100);

            // Header
            for (int i = 0; i < colCount; i++) {
                PdfPCell cell = new PdfPCell(new Phrase(model.getColumnName(i), headerFont));
                cell.setBackgroundColor(new Color(41, 128, 185));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                table.addCell(cell);
            }

            // Data rows
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < colCount; j++) {
                    Object val = model.getValueAt(i, j);
                    PdfPCell cell = new PdfPCell(new Phrase(val != null ? val.toString() : "", cellFont));
                    cell.setPadding(4);
                    if (i % 2 == 1) cell.setBackgroundColor(new Color(235, 245, 251));
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(null, "Xuất PDF thành công!\n" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất PDF: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}