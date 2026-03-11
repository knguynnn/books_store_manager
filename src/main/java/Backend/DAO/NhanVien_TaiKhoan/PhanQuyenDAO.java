package Backend.DAO.NhanVien_TaiKhoan;

import Backend.DatabaseHelper;
import Backend.DTO.NhanVien_TaiKhoan.PhanQuyenDTO;
import java.sql.*;
import java.util.ArrayList;

public class PhanQuyenDAO {

    // 1. Lấy danh sách tất cả các nhóm quyền (Dùng cho bảng quản lý phân quyền)
    public ArrayList<PhanQuyenDTO> getAll() {
        ArrayList<PhanQuyenDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM phanquyen";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy chi tiết một nhóm quyền theo Mã (Dùng khi đăng nhập để nạp quyền vào hệ thống)
    public PhanQuyenDTO getById(String maQuyen) {
        String sql = "SELECT * FROM phanquyen WHERE MaQuyen = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maQuyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDTO(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Thêm nhóm quyền mới
    public boolean insert(PhanQuyenDTO pq) {
        String sql = "INSERT INTO phanquyen VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, pq.getMaQuyen());
            ps.setString(2, pq.getTenQuyen());
            ps.setInt(3, pq.getQlKhachHang());
            ps.setInt(4, pq.getQlNhanVien());
            ps.setInt(5, pq.getQlSanPham());
            ps.setInt(6, pq.getQlNhapHang());
            ps.setInt(7, pq.getQlBanHang());
            ps.setInt(8, pq.getQlNhaCungCap());
            ps.setInt(9, pq.getQlThongKe());
            ps.setInt(10, pq.getQlPhanQuyen());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Cập nhật quyền hạn cho một nhóm
    public boolean update(PhanQuyenDTO pq) {
        String sql = "UPDATE phanquyen SET TenQuyen=?, QLKhachHang=?, QLNhanVien=?, QLSanPham=?, "
                   + "QLNhapHang=?, QLBanHang=?, QLNhaCungCap=?, QLThongKe=?, QLPhanQuyen=? "
                   + "WHERE MaQuyen=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, pq.getTenQuyen());
            ps.setInt(2, pq.getQlKhachHang());
            ps.setInt(3, pq.getQlNhanVien());
            ps.setInt(4, pq.getQlSanPham());
            ps.setInt(5, pq.getQlNhapHang());
            ps.setInt(6, pq.getQlBanHang());
            ps.setInt(7, pq.getQlNhaCungCap());
            ps.setInt(8, pq.getQlThongKe());
            ps.setInt(9, pq.getQlPhanQuyen());
            ps.setString(10, pq.getMaQuyen());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Xóa nhóm quyền
    public boolean delete(String maQuyen) {
        String sql = "DELETE FROM phanquyen WHERE MaQuyen = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maQuyen);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm phụ trợ để map dữ liệu từ ResultSet sang DTO (Giúp code ngắn gọn hơn)
    private PhanQuyenDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        return new PhanQuyenDTO(
            rs.getString("MaQuyen"),
            rs.getString("TenQuyen"),
            rs.getInt("QLKhachHang"),
            rs.getInt("QLNhanVien"),
            rs.getInt("QLSanPham"),
            rs.getInt("QLNhapHang"),
            rs.getInt("QLBanHang"),
            rs.getInt("QLNhaCungCap"),
            rs.getInt("QLThongKe"),
            rs.getInt("QLPhanQuyen")
        );
    }
}