package com.kelompok2.pajakform;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

public class FormMenu extends javax.swing.JFrame {

    public FormMenu() {
        initComponents();
        applyStyle();

        Tampilan.window(this);
        setLocationRelativeTo(null);
        Responsive.enable(this);
    }

    private void applyStyle() {
        getContentPane().setBackground(Tampilan.BACKGROUND);
        Tampilan.logo(lblLogo, 170);
        Tampilan.judul(lblTitle, Tampilan.SIZE_DASHBOARD);
        Tampilan.tombol(btnPelanggan);
        Tampilan.tombol(btnKendaraan);
        Tampilan.tombol(btnPajak);
        Tampilan.tombol(btnBalikNama);
        Tampilan.tombol(btnMonitoring);
        Tampilan.tombol(btnLaporan);
        Tampilan.tombol(btnUserAdmin);
        Tampilan.tombolKecil(btnKeluar);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLogo = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        btnPelanggan = new javax.swing.JButton();
        btnKendaraan = new javax.swing.JButton();
        btnPajak = new javax.swing.JButton();
        btnBalikNama = new javax.swing.JButton();
        btnMonitoring = new javax.swing.JButton();
        btnLaporan = new javax.swing.JButton();
        btnUserAdmin = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Utama");
        getContentPane().setLayout(null);

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblLogo);
        lblLogo.setBounds(465, 30, 170, 170);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 38)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Menu Utama");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 205, 1100, 55);

        btnPelanggan.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnPelanggan.setText("Data Pelanggan");
        btnPelanggan.setBorderPainted(false);
        btnPelanggan.setFocusPainted(false);
        btnPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPelangganActionPerformed(evt);
            }
        });
        getContentPane().add(btnPelanggan);
        btnPelanggan.setBounds(220, 295, 280, 55);

        btnKendaraan.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnKendaraan.setBorderPainted(false);
        btnKendaraan.setFocusPainted(false);
        btnKendaraan.setText("Data Kendaraan");
        btnKendaraan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKendaraanActionPerformed(evt);
            }
        });
        getContentPane().add(btnKendaraan);
        btnKendaraan.setBounds(220, 375, 280, 55);

        btnPajak.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnPajak.setText("Pembayaran Pajak Kendaraan");
        btnPajak.setBorderPainted(false);
        btnPajak.setFocusPainted(false);
        btnPajak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPajakActionPerformed(evt);
            }
        });
        getContentPane().add(btnPajak);
        btnPajak.setBounds(220, 455, 280, 55);

        btnBalikNama.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnBalikNama.setText("Balik Nama Kendaraan");
        btnBalikNama.setBorderPainted(false);
        btnBalikNama.setFocusPainted(false);
        btnBalikNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBalikNamaActionPerformed(evt);
            }
        });
        getContentPane().add(btnBalikNama);
        btnBalikNama.setBounds(220, 535, 280, 55);

        btnMonitoring.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnMonitoring.setBorderPainted(false);
        btnMonitoring.setFocusPainted(false);
        btnMonitoring.setText("Pemantauan Proses");
        btnMonitoring.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonitoringActionPerformed(evt);
            }
        });
        getContentPane().add(btnMonitoring);
        btnMonitoring.setBounds(600, 295, 280, 55);

        btnLaporan.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnLaporan.setBorderPainted(false);
        btnLaporan.setFocusPainted(false);
        btnLaporan.setText("Laporan");
        btnLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanActionPerformed(evt);
            }
        });
        getContentPane().add(btnLaporan);
        btnLaporan.setBounds(600, 375, 280, 55);

        btnUserAdmin.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnUserAdmin.setBorderPainted(false);
        btnUserAdmin.setFocusPainted(false);
        btnUserAdmin.setText("User Admin");
        btnUserAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserAdminActionPerformed(evt);
            }
        });
        getContentPane().add(btnUserAdmin);
        btnUserAdmin.setBounds(600, 455, 280, 55);

        btnKeluar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnKeluar.setBorderPainted(false);
        btnKeluar.setFocusPainted(false);
        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        getContentPane().add(btnKeluar);
        btnKeluar.setBounds(915, 35, 110, 38);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPelangganActionPerformed
        new FormPelanggan().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnPelangganActionPerformed

    private void btnKendaraanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKendaraanActionPerformed
        new FormKendaraan().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnKendaraanActionPerformed

    private void btnPajakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPajakActionPerformed
        new FormPembayaranPajak().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnPajakActionPerformed

    private void btnBalikNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBalikNamaActionPerformed
        new FormBalikNama().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnBalikNamaActionPerformed

    private void btnMonitoringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonitoringActionPerformed
        new FormMonitoring().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnMonitoringActionPerformed

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        new FormLaporan().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnLaporanActionPerformed

    private void btnUserAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserAdminActionPerformed
        new FormUserAdmin().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnUserAdminActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        new FormLogin().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBalikNama;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnKendaraan;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnMonitoring;
    private javax.swing.JButton btnPajak;
    private javax.swing.JButton btnPelanggan;
    private javax.swing.JButton btnUserAdmin;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}


