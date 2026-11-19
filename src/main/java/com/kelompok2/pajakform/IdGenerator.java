package com.kelompok2.pajakform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class IdGenerator {

    private IdGenerator() {
    }

    private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyyMM");
    private static final Map<String, Integer> COUNTERS = new HashMap<>();

    public static synchronized String next(String prefix) {
        String yyyymm = LocalDate.now().format(YEAR_MONTH);
        String key = prefix + "-" + yyyymm;
        int seq = COUNTERS.getOrDefault(key, 0) + 1;
        COUNTERS.put(key, seq);
        return String.format("%s-%s-%03d", prefix, yyyymm, seq);
    }

    public static synchronized String peekNext(String prefix) {
        String yyyymm = LocalDate.now().format(YEAR_MONTH);
        String key = prefix + "-" + yyyymm;
        int seq = COUNTERS.getOrDefault(key, 0) + 1;
        return String.format("%s-%s-%03d", prefix, yyyymm, seq);
    }

    /**
     * Sets the sequence counter for the current YYYYMM from the highest existing id in {@code tableName}
     * with pattern {@code PREFIX-yyyymm-NNN}.
     */
    public static synchronized void bootstrapFromDb(Connection conn, String prefix, String tableName) throws SQLException {
        String yyyymm = LocalDate.now().format(YEAR_MONTH);
        String likePattern = prefix + "-" + yyyymm + "-%";
        String sql = "SELECT id FROM `" + tableName + "` WHERE id LIKE ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, likePattern);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString(1);
                    if (id != null) {
                        String[] parts = id.split("-");
                        if (parts.length >= 3) {
                            try {
                                int seq = Integer.parseInt(parts[2]);
                                String key = prefix + "-" + yyyymm;
                                int prev = COUNTERS.getOrDefault(key, 0);
                                COUNTERS.put(key, Math.max(prev, seq));
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        }
    }
}
