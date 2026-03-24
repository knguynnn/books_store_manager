package Backend.DAO.KhachHang_BanHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.KhachHang_BanHang.HoaDonDTO;
import Backend.DatabaseHelper;

public class HoaDonDAO {

    // Lấy tất cả hóa đơn (mới nhất trước)
    public ArrayList<HoaDonDTO> getAll() {
        ArrayList<HoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon ORDER BY ThoiGian DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm hóa đơn theo mã
    public HoaDonDTO getById(String maHD) {
        String sql = "SELECT * FROM hoadon WHERE MaHD = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm hóa đơn theo cột và toán tử
    public ArrayList<HoaDonDTO> search(String column, String operator, String value) {
        ArrayList<HoaDonDTO> list = new ArrayList<>();
        String col;
        switch (column) {
            case "Mã HĐ": col = "MaHD"; break;
            case "Mã NV": col = "MaNV"; break;
            case "Mã KH": col = "MaKH"; break;
            case "Mã KM": col = "MaKM"; break;
            default: col = null; break;
        }

        String sql;
        if (col == null) {
            // Tìm tất cả cột
            sql = "SELECT * FROM hoadon WHERE MaHD LIKE ? OR MaNV LIKE ? OR MaKH LIKE ? OR IFNULL(MaKM,'') LIKE ? ORDER BY ThoiGian DESC";
        } else if ("Chứa".equals(operator)) {
            sql = "SELECT * FROM hoadon WHERE " + col + " LIKE ? ORDER BY ThoiGian DESC";
        } else if ("Bằng".equals(operator)) {
            sql = "SELECT * FROM hoadon WHERE " + col + " = ? ORDER BY ThoiGian DESC";
        } else {
            sql = "SELECT * FROM hoadon WHERE " + col + " " + mapOperator(operator) + " ? ORDER BY ThoiGian DESC";
        }

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (col == null) {
                String like = "%" + value + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, like);
            } else if ("Chứa".equals(operator)) {
                ps.setString(1, "%" + value + "%");
            } else {
                ps.setString(1, value);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm hóa đơn mới (dùng trong transaction)
    public boolean insert(HoaDonDTO hd, Connection conn) throws SQLException {
        String sql = "INSERT INTO hoadon (MaHD, ThoiGian, MaNV, MaKH, MaKM, TongTienTruocKM, GiamGiaHD, TongTienThanhToan) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setTimestamp(2, hd.getThoiGian());
            ps.setString(3, hd.getMaNV());
            ps.setString(4, hd.getMaKH());
            ps.setString(5, hd.getMaKM() == null || hd.getMaKM().isEmpty() ? null : hd.getMaKM());
            ps.setLong(6, hd.getTongTienTruocKM());
            ps.setLong(7, hd.getGiamGiaHD());
            ps.setLong(8, hd.getTongTienThanhToan());
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật hóa đơn
    public boolean update(HoaDonDTO hd, Connection conn) throws SQLException {
        String sql = "UPDATE hoadon SET MaNV=?, MaKH=?, MaKM=?, TongTienTruocKM=?, GiamGiaHD=?, TongTienThanhToan=? WHERE MaHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaNV());
            ps.setString(2, hd.getMaKH());
            ps.setString(3, hd.getMaKM() == null || hd.getMaKM().isEmpty() ? null : hd.getMaKM());
            ps.setLong(4, hd.getTongTienTruocKM());
            ps.setLong(5, hd.getGiamGiaHD());
            ps.setLong(6, hd.getTongTienThanhToan());
            ps.setString(7, hd.getMaHD());
            return ps.executeUpdate() > 0;
        }
    }

    // Xóa hóa đơn (cascade sẽ xóa cthoadon)
    public boolean delete(String maHD) {
        String sql = "DELETE FROM hoadon WHERE MaHD = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa hóa đơn trong transaction
    public boolean delete(String maHD, Connection conn) throws SQLException {
        String sql = "DELETE FROM hoadon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        }
    }

    // Sinh mã hóa đơn tự động: HD001, HD002...
    public String generateId() {
        String sql = "SELECT MaHD FROM hoadon ORDER BY MaHD DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String last = rs.getString("MaHD"); // HD001
                int num = Integer.parseInt(last.substring(2)) + 1;
                return String.format("HD%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HD001";
    }

    private HoaDonDTO mapRow(ResultSet rs) throws SQLException {
        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaHD(rs.getString("MaHD"));
        hd.setThoiGian(rs.getTimestamp("ThoiGian"));
        hd.setMaNV(rs.getString("MaNV"));
        hd.setMaKH(rs.getString("MaKH"));
        hd.setMaKM(rs.getString("MaKM"));
        hd.setTongTienTruocKM(rs.getLong("TongTienTruocKM"));
        hd.setGiamGiaHD(rs.getLong("GiamGiaHD"));
        hd.setTongTienThanhToan(rs.getLong("TongTienThanhToan"));
        return hd;
    }

    private String mapOperator(String op) {
        switch (op) {
            case ">": return ">";
            case ">=": return ">=";
            case "<": return "<";
            case "<=": return "<=";
            case "<>": return "<>";
            default: return "=";
        }
    }
}
