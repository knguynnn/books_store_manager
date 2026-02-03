package Backend.DTO.NCC_NhapHang;
import java.sql.Timestamp;

public class PhieuNhapHangDTO {
    private String maPhieuNhap;
    private Timestamp ngayNhap;
    private String maNV;
    private String maNCC;
    private long tongTienNhap;

    public PhieuNhapHangDTO() {}

    public PhieuNhapHangDTO(String maPhieuNhap, Timestamp ngayNhap, String maNV, String maNCC, long tongTienNhap) {
        this.maPhieuNhap = maPhieuNhap;
        this.ngayNhap = ngayNhap;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.tongTienNhap = tongTienNhap;
    }

    // Getter và Setter
    public String getMaPhieuNhap() { return maPhieuNhap; }
    public void setMaPhieuNhap(String maPhieuNhap) { this.maPhieuNhap = maPhieuNhap; }

    public Timestamp getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(Timestamp ngayNhap) { this.ngayNhap = ngayNhap; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public long getTongTienNhap() { return tongTienNhap; }
    public void setTongTienNhap(long tongTienNhap) { this.tongTienNhap = tongTienNhap; }
}

// Cập nhật tồn kho: Khi một phiếu nhập được lưu thành công vào Database, 
// bạn phải viết thêm logic để cộng số lượng trong CTPhieuNhapHang vào slTonKho của bảng SanPham.

// Giá nhập: Thông thường giá nhập sẽ thấp hơn giá bán. Bạn có thể dùng donGiaNhap này để tính toán lợi nhuận sau này.