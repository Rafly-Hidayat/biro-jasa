package com.kelompok2.pajakform;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

public final class KendaraanStore extends BaseStore {

    public static final KendaraanStore INSTANCE = new KendaraanStore();

    public static final String TABLE = "kendaraan";
    public static final String PREFIX = "KDR";
    public static final String COL_NO_POLISI = "noPolisi";
    public static final String COL_MERK = "merk";
    public static final String COL_TAHUN = "tahun";
    public static final String COL_ID_PELANGGAN = "idPelanggan";
    public static final String COL_FILE_STNK = "fileStnk";
    public static final String COL_OPERATOR = "operator";

    private static final String[] COLS_DB = { "no_polisi", "merk", "tahun", "id_pelanggan", "file_stnk", "operator" };

    private KendaraanStore() {
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
        r.put(COL_NO_POLISI, rs.getString("no_polisi"));
        r.put(COL_MERK, rs.getString("merk"));
        r.put(COL_TAHUN, rs.getString("tahun"));
        r.put(COL_ID_PELANGGAN, rs.getString("id_pelanggan"));
        r.put(COL_FILE_STNK, rs.getString("file_stnk"));
        r.put(COL_OPERATOR, rs.getString("operator"));
        return r;
    }

    @Override
    protected void bindData(PreparedStatement ps, LinkedHashMap<String, Object> row, int firstIndex) throws SQLException {
        int i = firstIndex;
        ps.setString(i++, nullToEmpty(row.get(COL_NO_POLISI)));
        ps.setString(i++, nullToEmpty(row.get(COL_MERK)));
        ps.setString(i++, nullToEmpty(row.get(COL_TAHUN)));
        ps.setString(i++, nullToEmpty(row.get(COL_ID_PELANGGAN)));
        ps.setString(i++, nullToEmpty(row.get(COL_FILE_STNK)));
        ps.setString(i++, nullToEmpty(row.get(COL_OPERATOR)));
    }

    private static String nullToEmpty(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    public static LinkedHashMap<String, Object> newRow(String noPolisi, String merk, String tahun,
            String idPelanggan, String fileStnk, String operator) {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, IdGenerator.next(PREFIX));
        r.put(COL_NO_POLISI, noPolisi);
        r.put(COL_MERK, merk);
        r.put(COL_TAHUN, tahun);
        r.put(COL_ID_PELANGGAN, idPelanggan);
        r.put(COL_FILE_STNK, fileStnk);
        r.put(COL_OPERATOR, operator);
        return r;
    }

    /** Cari kendaraan berdasarkan no polisi (trim, case-insensitive). */
    public synchronized LinkedHashMap<String, Object> findByNoPolisi(String noPolisi) {
        if (noPolisi == null || noPolisi.isBlank()) {
            return null;
        }
        String key = noPolisi.trim();
        for (LinkedHashMap<String, Object> r : all()) {
            Object v = r.get(COL_NO_POLISI);
            if (v != null && key.equalsIgnoreCase(String.valueOf(v).trim())) {
                return r;
            }
        }
        return null;
    }

    public void seedIfEmpty() {
        executeSeedIfEmpty(() -> {
            List<LinkedHashMap<String, Object>> pel = PelangganStore.INSTANCE.all();
            if (pel.isEmpty()) {
                throw new StoreException("Seed kendaraan membutuhkan data pelanggan.");
            }
            add(newRow("B 1234 TM", "Toyota Avanza", "2020", idPel(pel, 0), "", "admin"));
            add(newRow("B 4321 MAS", "Honda Brio", "2022", idPel(pel, 1), "", "admin"));
            add(newRow("B 5678 ABC", "Toyota Innova", "2019", idPel(pel, 2), "", "admin2"));
            add(newRow("B 2468 XYZ", "Daihatsu Xenia", "2021", idPel(pel, 3), "", "admin2"));
            add(newRow("D 1357 KLM", "Suzuki Ertiga", "2018", idPel(pel, 4), "", "operator1"));
            add(newRow("F 9988 PQR", "Mitsubishi Xpander", "2023", idPel(pel, 5), "", "operator1"));
            add(newRow("B 7766 STU", "Honda Vario", "2022", idPel(pel, 6), "", "admin"));
            add(newRow("AB 4455 VW", "Yamaha NMAX", "2021", idPel(pel, 7), "", "admin2"));
            add(newRow("L 3322 OP", "Honda CRV", "2020", idPel(pel, 8), "", "operator1"));
            add(newRow("DK 6611 IJ", "Toyota Fortuner", "2024", idPel(pel, 9), "", "admin"));
        });
    }

    private static String idPel(List<LinkedHashMap<String, Object>> pel, int i) {
        return String.valueOf(pel.get(i % pel.size()).get(ID_KEY));
    }
}
