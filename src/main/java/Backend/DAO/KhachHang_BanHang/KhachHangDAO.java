package Backend.DAO.KhachHang_BanHang;

import Backend.DatabaseHelper;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;
import java.sql.*;
import java.util.ArrayList;

public class KhachHangDAO {
    
    // Lấy tất cả khách hàng còn hoạt động
    public ArrayList<KhachHangDTO> getAll() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang ORDER BY MaKH"; // Bỏ WHERE để lấy tất cả
        
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(
                    rs.getString("MaKH"),
                    rs.getString("HoKH"),
                    rs.getString("TenKH"),
                    rs.getString("DChi"),  // Lưu ý: Tên cột là DChi, không phải DiaChi
                    rs.getString("Email"),
                    rs.getString("SDT"),
                    rs.getBoolean("TrangThai")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Thêm phương thức lấy chỉ khách hàng đang hoạt động (nếu cần)
    public ArrayList<KhachHangDTO> getActiveOnly() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang WHERE TrangThai = 1 ORDER BY MaKH";
        
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(
                    rs.getString("MaKH"),
                    rs.getString("HoKH"),
                    rs.getString("TenKH"),
                    rs.getString("DChi"),
                    rs.getString("Email"),
                    rs.getString("SDT"),
                    rs.getBoolean("TrangThai")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getActiveOnly: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Thêm khách hàng mới
    public boolean insert(KhachHangDTO kh) {
        // Sửa: DiaChi → DChi
        String sql = "INSERT INTO khachhang (MaKH, HoKH, TenKH, DChi, Email, SDT, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getHoKH());
            ps.setString(3, kh.getTenKH());
            ps.setString(4, kh.getDiaChi());  // DTO vẫn là getDiaChi()
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getSoDienThoai());
            ps.setBoolean(7, kh.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm khách hàng: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật khách hàng
    public boolean update(KhachHangDTO kh) {
        // Sửa: DiaChi → DChi
        String sql = "UPDATE khachhang SET HoKH=?, TenKH=?, DChi=?, Email=?, SDT=?, TrangThai=? WHERE MaKH=?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kh.getHoKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getDiaChi());  // DTO vẫn là getDiaChi()
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getSoDienThoai());
            ps.setBoolean(6, kh.isTrangThai());
            ps.setString(7, kh.getMaKH());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật khách hàng: " + e.getMessage());
            return false;
        }
    }
    
    // Xóa mềm (chuyển TrangThai = 0)
    public boolean delete(String maKH) {
        String sql = "UPDATE khachhang SET TrangThai = 0 WHERE MaKH = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa khách hàng: " + e.getMessage());
            return false;
        }
    }
    
    // Tìm kiếm trong database
    public ArrayList<KhachHangDTO> searchInDB(String keyword) {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang WHERE TrangThai = 1 AND "
                   + "(TenKH LIKE ? OR MaKH LIKE ? OR SDT LIKE ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new KhachHangDTO(
                        rs.getString("MaKH"),
                        rs.getString("HoKH"),
                        rs.getString("TenKH"),
                        rs.getString("DChi"),  // ← SỬA: DiaChi → DChi
                        rs.getString("Email"),
                        rs.getString("SDT"),
                        rs.getBoolean("TrangThai")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm: " + e.getMessage());
        }
        return list;
    }
}