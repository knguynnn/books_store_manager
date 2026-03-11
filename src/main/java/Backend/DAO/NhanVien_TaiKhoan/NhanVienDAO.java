package Backend.DAO.NhanVien_TaiKhoan;

import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;
import Backend.DatabaseHelper; // <--- ĐÃ SỬA: Dùng đúng file kết nối của bạn
import java.sql.*;
import java.util.ArrayList;

public class NhanVienDAO {

    // Lấy tất cả nhân viên (JOIN 2 bảng: NhanVien và ChiTietNhanVien)
    public ArrayList<NhanVienDTO> getAll() {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        // Kết nối bảng NhanVien và ChiTietNhanVien thông qua MaNV
        String sql = "SELECT * FROM NhanVien nv JOIN ChiTietNhanVien ct ON nv.MaNV = ct.MaNV";
        
        try (Connection conn = DatabaseHelper.getConnection(); // <--- ĐÃ SỬA
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                // Bảng NhanVien
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoNV(rs.getString("HoNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                
                // Bảng ChiTietNhanVien
                nv.setNgSinh(rs.getDate("NgSinh"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setSoDienThoai(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getString("DiaChi")); // Lưu ý: Bảng Nhân Viên là DiaChi (khác KhachHang là DChi)
                nv.setCccd(rs.getString("CCCD"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setLuongCoBan(rs.getLong("LuongCoBan")); 

                list.add(nv);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi getAll NhanVien: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Thêm nhân viên (Dùng Transaction để thêm vào cả 2 bảng)
    public boolean insert(NhanVienDTO nv) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection(); // <--- ĐÃ SỬA
            conn.setAutoCommit(false); // Bắt đầu Transaction (Tắt tự động lưu)

            // 1. Thêm vào bảng NhanVien trước
            String sql1 = "INSERT INTO NhanVien (MaNV, HoNV, TenNV, ChucVu, TrangThai) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps1 = conn.prepareStatement(sql1)) {
                ps1.setString(1, nv.getMaNV());
                ps1.setString(2, nv.getHoNV());
                ps1.setString(3, nv.getTenNV());
                ps1.setString(4, nv.getChucVu());
                ps1.setBoolean(5, nv.isTrangThai());
                ps1.executeUpdate();
            }

            // 2. Thêm vào bảng ChiTietNhanVien sau
            String sql2 = "INSERT INTO ChiTietNhanVien (MaNV, NgSinh, GioiTinh, SDT, Email, DiaChi, CCCD, NgayVaoLam, LuongCoBan) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                ps2.setString(1, nv.getMaNV());
                ps2.setDate(2, nv.getNgSinh());
                ps2.setString(3, nv.getGioiTinh());
                ps2.setString(4, nv.getSoDienThoai());
                ps2.setString(5, nv.getEmail());
                ps2.setString(6, nv.getDiaChi());
                ps2.setString(7, nv.getCccd());
                ps2.setDate(8, nv.getNgayVaoLam());
                ps2.setLong(9, nv.getLuongCoBan());
                ps2.executeUpdate();
            }

            conn.commit(); // Nếu cả 2 lệnh trên OK thì mới Lưu thật sự
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi insert NhanVien: " + e.getMessage());
            e.printStackTrace();
            // Nếu có lỗi, hoàn tác lại (không thêm bảng nào cả)
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            // Đóng kết nối thủ công vì mình quản lý Transaction
            try { if (conn != null) conn.close(); } catch (SQLException ex) {}
        }
    }

    // Cập nhật nhân viên (Cũng dùng Transaction)
    public boolean update(NhanVienDTO nv) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection(); // <--- ĐÃ SỬA
            conn.setAutoCommit(false);

            // 1. Update bảng NhanVien
            String sql1 = "UPDATE NhanVien SET HoNV=?, TenNV=?, ChucVu=?, TrangThai=? WHERE MaNV=?";
            try (PreparedStatement ps1 = conn.prepareStatement(sql1)) {
                ps1.setString(1, nv.getHoNV());
                ps1.setString(2, nv.getTenNV());
                ps1.setString(3, nv.getChucVu());
                ps1.setBoolean(4, nv.isTrangThai());
                ps1.setString(5, nv.getMaNV());
                ps1.executeUpdate();
            }

            // 2. Update bảng ChiTietNhanVien
            String sql2 = "UPDATE ChiTietNhanVien SET NgSinh=?, GioiTinh=?, SDT=?, Email=?, DiaChi=?, CCCD=?, NgayVaoLam=?, LuongCoBan=? WHERE MaNV=?";
            try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                ps2.setDate(1, nv.getNgSinh());
                ps2.setString(2, nv.getGioiTinh());
                ps2.setString(3, nv.getSoDienThoai());
                ps2.setString(4, nv.getEmail());
                ps2.setString(5, nv.getDiaChi());
                ps2.setString(6, nv.getCccd());
                ps2.setDate(7, nv.getNgayVaoLam());
                ps2.setLong(8, nv.getLuongCoBan());
                ps2.setString(9, nv.getMaNV());
                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi update NhanVien: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) {}
        }
    }

    // Xóa nhân viên (Khóa tài khoản - Soft Delete)
    public boolean delete(String maNV) {
        String sql = "UPDATE NhanVien SET TrangThai = 0 WHERE MaNV = ?";
        try (Connection conn = DatabaseHelper.getConnection(); // <--- ĐÃ SỬA
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi delete NhanVien: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}