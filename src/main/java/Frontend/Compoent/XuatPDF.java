// package Frontend.Compoent;

// import com.itextpdf.kernel.pdf.*;
// import com.itextpdf.layout.Document;
// import com.itextpdf.layout.element.Paragraph;
// import com.itextpdf.layout.element.Table;
// import com.itextpdf.kernel.font.PdfFont;
// import com.itextpdf.kernel.font.PdfFontFactory;
// import com.itextpdf.layout.properties.UnitValue;
// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;
// import java.io.File;

// public class XuatPDF {
//     public static void xuat(JTable jTable, String title) {
//         JFileChooser chooser = new JFileChooser();
//         chooser.setDialogTitle("Chọn nơi lưu file PDF");
//         if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//             String filePath = chooser.getSelectedFile().getAbsolutePath();
//             if (!filePath.toLowerCase().endsWith(".pdf"))
//                 filePath += ".pdf";

//             try {
//                 PdfWriter writer = new PdfWriter(filePath);
//                 PdfDocument pdf = new PdfDocument(writer);
//                 Document document = new Document(pdf);

//                 document.add(new Paragraph(title).setFontSize(20).setBold());

//                 DefaultTableModel model = (DefaultTableModel) jTable.getModel();
//                 Table table = new Table(UnitValue.createPointArray(new float[] { 30, 70, 100, 70, 100, 100 }));
//                 table.setWidth(UnitValue.createPercentValue(100));

//                 for (int i = 0; i < model.getColumnCount(); i++) {
//                     table.addHeaderCell(model.getColumnName(i));
//                 }

//                 for (int i = 0; i < model.getRowCount(); i++) {
//                     for (int j = 0; j < model.getColumnCount(); j++) {
//                         table.addCell(model.getValueAt(i, j).toString());
//                     }
//                 }

//                 document.add(table);
//                 document.close();
//                 JOptionPane.showMessageDialog(null, "Xuất PDF thành công!");
//             } catch (Exception e) {
//                 e.printStackTrace();
//                 JOptionPane.showMessageDialog(null, "Lỗi khi xuất PDF: " + e.getMessage());
//             }
//         }
//     }
// }