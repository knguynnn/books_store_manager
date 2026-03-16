package Backend.BUS.NCC_NhapHang;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import Backend.DAO.NCC_NhapHang.CTPhieuNhapHangDAO;
import Backend.DAO.NCC_NhapHang.PhieuNhapHangDAO;
import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.NCC_NhapHang.CTPhieuNhapHangDTO;
import Backend.DTO.NCC_NhapHang.PhieuNhapHangDTO;
import Backend.DatabaseHelper;

public class PhieuNhapHangBUS {
    private final PhieuNhapHangDAO phieuNhapDAO = new PhieuNhapHangDAO();
    private final CTPhieuNhapHangDAO ctPhieuNhapDAO = new CTPhieuNhapHangDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    public ArrayList<PhieuNhapHangDTO> getAll() {
        return phieuNhapDAO.getAll();
    }

    public PhieuNhapHangDTO getById(String maPhieuNhap) {
        return phieuNhapDAO.getById(maPhieuNhap);
    }

    public ArrayList<PhieuNhapHangDTO> search(String keyword) {
        return phieuNhapDAO.search(keyword);
    }

    public String generateId() {
        return phieuNhapDAO.generateId();
    }

    /**
     * Tạo phiếu nhập mới + chi tiết + cộng tồn kho (trong transaction)
     */
    public boolean taoPhieuNhap(PhieuNhapHangDTO pn, ArrayList<CTPhieuNhapHangDTO> dsCT) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            // 1. Sinh mã + set thời gian
            pn.setNgayNhap(new Timestamp(System.currentTimeMillis()));

            // 2. Tính tổng tiền từ chi tiết
            long tongTienNhap = 0;
            for (CTPhieuNhapHangDTO ct : dsCT) {
                ct.setThanhTien(ct.getDonGiaNhap() * ct.getSoLuong());
                tongTienNhap += ct.getThanhTien();
            }
            pn.setTongTienNhap(tongTienNhap);

            // 3. Insert phiếu nhập (retry nếu trùng mã)
            boolean inserted = false;
            for (int attempt = 0; attempt < 5; attempt++) {
                pn.setMaPhieuNhap(phieuNhapDAO.generateId());
                for (CTPhieuNhapHangDTO ct : dsCT) {
                    ct.setMaPhieuNhap(pn.getMaPhieuNhap());
                }
                try {
                    inserted = phieuNhapDAO.insert(pn, conn);
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

            // 4. Insert chi tiết + cộng tồn kho
            for (CTPhieuNhapHangDTO ct : dsCT) {
                ctPhieuNhapDAO.insert(ct, conn);
                boolean stockOk = sanPhamDAO.hoanSoLuongTon(ct.getMaSP(), ct.getSoLuong(), conn);
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
     * Sửa phiếu nhập: trừ kho cũ → xóa chi tiết cũ → insert chi tiết mới → cộng kho mới
     */
    public boolean suaPhieuNhap(PhieuNhapHangDTO pn, ArrayList<CTPhieuNhapHangDTO> dsCTMoi) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            // 1. Lấy chi tiết cũ → trừ kho (hoàn lại)
            ArrayList<CTPhieuNhapHangDTO> dsCu = ctPhieuNhapDAO.getByMaPhieuNhap(pn.getMaPhieuNhap());
            for (CTPhieuNhapHangDTO ctCu : dsCu) {
                boolean stockOk = sanPhamDAO.updateSoLuongTon(ctCu.getMaSP(), ctCu.getSoLuong(), conn);
                if (!stockOk) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Xóa chi tiết cũ
            ctPhieuNhapDAO.deleteByMaPhieuNhap(pn.getMaPhieuNhap(), conn);

            // 3. Tính tổng mới
            long tongTienNhap = 0;
            for (CTPhieuNhapHangDTO ct : dsCTMoi) {
                ct.setMaPhieuNhap(pn.getMaPhieuNhap());
                ct.setThanhTien(ct.getDonGiaNhap() * ct.getSoLuong());
                tongTienNhap += ct.getThanhTien();
            }
            pn.setTongTienNhap(tongTienNhap);

            // 4. Update phiếu nhập
            phieuNhapDAO.update(pn, conn);

            // 5. Insert chi tiết mới + cộng kho
            for (CTPhieuNhapHangDTO ct : dsCTMoi) {
                ctPhieuNhapDAO.insert(ct, conn);
                boolean stockOk = sanPhamDAO.hoanSoLuongTon(ct.getMaSP(), ct.getSoLuong(), conn);
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
     * Xóa phiếu nhập: trừ kho → xóa phiếu (cascade xóa chi tiết)
     */
    public boolean xoaPhieuNhap(String maPhieuNhap) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            // Trừ kho trước
            ArrayList<CTPhieuNhapHangDTO> dsCT = ctPhieuNhapDAO.getByMaPhieuNhap(maPhieuNhap);
            for (CTPhieuNhapHangDTO ct : dsCT) {
                boolean stockOk = sanPhamDAO.updateSoLuongTon(ct.getMaSP(), ct.getSoLuong(), conn);
                if (!stockOk) {
                    conn.rollback();
                    return false;
                }
            }

            // Xóa phiếu nhập (cascade xóa chi tiết)
            phieuNhapDAO.delete(maPhieuNhap, conn);

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
