// package Frontend.Compoent;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import java.io.FileOutputStream;
// import java.io.File;
// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;

// public class XuatExcel {
//     public static void xuat(JTable table) {
//         JFileChooser chooser = new JFileChooser();
//         chooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
//         if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//             String filePath = chooser.getSelectedFile().getAbsolutePath();
//             if (!filePath.toLowerCase().endsWith(".xlsx"))
//                 filePath += ".xlsx";

//             DefaultTableModel tblModel = (DefaultTableModel) table.getModel();

//             try (Workbook workbook = new XSSFWorkbook()) {
//                 Sheet sheet = workbook.createSheet("PhieuXuat");

//                 Row headerRow = sheet.createRow(0);
//                 for (int i = 0; i < tblModel.getColumnCount(); i++) {
//                     headerRow.createCell(i).setCellValue(tblModel.getColumnName(i));
//                 }

//                 for (int i = 0; i < tblModel.getRowCount(); i++) {
//                     Row row = sheet.createRow(i + 1);
//                     for (int j = 0; j < tblModel.getColumnCount(); j++) {
//                         row.createCell(j).setCellValue(tblModel.getValueAt(i, j).toString());
//                     }
//                 }

//                 try (FileOutputStream out = new FileOutputStream(new File(filePath))) {
//                     workbook.write(out);
//                     JOptionPane.showMessageDialog(null, "Xuất file Excel thành công!");
//                 }
//             } catch (Exception e) {
//                 e.printStackTrace();
//                 JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
//             }
//         }
//     }
// }