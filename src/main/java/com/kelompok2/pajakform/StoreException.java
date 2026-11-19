package com.kelompok2.pajakform;

/** Unchecked wrapper for JDBC failures from Store layer. */
public final class StoreException extends RuntimeException {

    public StoreException(String message) {
        super(message);
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
