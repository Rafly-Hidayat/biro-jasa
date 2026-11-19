package com.kelompok2.pajakform;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class FormLaporan extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy");
    private static final NumberFormat RUPIAH = NumberFormat.getInstance(new Locale("id", "ID"));

    private static final String SRC_PELANGGAN = "Pelanggan";
    private static final String SRC_KENDARAAN = "Kendaraan";
    private static final String SRC_PEMBAYARAN = "Pembayaran Pajak";
    private static final String SRC_BALIK_NAMA = "Balik Nama";
    private static final String SRC_MONITORING = "Monitoring";
    private static final String SRC_USER = "User Admin";

    public FormLaporan() {
        initComponents();
        applyStyle();
        setupTable();
        loadData();

        Tampilan.window(this);
        setLocationRelativeTo(null);
        Responsive.enable(this);
    }

    private void applyStyle() {
        getContentPane().setBackground(Tampilan.BACKGROUND);
        Tampilan.judul(lblTitle, Tampilan.SIZE_PAGE_TITLE);
        Tampilan.label(lblSumber);
        Tampilan.label(lblDari);
        Tampilan.label(lblSampai);
        Tampilan.label(lblJumlah);
        Tampilan.combo(cmbSumber);
        Tampilan.dateChooser(dtDari);
        Tampilan.dateChooser(dtSampai);
        Tampilan.tombolKecil(btnTampilkan);
        Tampilan.tombolKecil(btnExportCsv);
        Tampilan.tombolKecil(btnExportPdf);
        Tampilan.tombolKecil(btnCetak);
        Tampilan.tombolKecil(btnKembali);
        Tampilan.tabel(tblData);

        ActionListener srcListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDateFilterAvailability();
            }
        };
        cmbSumber.addActionListener(srcListener);
        updateDateFilterAvailability();
    }

    private void updateDateFilterAvailability() {
        boolean hasDate = sourceHasDate(String.valueOf(cmbSumber.getSelectedItem()));
        dtDari.setEnabled(hasDate);
        dtSampai.setEnabled(hasDate);
        if (!hasDate) {
            dtDari.setDate(null);
            dtSampai.setDate(null);
        }
    }

    private boolean sourceHasDate(String source) {
        return SRC_PEMBAYARAN.equals(source) || SRC_BALIK_NAMA.equals(source) || SRC_MONITORING.equals(source);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(new Object[] { "" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData.setModel(tableModel);
    }

    private static String fmtDate(Date d) {
        return d == null ? "" : DATE_FMT.format(d);
    }

    private static String formatRupiah(Object v) {
        if (v == null) return "";
        if (v instanceof Number) return RUPIAH.format(((Number) v).doubleValue());
        return String.valueOf(v);
    }

    private void loadData() {
        String source = String.valueOf(cmbSumber.getSelectedItem());
        Date from = dtDari.getDate();
        Date to = dtSampai.getDate();

        String[] cols;
        java.util.List<Object[]> rows = new java.util.ArrayList<>();

        switch (source) {
            case SRC_PELANGGAN: {
                cols = new String[] { "Id Pelanggan", "Nama", "Alamat", "No HP", "Operator" };
                for (LinkedHashMap<String, Object> r : PelangganStore.INSTANCE.all()) {
                    rows.add(new Object[] {
                            r.get("id"),
                            r.get(PelangganStore.COL_NAMA),
                            r.get(PelangganStore.COL_ALAMAT),
                            r.get(PelangganStore.COL_NO_HP),
                            r.get(PelangganStore.COL_OPERATOR)
                    });
                }
                break;
            }
            case SRC_KENDARAAN: {
                cols = new String[] { "Id Kendaraan", "No Polisi", "Merk", "Tahun", "Pelanggan", "File STNK", "Operator" };
                for (LinkedHashMap<String, Object> r : KendaraanStore.INSTANCE.all()) {
                    String idPlg = String.valueOf(r.getOrDefault(KendaraanStore.COL_ID_PELANGGAN, ""));
                    LinkedHashMap<String, Object> p = idPlg.isBlank() ? null : PelangganStore.INSTANCE.findById(idPlg);
                    String namaPlg = p == null ? idPlg : String.valueOf(p.getOrDefault(PelangganStore.COL_NAMA, ""));
                    rows.add(new Object[] {
                            r.get("id"),
                            r.get(KendaraanStore.COL_NO_POLISI),
                            r.get(KendaraanStore.COL_MERK),
                            r.get(KendaraanStore.COL_TAHUN),
                            namaPlg,
                            r.get(KendaraanStore.COL_FILE_STNK),
                            r.get(KendaraanStore.COL_OPERATOR)
                    });
                }
                break;
            }
            case SRC_PEMBAYARAN: {
                cols = new String[] { "Id", "No Polisi", "Pemilik", "Jenis", "Masa STNK",
                        "Tgl Pengurusan", "Biaya Pajak", "Biaya Jasa", "Total", "Status", "Operator" };
                for (LinkedHashMap<String, Object> r : PembayaranPajakStore.INSTANCE.all()) {
                    Date d = (Date) r.get(PembayaranPajakStore.COL_TGL_PENGURUSAN);
                    if (!isInRange(d, from, to)) continue;
                    String kid = String.valueOf(r.getOrDefault(PembayaranPajakStore.COL_KENDARAAN_ID, ""));
                    rows.add(new Object[] {
                            r.get("id"),
                            DataLookup.kendaraanNoPolisi(kid),
                            DataLookup.namaPemilikKendaraan(kid),
                            r.get(PembayaranPajakStore.COL_JENIS),
                            fmtDate((Date) r.get(PembayaranPajakStore.COL_MASA_STNK)),
                            fmtDate(d),
                            formatRupiah(r.get(PembayaranPajakStore.COL_BIAYA_PAJAK)),
                            formatRupiah(r.get(PembayaranPajakStore.COL_BIAYA_JASA)),
                            formatRupiah(r.get(PembayaranPajakStore.COL_TOTAL)),
                            r.get(PembayaranPajakStore.COL_STATUS),
                            r.get(PembayaranPajakStore.COL_OPERATOR)
                    });
                }
                break;
            }
            case SRC_BALIK_NAMA: {
                cols = new String[] { "No Transaksi", "No Polisi", "Pemilik Lama", "Pemilik Baru",
                        "NIK Baru", "Jenis", "Merk", "Tipe", "Tahun", "Tgl Pengurusan",
                        "Biaya Pajak", "Biaya BBNKB", "Biaya Jasa", "Biaya Lain", "Total Biaya", "Status", "Operator" };
                for (LinkedHashMap<String, Object> r : BalikNamaStore.INSTANCE.all()) {
                    Date d = (Date) r.get(BalikNamaStore.COL_TGL_PENGURUSAN);
                    if (!isInRange(d, from, to)) continue;
                    rows.add(new Object[] {
                            r.get("id"),
                            r.get(BalikNamaStore.COL_NO_POLISI),
                            r.get(BalikNamaStore.COL_PEMILIK_LAMA),
                            r.get(BalikNamaStore.COL_PEMILIK_BARU),
                            r.get(BalikNamaStore.COL_NIK_BARU),
                            r.get(BalikNamaStore.COL_JENIS_KENDARAAN),
                            r.get(BalikNamaStore.COL_MERK),
                            r.get(BalikNamaStore.COL_TIPE),
                            r.get(BalikNamaStore.COL_TAHUN),
                            fmtDate(d),
                            formatRupiah(r.get(BalikNamaStore.COL_BIAYA_PAJAK)),
                            formatRupiah(r.get(BalikNamaStore.COL_BIAYA_BBNKB)),
                            formatRupiah(r.get(BalikNamaStore.COL_BIAYA_JASA)),
                            formatRupiah(r.get(BalikNamaStore.COL_BIAYA_LAIN)),
                            formatRupiah(r.get(BalikNamaStore.COL_TOTAL)),
                            r.get(BalikNamaStore.COL_STATUS),
                            r.get(BalikNamaStore.COL_OPERATOR)
                    });
                }
                break;
            }
            case SRC_MONITORING: {
                cols = new String[] { "Id Proses", "Nama", "Kendaraan", "Status Proses", "Tanggal", "Operator" };
                for (LinkedHashMap<String, Object> r : MonitoringStore.INSTANCE.all()) {
                    Date d = (Date) r.get(MonitoringStore.COL_TANGGAL);
                    if (!isInRange(d, from, to)) continue;
                    rows.add(new Object[] {
                            r.get("id"),
                            r.get(MonitoringStore.COL_NAMA),
                            r.get(MonitoringStore.COL_KENDARAAN),
                            r.get(MonitoringStore.COL_STATUS),
                            fmtDate(d),
                            r.get(MonitoringStore.COL_OPERATOR)
                    });
                }
                break;
            }
            case SRC_USER: {
                cols = new String[] { "Id User", "Username", "Nama Lengkap", "Status" };
                for (UserStore.User u : UserStore.all()) {
                    rows.add(new Object[] { u.id, u.username, u.namaLengkap, u.status });
                }
                break;
            }
            default:
                cols = new String[] { "Data" };
        }

        tableModel.setDataVector(rows.toArray(new Object[0][]), cols);
        for (int i = 0; i < tblData.getColumnCount(); i++) {
            tblData.getColumnModel().getColumn(i).setMinWidth(80);
        }
        lblJumlah.setText("Total baris: " + rows.size());
    }

    private boolean isInRange(Date d, Date from, Date to) {
        if (d == null) {
            return from == null && to == null;
        }
        if (from != null && d.before(startOfDay(from))) return false;
        if (to != null && d.after(endOfDay(to))) return false;
        return true;
    }

    private Date startOfDay(Date d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(d);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date endOfDay(Date d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(d);
        c.set(java.util.Calendar.HOUR_OF_DAY, 23);
        c.set(java.util.Calendar.MINUTE, 59);
        c.set(java.util.Calendar.SECOND, 59);
        c.set(java.util.Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    private String csvEscape(Object v) {
        String s = v == null ? "" : String.valueOf(v);
        boolean needQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        s = s.replace("\"", "\"\"");
        return needQuote ? "\"" + s + "\"" : s;
    }

    private void exportCsv() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk diekspor.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan CSV");
        chooser.setFileFilter(new FileNameExtensionFilter("CSV file (*.csv)", "csv"));
        String defaultName = "laporan_"
                + String.valueOf(cmbSumber.getSelectedItem()).toLowerCase().replace(' ', '_')
                + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
        chooser.setSelectedFile(new File(defaultName));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File target = chooser.getSelectedFile();
        if (!target.getName().toLowerCase().endsWith(".csv")) {
            target = new File(target.getAbsolutePath() + ".csv");
        }

        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(target.toPath()), StandardCharsets.UTF_8))) {
            w.write('\ufeff');
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < tableModel.getColumnCount(); c++) {
                if (c > 0) sb.append(",");
                sb.append(csvEscape(tableModel.getColumnName(c)));
            }
            w.write(sb.toString());
            w.newLine();

            for (int r = 0; r < tableModel.getRowCount(); r++) {
                sb.setLength(0);
                for (int c = 0; c < tableModel.getColumnCount(); c++) {
                    if (c > 0) sb.append(",");
                    sb.append(csvEscape(tableModel.getValueAt(r, c)));
                }
                w.write(sb.toString());
                w.newLine();
            }

            JOptionPane.showMessageDialog(this,
                    "Laporan disimpan ke: " + target.getAbsolutePath(),
                    "Export CSV", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menulis CSV: " + ex.getMessage(),
                    "Export CSV", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Kolom yang ikut dicetak ke PDF per sumber data. Sengaja lebih ringkas daripada
     * tampilan tabel supaya muat satu halaman dan fokus ke nominal biaya.
     */
    private static String[] pdfColumns(String source) {
        switch (source) {
            case SRC_PELANGGAN:
                return new String[] { "Id Pelanggan", "Nama", "Alamat", "No HP" };
            case SRC_KENDARAAN:
                return new String[] { "No Polisi", "Merk", "Tahun", "Pelanggan" };
            case SRC_PEMBAYARAN:
                return new String[] { "Id", "No Polisi", "Pemilik", "Jenis", "Tgl Pengurusan",
                        "Biaya Pajak", "Biaya Jasa", "Total", "Status" };
            case SRC_BALIK_NAMA:
                return new String[] { "No Transaksi", "No Polisi", "Pemilik Baru", "Tgl Pengurusan",
                        "Biaya Pajak", "Biaya BBNKB", "Biaya Jasa", "Biaya Lain", "Total Biaya", "Status" };
            case SRC_MONITORING:
                return new String[] { "Id Proses", "Nama", "Kendaraan", "Status Proses", "Tanggal" };
            case SRC_USER:
                return new String[] { "Username", "Nama Lengkap", "Status" };
            default:
                return null;
        }
    }

    private String periodeLabel() {
        Date from = dtDari.getDate();
        Date to = dtSampai.getDate();
        if (from == null && to == null) {
            return "Semua data";
        }
        return (from == null ? "awal" : DATE_FMT.format(from))
                + " s/d " + (to == null ? "sekarang" : DATE_FMT.format(to));
    }

    private void exportPdf() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk diekspor.");
            return;
        }
        String sumber = String.valueOf(cmbSumber.getSelectedItem());

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("PDF file (*.pdf)", "pdf"));
        String defaultName = "laporan_" + sumber.toLowerCase().replace(' ', '_')
                + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        chooser.setSelectedFile(new File(defaultName));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File target = chooser.getSelectedFile();
        if (!target.getName().toLowerCase().endsWith(".pdf")) {
            target = new File(target.getAbsolutePath() + ".pdf");
        }

        try {
            LaporanPdfOpen.fromTableModel(tableModel, sumber, periodeLabel(),
                    Session.getCurrentNamaLengkap(), Session.getCurrentUsername(), pdfColumns(sumber))
                    .writeTo(target);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menulis PDF: " + ex.getMessage(),
                    "Export PDF", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int open = JOptionPane.showConfirmDialog(this,
                "Laporan disimpan ke:\n" + target.getAbsolutePath() + "\n\nBuka file sekarang?",
                "Export PDF", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (open == JOptionPane.YES_OPTION) {
            bukaFile(target);
        }
    }

    private void bukaFile(File file) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException | UnsupportedOperationException ex) {
            JOptionPane.showMessageDialog(this,
                    "File tersimpan, tetapi tidak bisa dibuka otomatis: " + ex.getMessage(),
                    "Export PDF", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cetak() {
        try {
            MessageFormat header = new MessageFormat("Laporan " + cmbSumber.getSelectedItem());
            MessageFormat footer = new MessageFormat("Halaman {0}");
            boolean ok = tblData.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Pencetakan dibatalkan.");
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mencetak: " + ex.getMessage(),
                    "Cetak", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblSumber = new javax.swing.JLabel();
        cmbSumber = new javax.swing.JComboBox<>();
        lblDari = new javax.swing.JLabel();
        dtDari = new com.toedter.calendar.JDateChooser();
        lblSampai = new javax.swing.JLabel();
        dtSampai = new com.toedter.calendar.JDateChooser();
        btnTampilkan = new javax.swing.JButton();
        lblJumlah = new javax.swing.JLabel();
        btnExportCsv = new javax.swing.JButton();
        btnExportPdf = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Laporan");
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Laporan");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 20, 1200, 45);

        lblSumber.setText("Sumber");
        getContentPane().add(lblSumber);
        lblSumber.setBounds(40, 90, 100, 28);

        cmbSumber.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
                SRC_PELANGGAN, SRC_KENDARAAN, SRC_PEMBAYARAN, SRC_BALIK_NAMA, SRC_MONITORING, SRC_USER
        }));
        getContentPane().add(cmbSumber);
        cmbSumber.setBounds(150, 92, 230, 28);

        lblDari.setText("Dari Tgl");
        getContentPane().add(lblDari);
        lblDari.setBounds(410, 90, 90, 28);
        getContentPane().add(dtDari);
        dtDari.setBounds(500, 92, 170, 28);

        lblSampai.setText("Sampai Tgl");
        getContentPane().add(lblSampai);
        lblSampai.setBounds(690, 90, 110, 28);
        getContentPane().add(dtSampai);
        dtSampai.setBounds(800, 92, 170, 28);

        btnTampilkan.setText("Tampilkan");
        btnTampilkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTampilkanActionPerformed(evt);
            }
        });
        getContentPane().add(btnTampilkan);
        btnTampilkan.setBounds(995, 90, 150, 32);

        tblData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {

                }));
        jScrollPane1.setViewportView(tblData);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(40, 145, 1105, 415);

        lblJumlah.setText("Total baris: 0");
        getContentPane().add(lblJumlah);
        lblJumlah.setBounds(40, 575, 250, 32);

        btnExportCsv.setText("Export CSV");
        btnExportCsv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportCsvActionPerformed(evt);
            }
        });
        getContentPane().add(btnExportCsv);
        btnExportCsv.setBounds(530, 575, 140, 36);

        btnExportPdf.setText("Export PDF");
        btnExportPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportPdfActionPerformed(evt);
            }
        });
        getContentPane().add(btnExportPdf);
        btnExportPdf.setBounds(685, 575, 140, 36);

        btnCetak.setText("Cetak");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        getContentPane().add(btnCetak);
        btnCetak.setBounds(840, 575, 140, 36);

        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btnKembali);
        btnKembali.setBounds(1005, 575, 140, 36);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTampilkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanActionPerformed
        loadData();
    }//GEN-LAST:event_btnTampilkanActionPerformed

    private void btnExportCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportCsvActionPerformed
        exportCsv();
    }//GEN-LAST:event_btnExportCsvActionPerformed

    private void btnExportPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportPdfActionPerformed
        exportPdf();
    }//GEN-LAST:event_btnExportPdfActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        cetak();
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        new FormMenu().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnKembaliActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnExportCsv;
    private javax.swing.JButton btnExportPdf;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTampilkan;
    private javax.swing.JComboBox<String> cmbSumber;
    private com.toedter.calendar.JDateChooser dtDari;
    private com.toedter.calendar.JDateChooser dtSampai;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDari;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblSampai;
    private javax.swing.JLabel lblSumber;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblData;
    // End of variables declaration//GEN-END:variables
}
