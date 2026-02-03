package Backend.DTO.NhanVien_TaiKhoan;
import java.sql.Date;

public class ChiTietNhanVienDTO {
    private String maNV;
    private Date ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String cccd;
    private Date ngayVaoLam;
    private long luongCoBan;

    public ChiTietNhanVienDTO() {}

    public ChiTietNhanVienDTO(String maNV, Date ngaySinh, String gioiTinh, String soDienThoai, 
                              String email, String diaChi, String cccd, Date ngayVaoLam, long luongCoBan) {
        this.maNV = maNV;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.cccd = cccd;
        this.ngayVaoLam = ngayVaoLam;
        this.luongCoBan = luongCoBan;
    }

    // Getter và Setter
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public long getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(long luongCoBan) { this.luongCoBan = luongCoBan; }
}