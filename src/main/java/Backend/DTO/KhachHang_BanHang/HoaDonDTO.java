package Backend.DTO.KhachHang_BanHang;
import java.sql.Timestamp;

public class HoaDonDTO {
    private String maHD;
    private Timestamp thoiGian;
    private String maNV;
    private String maKH;
    private String maKM;
    private long tongTienTruocKM;
    private long giamGiaHD;
    private long tongTienThanhToan;

    public HoaDonDTO() {}

    public HoaDonDTO(String maHD, Timestamp thoiGian, String maNV, String maKH, String maKM, 
                     long tongTienTruocKM, long giamGiaHD, long tongTienThanhToan) {
        this.maHD = maHD;
        this.thoiGian = thoiGian;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
        this.tongTienTruocKM = tongTienTruocKM;
        this.giamGiaHD = giamGiaHD;
        this.tongTienThanhToan = tongTienThanhToan;
    }

    // Getter và Setter
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public Timestamp getThoiGian() { return thoiGian; }
    public void setThoiGian(Timestamp thoiGian) { this.thoiGian = thoiGian; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public long getTongTienTruocKM() { return tongTienTruocKM; }
    public void setTongTienTruocKM(long tongTienTruocKM) { this.tongTienTruocKM = tongTienTruocKM; }

    public long getGiamGiaHD() { return giamGiaHD; }
    public void setGiamGiaHD(long giamGiaHD) { this.giamGiaHD = giamGiaHD; }

    public long getTongTienThanhToan() { return tongTienThanhToan; }
    public void setTongTienThanhToan(long tongTienThanhToan) { this.tongTienThanhToan = tongTienThanhToan; }
}