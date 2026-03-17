package Backend.BUS.KhachHang_BanHang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Backend.DAO.KhachHang_BanHang.CTHoaDonDAO;
import Backend.DAO.KhachHang_BanHang.HoaDonDAO;
import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.KhachHang_BanHang.CTHoaDonDTO;
import Backend.DTO.KhachHang_BanHang.HoaDonDTO;

public class HoaDonBUSLogicTest {

    private HoaDonDAO hoaDonDAO;
    private CTHoaDonDAO ctHoaDonDAO;
    private SanPhamDAO sanPhamDAO;
    private Connection conn;
    private HoaDonBUS bus;

    @Before
    public void setUp() throws Exception {
        hoaDonDAO = org.mockito.Mockito.mock(HoaDonDAO.class);
        ctHoaDonDAO = org.mockito.Mockito.mock(CTHoaDonDAO.class);
        sanPhamDAO = org.mockito.Mockito.mock(SanPhamDAO.class);
        conn = org.mockito.Mockito.mock(Connection.class);

        doNothing().when(conn).setAutoCommit(anyBoolean());
        doNothing().when(conn).commit();
        doNothing().when(conn).rollback();
        doNothing().when(conn).close();

        bus = new HoaDonBUS(hoaDonDAO, ctHoaDonDAO, sanPhamDAO) {
            @Override
            protected Connection openConnection() {
                return conn;
            }
        };
    }

    @Test
    public void taoHoaDon_shouldCommitAndSetHeaderTotals() throws Exception {
        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaNV("NV001");
        hd.setMaKH("KH001");
        hd.setGiamGiaHD(1000);

        CTHoaDonDTO ct = new CTHoaDonDTO();
        ct.setMaSP("SP001");
        ct.setSoLuong(2);
        ct.setDonGiaBan(10000);
        ArrayList<CTHoaDonDTO> ds = new ArrayList<>();
        ds.add(ct);

        when(hoaDonDAO.generateId()).thenReturn("HD001");
        when(hoaDonDAO.insert(any(HoaDonDTO.class), eq(conn))).thenReturn(true);
        when(ctHoaDonDAO.insert(any(CTHoaDonDTO.class), eq(conn))).thenReturn(true);
        when(sanPhamDAO.updateSoLuongTon("SP001", 2, conn)).thenReturn(true);

        boolean ok = bus.taoHoaDon(hd, ds);

        assertTrue(ok);
        assertEquals("HD001", hd.getMaHD());
        assertEquals(20000, hd.getTongTienTruocKM());
        assertEquals(19000, hd.getTongTienThanhToan());
        verify(conn).commit();
    }

    @Test
    public void suaHoaDon_shouldRollbackWhenOldStockRestoreFails() throws Exception {
        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaHD("HD001");
        hd.setGiamGiaHD(0);

        CTHoaDonDTO oldCt = new CTHoaDonDTO("HD001", "SP001", 3, 10000, 30000);
        ArrayList<CTHoaDonDTO> dsCu = new ArrayList<>();
        dsCu.add(oldCt);

        CTHoaDonDTO newCt = new CTHoaDonDTO("HD001", "SP002", 1, 5000, 5000);
        ArrayList<CTHoaDonDTO> dsMoi = new ArrayList<>();
        dsMoi.add(newCt);

        when(ctHoaDonDAO.getByMaHD("HD001")).thenReturn(dsCu);
        when(sanPhamDAO.hoanSoLuongTon("SP001", 3, conn)).thenReturn(false);

        boolean ok = bus.suaHoaDon(hd, dsMoi);

        assertFalse(ok);
        verify(conn).rollback();
        verify(ctHoaDonDAO, never()).deleteByMaHD(any(String.class), eq(conn));
    }

    @Test
    public void xoaHoaDon_shouldCommitWhenRestoreAndDeleteSuccess() throws Exception {
        CTHoaDonDTO ct = new CTHoaDonDTO("HD009", "SP008", 4, 1000, 4000);
        ArrayList<CTHoaDonDTO> ds = new ArrayList<>();
        ds.add(ct);

        when(ctHoaDonDAO.getByMaHD("HD009")).thenReturn(ds);
        when(sanPhamDAO.hoanSoLuongTon("SP008", 4, conn)).thenReturn(true);
        when(hoaDonDAO.delete("HD009", conn)).thenReturn(true);

        boolean ok = bus.xoaHoaDon("HD009");

        assertTrue(ok);
        verify(conn).commit();
    }
}
