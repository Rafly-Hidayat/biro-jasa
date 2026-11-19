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

public final class BalikNamaStore extends BaseStore {

    public static final BalikNamaStore INSTANCE = new BalikNamaStore();

    public static final String TABLE = "balik_nama";
    public static final String PREFIX = "BN";
    public static final String COL_NO_POLISI = "noPolisi";
    public static final String COL_PEMILIK_LAMA = "namaPemilikLama";
    public static final String COL_PEMILIK_BARU = "namaPemilikBaru";
    public static final String COL_NIK_BARU = "nikPemilikBaru";
    public static final String COL_ALAMAT_BARU = "alamatPemilikBaru";
    public static final String COL_JENIS_KENDARAAN = "jenisKendaraan";
    public static final String COL_MERK = "merkKendaraan";
    public static final String COL_TIPE = "tipeKendaraan";
    public static final String COL_TAHUN = "tahunKendaraan";
    public static final String COL_NO_RANGKA = "nomorRangka";
    public static final String COL_NO_MESIN = "nomorMesin";
    public static final String COL_TGL_PENGURUSAN = "tanggalPengurusan";
    public static final String COL_BIAYA_PAJAK = "biayaPajak";
    public static final String COL_BIAYA_BBNKB = "biayaBbnkb";
    public static final String COL_BIAYA_JASA = "biayaJasa";
    public static final String COL_BIAYA_LAIN = "biayaLain";
    public static final String COL_TOTAL = "totalBiaya";
    public static final String COL_STATUS = "statusTransaksi";
    public static final String COL_FILE_KTP = "fileKtp";
    public static final String COL_FILE_STNK = "fileStnk";
    public static final String COL_FILE_BPKB = "fileBpkb";
    public static final String COL_FILE_KWITANSI = "fileKwitansi";
    public static final String COL_CATATAN = "catatan";
    public static final String COL_OPERATOR = "operator";

    private static final String[] COLS_DB = {
            "no_polisi", "nama_pemilik_lama", "nama_pemilik_baru", "nik_pemilik_baru", "alamat_pemilik_baru",
            "jenis_kendaraan", "merk_kendaraan", "tipe_kendaraan", "tahun_kendaraan", "nomor_rangka", "nomor_mesin",
            "tanggal_pengurusan", "biaya_pajak", "biaya_bbnkb", "biaya_jasa", "biaya_lain", "total_biaya",
            "status_transaksi", "file_ktp", "file_stnk", "file_bpkb", "file_kwitansi", "catatan", "operator"
    };

    private BalikNamaStore() {
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
        r.put(COL_NO_POLISI, rs.getString("no_polisi"));
        r.put(COL_PEMILIK_LAMA, rs.getString("nama_pemilik_lama"));
        r.put(COL_PEMILIK_BARU, rs.getString("nama_pemilik_baru"));
        r.put(COL_NIK_BARU, rs.getString("nik_pemilik_baru"));
        r.put(COL_ALAMAT_BARU, rs.getString("alamat_pemilik_baru"));
        r.put(COL_JENIS_KENDARAAN, rs.getString("jenis_kendaraan"));
        r.put(COL_MERK, rs.getString("merk_kendaraan"));
        r.put(COL_TIPE, rs.getString("tipe_kendaraan"));
        r.put(COL_TAHUN, rs.getInt("tahun_kendaraan"));
        r.put(COL_NO_RANGKA, rs.getString("nomor_rangka"));
        r.put(COL_NO_MESIN, rs.getString("nomor_mesin"));
        r.put(COL_TGL_PENGURUSAN, toDate(rs.getTimestamp("tanggal_pengurusan")));
        r.put(COL_BIAYA_PAJAK, nz(rs.getBigDecimal("biaya_pajak")));
        r.put(COL_BIAYA_BBNKB, nz(rs.getBigDecimal("biaya_bbnkb")));
        r.put(COL_BIAYA_JASA, nz(rs.getBigDecimal("biaya_jasa")));
        r.put(COL_BIAYA_LAIN, nz(rs.getBigDecimal("biaya_lain")));
        r.put(COL_TOTAL, nz(rs.getBigDecimal("total_biaya")));
        r.put(COL_STATUS, rs.getString("status_transaksi"));
        r.put(COL_FILE_KTP, rs.getString("file_ktp"));
        r.put(COL_FILE_STNK, rs.getString("file_stnk"));
        r.put(COL_FILE_BPKB, rs.getString("file_bpkb"));
        r.put(COL_FILE_KWITANSI, rs.getString("file_kwitansi"));
        r.put(COL_CATATAN, rs.getString("catatan"));
        r.put(COL_OPERATOR, rs.getString("operator"));
        return r;
    }

