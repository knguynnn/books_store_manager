package Backend.BUS.ThongKe_PhanQuyen;

import Backend.DAO.ThongKe_PhanQuyen.ThongKeDAO;
import Backend.DTO.ThongKe_PhanQuyen.ThongKeDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThongKeBUS {
    private ThongKeDAO tkDAO;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ThongKeBUS() {
        tkDAO = new ThongKeDAO();
    }

    // 1. Lấy dữ liệu tổng quan cho 5 thẻ Dashboard
    public ThongKeDTO.TongQuan getTongQuan(Date tuNgay, Date denNgay) {
        // Chuyển đổi Date sang String định dạng yyyy-MM-dd để DAO truy vấn SQL
        String strTuNgay = sdf.format(tuNgay);
        String strDenNgay = sdf.format(denNgay);
        
        return tkDAO.getTongQuan(strTuNgay, strDenNgay);
    }

    // 2. Lấy chi tiết theo Quý dựa trên lựa chọn của ComboBox
    public ArrayList<ThongKeDTO.ChiTiet> getChiTietTheoQuy(int loaiIndex, int nam) {
        /*
            loaiIndex:
            0 - Nhân viên
            1 - Khách hàng
            2 - Sản phẩm
        */
        switch (loaiIndex) {
            case 0:
                return tkDAO.getDoanhThuNhanVienTheoQuy(nam);
            case 1:
                return tkDAO.getDoanhThuKhachHangTheoQuy(nam);
            case 2:
                return tkDAO.getDoanhThuSanPhamTheoQuy(nam);
            default:
                return new ArrayList<>();
        }
    }

    // 3. Hàm hỗ trợ lấy năm hiện tại để mặc định cho bảng
    public int getNamHienTai() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
    // 4. Hàm hỗ trợ định dạng tiền tệ (Ví dụ: 1000000 -> 1.000.000 VNĐ)
    public String formatMoney(double money) {
        return String.format("%,.0f", money) + " VNĐ";
    }
}