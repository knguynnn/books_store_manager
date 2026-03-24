package Backend.BUS.NCC_NhapHang;

import java.util.ArrayList;

import Backend.DAO.NCC_NhapHang.CTPhieuNhapHangDAO;
import Backend.DTO.NCC_NhapHang.CTPhieuNhapHangDTO;

public class CTPhieuNhapHangBUS {
    private final CTPhieuNhapHangDAO dao = new CTPhieuNhapHangDAO();

    public ArrayList<CTPhieuNhapHangDTO> getByMaPhieuNhap(String maPhieuNhap) {
        return dao.getByMaPhieuNhap(maPhieuNhap);
    }

    /**
     * Trả về đơn giá nhập mới nhất của sản phẩm (từ phiếu nhập gần nhất).
     * Trả về 0 nếu chưa có phiếu nhập nào.
     */
    public long getLatestGiaNhapBySP(String maSP) {
        return dao.getLatestGiaNhapBySP(maSP);
    }
}