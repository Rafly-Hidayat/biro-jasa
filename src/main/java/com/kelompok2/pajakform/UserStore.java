package com.kelompok2.pajakform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class UserStore {

    private UserStore() {
    }

    public static final class User {
        public String id;
        public String username;
        public String password;
        public String namaLengkap;
        public String status;

        public User(String id, String username, String password, String namaLengkap, String status) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.namaLengkap = namaLengkap;
            this.status = status;
        }

        public User copy() {
            return new User(id, username, password, namaLengkap, status);
        }
    }

    public static final String STATUS_AKTIF = "Aktif";
    public static final String STATUS_NONAKTIF = "Nonaktif";

    private static volatile String defaultAdminId;

    public static synchronized int count() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new StoreException("Gagal menghitung users", e);
        }
    }

    /**
     * Seeds demo users if the table is empty.
     */
    public static synchronized void seedIfEmpty() {
        if (count() > 0) {
            return;
        }
        insertUser(IdGenerator.next("USR"), "admin", "admin", "Administrator", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "admin2", "admin2", "Admin Kedua", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "operator1", "op123", "Operator Satu", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "operator2", "op456", "Operator Dua", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "operator3", "op789", "Operator Tiga", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "operator4", "op000", "Operator Empat", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "operator5", "op111", "Operator Lima", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "operator6", "op222", "Operator Enam", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "kasir1", "kasir123", "Kasir Utama", STATUS_AKTIF);
        insertUser(IdGenerator.next("USR"), "viewer1", "view123", "Viewer Readonly", STATUS_NONAKTIF);
    }

    public static synchronized void loadDefaultAdminId() {
        String sql = "SELECT id FROM users WHERE username=? LIMIT 1";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "admin");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    defaultAdminId = rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new StoreException("Gagal memuat admin default", e);
        }
    }

    private static void insertUser(String id, String username, String password, String namaLengkap, String status) {
        String sql = "INSERT INTO users (id, username, password, nama_lengkap, status) VALUES (?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, namaLengkap);
            ps.setString(5, status);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException("Gagal insert user", e);
        }
    }

    private static User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("nama_lengkap"),
                rs.getString("status"));
    }

    public static synchronized List<User> all() {
        String sql = "SELECT id, username, password, nama_lengkap, status FROM users ORDER BY id";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<User> out = new ArrayList<>();
            while (rs.next()) {
                out.add(mapUser(rs).copy());
            }
            return out;
        } catch (SQLException e) {
            throw new StoreException("Gagal memuat users", e);
        }
    }

    public static synchronized User findById(String id) {
        String sql = "SELECT id, username, password, nama_lengkap, status FROM users WHERE id=?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs).copy();
                }
            }
            return null;
        } catch (SQLException e) {
            throw new StoreException("Gagal cari user", e);
        }
    }

    public static synchronized boolean isUsernameTaken(String username, String exceptId) {
        String sql = "SELECT id FROM users WHERE LOWER(username)=LOWER(?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    if (exceptId == null || !exceptId.equals(id)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new StoreException("Gagal cek username", e);
        }
    }

    public static synchronized User authenticate(String username, String password) {
        String sql = "SELECT id, username, password, nama_lengkap, status FROM users WHERE username=? AND password=?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = mapUser(rs);
                    if (STATUS_AKTIF.equalsIgnoreCase(u.status)) {
                        return u.copy();
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            throw new StoreException("Gagal autentikasi", e);
        }
    }

    public static synchronized User add(String username, String password, String namaLengkap, String status) {
        String id = IdGenerator.next("USR");
        insertUser(id, username, password, namaLengkap, status);
        return findById(id);
    }

    public static synchronized void update(String id, String username, String password, String namaLengkap, String status) {
        if (password != null && !password.isEmpty()) {
            String sql = "UPDATE users SET username=?, password=?, nama_lengkap=?, status=? WHERE id=?";
            try (Connection conn = Database.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, namaLengkap);
                ps.setString(4, status);
                ps.setString(5, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new StoreException("Gagal update user", e);
            }
        } else {
            String sql = "UPDATE users SET username=?, nama_lengkap=?, status=? WHERE id=?";
            try (Connection conn = Database.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, namaLengkap);
                ps.setString(3, status);
                ps.setString(4, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new StoreException("Gagal update user", e);
            }
        }
    }

    public static synchronized void delete(String id) {
        if (isDefaultAdmin(id)) {
            throw new IllegalStateException("User admin default tidak boleh dihapus.");
        }
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException("Gagal hapus user", e);
        }
    }

    public static boolean isDefaultAdmin(String id) {
        return id != null && id.equals(defaultAdminId);
    }

    public static String defaultAdminId() {
        return defaultAdminId;
    }
}