/*
 * Catatan: aturan desain dan validasi ditempatkan di file JFrame Form ini
 * agar project tidak membutuhkan file helper Java tambahan.
 */

/**
 * Pusat aturan desain aplikasi: ukuran font, warna, ukuran window,
 * serta styling komponen Swing agar semua form konsisten.
 */
final class Tampilan {
    private Tampilan() {}

    public static final int SIZE_BADGE = 10;        // Badge label, caption kecil
    public static final int SIZE_LABEL = 11;        // Label form, helper text
    public static final int SIZE_BODY = 13;         // Teks body, isi tabel
    public static final int SIZE_BODY_LARGE = 14;   // Body agak besar, deskripsi
    public static final int SIZE_SUB_HEADING = 16;  // Sub-heading, judul card
    public static final int SIZE_SECTION = 18;      // Heading section, judul panel
    public static final int SIZE_PAGE_TITLE = 22;   // Judul halaman
    public static final int SIZE_DASHBOARD = 28;    // Heading utama / dashboard
    public static final int SIZE_DISPLAY = 34;      // Display / branding besar

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 800;

    public static final Color NAVY = new Color(0, 43, 91);
    public static final Color NAVY_DARK = new Color(0, 25, 56);
    public static final Color NAVY_SOFT = new Color(232, 240, 250);
    public static final Color GOLD = new Color(205, 158, 39);
    public static final Color GOLD_DARK = new Color(168, 124, 18);
    public static final Color GOLD_LIGHT = new Color(255, 246, 222);
    public static final Color BACKGROUND = new Color(246, 248, 252);
    public static final Color FIELD_BACKGROUND = Color.WHITE;
    public static final Color FIELD_READ_ONLY = new Color(238, 242, 247);
    public static final Color GRID = new Color(224, 230, 238);
    public static final Color TEKS = new Color(18, 24, 38);
    public static final Color MUTED = new Color(92, 105, 122);

    private static final String LOGO_ROUNDED = "/images/logo-akbar-rounded.png";
    private static final String LOGO_ICON = "/images/logo-akbar-icon.png";

