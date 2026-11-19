package com.kelompok2.pajakform;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public final class MonitoringStore extends BaseStore {

    public static final MonitoringStore INSTANCE = new MonitoringStore();

    public static final String TABLE = "monitoring";
    public static final String PREFIX = "PRS";
    public static final String COL_NAMA = "nama";
    public static final String COL_KENDARAAN = "kendaraan";
    public static final String COL_STATUS = "statusProses";
    public static final String COL_TANGGAL = "tanggal";
    public static final String COL_OPERATOR = "operator";

    private static final String[] COLS_DB = { "nama", "kendaraan", "status_proses", "tanggal", "operator" };

    private MonitoringStore() {
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

    @Override
    protected LinkedHashMap<String, Object> mapRow(ResultSet rs) throws SQLException {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, rs.getString("id"));
        r.put(COL_NAMA, rs.getString("nama"));
        r.put(COL_KENDARAAN, rs.getString("kendaraan"));
        r.put(COL_STATUS, rs.getString("status_proses"));
        r.put(COL_TANGGAL, toDate(rs.getTimestamp("tanggal")));
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
        ps.setString(i++, nullToEmpty(row.get(COL_NAMA)));
        ps.setString(i++, nullToEmpty(row.get(COL_KENDARAAN)));
        ps.setString(i++, nullToEmpty(row.get(COL_STATUS)));
        setTimestamp(ps, i++, row.get(COL_TANGGAL));
        ps.setString(i++, nullToEmpty(row.get(COL_OPERATOR)));
    }

    private static String nullToEmpty(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    public static LinkedHashMap<String, Object> newRow(String nama, String kendaraan, String status, Date tanggal, String operator) {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, IdGenerator.next(PREFIX));
        r.put(COL_NAMA, nama);
        r.put(COL_KENDARAAN, kendaraan);
        r.put(COL_STATUS, status);
        r.put(COL_TANGGAL, tanggal);
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
            add(newRow("Budi Santoso", "B 1234 TM", "Diproses", dayOffset(-2), "admin"));
            add(newRow("Siti Aminah", "B 4321 MAS", "Selesai", dayOffset(-5), "admin"));
            add(newRow("Joko Widodo", "B 5678 ABC", "Diproses", dayOffset(-1), "admin2"));
            add(newRow("Rina Marlina", "B 2468 XYZ", "Selesai", dayOffset(-10), "admin2"));
            add(newRow("Andi Pratama", "D 1357 KLM", "Diproses", dayOffset(0), "operator1"));
            add(newRow("Dewi Lestari", "F 9988 PQR", "Dibatalkan", dayOffset(-7), "operator1"));
            add(newRow("Eko Saputra", "B 7766 STU", "Selesai", dayOffset(-15), "admin"));
            add(newRow("Fitri Handayani", "AB 4455 VW", "Diproses", dayOffset(-3), "admin2"));
            add(newRow("Galih Wijaya", "L 3322 OP", "Selesai", dayOffset(-20), "operator1"));
            add(newRow("Hana Permata", "DK 6611 IJ", "Diproses", dayOffset(-4), "admin"));
        });
    }
}