    private static double nz(BigDecimal b) {
        return b == null ? 0.0 : b.doubleValue();
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
        ps.setString(i++, nullToEmpty(row.get(COL_NO_POLISI)));
        ps.setString(i++, nullToEmpty(row.get(COL_PEMILIK_LAMA)));
        ps.setString(i++, nullToEmpty(row.get(COL_PEMILIK_BARU)));
        ps.setString(i++, nullToEmpty(row.get(COL_NIK_BARU)));
        ps.setString(i++, nullToEmpty(row.get(COL_ALAMAT_BARU)));
        ps.setString(i++, nullToEmpty(row.get(COL_JENIS_KENDARAAN)));
        ps.setString(i++, nullToEmpty(row.get(COL_MERK)));
        ps.setString(i++, nullToEmpty(row.get(COL_TIPE)));
        Object th = row.get(COL_TAHUN);
        if (th instanceof Number) {
            ps.setInt(i++, ((Number) th).intValue());
        } else {
            ps.setInt(i++, Integer.parseInt(String.valueOf(th == null ? "0" : th)));
        }
        ps.setString(i++, nullToEmpty(row.get(COL_NO_RANGKA)));
        ps.setString(i++, nullToEmpty(row.get(COL_NO_MESIN)));
        setTimestamp(ps, i++, row.get(COL_TGL_PENGURUSAN));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_BIAYA_PAJAK))));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_BIAYA_BBNKB))));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_BIAYA_JASA))));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_BIAYA_LAIN))));
        ps.setBigDecimal(i++, BigDecimal.valueOf(toDouble(row.get(COL_TOTAL))));
        ps.setString(i++, nullToEmpty(row.get(COL_STATUS)));
        ps.setString(i++, nullToEmpty(row.get(COL_FILE_KTP)));
        ps.setString(i++, nullToEmpty(row.get(COL_FILE_STNK)));
        ps.setString(i++, nullToEmpty(row.get(COL_FILE_BPKB)));
        ps.setString(i++, nullToEmpty(row.get(COL_FILE_KWITANSI)));
        ps.setString(i++, nullToEmpty(row.get(COL_CATATAN)));
        ps.setString(i++, nullToEmpty(row.get(COL_OPERATOR)));
    }

    private static String nullToEmpty(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    public static LinkedHashMap<String, Object> newRow(
            String noPolisi, String pemilikLama, String pemilikBaru, String nikBaru, String alamatBaru,
            String jenisKendaraan, String merk, String tipe, int tahun,
            String noRangka, String noMesin, Date tglPengurusan,
            double biayaPajak, double biayaBbnkb, double biayaJasa, double biayaLain,
            String status,
            String fileKtp, String fileStnk, String fileBpkb, String fileKwitansi,
            String catatan, String operator) {
        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put(ID_KEY, IdGenerator.next(PREFIX));
        r.put(COL_NO_POLISI, noPolisi);
        r.put(COL_PEMILIK_LAMA, pemilikLama);
        r.put(COL_PEMILIK_BARU, pemilikBaru);
        r.put(COL_NIK_BARU, nikBaru);
        r.put(COL_ALAMAT_BARU, alamatBaru);
        r.put(COL_JENIS_KENDARAAN, jenisKendaraan);
        r.put(COL_MERK, merk);
        r.put(COL_TIPE, tipe);
        r.put(COL_TAHUN, tahun);
        r.put(COL_NO_RANGKA, noRangka);
        r.put(COL_NO_MESIN, noMesin);
        r.put(COL_TGL_PENGURUSAN, tglPengurusan);
        r.put(COL_BIAYA_PAJAK, biayaPajak);
        r.put(COL_BIAYA_BBNKB, biayaBbnkb);
        r.put(COL_BIAYA_JASA, biayaJasa);
        r.put(COL_BIAYA_LAIN, biayaLain);
        r.put(COL_TOTAL, biayaPajak + biayaBbnkb + biayaJasa + biayaLain);
        r.put(COL_STATUS, status);
        r.put(COL_FILE_KTP, fileKtp);
        r.put(COL_FILE_STNK, fileStnk);
        r.put(COL_FILE_BPKB, fileBpkb);
        r.put(COL_FILE_KWITANSI, fileKwitansi);
        r.put(COL_CATATAN, catatan);
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
            add(newRow("B 1234 TM", "Budi Santoso", "Jeremy Vincent", "3201010101010001",
                    "Jl. Merdeka No. 12, Jakarta", "Mobil", "Toyota", "Avanza", 2020,
                    "MHKAB123A1G123456", "ENG1A2B3C123", dayOffset(-3),
                    2_500_000, 1_800_000, 500_000, 100_000,
                    "Selesai", "", "", "", "", "Sertifikat lengkap.", "admin"));
            add(newRow("B 4321 MAS", "Siti Aminah", "Rangga Putra", "3201020202020002",
                    "Jl. Sudirman No. 45, Tangerang", "Mobil", "Honda", "Brio", 2022,
                    "MHKBR456B2H789012", "ENG2B3C4D456", dayOffset(-6),
                    1_800_000, 1_500_000, 400_000, 80_000,
                    "Diproses", "", "", "", "", "", "admin"));
            add(newRow("B 5678 ABC", "Joko Widodo", "Liana Salim", "3201030303030003",
                    "Jl. Mawar No. 7, Bogor", "Mobil", "Toyota", "Innova", 2019,
                    "MHKIN789C3I345678", "ENG3C4D5E789", dayOffset(-10),
                    3_200_000, 2_100_000, 600_000, 150_000,
                    "Selesai", "", "", "", "", "Pajak 5 tahunan.", "admin2"));
            add(newRow("B 2468 XYZ", "Rina Marlina", "Bayu Kusuma", "3201040404040004",
                    "Jl. Anggrek No. 19, Depok", "Mobil", "Daihatsu", "Xenia", 2021,
                    "MHKXE012D4J901234", "ENG4D5E6F012", dayOffset(-1),
                    2_100_000, 1_650_000, 450_000, 90_000,
                    "Diproses", "", "", "", "", "", "admin2"));
            add(newRow("D 1357 KLM", "Andi Pratama", "Yusuf Hidayat", "3201050505050005",
                    "Jl. Melati No. 5, Bekasi", "Mobil", "Suzuki", "Ertiga", 2018,
                    "MHKER345E5K567890", "ENG5E6F7G345", dayOffset(-15),
                    1_950_000, 1_400_000, 425_000, 75_000,
                    "Selesai", "", "", "", "", "", "operator1"));
            add(newRow("F 9988 PQR", "Dewi Lestari", "Maya Anjani", "3201060606060006",
                    "Jl. Kenanga No. 8, Jakarta Selatan", "Mobil", "Mitsubishi", "Xpander", 2023,
                    "MHKXP678F6L234567", "ENG6F7G8H678", dayOffset(-2),
                    2_800_000, 1_950_000, 550_000, 120_000,
                    "Diproses", "", "", "", "", "Pemilik baru pindah domisili.", "operator1"));
            add(newRow("B 7766 STU", "Eko Saputra", "Indra Maulana", "3201070707070007",
                    "Jl. Cendana No. 22, Bandung", "Motor", "Honda", "Vario", 2022,
                    "MH1JFV123G7M890123", "MTR7G8H9I901", dayOffset(-8),
                    450_000, 850_000, 250_000, 50_000,
                    "Selesai", "", "", "", "", "", "admin"));
            add(newRow("AB 4455 VW", "Fitri Handayani", "Citra Dewi", "3201080808080008",
                    "Jl. Cempaka No. 30, Surabaya", "Motor", "Yamaha", "NMAX", 2021,
                    "MH3SG456H8N456789", "MTR8H9I0J234", dayOffset(-12),
                    500_000, 900_000, 280_000, 60_000,
                    "Selesai", "", "", "", "", "", "admin2"));
            add(newRow("L 3322 OP", "Galih Wijaya", "Rama Saputra", "3201090909090009",
                    "Jl. Dahlia No. 14, Yogyakarta", "Mobil", "Honda", "CRV", 2020,
                    "MHKCV789I9O012345", "ENG9I0J1K567", dayOffset(-18),
                    2_900_000, 2_050_000, 575_000, 110_000,
                    "Selesai", "", "", "", "", "", "operator1"));
            add(newRow("DK 6611 IJ", "Hana Permata", "Tegar Pradana", "3201101010101010",
                    "Jl. Flamboyan No. 9, Semarang", "Mobil", "Toyota", "Fortuner", 2024,
                    "MHKFT012J0P678901", "ENG0J1K2L890", dayOffset(-1),
                    4_200_000, 2_800_000, 700_000, 180_000,
                    "Draft", "", "", "", "", "Baru masuk.", "admin"));
        });
    }
}
