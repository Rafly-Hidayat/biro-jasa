package com.kelompok2.pajakform;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public final class PembayaranPajakStore extends BaseStore {

    public static final PembayaranPajakStore INSTANCE = new PembayaranPajakStore();

    public static final String TABLE = "pembayaran_pajak";
    public static final String PREFIX = "PJK";
    public static final String COL_KENDARAAN_ID = "kendaraanId";
    public static final String COL_JENIS = "jenisPerpanjangan";
    public static final String COL_MASA_STNK = "masaBerlakuStnk";
    public static final String COL_TGL_PENGURUSAN = "tanggalPengurusan";
    public static final String COL_BIAYA_PAJAK = "biayaPajak";
    public static final String COL_BIAYA_JASA = "biayaJasa";
    public static final String COL_TOTAL = "total";
    public static final String COL_STATUS = "status";
    public static final String COL_OPERATOR = "operator";

    private static final String[] COLS_DB = {
            "kendaraan_id", "jenis_perpanjangan", "masa_berlaku_stnk", "tanggal_pengurusan",
            "biaya_pajak", "biaya_jasa", "total", "status", "operator"
    };

    private PembayaranPajakStore() {
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] columnsDb() {
        return COLS_DB;
    }

    private static Date toDate(Timestamp t) {
        return t == null ? null : new Date(t.getTime());
    }

    private static double toDouble(Object v) {
        if (v == null) {
            return 0.0;
        }
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        }
        return Double.parseDouble(String.valueOf(v));
    }

    @Override
    protected LinkedHashMap<String, Object> mapRow(ResultSet rs) throws SQLException {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, rs.getString("id"));
        r.put(COL_KENDARAAN_ID, rs.getString("kendaraan_id"));
        r.put(COL_JENIS, rs.getString("jenis_perpanjangan"));
        r.put(COL_MASA_STNK, toDate(rs.getTimestamp("masa_berlaku_stnk")));
        r.put(COL_TGL_PENGURUSAN, toDate(rs.getTimestamp("tanggal_pengurusan")));
        BigDecimal bp = rs.getBigDecimal("biaya_pajak");
        r.put(COL_BIAYA_PAJAK, bp == null ? 0.0 : bp.doubleValue());
        BigDecimal bj = rs.getBigDecimal("biaya_jasa");
        r.put(COL_BIAYA_JASA, bj == null ? 0.0 : bj.doubleValue());
        BigDecimal tot = rs.getBigDecimal("total");
        r.put(COL_TOTAL, tot == null ? 0.0 : tot.doubleValue());
        r.put(COL_STATUS, rs.getString("status"));
        r.put(COL_OPERATOR, rs.getString("operator"));
        return r;
    }

    private static void setTimestamp(PreparedStatement ps, int i, Object v) throws SQLException {
        if (v == null) {
            ps.setNull(i, Types.TIMESTAMP);
            return;
        }
        if (v instanceof Date) {
            ps.setTimestamp(i, new Timestamp(((Date) v).getTime()));
        } else {
            ps.setNull(i, Types.TIMESTAMP);
        }
    }

    @Override
    protected void bindData(PreparedStatement ps, LinkedHashMap<String, Object> row, int firstIndex) throws SQLException {
        int i = firstIndex;
        ps.setString(i++, nullToEmpty(row.get(COL_KENDARAAN_ID)));
        ps.setString(i++, nullToEmpty(row.get(COL_JENIS)));
        setTimestamp(ps, i++, row.get(COL_MASA_STNK));
        setTimestamp(ps, i++, row.get(COL_TGL_PENGURUSAN));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_BIAYA_PAJAK))));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_BIAYA_JASA))));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_TOTAL))));
        ps.setString(i++, nullToEmpty(row.get(COL_STATUS)));
        ps.setString(i++, nullToEmpty(row.get(COL_OPERATOR)));
    }

    private static String nullToEmpty(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    public static LinkedHashMap<String, Object> newRow(
            String kendaraanId, String jenis,
            Date masaStnk, Date tglPengurusan,
            double biayaPajak, double biayaJasa,
            String status, String operator) {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, IdGenerator.next(PREFIX));
        r.put(COL_KENDARAAN_ID, kendaraanId);
        r.put(COL_JENIS, jenis);
        r.put(COL_MASA_STNK, masaStnk);
        r.put(COL_TGL_PENGURUSAN, tglPengurusan);
        r.put(COL_BIAYA_PAJAK, biayaPajak);
        r.put(COL_BIAYA_JASA, biayaJasa);
        r.put(COL_TOTAL, biayaPajak + biayaJasa);
        r.put(COL_STATUS, status);
        r.put(COL_OPERATOR, operator);
        return r;
    }

    private static Date dayOffset(int offsetDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, offsetDays);
        return c.getTime();
    }

    public void seedIfEmpty() {
        executeSeedIfEmpty(() -> {
            List<LinkedHashMap<String, Object>> kdrs = KendaraanStore.INSTANCE.all();
            if (kdrs.isEmpty()) {
                throw new StoreException("Seed pembayaran pajak membutuhkan data kendaraan.");
            }
            add(newRow(kId(kdrs, 0), "Tahunan", dayOffset(330), dayOffset(-3), 350_000, 150_000, "Selesai", "admin"));
            add(newRow(kId(kdrs, 1), "5 Tahunan", dayOffset(1750), dayOffset(-5), 1_200_000, 250_000, "Selesai", "admin"));
            add(newRow(kId(kdrs, 2), "Tahunan", dayOffset(360), dayOffset(-1), 400_000, 175_000, "Diproses", "admin2"));
            add(newRow(kId(kdrs, 3), "Tahunan", dayOffset(340), dayOffset(-2), 325_000, 125_000, "Diproses", "admin2"));
            add(newRow(kId(kdrs, 4), "5 Tahunan", dayOffset(1800), dayOffset(-7), 1_550_000, 350_000, "Selesai", "operator1"));
            add(newRow(kId(kdrs, 5), "Tahunan", dayOffset(355), dayOffset(0), 420_000, 200_000, "Draft", "operator1"));
            add(newRow(kId(kdrs, 6), "Tahunan", dayOffset(345), dayOffset(-9), 300_000, 150_000, "Selesai", "admin"));
            add(newRow(kId(kdrs, 7), "Tahunan", dayOffset(310), dayOffset(-12), 275_000, 140_000, "Selesai", "admin2"));
            add(newRow(kId(kdrs, 8), "5 Tahunan", dayOffset(1700), dayOffset(-15), 1_400_000, 300_000, "Selesai", "operator1"));
            add(newRow(kId(kdrs, 9), "Tahunan", dayOffset(360), dayOffset(-1), 380_000, 175_000, "Diproses", "admin"));
        });
    }

    private static String kId(List<LinkedHashMap<String, Object>> kdrs, int i) {
        return String.valueOf(kdrs.get(i % kdrs.size()).get(ID_KEY));
    }
}
