package Backend.DTO.SanPham_DanhMuc;

public class NhaXuatBanDTO {
    private String maNXB;
    private String tenNXB;
    private String soDienThoai;
    private String diaChi;
    private String email;

    public NhaXuatBanDTO() {}

    public NhaXuatBanDTO(String maNXB, String tenNXB, String soDienThoai, String diaChi, String email) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.email = email;
    }

    public String getMaNXB() { return maNXB; }
    public void setMaNXB(String maNXB) { this.maNXB = maNXB; }

    public String getTenNXB() { return tenNXB; }
    public void setTenNXB(String tenNXB) { this.tenNXB = tenNXB; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}