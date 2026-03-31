package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.TheLoaiDAO;
import Backend.DTO.SanPham_DanhMuc.TheLoaiDTO;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class TheLoaiBUS {
    private ArrayList<TheLoaiDTO> listTL;
    private TheLoaiDAO tlDAO;

    public TheLoaiBUS() {
        tlDAO = new TheLoaiDAO();
        refreshData();
    }

    public void refreshData() {
        listTL = tlDAO.getAll();
    }

    public ArrayList<TheLoaiDTO> getAll() {
        return listTL;
    }

    /**
     * Thêm Thể Loại: Tự động gán mã TLxxx
     */
    public String validateAndAdd(TheLoaiDTO tl) {
        if (tl.getTenTL() == null || tl.getTenTL().trim().isEmpty()) {
            return "Tên thể loại không được để trống!";
        }

        // Ép buộc tạo mã mới từ hệ thống
        tl.setMaTL(generateNewMaTL());

        if (tlDAO.insert(tl)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể thêm thể loại!";
    }

    /**
     * Cập nhật Thể Loại
     */
    public String validateAndUpdate(TheLoaiDTO tl) {
        if (tl.getTenTL() == null || tl.getTenTL().trim().isEmpty()) {
            return "Tên thể loại không được để trống!";
        }

        if (tlDAO.update(tl)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể cập nhật thông tin!";
    }

    public boolean deleteTheLoai(String maTL) {
        if (tlDAO.delete(maTL)) {
            refreshData();
            return true;
        }
        return false;
    }

    /**
     * Sinh mã TL001, TL002... 
     * Cắt từ vị trí thứ 2 (sau chữ "TL")
     */
    public String generateNewMaTL() {
        if (listTL == null || listTL.isEmpty()) return "TL001";
        
        int max = 0;
        for (TheLoaiDTO tl : listTL) {
            String ma = tl.getMaTL();
            if (ma != null && ma.startsWith("TL")) {
                try {
                    int num = Integer.parseInt(ma.substring(2));
                    if (num > max) max = num;
                } catch (NumberFormatException e) {}
            }
        }
        return String.format("TL%03d", max + 1);
    }

    public ArrayList<TheLoaiDTO> search(String keyword) {
        ArrayList<TheLoaiDTO> result = new ArrayList<>();
        String key = keyword.toLowerCase().trim();
        for (TheLoaiDTO tl : listTL) {
            if (tl.getMaTL().toLowerCase().contains(key) || 
                tl.getTenTL().toLowerCase().contains(key)) {
                result.add(tl);
            }
        }
        return result;
    }

    public TheLoaiDTO getById(String id) {
        for (TheLoaiDTO tl : listTL) {
            if (tl.getMaTL().equalsIgnoreCase(id)) return tl;
        }
        return null;
    }

    public String getTenById(String id) {
        TheLoaiDTO tl = getById(id);
        return (tl != null) ? tl.getTenTL() : "Chưa xác định";
    }

    public String getMaByTen(String ten) {
        for (TheLoaiDTO tl : listTL) {
            if (tl.getTenTL().equalsIgnoreCase(ten)) return tl.getMaTL();
        }
        return "";
    }

    public String nhapExcel(String filePath) {
        int count = 0;
        int skip = 0;
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    skip++;
                    continue;
                }

                // Giả sử cột 0 là Tên thể loại trong file Excel
                Cell cellTen = row.getCell(0);
                if (cellTen == null) {
                    skip++;
                    continue;
                }

                String tenTL = cellTen.getStringCellValue().trim();
                if (tenTL.isEmpty()) {
                    skip++;
                    continue;
                }

                // Kiểm tra trùng lặp tên thể loại để tránh thêm mới dữ liệu đã có
                boolean isDuplicate = false;
                for (TheLoaiDTO existing : listTL) {
                    if (existing.getTenTL().equalsIgnoreCase(tenTL)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    TheLoaiDTO tl = new TheLoaiDTO();
                    tl.setTenTL(tenTL);
                    tl.setMaTL(generateNewMaTL());

                    if (tlDAO.insert(tl)) {
                        listTL.add(tl); // Thêm vào list tạm để hàm generate mã dòng sau chính xác
                        count++;
                    } else {
                        skip++;
                    }
                } else {
                    skip++;
                }
            }
            refreshData(); // Đồng bộ lại listTL từ Database
            return "SUCCESS:" + count + ":" + skip;
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}