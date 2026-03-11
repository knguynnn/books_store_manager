package Backend.DTO.NhanVien_TaiKhoan;

public class TaiKhoanDTO {
    private String tenDangNhap;
    private String matKhau;
    private String maNV;
    private String maQuyen;
    private boolean trangThai;

    public TaiKhoanDTO() {}

    public TaiKhoanDTO(String tenDangNhap, String matKhau, String maNV, String maQuyen, boolean trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
        this.maQuyen = maQuyen;
        this.trangThai = trangThai;
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getmaQuyen() { return maQuyen; }
    public void setmaQuyen(String maQuyen) { this.maQuyen = maQuyen; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}