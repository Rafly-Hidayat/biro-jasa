package com.kelompok2.pajakform;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * JDBC bootstrap: create database and tables, then seed empty tables.
 */
public final class Database {

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
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fatal("Driver MySQL tidak ditemukan di classpath.", e);
        }

        try {
            Properties file = new Properties();
            try (InputStream in = Database.class.getResourceAsStream("/db.properties")) {
                if (in != null) {
                    file.load(in);
                }
            }

            String host = resolveProp(file, "db.host", "DB_HOST", "localhost");
            String port = resolveProp(file, "db.port", "DB_PORT", "3306");
            String name = resolveProp(file, "db.name", "DB_NAME", "biro_jasa_pajak");
            dbUser = resolveProp(file, "db.user", "DB_USER", "root");
            dbPassword = resolveProp(file, "db.password", "DB_PASSWORD", "");

            String params = "?serverTimezone=Asia/Jakarta&useUnicode=true&characterEncoding=utf8"
                    + "&allowPublicKeyRetrieval=true&useSSL=false";
            String baseUrl = "jdbc:mysql://" + host + ":" + port + "/";

            try (Connection conn = DriverManager.getConnection(baseUrl + params, dbUser, dbPassword);
                    Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + escIdent(name)
                        + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            }

            jdbcUrlWithDatabase = baseUrl + name + params;

            try (Connection conn = getConnection()) {
                createTables(conn);
                migrateSchema(conn);

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

    private static String escIdent(String name) {
        return name.replace("`", "``");
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
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""");

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS pelanggan (
                      id VARCHAR(32) PRIMARY KEY,
                      nama VARCHAR(128) NOT NULL,
                      alamat TEXT,
                      no_hp VARCHAR(32),
                      operator VARCHAR(64)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""");

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
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""");

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
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""");

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
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""");

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS monitoring (
                      id VARCHAR(32) PRIMARY KEY,
                      nama VARCHAR(128),
                      kendaraan VARCHAR(64),
                      status_proses VARCHAR(32),
                      tanggal DATETIME,
                      operator VARCHAR(64)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""");
        }
    }

    private static boolean columnExists(Connection conn, String table, String column) throws SQLException {
        String sql = "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table);
            ps.setString(2, column);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean foreignKeyExists(Connection conn, String fkName) throws SQLException {
        String sql = "SELECT 1 FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND CONSTRAINT_NAME = ? AND CONSTRAINT_TYPE = 'FOREIGN KEY' LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fkName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void addForeignKeyIfMissing(Connection conn, String fkName, String table, String column,
            String refTable, String refColumn) throws SQLException {
        if (foreignKeyExists(conn, fkName)) {
            return;
        }
        String sql = "ALTER TABLE `" + escIdent(table) + "` ADD CONSTRAINT `" + escIdent(fkName) + "` FOREIGN KEY (`"
                + escIdent(column) + "`) REFERENCES `" + escIdent(refTable) + "` (`" + escIdent(refColumn) + "`) ON UPDATE CASCADE ON DELETE RESTRICT";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            // Tabel lama mungkin masih berisi data tidak konsisten; biarkan aplikasi tetap jalan.
            e.printStackTrace();
        }
    }

    private static void migrateSchema(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        try (Statement st = conn.createStatement()) {
            if (!columnExists(conn, "kendaraan", "file_stnk")) {
                st.executeUpdate("ALTER TABLE kendaraan ADD COLUMN file_stnk VARCHAR(255) NULL");
            }
            /* Pastikan setiap kendaraan punya pelanggan sebelum NOT NULL / FK */
            st.executeUpdate("""
                    UPDATE kendaraan k
                    SET id_pelanggan = (SELECT id FROM (SELECT id FROM pelanggan ORDER BY id LIMIT 1) t)
                    WHERE (k.id_pelanggan IS NULL OR TRIM(k.id_pelanggan) = '')
                      AND EXISTS (SELECT 1 FROM pelanggan p)
                    """);

            if (!columnExists(conn, "pembayaran_pajak", "kendaraan_id")) {
                st.executeUpdate("ALTER TABLE pembayaran_pajak ADD COLUMN kendaraan_id VARCHAR(32) NULL");
            }

            if (columnExists(conn, "pembayaran_pajak", "no_polisi")) {
                st.executeUpdate("""
                        UPDATE pembayaran_pajak p
                        INNER JOIN kendaraan k
                          ON TRIM(UPPER(IFNULL(p.no_polisi,''))) = TRIM(UPPER(IFNULL(k.no_polisi,'')))
                        SET p.kendaraan_id = k.id
                        WHERE p.kendaraan_id IS NULL OR TRIM(p.kendaraan_id) = ''
                        """);
                st.executeUpdate("""
                        UPDATE pembayaran_pajak p
                        SET p.kendaraan_id = (SELECT id FROM (SELECT id FROM kendaraan ORDER BY id LIMIT 1) t)
                        WHERE (p.kendaraan_id IS NULL OR TRIM(p.kendaraan_id) = '')
                          AND EXISTS (SELECT 1 FROM kendaraan k3)
                        """);
            }

            if (columnExists(conn, "pembayaran_pajak", "file_stnk") && columnExists(conn, "kendaraan", "file_stnk")) {
                st.executeUpdate("""
                        UPDATE kendaraan k
                        INNER JOIN pembayaran_pajak p ON p.kendaraan_id = k.id
                        SET k.file_stnk = p.file_stnk
                        WHERE (k.file_stnk IS NULL OR TRIM(k.file_stnk) = '')
                          AND p.file_stnk IS NOT NULL AND TRIM(p.file_stnk) != ''
                        """);
            }

            if (columnExists(conn, "pembayaran_pajak", "nama_pemilik")) {
                st.executeUpdate("ALTER TABLE pembayaran_pajak DROP COLUMN nama_pemilik");
            }
            if (columnExists(conn, "pembayaran_pajak", "file_stnk")) {
                st.executeUpdate("ALTER TABLE pembayaran_pajak DROP COLUMN file_stnk");
            }
            if (columnExists(conn, "pembayaran_pajak", "no_polisi")) {
                st.executeUpdate("ALTER TABLE pembayaran_pajak DROP COLUMN no_polisi");
            }

            try {
                st.executeUpdate("ALTER TABLE kendaraan MODIFY COLUMN id_pelanggan VARCHAR(32) NOT NULL");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (columnExists(conn, "pembayaran_pajak", "kendaraan_id")) {
                try {
                    st.executeUpdate("ALTER TABLE pembayaran_pajak MODIFY COLUMN kendaraan_id VARCHAR(32) NOT NULL");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            addForeignKeyIfMissing(conn, "fk_kendaraan_pelanggan", "kendaraan", "id_pelanggan", "pelanggan", "id");
            addForeignKeyIfMissing(conn, "fk_pembayaran_kendaraan", "pembayaran_pajak", "kendaraan_id", "kendaraan", "id");

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
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
