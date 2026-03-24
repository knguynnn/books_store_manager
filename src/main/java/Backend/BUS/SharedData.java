package Backend.BUS;

import Backend.DTO.NhanVien_TaiKhoan.TaiKhoanDTO;
import Backend.DTO.NhanVien_TaiKhoan.PhanQuyenDTO;

public class SharedData {
    // Biến static để có thể gọi ở bất cứ đâu trong dự án mà không cần khởi tạo lại
    public static TaiKhoanDTO taiKhoanHienTai;
    public static PhanQuyenDTO quyenHienTai;

    public static void logOut() {
        taiKhoanHienTai = null;
        quyenHienTai = null;
    }
}