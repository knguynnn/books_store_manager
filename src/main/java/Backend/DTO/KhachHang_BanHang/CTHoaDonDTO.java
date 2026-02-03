package Backend.DTO.KhachHang_BanHang;

public class CTHoaDonDTO {
    private String maHD;
    private String maSP;
    private int soLuong;
    private long donGiaBan;
    private long thanhTien;

    public CTHoaDonDTO() {}

    public CTHoaDonDTO(String maHD, String maSP, int soLuong, long donGiaBan, long thanhTien) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGiaBan = donGiaBan;
        this.thanhTien = thanhTien;
    }

    // Getter và Setter
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public long getDonGiaBan() { return donGiaBan; }
    public void setDonGiaBan(long donGiaBan) { this.donGiaBan = donGiaBan; }

    public long getThanhTien() { return thanhTien; }
    public void setThanhTien(long thanhTien) { this.thanhTien = thanhTien; }
}