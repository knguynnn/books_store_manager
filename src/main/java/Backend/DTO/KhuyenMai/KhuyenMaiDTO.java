package Backend.DTO.KhuyenMai;
import java.sql.Timestamp;

public class KhuyenMaiDTO {
    private String maKM;
    private String tenKM;
    private Timestamp ngayBatDau;
    private Timestamp ngayKetThuc;
    private String loaiKM; // GIAM_SP hoặc GIAM_HD
    private int giaTri;    // Phần trăm hoặc số tiền giảm
    private long dieuKien; // Ví dụ: Đơn hàng trên 500k mới áp dụng
    private boolean trangThai;

    public KhuyenMaiDTO() {}

    public KhuyenMaiDTO(String maKM, String tenKM, Timestamp ngayBatDau, Timestamp ngayKetThuc, 
                        String loaiKM, int giaTri, long dieuKien, boolean trangThai) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.loaiKM = loaiKM;
        this.giaTri = giaTri;
        this.dieuKien = dieuKien;
        this.trangThai = trangThai;
    }

    // Getter và Setter
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public Timestamp getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Timestamp ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Timestamp getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Timestamp ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getLoaiKM() { return loaiKM; }
    public void setLoaiKM(String loaiKM) { this.loaiKM = loaiKM; }

    public int getGiaTri() { return giaTri; }
    public void setGiaTri(int giaTri) { this.giaTri = giaTri; }

    public long getDieuKien() { return dieuKien; }
    public void setDieuKien(long dieuKien) { this.dieuKien = dieuKien; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}