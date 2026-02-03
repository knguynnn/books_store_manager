package Backend.DTO.KhachHang_BanHang;

public class KhachHangDTO {
    private String maKH;
    private String hoKH;
    private String tenKH;
    private String diaChi;
    private String email;
    private String soDienThoai;
    private boolean trangThai;

    public KhachHangDTO() {}

    public KhachHangDTO(String maKH, String hoKH, String tenKH, String diaChi, String email, String soDienThoai, boolean trangThai) {
        this.maKH = maKH;
        this.hoKH = hoKH;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.trangThai = trangThai;
    }

    // Getter và Setter
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getHoKH() { return hoKH; }
    public void setHoKH(String hoKH) { this.hoKH = hoKH; }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
