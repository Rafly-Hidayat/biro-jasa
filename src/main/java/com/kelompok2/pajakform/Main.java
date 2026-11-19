package com.kelompok2.pajakform;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Tampilan.pasangIconAplikasi();

        Database.init();

        SwingUtilities.invokeLater(() -> {
            FormLogin formLogin = new FormLogin();
            formLogin.setLocationRelativeTo(null);
            formLogin.setVisible(true);
        });
    }
}
