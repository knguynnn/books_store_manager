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

    /**
     * Thêm Thể Loại: Tự động gán mã TLxxx
     */
    public String validateAndAdd(TheLoaiDTO tl) {
        if (tl.getTenTL() == null || tl.getTenTL().trim().isEmpty()) {
            return "Tên thể loại không được để trống!";
        }

        // Ép buộc tạo mã mới từ hệ thống
        tl.setMaTL(generateNewMaTL());

        if (tlDAO.insert(tl)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể thêm thể loại!";
    }

    /**
     * Cập nhật Thể Loại
     */
    public String validateAndUpdate(TheLoaiDTO tl) {
        if (tl.getTenTL() == null || tl.getTenTL().trim().isEmpty()) {
            return "Tên thể loại không được để trống!";
        }

        if (tlDAO.update(tl)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể cập nhật thông tin!";
    }

    public boolean deleteTheLoai(String maTL) {
        if (tlDAO.delete(maTL)) {
            refreshData();
            return true;
        }
        return false;
    }

    /**
     * Sinh mã TL001, TL002... 
     * Cắt từ vị trí thứ 2 (sau chữ "TL")
     */
    public String generateNewMaTL() {
        if (listTL == null || listTL.isEmpty()) return "TL001";
        
        int max = 0;
        for (TheLoaiDTO tl : listTL) {
            String ma = tl.getMaTL();
            if (ma != null && ma.startsWith("TL")) {
                try {
                    int num = Integer.parseInt(ma.substring(2));
                    if (num > max) max = num;
                } catch (NumberFormatException e) {}
            }
        }
        return String.format("TL%03d", max + 1);
    }

    public ArrayList<TheLoaiDTO> search(String keyword) {
        ArrayList<TheLoaiDTO> result = new ArrayList<>();
        String key = keyword.toLowerCase().trim();
        for (TheLoaiDTO tl : listTL) {
            if (tl.getMaTL().toLowerCase().contains(key) || 
                tl.getTenTL().toLowerCase().contains(key)) {
                result.add(tl);
            }
        }
        return result;
    }

    public TheLoaiDTO getById(String id) {
        for (TheLoaiDTO tl : listTL) {
            if (tl.getMaTL().equalsIgnoreCase(id)) return tl;
        }
        return null;
    }

    public String getTenById(String id) {
        TheLoaiDTO tl = getById(id);
        return (tl != null) ? tl.getTenTL() : "Chưa xác định";
    }

    public String getMaByTen(String ten) {
        for (TheLoaiDTO tl : listTL) {
            if (tl.getTenTL().equalsIgnoreCase(ten)) return tl.getMaTL();
        }
        return "";
    }
}