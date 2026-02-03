package Backend.DTO.SanPham_DanhMuc;

public class TacGiaDTO {
    private String maTG;
    private String hoTG;
    private String tenTG;

    public TacGiaDTO() {}

    public TacGiaDTO(String maTG, String hoTG, String tenTG) {
        this.maTG = maTG;
        this.hoTG = hoTG;
        this.tenTG = tenTG;
    }

    public String getMaTG() { return maTG; }
    public void setMaTG(String maTG) { this.maTG = maTG; }

    public String getHoTG() { return hoTG; }
    public void setHoTG(String hoTG) { this.hoTG = hoTG; }

    public String getTenTG() { return tenTG; }
    public void setTenTG(String tenTG) { this.tenTG = tenTG; }
}