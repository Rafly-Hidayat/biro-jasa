package com.kelompok2.pajakform;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public final class PelangganStore extends BaseStore {

    public static final PelangganStore INSTANCE = new PelangganStore();

    public static final String TABLE = "pelanggan";
    public static final String PREFIX = "PLG";
    public static final String COL_NAMA = "nama";
    public static final String COL_ALAMAT = "alamat";
    public static final String COL_NO_HP = "noHp";
    public static final String COL_OPERATOR = "operator";

    private static final String[] COLS_DB = { "nama", "alamat", "no_hp", "operator" };

    private PelangganStore() {
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] columnsDb() {
        return COLS_DB;
    }

    @Override
    protected LinkedHashMap<String, Object> mapRow(ResultSet rs) throws SQLException {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, rs.getString("id"));
        r.put(COL_NAMA, rs.getString("nama"));
        r.put(COL_ALAMAT, rs.getString("alamat"));
        r.put(COL_NO_HP, rs.getString("no_hp"));
        r.put(COL_OPERATOR, rs.getString("operator"));
        return r;
    }

    @Override
    protected void bindData(PreparedStatement ps, LinkedHashMap<String, Object> row, int firstIndex) throws SQLException {
        int i = firstIndex;
        ps.setString(i++, nullToEmpty(row.get(COL_NAMA)));
        ps.setString(i++, nullToEmpty(row.get(COL_ALAMAT)));
        ps.setString(i++, nullToEmpty(row.get(COL_NO_HP)));
        ps.setString(i++, nullToEmpty(row.get(COL_OPERATOR)));
    }

    private static String nullToEmpty(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    public static LinkedHashMap<String, Object> newRow(String nama, String alamat, String noHp, String operator) {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, IdGenerator.next(PREFIX));
        r.put(COL_NAMA, nama);
        r.put(COL_ALAMAT, alamat);
        r.put(COL_NO_HP, noHp);
        r.put(COL_OPERATOR, operator);
        return r;
    }

    public void seedIfEmpty() {
        executeSeedIfEmpty(() -> {
            add(newRow("Budi Santoso", "Jl. Merdeka No. 12, Jakarta", "081234567890", "admin"));
            add(newRow("Siti Aminah", "Jl. Sudirman No. 45, Tangerang", "082112223333", "admin"));
            add(newRow("Joko Widodo", "Jl. Mawar No. 7, Bogor", "081298765432", "admin2"));
            add(newRow("Rina Marlina", "Jl. Anggrek No. 19, Depok", "085711223344", "admin2"));
            add(newRow("Andi Pratama", "Jl. Melati No. 5, Bekasi", "087812345678", "operator1"));
            add(newRow("Dewi Lestari", "Jl. Kenanga No. 8, Jakarta Selatan", "081377889900", "operator1"));
            add(newRow("Eko Saputra", "Jl. Cendana No. 22, Bandung", "082199887766", "admin"));
            add(newRow("Fitri Handayani", "Jl. Cempaka No. 30, Surabaya", "081366554433", "admin2"));
            add(newRow("Galih Wijaya", "Jl. Dahlia No. 14, Yogyakarta", "087722334455", "operator1"));
            add(newRow("Hana Permata", "Jl. Flamboyan No. 9, Semarang", "085644556677", "admin"));
        });
    }
}
