package Backend.BUS.KhachHang_BanHang;

import java.util.ArrayList;

import Backend.DAO.KhachHang_BanHang.CTHoaDonDAO;
import Backend.DTO.KhachHang_BanHang.CTHoaDonDTO;

public class CTHoaDonBUS {
    private final CTHoaDonDAO ctHoaDonDAO = new CTHoaDonDAO();

    public ArrayList<CTHoaDonDTO> getByMaHD(String maHD) {
        return ctHoaDonDAO.getByMaHD(maHD);
    }

    public String getTenSP(String maSP) {
        return ctHoaDonDAO.getTenSP(maSP);
    }
}
