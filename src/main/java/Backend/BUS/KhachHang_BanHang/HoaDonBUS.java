package Backend.BUS.KhachHang_BanHang;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import Backend.DAO.KhachHang_BanHang.CTHoaDonDAO;
import Backend.DAO.KhachHang_BanHang.HoaDonDAO;
import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.KhachHang_BanHang.CTHoaDonDTO;
import Backend.DTO.KhachHang_BanHang.HoaDonDTO;
import Backend.DatabaseHelper;

public class HoaDonBUS {
    private final HoaDonDAO hoaDonDAO;
    private final CTHoaDonDAO ctHoaDonDAO;
    private final SanPhamDAO sanPhamDAO;

    public HoaDonBUS() {
        this(new HoaDonDAO(), new CTHoaDonDAO(), new SanPhamDAO());
    }

    public HoaDonBUS(HoaDonDAO hoaDonDAO, CTHoaDonDAO ctHoaDonDAO, SanPhamDAO sanPhamDAO) {
        this.hoaDonDAO = hoaDonDAO;
        this.ctHoaDonDAO = ctHoaDonDAO;
        this.sanPhamDAO = sanPhamDAO;
    }

    protected Connection openConnection() throws SQLException {
        return DatabaseHelper.getConnection();
    }

    public ArrayList<HoaDonDTO> getAll() {
        return hoaDonDAO.getAll();
    }

    public HoaDonDTO getById(String maHD) {
        return hoaDonDAO.getById(maHD);
    }

    public ArrayList<HoaDonDTO> search(String column, String operator, String value) {
        return hoaDonDAO.search(column, operator, value);
    }

    public String generateId() {
        return hoaDonDAO.generateId();
    }

    /**
     * Tạo hóa đơn mới + chi tiết + trừ tồn kho (trong transaction)
     */
    public boolean taoHoaDon(HoaDonDTO hd, ArrayList<CTHoaDonDTO> dsCTHD) {
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            // 1. Sinh mã + set thời gian
            hd.setThoiGian(new Timestamp(System.currentTimeMillis()));

            // 2. Tính tổng tiền từ chi tiết
            long tongTienTruocKM = 0;
            for (CTHoaDonDTO ct : dsCTHD) {
                ct.setThanhTien(ct.getDonGiaBan() * ct.getSoLuong());
                tongTienTruocKM += ct.getThanhTien();
            }
            hd.setTongTienTruocKM(tongTienTruocKM);
            hd.setTongTienThanhToan(tongTienTruocKM - hd.getGiamGiaHD());

            boolean inserted = false;
            for (int attempt = 0; attempt < 5; attempt++) {
                hd.setMaHD(hoaDonDAO.generateId());
                try {
                    inserted = hoaDonDAO.insert(hd, conn);
                    break;
                } catch (SQLException ex) {
                    if (!isDuplicateKey(ex)) {
                        throw ex;
                    }
                }
            }
            if (!inserted) {
                conn.rollback();
                return false;
            }

            for (CTHoaDonDTO ct : dsCTHD) {
                ct.setMaHD(hd.getMaHD());
            }

            // 3. Insert chi tiết + trừ tồn kho
            for (CTHoaDonDTO ct : dsCTHD) {
                ctHoaDonDAO.insert(ct, conn);
                boolean stockOk = sanPhamDAO.updateSoLuongTon(ct.getMaSP(), ct.getSoLuong(), conn);
                if (!stockOk) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }

    /**
     * Cập nhật hóa đơn: xóa chi tiết cũ → hoàn kho → insert chi tiết mới → trừ kho
     */
    public boolean suaHoaDon(HoaDonDTO hd, ArrayList<CTHoaDonDTO> dsCTHDMoi) {
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            // 1. Lấy chi tiết cũ → hoàn kho
            ArrayList<CTHoaDonDTO> dsCu = ctHoaDonDAO.getByMaHD(hd.getMaHD());
            for (CTHoaDonDTO ctCu : dsCu) {
                boolean stockOk = sanPhamDAO.hoanSoLuongTon(ctCu.getMaSP(), ctCu.getSoLuong(), conn);
                if (!stockOk) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Xóa chi tiết cũ
            ctHoaDonDAO.deleteByMaHD(hd.getMaHD(), conn);

            // 3. Tính tổng mới
            long tongTienTruocKM = 0;
            for (CTHoaDonDTO ct : dsCTHDMoi) {
                ct.setMaHD(hd.getMaHD());
                ct.setThanhTien(ct.getDonGiaBan() * ct.getSoLuong());
                tongTienTruocKM += ct.getThanhTien();
            }
            hd.setTongTienTruocKM(tongTienTruocKM);
            hd.setTongTienThanhToan(tongTienTruocKM - hd.getGiamGiaHD());

            // 4. Update hóa đơn
            hoaDonDAO.update(hd, conn);

            // 5. Insert chi tiết mới + trừ kho
            for (CTHoaDonDTO ct : dsCTHDMoi) {
                ctHoaDonDAO.insert(ct, conn);
                boolean stockOk = sanPhamDAO.updateSoLuongTon(ct.getMaSP(), ct.getSoLuong(), conn);
                if (!stockOk) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }

    /**
     * Xóa hóa đơn: hoàn kho trước → xóa HĐ (cascade xóa chi tiết)
     */
    public boolean xoaHoaDon(String maHD) {
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            // Hoàn kho
            ArrayList<CTHoaDonDTO> dsCT = ctHoaDonDAO.getByMaHD(maHD);
            for (CTHoaDonDTO ct : dsCT) {
                boolean stockOk = sanPhamDAO.hoanSoLuongTon(ct.getMaSP(), ct.getSoLuong(), conn);
                if (!stockOk) {
                    conn.rollback();
                    return false;
                }
            }

            // Xóa HĐ (cascade xóa cthoadon)
            hoaDonDAO.delete(maHD, conn);

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }

    private boolean isDuplicateKey(SQLException ex) {
        String state = ex.getSQLState();
        String message = ex.getMessage();
        return (state != null && state.startsWith("23"))
                || (message != null && message.toLowerCase().contains("duplicate"));
    }
}