    private static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, SIZE_BODY);
    private static final Font FONT_MEDIUM = new Font("Segoe UI", Font.PLAIN, SIZE_BODY_LARGE);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, SIZE_BODY);

    public static void window(JFrame frame) {
        pasangIconFrame(frame);
        Dimension oldSize = frame.getSize();
        Dimension designSize = estimateDesignSize(frame.getContentPane(), oldSize);
        if (designSize.width > 0 && designSize.height > 0
                && (designSize.width != WINDOW_WIDTH || designSize.height != WINDOW_HEIGHT)) {
            double sx = WINDOW_WIDTH / (double) designSize.width;
            double sy = WINDOW_HEIGHT / (double) designSize.height;
            double sf = Math.min(sx, sy);
            scaleChildren(frame.getContentPane(), sx, sy, sf);
        }
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setLocationRelativeTo(null);
    }

    private static Dimension estimateDesignSize(Container container, Dimension frameSize) {
        int maxX = 0;
        int maxY = 0;
        for (Component child : container.getComponents()) {
            maxX = Math.max(maxX, child.getX() + child.getWidth());
            maxY = Math.max(maxY, child.getY() + child.getHeight());
        }
        int width = Math.max(frameSize == null ? 0 : frameSize.width, maxX + 24);
        int height = Math.max(frameSize == null ? 0 : frameSize.height, maxY + 48);
        return new Dimension(width, height);
    }

    private static void scaleChildren(Container container, double sx, double sy, double sf) {
        for (Component child : container.getComponents()) {
            child.setBounds(
                    (int) Math.round(child.getX() * sx),
                    (int) Math.round(child.getY() * sy),
                    (int) Math.round(child.getWidth() * sx),
                    (int) Math.round(child.getHeight() * sy));
            if (child instanceof Container) {
                scaleChildren((Container) child, sx, sy, sf);
            }
        }
    }

    public static void logo(JLabel label, int size) {
        ImageIcon icon = loadIcon(LOGO_ROUNDED, size, size);
        if (icon != null) {
            label.setIcon(icon);
            label.setText("");
        } else {
            label.setText("BJ");
            label.setFont(new Font("Segoe UI", Font.BOLD, SIZE_DISPLAY));
            label.setForeground(GOLD);
        }
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setOpaque(false);
    }

    public static void judul(JLabel label, int ignoredSize) {
        label.setFont(new Font("Segoe UI", Font.BOLD, SIZE_DASHBOARD));
        label.setForeground(NAVY);
        label.setHorizontalAlignment(JLabel.CENTER);
    }

    public static void label(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, SIZE_BODY));
        label.setForeground(NAVY);
    }

    public static void labelKecil(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, SIZE_LABEL));
        label.setForeground(MUTED);
    }

    public static void isian(JTextComponent field) {
        field.setFont(FONT_REGULAR);
        field.setBorder(fieldBorder(false));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEKS);
        field.setCaretColor(NAVY);
        field.setSelectionColor(GOLD_LIGHT);
        field.setSelectedTextColor(NAVY_DARK);
    }

    public static void isianReadOnly(JTextComponent field) {
        field.setFont(FONT_REGULAR);
        field.setBorder(fieldBorder(true));
        field.setBackground(FIELD_READ_ONLY);
        field.setForeground(MUTED);
        field.setEditable(false);
        field.setFocusable(false);
    }

    public static void tombol(JButton button) {
        styleButton(button, SIZE_SUB_HEADING, true);
    }

    public static void tombolKecil(JButton button) {
        styleButton(button, SIZE_BODY, true);
    }

    public static void combo(JComboBox<?> combo) {
        combo.setFont(FONT_REGULAR);
        combo.setBackground(FIELD_BACKGROUND);
        combo.setForeground(TEKS);
        combo.setBorder(fieldBorder(false));
    }

    public static void tabel(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setForeground(TEKS);
        table.setBackground(Color.WHITE);
        table.setRowHeight(32);
        table.setGridColor(GRID);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(GOLD_LIGHT);
        table.setSelectionForeground(NAVY_DARK);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(FONT_BOLD);
            header.setBackground(NAVY);
            header.setForeground(GOLD_LIGHT);
            header.setOpaque(true);
            header.setReorderingAllowed(false);
        }

        SwingUtilities.invokeLater(() -> {
            JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, table);
            if (sp != null) {
                sp.setBorder(BorderFactory.createLineBorder(GRID));
                sp.getViewport().setBackground(Color.WHITE);
            }
        });
    }

    public static void dateChooser(JDateChooser chooser) {
        chooser.setFont(FONT_REGULAR);
        chooser.setBackground(FIELD_BACKGROUND);
        chooser.setForeground(TEKS);
        chooser.setDateFormatString("dd/MM/yyyy");
        chooser.setBorder(fieldBorder(false));
        if (chooser.getDateEditor() != null && chooser.getDateEditor().getUiComponent() != null) {
            Component c = chooser.getDateEditor().getUiComponent();
            c.setBackground(FIELD_BACKGROUND);
            c.setForeground(TEKS);
            c.setFont(FONT_REGULAR);
            if (c instanceof JTextComponent) {
                ((JTextComponent) c).setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            }
        }
    }

    public static void dateChooser(JDateChooser chooser, String pattern) {
        dateChooser(chooser);
        chooser.setDateFormatString(pattern);
    }

    /**
     * Ikon Dock macOS dan taskbar Linux tidak ikut {@code setIconImages} milik frame, jadi
     * harus diset sekali lewat {@link Taskbar}. Tanpa ini aplikasi muncul dengan ikon Java
     * bawaan saat JAR dijalankan langsung (di luar bundel .app/.exe hasil jpackage).
     */
    public static void pasangIconAplikasi() {
        if (!Taskbar.isTaskbarSupported()) {
            return;
        }
        Taskbar taskbar = Taskbar.getTaskbar();
        if (!taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            return;
        }
        ImageIcon icon = loadIcon(LOGO_ROUNDED, 512, 512);
        if (icon != null) {
            taskbar.setIconImage(icon.getImage());
        }
    }

    /** Windows memilih sendiri ukuran yang pas untuk taskbar, Alt+Tab, dan title bar. */
    private static final int[] UKURAN_IKON = { 16, 24, 32, 48, 64, 128, 256 };

    private static void pasangIconFrame(JFrame frame) {
        java.util.List<Image> ikon = new java.util.ArrayList<>(UKURAN_IKON.length);
        for (int ukuran : UKURAN_IKON) {
            ImageIcon icon = loadIcon(ukuran <= 64 ? LOGO_ICON : LOGO_ROUNDED, ukuran, ukuran);
            if (icon != null) {
                ikon.add(icon.getImage());
            }
        }
        if (!ikon.isEmpty()) {
            frame.setIconImages(ikon);
        }
    }

    private static ImageIcon loadIcon(String path, int width, int height) {
        java.net.URL url = Tampilan.class.getResource(path);
        if (url == null) {
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static Border fieldBorder(boolean readOnly) {
        Color line = readOnly ? GRID : GOLD;
        return BorderFactory.createCompoundBorder(
                new RoundedLineBorder(line, 12),
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private static void styleButton(JButton button, int size, boolean installHover) {
        button.setFont(new Font("Segoe UI", Font.BOLD, size));
        button.setBackground(GOLD);
        button.setForeground(NAVY_DARK);
        button.setBorder(new RoundedLineBorder(GOLD_DARK, 14));
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setMargin(new Insets(6, 12, 6, 12));

        if (installHover) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (button.isEnabled()) {
                        button.setBackground(GOLD_DARK);
                        button.setForeground(Color.WHITE);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (button.isEnabled()) {
                        button.setBackground(GOLD);
                        button.setForeground(NAVY_DARK);
                    }
                }
            });
        }
    }

    private static final class RoundedLineBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        private RoundedLineBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = 1;
            return insets;
        }
    }

    static {
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEKS);
        UIManager.put("Button.background", GOLD);
        UIManager.put("Button.foreground", NAVY_DARK);
    }
}

