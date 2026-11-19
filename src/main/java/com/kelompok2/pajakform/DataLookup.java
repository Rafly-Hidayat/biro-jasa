package com.kelompok2.pajakform;

import java.util.LinkedHashMap;

/**
 * Resolved display values across pelanggan ↔ kendaraan ↔ pembayaran.
 */
public final class DataLookup {

    private DataLookup() {
    }

    /** @return trimmed no_polisi or "" */
    public static String kendaraanNoPolisi(String kendaraanId) {
        if (kendaraanId == null || kendaraanId.isBlank()) {
            return "";
        }
        LinkedHashMap<String, Object> k = KendaraanStore.INSTANCE.findById(kendaraanId);
        if (k == null) {
            return "";
        }
        Object v = k.get(KendaraanStore.COL_NO_POLISI);
        return v == null ? "" : String.valueOf(v).trim();
    }

    public static LinkedHashMap<String, Object> pelangganForKendaraan(String kendaraanId) {
        if (kendaraanId == null || kendaraanId.isBlank()) {
            return null;
        }
        LinkedHashMap<String, Object> k = KendaraanStore.INSTANCE.findById(kendaraanId);
        if (k == null) {
            return null;
        }
        String pid = String.valueOf(k.getOrDefault(KendaraanStore.COL_ID_PELANGGAN, "")).trim();
        if (pid.isEmpty()) {
            return null;
        }
        return PelangganStore.INSTANCE.findById(pid);
    }

    public static String namaPemilikKendaraan(String kendaraanId) {
        LinkedHashMap<String, Object> p = pelangganForKendaraan(kendaraanId);
        if (p == null) {
            return "";
        }
        Object n = p.get(PelangganStore.COL_NAMA);
        return n == null ? "" : String.valueOf(n);
    }

}
