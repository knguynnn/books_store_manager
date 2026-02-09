package Backend.BUS.KhachHang_BanHang;

import Backend.DatabaseHelper;
import Backend.DAO.KhachHang_BanHang.*;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

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
}