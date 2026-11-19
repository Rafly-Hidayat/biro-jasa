package com.kelompok2.pajakform;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public final class FileUploader {

    private FileUploader() {
    }

    private static final Locale IND = new Locale("id", "ID");

    private static final String ROOT = "uploads";
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /** Teks placeholder di label form saat belum ada file. */
    private static final String KOSONG = "(kosong)";

    /** Hanya tipe ini yang bisa ditampilkan langsung di aplikasi. */
    private static final Set<String> EKSTENSI_GAMBAR = Set.of("jpg", "jpeg", "png", "gif", "bmp");

    private static final int PRATINJAU_MAX_LEBAR = 700;
    private static final int PRATINJAU_MAX_TINGGI = 440;
    private static final int CATATAN_LEBAR = 520;
    private static final int CATATAN_TINGGI = 160;

    private static final int TOMBOL_LIHAT_LEBAR = 70;
    private static final int JARAK_KOMPONEN = 6;
    private static final int LABEL_MIN_LEBAR = 80;

    private static final long KB = 1024L;

    public static String upload(Component parent, String subFolder) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih File untuk " + subFolder);
        int result = chooser.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File source = chooser.getSelectedFile();
        if (source == null || !source.exists()) {
            return null;
        }

        try {
            Path targetDir = Database.DATA_DIR.resolve(ROOT).resolve(subFolder);
            Files.createDirectories(targetDir);

            String stamp = LocalDateTime.now().format(STAMP);
            String safeName = stamp + "_" + source.getName().replaceAll("\\s+", "_");
            Path target = targetDir.resolve(safeName);

            Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/" + ROOT + "/" + subFolder + "/" + safeName;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Gagal menyalin file: " + ex.getMessage(),
                    "Upload Gagal",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /** Buka file dengan aplikasi default sistem. */
    public static boolean openFile(Component parent, String relativePath) {
        File file = resolve(parent, relativePath);
        if (file == null) {
            return false;
        }
        if (!Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            JOptionPane.showMessageDialog(parent,
                    "Sistem ini tidak mendukung membuka file otomatis.\n"
                            + "Pakai tombol \"Simpan Ke...\" lalu buka filenya manual.",
                    "Buka File", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Desktop.getDesktop().open(file);
            return true;
        } catch (IOException | RuntimeException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Gagal membuka file: " + ex.getMessage(),
                    "Buka File", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Pratinjau file yang sudah diupload. Gambar tampil langsung di dialog; tipe lain
     * (PDF dan sejenisnya) dibuka dengan aplikasi default sistem lewat tombol di dialog.
     */
    public static void preview(Component parent, String relativePath) {
        File file = resolve(parent, relativePath);
        if (file == null) {
            return;
        }

        JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(parent),
                "Pratinjau Dokumen", true);
        JComponent isi = (JComponent) dialog.getContentPane();
        isi.setLayout(new BorderLayout(0, 12));
        isi.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        isi.setBackground(Tampilan.BACKGROUND);

        isi.add(panelJudul(file, relativePath), BorderLayout.NORTH);
        isi.add(panelPratinjau(file), BorderLayout.CENTER);
        isi.add(panelAksi(dialog, relativePath), BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /** Simpan salinan file yang sudah diupload ke lokasi pilihan user. */
    public static void download(Component parent, String relativePath) {
        File source = resolve(parent, relativePath);
        if (source == null) {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan File");
        chooser.setSelectedFile(new File(namaSaran(source.getName())));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File target = chooser.getSelectedFile();
        if (target == null) {
            return;
        }
        if (target.exists()) {
            int jawab = JOptionPane.showConfirmDialog(parent,
                    "File \"" + target.getName() + "\" sudah ada. Timpa?",
                    "Simpan File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (jawab != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(parent,
                    "File disimpan ke:\n" + target.getAbsolutePath(),
                    "Simpan File", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Gagal menyimpan file: " + ex.getMessage(),
                    "Simpan File", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pasang tombol "Lihat" di sebelah tombol upload, di luar blok {@code initComponents()}
     * supaya form tetap bisa dibuka di GUI builder. Label nilai file dipersempit sesuai
     * ruang yang dipakai tombol baru. {@code sumberPath} dibaca saat tombol diklik, jadi
     * selalu mengikuti data baris yang sedang dipilih.
     */
    public static void pasangTombolLihat(JFrame form, JButton tombolUpload, JLabel labelNilai,
            Supplier<String> sumberPath) {
        JButton tombol = new JButton("Lihat");
        tombol.setToolTipText("Pratinjau dan simpan file yang sudah diupload");
        Tampilan.tombolKecil(tombol);
        tombol.addActionListener(evt -> preview(form, sumberPath.get()));

        Rectangle upload = tombolUpload.getBounds();
        Rectangle nilai = labelNilai.getBounds();
        int tombolX = upload.x + upload.width + JARAK_KOMPONEN;
        tombol.setBounds(tombolX, upload.y, TOMBOL_LIHAT_LEBAR, upload.height);

        int nilaiX = tombolX + TOMBOL_LIHAT_LEBAR + JARAK_KOMPONEN;
        int nilaiLebar = Math.max(LABEL_MIN_LEBAR, nilai.x + nilai.width - nilaiX);
        labelNilai.setBounds(nilaiX, nilai.y, nilaiLebar, nilai.height);

        form.getContentPane().add(tombol);
    }

    // --- bagian dalam -----------------------------------------------------

    /** Cek path dan keberadaan file; tampilkan pesan ke user kalau file tidak bisa dipakai. */
    private static File resolve(Component parent, String relativePath) {
        if (relativePath == null || relativePath.isBlank() || KOSONG.equals(relativePath)) {
            JOptionPane.showMessageDialog(parent,
                    "Belum ada file yang diupload untuk data ini.",
                    "Dokumen", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String trimmed = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        File file = Database.DATA_DIR.resolve(trimmed).toFile();
        if (!file.isFile()) {
            JOptionPane.showMessageDialog(parent,
                    "File tidak ditemukan:\n" + relativePath + "\n\n"
                            + "File mungkin sudah dipindah atau dihapus dari folder " + ROOT + ".",
                    "Dokumen", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return file;
    }

    private static JComponent panelJudul(File file, String relativePath) {
        JLabel nama = new JLabel(file.getName());
        Tampilan.label(nama);

        JLabel detail = new JLabel(ukuran(file) + " - " + relativePath);
        Tampilan.labelKecil(detail);

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 2));
        panel.setOpaque(false);
        panel.add(nama);
        panel.add(detail);
        return panel;
    }

    private static JComponent panelPratinjau(File file) {
        String ekstensi = ekstensi(file.getName());
        if (!EKSTENSI_GAMBAR.contains(ekstensi)) {
            return catatan("Tipe <b>"
                    + (ekstensi.isEmpty() ? "tanpa ekstensi" : ekstensi.toUpperCase(IND))
                    + "</b> tidak bisa ditampilkan di dalam aplikasi.<br><br>"
                    + "Pakai tombol <b>Buka di Aplikasi Sistem</b> untuk membukanya dengan "
                    + "aplikasi default (misalnya pembaca PDF), atau <b>Simpan Ke...</b> "
                    + "untuk menyalinnya.");
        }

        BufferedImage gambar;
        try {
            gambar = ImageIO.read(file);
        } catch (IOException ex) {
            gambar = null;
        }
        if (gambar == null) {
            return catatan("Gagal membaca gambar ini. Filenya mungkin rusak atau sebenarnya "
                    + "bukan gambar.<br><br>Coba buka dengan aplikasi sistem.");
        }

        double skala = Math.min(1d, Math.min(
                PRATINJAU_MAX_LEBAR / (double) gambar.getWidth(),
                PRATINJAU_MAX_TINGGI / (double) gambar.getHeight()));
        int lebar = Math.max(1, (int) Math.round(gambar.getWidth() * skala));
        int tinggi = Math.max(1, (int) Math.round(gambar.getHeight() * skala));

        JLabel view = new JLabel(new ImageIcon(
                gambar.getScaledInstance(lebar, tinggi, Image.SCALE_SMOOTH)));
        view.setHorizontalAlignment(SwingConstants.CENTER);
        view.setBorder(BorderFactory.createLineBorder(Tampilan.GRID));

        JScrollPane scroll = new JScrollPane(view);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Tampilan.BACKGROUND);
        return scroll;
    }

    private static JComponent catatan(String pesanHtml) {
        JLabel label = new JLabel("<html><div style='text-align:center;'>"
                + pesanHtml + "</div></html>", SwingConstants.CENTER);
        Tampilan.labelKecil(label);
        label.setPreferredSize(new Dimension(CATATAN_LEBAR, CATATAN_TINGGI));
        label.setBorder(BorderFactory.createLineBorder(Tampilan.GRID));
        return label;
    }

    private static JComponent panelAksi(JDialog dialog, String relativePath) {
        JButton btnBuka = new JButton("Buka di Aplikasi Sistem");
        JButton btnSimpan = new JButton("Simpan Ke...");
        JButton btnTutup = new JButton("Tutup");
        Tampilan.tombolKecil(btnBuka);
        Tampilan.tombolKecil(btnSimpan);
        Tampilan.tombolKecil(btnTutup);

        btnBuka.addActionListener(evt -> openFile(dialog, relativePath));
        btnSimpan.addActionListener(evt -> download(dialog, relativePath));
        btnTutup.addActionListener(evt -> dialog.dispose());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);
        panel.add(btnBuka);
        panel.add(btnSimpan);
        panel.add(btnTutup);
        return panel;
    }

    /** Buang prefiks stempel waktu hasil upload supaya saran nama kembali seperti aslinya. */
    private static String namaSaran(String fileName) {
        return fileName.replaceFirst("^\\d{8}_\\d{6}_", "");
    }

    private static String ekstensi(String fileName) {
        int titik = fileName.lastIndexOf('.');
        if (titik < 0 || titik == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(titik + 1).toLowerCase(IND);
    }

    private static String ukuran(File file) {
        long bytes = file.length();
        if (bytes < KB) {
            return bytes + " B";
        }
        if (bytes < KB * KB) {
            return (bytes / KB) + " KB";
        }
        return String.format(IND, "%.1f MB", bytes / (double) (KB * KB));
    }
}
