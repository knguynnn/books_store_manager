package Backend.DAO.NhanVien_TaiKhoan;

import Backend.DatabaseHelper;
import Backend.DTO.NhanVien_TaiKhoan.TaiKhoanDTO;
import java.sql.*;
import java.util.ArrayList;

public class TaiKhoanDAO {

    // 1. Hàm quan trọng nhất: Kiểm tra đăng nhập
    public TaiKhoanDTO checkLogin(String user, String pass) {
        String sql = "SELECT * FROM taikhoan WHERE TenDangNhap = ? AND MatKhau = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user);
            ps.setString(2, pass);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDTO(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Đăng nhập thất bại
    }

    // 2. Lấy danh sách tất cả tài khoản
    public ArrayList<TaiKhoanDTO> getAll() {
        ArrayList<TaiKhoanDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";
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

    // 3. Thêm tài khoản mới
    public boolean insert(TaiKhoanDTO tk) {
        String sql = "INSERT INTO taikhoan (TenDangNhap, MatKhau, MaNV, MaQuyen, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getMaNV());
            ps.setString(4, tk.getmaQuyen());
            ps.setBoolean(5, tk.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Cập nhật tài khoản (Đổi mật khẩu hoặc đổi quyền)
    public boolean update(TaiKhoanDTO tk) {
        String sql = "UPDATE taikhoan SET MatKhau = ?, MaQuyen = ?, TrangThai = ? WHERE TenDangNhap = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tk.getMatKhau());
            ps.setString(2, tk.getmaQuyen());
            ps.setBoolean(3, tk.isTrangThai());
            ps.setString(4, tk.getTenDangNhap());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Xóa tài khoản
    public boolean delete(String tenDangNhap) {
        String sql = "DELETE FROM taikhoan WHERE TenDangNhap = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tenDangNhap);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm map dữ liệu từ DB sang DTO
    private TaiKhoanDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        return new TaiKhoanDTO(
            rs.getString("TenDangNhap"),
            rs.getString("MatKhau"),
            rs.getString("MaNV"),
            rs.getString("MaQuyen"),
            rs.getBoolean("TrangThai")
        );
    }
}