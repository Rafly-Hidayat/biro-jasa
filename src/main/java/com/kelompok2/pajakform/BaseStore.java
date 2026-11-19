package com.kelompok2.pajakform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;

public abstract class BaseStore {

    protected static final String ID_KEY = "id";

    /** SQL table name (backtick-safe literal from code only). */
    protected abstract String tableName();

    /** DB column names (no {@code id}) in bind order. */
    protected abstract String[] columnsDb();

    /**
     * Bind non-id columns for INSERT/UPDATE in the same order as {@link #columnsDb()}.
     *
     * @param firstIndex 1-based JDBC parameter index for the first data column
     */
    protected abstract void bindData(PreparedStatement ps, LinkedHashMap<String, Object> row, int firstIndex)
            throws SQLException;

    protected abstract LinkedHashMap<String, Object> mapRow(ResultSet rs) throws SQLException;

    protected final String sqlColumnList() {
        StringJoiner j = new StringJoiner(", ");
        for (String c : columnsDb()) {
            j.add("`" + c + "`");
        }
        return j.toString();
    }

    protected final String sqlPlaceholders(int n) {
        StringJoiner j = new StringJoiner(", ");
        for (int i = 0; i < n; i++) {
            j.add("?");
        }
        return j.toString();
    }

    protected final String sqlSetUpdates() {
        StringJoiner j = new StringJoiner(", ");
        for (String c : columnsDb()) {
            j.add("`" + c + "`=?");
        }
        return j.toString();
    }

    public final synchronized List<LinkedHashMap<String, Object>> all() {
        String sql = "SELECT id, " + sqlColumnList() + " FROM `" + tableName() + "` ORDER BY id";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<LinkedHashMap<String, Object>> out = new ArrayList<>();
            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return out;
        } catch (SQLException e) {
            throw new StoreException("Gagal memuat data dari " + tableName(), e);
        }
    }

    public final synchronized LinkedHashMap<String, Object> findById(String id) {
        if (id == null) {
            return null;
        }
        String sql = "SELECT id, " + sqlColumnList() + " FROM `" + tableName() + "` WHERE id=?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new StoreException("Gagal mencari id di " + tableName(), e);
        }
    }

    public final synchronized void add(LinkedHashMap<String, Object> row) {
        String id = String.valueOf(row.get(ID_KEY));
        String sql = "INSERT INTO `" + tableName() + "` (id, " + sqlColumnList() + ") VALUES (?, " + sqlPlaceholders(columnsDb().length) + ")";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            bindData(ps, row, 2);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException("Gagal menambah data ke " + tableName(), e);
        }
    }

    public final synchronized void update(String id, LinkedHashMap<String, Object> row) {
        String sql = "UPDATE `" + tableName() + "` SET " + sqlSetUpdates() + " WHERE id=?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            bindData(ps, row, 1);
            ps.setString(columnsDb().length + 1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException("Gagal mengubah data di " + tableName(), e);
        }
    }

    public final synchronized void delete(String id) {
        String sql = "DELETE FROM `" + tableName() + "` WHERE id=?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException("Gagal menghapus data di " + tableName(), e);
        }
    }

    public final synchronized int size() {
        String sql = "SELECT COUNT(*) FROM `" + tableName() + "`";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new StoreException("Gagal menghitung baris di " + tableName(), e);
        }
    }

    /** Runs {@code seeder} only when the table has zero rows. */
    protected final void executeSeedIfEmpty(Runnable seeder) {
        if (size() == 0) {
            seeder.run();
        }
    }
}