/**
 * Helper validasi input form agar pesan error konsisten di seluruh aplikasi.
 */
final class Validasi {
    private Validasi() {}

    public static boolean wajib(Component parent, JTextComponent field, String label) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            gagal(parent, field, label + " wajib diisi.");
            return false;
        }
        return true;
    }

    public static boolean noHp(Component parent, JTextComponent field) {
        String raw = field.getText() == null ? "" : field.getText().trim();
        if (raw.isEmpty()) {
            gagal(parent, field, "No. HP wajib diisi.");
            return false;
        }
        String digits = raw.replaceAll("[^0-9]", "");
        if (!(digits.startsWith("08") || digits.startsWith("62")) || digits.length() < 10 || digits.length() > 15) {
            gagal(parent, field, "No. HP harus berisi 10-15 digit dan diawali 08 atau 62.");
            return false;
        }
        return true;
    }

    public static boolean nik(Component parent, JTextComponent field) {
        String digits = field.getText() == null ? "" : field.getText().trim().replaceAll("[^0-9]", "");
        if (digits.length() != 16) {
            gagal(parent, field, "NIK harus berisi tepat 16 digit angka.");
            return false;
        }
        field.setText(digits);
        return true;
    }

    public static boolean noPolisi(Component parent, JTextComponent field) {
        String value = normalisasiNoPolisi(field.getText());
        if (value.isEmpty()) {
            gagal(parent, field, "No. Polisi wajib diisi.");
            return false;
        }
        if (!value.matches("^[A-Z]{1,2} [0-9]{1,4}([ ]?[A-Z]{1,3})?$")) {
            gagal(parent, field, "Format No. Polisi tidak valid. Contoh: B 1234 CD atau AB 1234 XY.");
            return false;
        }
        field.setText(value);
        return true;
    }

    public static boolean tahun(Component parent, JTextComponent field) {
        String value = field.getText() == null ? "" : field.getText().trim();
        if (!value.matches("^[0-9]{4}$")) {
            gagal(parent, field, "Tahun harus berisi 4 digit angka.");
            return false;
        }
        int tahun = Integer.parseInt(value);
        int max = Year.now().getValue() + 1;
        if (tahun < 1900 || tahun > max) {
            gagal(parent, field, "Tahun harus berada di antara 1900 sampai " + max + ".");
            return false;
        }
        return true;
    }

    public static boolean tanggal(Component parent, Date date, JComponent field, String label) {
        if (date == null) {
            gagal(parent, field, label + " wajib dipilih.");
            return false;
        }
        return true;
    }

    public static boolean biaya(Component parent, JTextComponent field, String label) {
        String raw = field.getText() == null ? "" : field.getText().trim();
        if (raw.isEmpty()) {
            gagal(parent, field, label + " wajib diisi. Isi 0 jika tidak ada biaya.");
            return false;
        }
        try {
            double value = parseBiaya(raw);
            if (value < 0) {
                gagal(parent, field, label + " tidak boleh bernilai negatif.");
                return false;
            }
            return true;
        } catch (IllegalArgumentException ex) {
            gagal(parent, field, label + " harus berupa angka. Contoh: 1500000 atau 1.500.000.");
            return false;
        }
    }

    public static double parseBiaya(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Biaya kosong");
        }
        String cleaned = text.trim()
                .replace("Rp", "")
                .replace("rp", "")
                .replaceAll("\\s+", "");
        cleaned = cleaned.replaceAll("[^0-9,.-]", "");
        if (cleaned.isEmpty() || cleaned.equals("-") || cleaned.equals(",") || cleaned.equals(".")) {
            throw new IllegalArgumentException("Format biaya tidak valid");
        }

        int comma = cleaned.lastIndexOf(',');
        int dot = cleaned.lastIndexOf('.');
        if (comma >= 0 && dot >= 0) {
            if (comma > dot) {
                cleaned = cleaned.replace(".", "").replace(',', '.');
            } else {
                cleaned = cleaned.replace(",", "");
            }
        } else if (comma >= 0) {
            cleaned = cleaned.replace('.', ' '); // titik tidak dipakai saat koma menjadi desimal
            cleaned = cleaned.replace(" ", "").replace(',', '.');
        } else if (dot >= 0) {
            int firstDot = cleaned.indexOf('.');
            if (firstDot != dot || cleaned.substring(dot + 1).length() == 3) {
                cleaned = cleaned.replace(".", "");
            }
        }
        return Double.parseDouble(cleaned);
    }

    public static double parseBiayaAman(String text) {
        try {
            return parseBiaya(text);
        } catch (Exception ex) {
            return 0d;
        }
    }

    public static int parseTahun(String text) {
        return Integer.parseInt(text.trim());
    }

    public static boolean konfirmasiHapus(Component parent, String namaData) {
        int pilihan = JOptionPane.showConfirmDialog(parent,
                "Yakin ingin menghapus " + namaData + "?\nData yang sudah dihapus tidak dapat dikembalikan.",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return pilihan == JOptionPane.YES_OPTION;
    }

    private static String normalisasiNoPolisi(String raw) {
        if (raw == null) {
            return "";
        }
        String v = raw.toUpperCase().replaceAll("[^A-Z0-9]", " ").trim().replaceAll("\\s+", " ");
        String[] parts = v.split(" ");
        if (parts.length == 1) {
            return v;
        }
        return v;
    }

    private static void gagal(Component parent, Component target, String message) {
        JOptionPane.showMessageDialog(parent, message, "Validasi Input", JOptionPane.WARNING_MESSAGE);
        if (target != null) {
            target.requestFocusInWindow();
        }
    }
}
