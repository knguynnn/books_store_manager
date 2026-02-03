package Backend.DTO.NCC_NhapHang;

public class NhaCungCapDTO {
    private String maNCC;
    private String tenNCC;
    private String soDienThoai;
    private String diaChi;
    private String email;

    public NhaCungCapDTO() {}

    public NhaCungCapDTO(String maNCC, String tenNCC, String soDienThoai, String diaChi, String email) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.email = email;
    }

    // Getter và Setter
    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public String getTenNCC() { return tenNCC; }
    public void setTenNCC(String tenNCC) { this.tenNCC = tenNCC; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}