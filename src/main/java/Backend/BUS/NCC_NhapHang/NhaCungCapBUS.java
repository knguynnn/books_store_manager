package Backend.BUS.NCC_NhapHang;

import Backend.DAO.NCC_NhapHang.NhaCungCapDAO;
import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class NhaCungCapBUS {
    private NhaCungCapDAO dao = new NhaCungCapDAO();
    private ArrayList<NhaCungCapDTO> listNCC;

    public NhaCungCapBUS() {
        refreshData();
    }

    public void refreshData() {
        listNCC = dao.getAll();
    }

    public ArrayList<NhaCungCapDTO> getAll() {
        return listNCC;
    }

    public boolean validate(NhaCungCapDTO ncc) {
        if (ncc.getTenNCC().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên nhà cung cấp không được để trống!");
            return false;
        }
        if (!ncc.getSdt().matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "SĐT không hợp lệ! (10 số, bắt đầu bằng 0)");
            return false;
        }
        if (!ncc.getEmail().matches("^[a-z0-9](\\.?[a-z0-9])*@gmail\\.com$")) {
            JOptionPane.showMessageDialog(null, "Email phải có định dạng @gmail.com!");
            return false;
        }
        return true;
    }

    public boolean addNCC(NhaCungCapDTO ncc) {
        if (!validate(ncc)) return false;
        if (dao.insert(ncc)) {
            listNCC.add(ncc);
            return true;
        }
        return false;
    }

    public boolean updateNCC(NhaCungCapDTO ncc) {
        if (!validate(ncc)) return false;
        if (dao.update(ncc)) {
            for (int i = 0; i < listNCC.size(); i++) {
                if (listNCC.get(i).getMaNCC().equals(ncc.getMaNCC())) {
                    listNCC.set(i, ncc);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteNCC(String maNCC) {
        if (dao.delete(maNCC)) {
            listNCC.removeIf(ncc -> ncc.getMaNCC().equals(maNCC));
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể xóa nhà cung cấp này (có thể do dữ liệu liên quan)!");
        return false;
    }

    public ArrayList<NhaCungCapDTO> search(String keyword) {
        String lowerKey = keyword.toLowerCase().trim();
        if (lowerKey.isEmpty()) return listNCC;
        return listNCC.stream()
            .filter(ncc -> ncc.getMaNCC().toLowerCase().contains(lowerKey) || 
                           ncc.getTenNCC().toLowerCase().contains(lowerKey) ||
                           ncc.getSdt().contains(lowerKey) ||
                           ncc.getEmail().toLowerCase().contains(lowerKey))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public String generateNewMaNCC() {
        int maxId = 0;
        for (NhaCungCapDTO ncc : listNCC) {
            try {
                int id = Integer.parseInt(ncc.getMaNCC().replaceAll("[^0-9]", ""));
                if (id > maxId) maxId = id;
            } catch (NumberFormatException e) { }
        }
        return String.format("NCC%03d", maxId + 1);
    }

    public boolean exportExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(file)) {
            Sheet sheet = workbook.createSheet("DanhSachNCC");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã NCC", "Tên NCC", "SĐT", "Địa chỉ", "Email"};
            for (int i = 0; i < columns.length; i++) {
                headerRow.createCell(i).setCellValue(columns[i]);
            }
            for (int i = 0; i < listNCC.size(); i++) {
                Row row = sheet.createRow(i + 1);
                NhaCungCapDTO ncc = listNCC.get(i);
                row.createCell(0).setCellValue(ncc.getMaNCC());
                row.createCell(1).setCellValue(ncc.getTenNCC());
                row.createCell(2).setCellValue(ncc.getSdt());
                row.createCell(3).setCellValue(ncc.getDiaChi());
                row.createCell(4).setCellValue(ncc.getEmail());
            }
            workbook.write(fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int importExcel(File file) {
        int count = 0;
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                NhaCungCapDTO ncc = new NhaCungCapDTO(
                    generateNewMaNCC(),
                    getCellValue(row.getCell(1)),
                    getCellValue(row.getCell(2)),
                    getCellValue(row.getCell(3)),
                    getCellValue(row.getCell(4))
                );
                if (addNCC(ncc)) count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }
}
