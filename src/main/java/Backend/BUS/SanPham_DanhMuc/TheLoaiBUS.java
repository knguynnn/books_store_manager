package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.TheLoaiDAO;
import Backend.DTO.SanPham_DanhMuc.TheLoaiDTO;
import java.util.ArrayList;

public class TheLoaiBUS {
    private ArrayList<TheLoaiDTO> listTL;
    private TheLoaiDAO tlDAO;

    public TheLoaiBUS() {
        tlDAO = new TheLoaiDAO();
        refreshData();
    }

    public void refreshData() {
        listTL = tlDAO.getAll();
    }

    public ArrayList<TheLoaiDTO> getAll() {
        return listTL;
    }

    public boolean addTheLoai(TheLoaiDTO tl) {
        if (tlDAO.insert(tl)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean updateTheLoai(TheLoaiDTO tl) {
        if (tlDAO.update(tl)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean deleteTheLoai(String maTL) {
        if (tlDAO.delete(maTL)) {
            refreshData();
            return true;
        }
        return false;
    }

    // Hàm tìm kiếm: Lọc danh sách theo Mã hoặc Tên thể loại (Không phân biệt hoa thường)
    public ArrayList<TheLoaiDTO> search(String keyword) {
        ArrayList<TheLoaiDTO> result = new ArrayList<>();
        String key = keyword.toLowerCase(); // Chuyển từ khóa về chữ thường để so sánh
        for (TheLoaiDTO tl : listTL) {
            if (tl.getMaTL().toLowerCase().contains(key) || 
                tl.getTenTL().toLowerCase().contains(key)) {
                result.add(tl);
            }
        }
        return result;
    }

    // Tự động tạo mã thể loại mới (Định dạng TL001, TL002...)
    public String generateNewMaTL() {
        if (listTL.isEmpty()) return "TL001"; 
        int max = 0;
        for (TheLoaiDTO tl : listTL) {
            try {
                int num = Integer.parseInt(tl.getMaTL().substring(2));
                if (num > max) max = num;
            } catch (Exception e) {}
        }
        return String.format("TL%03d", max + 1);
    }

    public String getTenById(String id) {
        for (TheLoaiDTO tl : listTL) {
            if (tl.getMaTL().equals(id)) return tl.getTenTL();
        }
        return "Chưa xác định";
    }

    public String getMaByTen(String ten) {
        for (TheLoaiDTO tl : listTL) {
            if (tl.getTenTL().equalsIgnoreCase(ten)) return tl.getMaTL();
        }
        return "";
    }
}