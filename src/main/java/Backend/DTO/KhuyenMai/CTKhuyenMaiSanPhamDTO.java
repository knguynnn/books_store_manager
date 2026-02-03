package Backend.DTO.KhuyenMai;

public class CTKhuyenMaiSanPhamDTO {
    private String maKM;
    private String maSP;

    public CTKhuyenMaiSanPhamDTO() {}

    public CTKhuyenMaiSanPhamDTO(String maKM, String maSP) {
        this.maKM = maKM;
        this.maSP = maSP;
    }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
}