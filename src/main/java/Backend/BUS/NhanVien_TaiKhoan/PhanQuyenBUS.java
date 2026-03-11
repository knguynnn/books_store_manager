package Backend.BUS.NhanVien_TaiKhoan;

import Backend.DAO.NhanVien_TaiKhoan.PhanQuyenDAO;
import Backend.DTO.NhanVien_TaiKhoan.PhanQuyenDTO;
import java.util.ArrayList;

public class PhanQuyenBUS {
    private final PhanQuyenDAO pqDAO = new PhanQuyenDAO();
    private ArrayList<PhanQuyenDTO> listPhanQuyen;

    public PhanQuyenBUS() {
        loadData();
    }

    // 1. Tải dữ liệu từ database lên list
    public void loadData() {
        listPhanQuyen = pqDAO.getAll();
    }

    // 2. Lấy toàn bộ danh sách
    public ArrayList<PhanQuyenDTO> getAll() {
        if (listPhanQuyen == null) loadData();
        return listPhanQuyen;
    }

    // 3. Tìm kiếm nhóm quyền theo mã
    public PhanQuyenDTO getById(String maQuyen) {
        for (PhanQuyenDTO pq : listPhanQuyen) {
            if (pq.getMaQuyen().equalsIgnoreCase(maQuyen)) {
                return pq;
            }
        }
        return null;
    }

    // 4. Thêm nhóm quyền mới
    public String add(PhanQuyenDTO pq) {
        if (pq.getMaQuyen().trim().isEmpty() || pq.getTenQuyen().trim().isEmpty()) {
            return "Mã quyền và tên quyền không được để trống!";
        }
        
        if (getById(pq.getMaQuyen()) != null) {
            return "Mã quyền này đã tồn tại!";
        }

        if (pqDAO.insert(pq)) {
            listPhanQuyen.add(pq);
            return "Thêm nhóm quyền thành công!";
        }
        return "Thêm thất bại!";
    }

    // 5. Cập nhật nhóm quyền
    public String update(PhanQuyenDTO pq) {
        if (getById(pq.getMaQuyen()) == null) {
            return "Không tìm thấy mã quyền để cập nhật!";
        }

        if (pqDAO.update(pq)) {
            // Cập nhật lại list trong bộ nhớ
            for (int i = 0; i < listPhanQuyen.size(); i++) {
                if (listPhanQuyen.get(i).getMaQuyen().equals(pq.getMaQuyen())) {
                    listPhanQuyen.set(i, pq);
                    break;
                }
            }
            return "Cập nhật quyền thành công!";
        }
        return "Cập nhật thất bại!";
    }

    // 6. Xóa nhóm quyền
    public String delete(String maQuyen) {
        // Không cho phép xóa quyền ADMIN để tránh lỗi hệ thống
        if (maQuyen.equalsIgnoreCase("ADMIN")) {
            return "Không được phép xóa quyền Quản trị viên cao nhất!";
        }

        if (pqDAO.delete(maQuyen)) {
            listPhanQuyen.removeIf(pq -> pq.getMaQuyen().equals(maQuyen));
            return "Xóa nhóm quyền thành công!";
        }
        return "Xóa thất bại (Có thể quyền này đang được gán cho nhân viên)!";
    }

    // 7. Tìm kiếm theo tên (Dùng cho chức năng tìm kiếm trên giao diện)
    public ArrayList<PhanQuyenDTO> search(String keyword) {
        ArrayList<PhanQuyenDTO> result = new ArrayList<>();
        for (PhanQuyenDTO pq : listPhanQuyen) {
            if (pq.getTenQuyen().toLowerCase().contains(keyword.toLowerCase()) || 
                pq.getMaQuyen().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(pq);
            }
        }
        return result;
    }
}