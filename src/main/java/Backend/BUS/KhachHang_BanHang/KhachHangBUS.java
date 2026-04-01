package Backend.BUS.KhachHang_BanHang;

import Backend.DatabaseHelper;
import Backend.DAO.KhachHang_BanHang.*;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.io.OutputStream;

public class KhachHangBUS {
    private ArrayList<KhachHangDTO> listKH;
    private KhachHangDAO dao = new KhachHangDAO();

    public KhachHangBUS() {
        refreshData();
    }

    public void refreshData() {
        listKH = dao.getAll();
    }

    public ArrayList<KhachHangDTO> getActiveOnly() {
        ArrayList<KhachHangDTO> activeList = new ArrayList<>();
        for (KhachHangDTO kh : listKH) {
            if (kh.isTrangThai()) {
                activeList.add(kh);
            }
        }
        return activeList;
    }

    public ArrayList<KhachHangDTO> getAll() {
        return listKH;
    }

    // Phương thức sinh mã khách hàng mới tự động
    public String generateNewMaKH() {
        if (listKH == null || listKH.isEmpty()) {
            return "KH001"; // Mã đầu tiên
        }
        
        // Tìm mã lớn nhất trong danh sách
        int maxNumber = 0;
        for (KhachHangDTO kh : listKH) {
            String maKH = kh.getMaKH();
            if (maKH.startsWith("KH")) {
                try {
                    int number = Integer.parseInt(maKH.substring(2));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu không phải định dạng số
                }
            }
        }
        
        // Tăng lên 1 và format thành 3 chữ số
        int newNumber = maxNumber + 1;
        return String.format("KH%03d", newNumber);
    }

    // Tìm kiếm trên ArrayList
    public ArrayList<KhachHangDTO> search(String keyword) {
        ArrayList<KhachHangDTO> result = new ArrayList<>();
        for (KhachHangDTO kh : listKH) {
            if (kh.getTenKH().toLowerCase().contains(keyword.toLowerCase()) || 
                kh.getMaKH().toLowerCase().contains(keyword.toLowerCase()) ||
                kh.getSoDienThoai().contains(keyword)) {
                result.add(kh);
            }
        }
        return result;
    }
    
    // Thêm khách hàng mới
    public boolean addKhachHang(KhachHangDTO kh) {
        boolean result = dao.insert(kh);
        if (result) refreshData();
        return result;
    }
    
    // Sửa thông tin khách hàng
    public boolean updateKhachHang(KhachHangDTO kh) {
        boolean result = dao.update(kh);
        if (result) refreshData();
        return result;
    }
    
    // Xóa khách hàng
    public boolean deleteKhachHang(String maKH) {
        boolean result = dao.delete(maKH);
        if (result) refreshData();
        return result;
    }
    
    // Kiểm tra mã KH đã tồn tại chưa
    public boolean isMaKHExist(String maKH) {
        for (KhachHangDTO kh : listKH) {
            if (kh.getMaKH().equals(maKH)) {
                return true;
            }
        }
        return false;
    }

    public boolean restoreKhachHang(String maKH) {
        String sql = "UPDATE khachhang SET TrangThai = 1 WHERE MaKH = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            boolean result = ps.executeUpdate() > 0;
            if (result) refreshData();
            return result;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khôi phục khách hàng: " + e.getMessage());
            return false;
        }
    }


    public void exportToExcel(OutputStream out) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("KhachHang");
            
            // Tạo header
            String[] titles = {"Mã KH", "Họ", "Tên", "Địa chỉ", "Email", "SĐT"};
            Row header = sheet.createRow(0);
            for(int i=0; i<titles.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(titles[i]);
                // Format tiêu đề đậm
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Đổ dữ liệu
            int rowIdx = 1;
            for (KhachHangDTO kh : getActiveOnly()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(kh.getMaKH());
                row.createCell(1).setCellValue(kh.getHoKH());
                row.createCell(2).setCellValue(kh.getTenKH());
                row.createCell(3).setCellValue(kh.getDiaChi());
                row.createCell(4).setCellValue(kh.getEmail());
                row.createCell(5).setCellValue(kh.getSoDienThoai());
            }
            workbook.write(out);
        }
    }

    // 2. Nhập file Excel 
    public void importFromExcel(InputStream is) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                KhachHangDTO kh = new KhachHangDTO();
                // Đọc dữ liệu từ các cột (0: Mã, 1: Họ, 2: Tên, 3: Địa chỉ, 4: Email, 5: SĐT)
                kh.setMaKH(row.getCell(0).getStringCellValue());
                kh.setHoKH(row.getCell(1).getStringCellValue());
                kh.setTenKH(row.getCell(2).getStringCellValue());
                kh.setDiaChi(row.getCell(3).getStringCellValue());
                kh.setEmail(row.getCell(4).getStringCellValue());
                kh.setSoDienThoai(row.getCell(5).getStringCellValue());
                kh.setTrangThai(true);
                
                addKhachHang(kh); // Lưu vào Database
            }
        }
    }
}
