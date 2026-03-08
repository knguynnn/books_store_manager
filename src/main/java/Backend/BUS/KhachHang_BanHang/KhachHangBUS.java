package Backend.BUS.KhachHang_BanHang;

import java.util.ArrayList;

import Backend.DAO.KhachHang_BanHang.KhachHangDAO;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;

public class KhachHangBUS {
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public ArrayList<KhachHangDTO> getAll() {
        return khachHangDAO.getAll();
    }

    public KhachHangDTO getById(String maKH) {
        return khachHangDAO.getById(maKH);
    }

    public ArrayList<KhachHangDTO> search(String keyword) {
        return khachHangDAO.search(keyword);
    }
}
