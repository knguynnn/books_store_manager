package Backend.BUS.NCC_NhapHang;

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

import Backend.DAO.NCC_NhapHang.CTPhieuNhapHangDAO;
import Backend.DAO.NCC_NhapHang.PhieuNhapHangDAO;
import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.NCC_NhapHang.CTPhieuNhapHangDTO;
import Backend.DTO.NCC_NhapHang.PhieuNhapHangDTO;

public class PhieuNhapHangBUSLogicTest {

    private PhieuNhapHangDAO phieuNhapDAO;
    private CTPhieuNhapHangDAO ctPhieuNhapDAO;
    private SanPhamDAO sanPhamDAO;
    private Connection conn;
    private PhieuNhapHangBUS bus;

    @Before
    public void setUp() throws Exception {
        phieuNhapDAO = org.mockito.Mockito.mock(PhieuNhapHangDAO.class);
        ctPhieuNhapDAO = org.mockito.Mockito.mock(CTPhieuNhapHangDAO.class);
        sanPhamDAO = org.mockito.Mockito.mock(SanPhamDAO.class);
        conn = org.mockito.Mockito.mock(Connection.class);

        doNothing().when(conn).setAutoCommit(anyBoolean());
        doNothing().when(conn).commit();
        doNothing().when(conn).rollback();
        doNothing().when(conn).close();

        bus = new PhieuNhapHangBUS(phieuNhapDAO, ctPhieuNhapDAO, sanPhamDAO) {
            @Override
            protected Connection openConnection() {
                return conn;
            }
        };
    }

    @Test
    public void taoPhieuNhap_shouldCommitWhenAllStepsSuccess() throws Exception {
        PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
        pn.setMaNV("NV001");
        pn.setMaNCC("NCC001");

        CTPhieuNhapHangDTO ct = new CTPhieuNhapHangDTO();
        ct.setMaSP("SP001");
        ct.setSoLuong(5);
        ct.setDonGiaNhap(2000);
        ArrayList<CTPhieuNhapHangDTO> ds = new ArrayList<>();
        ds.add(ct);

        when(phieuNhapDAO.generateId()).thenReturn("PN001");
        when(phieuNhapDAO.insert(any(PhieuNhapHangDTO.class), eq(conn))).thenReturn(true);
        when(ctPhieuNhapDAO.insert(any(CTPhieuNhapHangDTO.class), eq(conn))).thenReturn(true);
        when(sanPhamDAO.hoanSoLuongTon("SP001", 5, conn)).thenReturn(true);

        boolean ok = bus.taoPhieuNhap(pn, ds);

        assertTrue(ok);
        verify(conn).commit();
    }

    @Test
    public void suaPhieuNhap_shouldRollbackWhenOldStockDeductionFails() throws Exception {
        PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
        pn.setMaPhieuNhap("PN010");

        CTPhieuNhapHangDTO oldCt = new CTPhieuNhapHangDTO("PN010", "SP001", 10, 1000, 10000);
        ArrayList<CTPhieuNhapHangDTO> dsCu = new ArrayList<>();
        dsCu.add(oldCt);

        CTPhieuNhapHangDTO newCt = new CTPhieuNhapHangDTO("PN010", "SP002", 2, 2000, 4000);
        ArrayList<CTPhieuNhapHangDTO> dsMoi = new ArrayList<>();
        dsMoi.add(newCt);

        when(ctPhieuNhapDAO.getByMaPhieuNhap("PN010")).thenReturn(dsCu);
        when(sanPhamDAO.updateSoLuongTon("SP001", 10, conn)).thenReturn(false);

        boolean ok = bus.suaPhieuNhap(pn, dsMoi);

        assertFalse(ok);
        verify(conn).rollback();
        verify(ctPhieuNhapDAO, never()).deleteByMaPhieuNhap(any(String.class), eq(conn));
    }

    @Test
    public void xoaPhieuNhap_shouldCommitWhenStockAndDeleteSuccess() throws Exception {
        CTPhieuNhapHangDTO ct = new CTPhieuNhapHangDTO("PN011", "SP003", 3, 2000, 6000);
        ArrayList<CTPhieuNhapHangDTO> ds = new ArrayList<>();
        ds.add(ct);

        when(ctPhieuNhapDAO.getByMaPhieuNhap("PN011")).thenReturn(ds);
        when(sanPhamDAO.updateSoLuongTon("SP003", 3, conn)).thenReturn(true);
        when(phieuNhapDAO.delete("PN011", conn)).thenReturn(true);

        boolean ok = bus.xoaPhieuNhap("PN011");

        assertTrue(ok);
        verify(conn).commit();
    }
}
