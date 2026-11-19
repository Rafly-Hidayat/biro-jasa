package com.kelompok2.pajakform;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * JDBC bootstrap: create database and tables, then seed empty tables.
 */
public final class Database {

    /** Folder data aplikasi di home user: file database dan upload disimpan di sini. */
    public static final Path DATA_DIR = Paths.get(System.getProperty("user.home"), ".biro-jasa-akbar");

    private Database() {
    }

    private static String jdbcUrlWithDatabase;
    private static String dbUser;
    private static String dbPassword;

    public static String url() {
        return jdbcUrlWithDatabase;
    }

    public static synchronized Connection getConnection() throws java.sql.SQLException {
        if (jdbcUrlWithDatabase == null) {
            throw new IllegalStateException("Database belum diinisialisasi.");
        }
        return DriverManager.getConnection(jdbcUrlWithDatabase, dbUser, dbPassword);
    }

    public static void init() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            fatal("Driver H2 tidak ditemukan di classpath.", e);
        }

        try {
            Properties file = new Properties();
            try (InputStream in = Database.class.getResourceAsStream("/db.properties")) {
                if (in != null) {
                    file.load(in);
                }
            }

            String dir = resolveProp(file, "db.dir", "DB_DIR", DATA_DIR.toString());
            String name = resolveProp(file, "db.name", "DB_NAME", "biro_jasa_pajak");
            dbUser = resolveProp(file, "db.user", "DB_USER", "sa");
            dbPassword = resolveProp(file, "db.password", "DB_PASSWORD", "");

            Files.createDirectories(Paths.get(dir));
            // H2 creates the file on first connect; MySQL mode keeps backticks/LIMIT/IFNULL working
            jdbcUrlWithDatabase = "jdbc:h2:file:" + Paths.get(dir, name).toAbsolutePath()
                    + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE";

            try (Connection conn = getConnection()) {
                createTables(conn);

                IdGenerator.bootstrapFromDb(conn, "USR", "users");
                IdGenerator.bootstrapFromDb(conn, PelangganStore.PREFIX, PelangganStore.TABLE);
                IdGenerator.bootstrapFromDb(conn, KendaraanStore.PREFIX, KendaraanStore.TABLE);
                IdGenerator.bootstrapFromDb(conn, PembayaranPajakStore.PREFIX, PembayaranPajakStore.TABLE);
                IdGenerator.bootstrapFromDb(conn, BalikNamaStore.PREFIX, BalikNamaStore.TABLE);
                IdGenerator.bootstrapFromDb(conn, MonitoringStore.PREFIX, MonitoringStore.TABLE);

                UserStore.seedIfEmpty();
                UserStore.loadDefaultAdminId();
                PelangganStore.INSTANCE.seedIfEmpty();
                KendaraanStore.INSTANCE.seedIfEmpty();
                PembayaranPajakStore.INSTANCE.seedIfEmpty();
                BalikNamaStore.INSTANCE.seedIfEmpty();
                MonitoringStore.INSTANCE.seedIfEmpty();
            }
        } catch (Exception ex) {
            fatal("Gagal menginisialisasi database: " + ex.getMessage(), ex);
        }
    }

    private static void createTables(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                      id VARCHAR(32) PRIMARY KEY,
                      username VARCHAR(64) NOT NULL UNIQUE,
                      password VARCHAR(128) NOT NULL,
                      nama_lengkap VARCHAR(128),
                      status VARCHAR(16) NOT NULL
                    )""");

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS pelanggan (
                      id VARCHAR(32) PRIMARY KEY,
                      nama VARCHAR(128) NOT NULL,
                      alamat TEXT,
                      no_hp VARCHAR(32),
                      operator VARCHAR(64)
                    )""");

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS kendaraan (
                      id VARCHAR(32) PRIMARY KEY,
                      no_polisi VARCHAR(32),
                      merk VARCHAR(64),
                      tahun VARCHAR(8),
                      id_pelanggan VARCHAR(32) NOT NULL,
                      file_stnk VARCHAR(255),
                      operator VARCHAR(64),
                      CONSTRAINT fk_kendaraan_pelanggan FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id)
                        ON UPDATE CASCADE ON DELETE RESTRICT
                    )""");

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS pembayaran_pajak (
                      id VARCHAR(32) PRIMARY KEY,
                      kendaraan_id VARCHAR(32) NOT NULL,
                      jenis_perpanjangan VARCHAR(32),
                      masa_berlaku_stnk DATETIME,
                      tanggal_pengurusan DATETIME,
                      biaya_pajak DECIMAL(15,2),
                      biaya_jasa DECIMAL(15,2),
                      total DECIMAL(15,2),
                      status VARCHAR(32),
                      operator VARCHAR(64),
                      CONSTRAINT fk_pembayaran_kendaraan FOREIGN KEY (kendaraan_id) REFERENCES kendaraan(id)
                        ON UPDATE CASCADE ON DELETE RESTRICT
                    )""");

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS balik_nama (
                      id VARCHAR(32) PRIMARY KEY,
                      no_polisi VARCHAR(32),
                      nama_pemilik_lama VARCHAR(128),
                      nama_pemilik_baru VARCHAR(128),
                      nik_pemilik_baru VARCHAR(32),
                      alamat_pemilik_baru TEXT,
                      jenis_kendaraan VARCHAR(32),
                      merk_kendaraan VARCHAR(64),
                      tipe_kendaraan VARCHAR(64),
                      tahun_kendaraan INT,
                      nomor_rangka VARCHAR(64),
                      nomor_mesin VARCHAR(64),
                      tanggal_pengurusan DATETIME,
                      biaya_pajak DECIMAL(15,2),
                      biaya_bbnkb DECIMAL(15,2),
                      biaya_jasa DECIMAL(15,2),
                      biaya_lain DECIMAL(15,2),
                      total_biaya DECIMAL(15,2),
                      status_transaksi VARCHAR(32),
                      file_ktp VARCHAR(255),
                      file_stnk VARCHAR(255),
                      file_bpkb VARCHAR(255),
                      file_kwitansi VARCHAR(255),
                      catatan TEXT,
                      operator VARCHAR(64)
                    )""");

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS monitoring (
                      id VARCHAR(32) PRIMARY KEY,
                      nama VARCHAR(128),
                      kendaraan VARCHAR(64),
                      status_proses VARCHAR(32),
                      tanggal DATETIME,
                      operator VARCHAR(64)
                    )""");
        }
    }

    private static String resolveProp(Properties file, String key, String envName, String def) {
        String v = System.getenv(envName);
        if (v != null && !v.isBlank()) {
            return v.trim();
        }
        v = System.getProperty(key);
        if (v != null && !v.isBlank()) {
            return v.trim();
        }
        v = file.getProperty(key);
        if (v != null && !v.isBlank()) {
            return v.trim();
        }
        return def;
    }

    private static void fatal(String msg, Throwable cause) {
        cause.printStackTrace();
        JOptionPane.showMessageDialog(null, msg, "Database", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
