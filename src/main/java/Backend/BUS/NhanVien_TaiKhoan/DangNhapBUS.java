package Backend.BUS.NhanVien_TaiKhoan;

import Backend.DAO.NhanVien_TaiKhoan.TaiKhoanDAO;
import Backend.DAO.NhanVien_TaiKhoan.PhanQuyenDAO;
import Backend.DTO.NhanVien_TaiKhoan.TaiKhoanDTO;
import Backend.BUS.SharedData;

public class DangNhapBUS {
    private TaiKhoanDAO tkDAO = new TaiKhoanDAO();
    private PhanQuyenDAO pqDAO = new PhanQuyenDAO();

    public String login(String user, String pass) {
        if (user.isEmpty() || pass.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin!";
        }

        TaiKhoanDTO tk = tkDAO.checkLogin(user, pass);

        if (tk == null) {
            return "Sai tên đăng nhập hoặc mật khẩu!";
        }

        if (!tk.isTrangThai()) {
            return "Tài khoản hiện đang bị khóa!";
        }

        // Đăng nhập thành công -> Nạp dữ liệu vào bộ nhớ tạm
        SharedData.taiKhoanHienTai = tk;
        SharedData.quyenHienTai = pqDAO.getById(tk.getmaQuyen());

        return "SUCCESS";
    }
}