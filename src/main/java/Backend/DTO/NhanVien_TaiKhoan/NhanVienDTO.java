package Backend.DTO.NhanVien_TaiKhoan;

import java.sql.Date;

public class NhanVienDTO {
    // --- BẢNG NHANVIEN ---
    private String maNV;
    private String hoNV;
    private String tenNV;
    private String chucVu;
    private boolean trangThai;

    // --- BẢNG CHITIETNHANVIEN ---
    private Date ngSinh;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String cccd;
    private Date ngayVaoLam;
    private long luongCoBan;

    // Constructor không tham số
    public NhanVienDTO() {
    }

    // Constructor đầy đủ tham số
    public NhanVienDTO(String maNV, String hoNV, String tenNV, String chucVu, boolean trangThai, 
                       Date ngSinh, String gioiTinh, String soDienThoai, String email, 
                       String diaChi, String cccd, Date ngayVaoLam, long luongCoBan) {
        this.maNV = maNV;
        this.hoNV = hoNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.trangThai = trangThai;
        this.ngSinh = ngSinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.cccd = cccd;
        this.ngayVaoLam = ngayVaoLam;
        this.luongCoBan = luongCoBan;
    }

    // --- GETTER & SETTER ---
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoNV() { return hoNV; }
    public void setHoNV(String hoNV) { this.hoNV = hoNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    public Date getNgSinh() { return ngSinh; }
    public void setNgSinh(Date ngSinh) { this.ngSinh = ngSinh; }

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