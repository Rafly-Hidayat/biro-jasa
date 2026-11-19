package com.kelompok2.pajakform;

public final class Session {

    private Session() {
    }

    private static UserStore.User currentUser;

    public static synchronized void setCurrentUser(UserStore.User user) {
        currentUser = user;
    }

    public static synchronized UserStore.User getCurrentUser() {
        return currentUser;
    }

    public static synchronized String getCurrentUsername() {
        return currentUser == null ? "" : currentUser.username;
    }

    public static synchronized String getCurrentNamaLengkap() {
        if (currentUser == null) return "";
        if (currentUser.namaLengkap == null || currentUser.namaLengkap.isEmpty()) {
            return currentUser.username;
        }
        return currentUser.namaLengkap;
    }

    public static synchronized void clear() {
        currentUser = null;
    }
}
