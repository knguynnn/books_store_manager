package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.TacGiaDAO;
import Backend.DTO.SanPham_DanhMuc.TacGiaDTO;
import java.util.ArrayList;

public class TacGiaBUS {
    private ArrayList<TacGiaDTO> listTG;
    private TacGiaDAO tgDAO;

    public TacGiaBUS() {
        tgDAO = new TacGiaDAO();
        refreshData();
    }

    public void refreshData() {
        listTG = tgDAO.getAll();
    }

    public ArrayList<TacGiaDTO> getAll() {
        return listTG;
    }

    /**
     * Logic Thêm Tác Giả: Tự động gán mã mới nhất
     */
    public String validateAndAdd(TacGiaDTO tg) {
        if (tg.getHoTG() == null || tg.getHoTG().trim().isEmpty()) return "Họ tác giả không được để trống!";
        if (tg.getTenTG() == null || tg.getTenTG().trim().isEmpty()) return "Tên tác giả không được để trống!";

        tg.setMaTG(generateNewMaTG());

        if (tgDAO.insert(tg)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể thêm tác giả vào Cơ sở dữ liệu!";
    }

    /**
     * Logic Cập Nhật: Giữ nguyên mã, chỉ sửa thông tin
     */
    public String validateAndUpdate(TacGiaDTO tg) {
        if (tg.getHoTG() == null || tg.getHoTG().trim().isEmpty()) return "Họ tác giả không được để trống!";
        if (tg.getTenTG() == null || tg.getTenTG().trim().isEmpty()) return "Tên tác giả không được để trống!";

        if (tgDAO.update(tg)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể cập nhật thông tin!";
    }

    
    public boolean deleteTacGia(String maTG) {
        if (tgDAO.delete(maTG)) {
            refreshData();
            return true;
        }
        return false;
    }

    /**
     * Tự động sinh mã TG001, TG002... dựa trên danh sách hiện tại
     */
    public String generateNewMaTG() {
        if (listTG == null || listTG.isEmpty()) return "TG001";
        
        int maxNumber = 0;
        for (TacGiaDTO tg : listTG) {
            String ma = tg.getMaTG();
            if (ma != null && ma.startsWith("TG")) {
                try {
                    int number = Integer.parseInt(ma.substring(2));
                    if (number > maxNumber) maxNumber = number;
                } catch (NumberFormatException e) {
                    // Bỏ qua các mã không đúng định dạng TGxxx
                }
            }
        }
        return String.format("TG%03d", maxNumber + 1);
    }

    /**
     * Tìm kiếm đa năng: Theo Mã, Tên hoặc Họ Tên đầy đủ
     */
    public ArrayList<TacGiaDTO> search(String keyword) {
        ArrayList<TacGiaDTO> result = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        
        for (TacGiaDTO tg : listTG) {
            String hoTen = (tg.getHoTG() + " " + tg.getTenTG()).toLowerCase();
            if (tg.getMaTG().toLowerCase().contains(kw) || 
                tg.getHoTG().toLowerCase().contains(kw) || 
                tg.getTenTG().toLowerCase().contains(kw) ||
                hoTen.contains(kw)) {
                result.add(tg);
            }
        }
        return result;
    }

    public TacGiaDTO getById(String maTG) {
        for (TacGiaDTO tg : listTG) {
            if (tg.getMaTG().equalsIgnoreCase(maTG)) return tg;
        }
        return null;
    }

    public String getTenById(String maTG) {
        TacGiaDTO tg = getById(maTG);
        return (tg != null) ? (tg.getHoTG() + " " + tg.getTenTG()).trim() : "Chưa xác định";
    }




}