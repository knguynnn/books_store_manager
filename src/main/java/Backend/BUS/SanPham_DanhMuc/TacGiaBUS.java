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

    public boolean addTacGia(TacGiaDTO tg) {
        if (tgDAO.insert(tg)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean updateTacGia(TacGiaDTO tg) {
        if (tgDAO.update(tg)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean deleteTacGia(String maTG) {
        if (tgDAO.delete(maTG)) {
            refreshData();
            return true;
        }
        return false;
    }

    public ArrayList<TacGiaDTO> search(String keyword) {
        ArrayList<TacGiaDTO> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (TacGiaDTO tg : listTG) {
            if (tg.getTenTG().toLowerCase().contains(kw) || tg.getHoTG().toLowerCase().contains(kw) || tg.getMaTG().toLowerCase().contains(kw)) {
                result.add(tg);
            }
        }
        return result;
    }

    // Tự động tạo mã tác giả mới (TG001, TG002...) tương tự KhachHangBUS
    public String generateNewMaTG() {
        if (listTG.isEmpty()) return "TG001";
        int maxNumber = 0;
        for (TacGiaDTO tg : listTG) {
            if (tg.getMaTG().startsWith("TG")) {
                try {
                    // Cắt chuỗi lấy phần số sau "TG"
                    int number = Integer.parseInt(tg.getMaTG().substring(2));
                    if (number > maxNumber) maxNumber = number;
                } catch (NumberFormatException e) {}
            }
        }
        return String.format("TG%03d", maxNumber + 1);
    }

    // Hàm hỗ trợ lấy HỌ TÊN tác giả khi biết mã
    public String getTenById(String maTG) {
        for (TacGiaDTO tg : listTG) {
            if (tg.getMaTG().equals(maTG)) {
                // Kết hợp Họ và Tên, dùng trim() để tránh khoảng trắng thừa nếu một trong hai bị rỗng
                return (tg.getHoTG() + " " + tg.getTenTG()).trim();
            }
        }
        return "Chưa xác định";
    }

    public String getMaByTen(String ten) {
        for (TacGiaDTO tg : listTG) {
            if (tg.getTenTG().equalsIgnoreCase(ten)) return tg.getMaTG();
        }
        return "";
    }
}