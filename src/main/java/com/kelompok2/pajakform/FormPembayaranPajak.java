package com.kelompok2.pajakform;

import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FormPembayaranPajak extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy");
    private static final NumberFormat RUPIAH = NumberFormat.getInstance(new Locale("id", "ID"));

    private final List<String> kendaraanIds = new ArrayList<>();

    public FormPembayaranPajak() {
        initComponents();
        applyStyle();
        setupTable();
        attachAutoTotal();
        reloadTable();
        clearInput();
        cmbKendaraan.addActionListener(e -> syncPemilikInfo());

        Tampilan.window(this);
        setLocationRelativeTo(null);
        Responsive.enable(this);
    }

    private void applyStyle() {
        getContentPane().setBackground(Tampilan.BACKGROUND);
        Tampilan.judul(lblTitle, Tampilan.SIZE_PAGE_TITLE);
        Tampilan.labelKecil(lblLoginUser);
        lblLoginUser.setText("Login sebagai: " + Session.getCurrentUsername());
        Tampilan.label(lblField1);
        Tampilan.label(lblField2);
        Tampilan.label(lblField3);
        Tampilan.label(lblField4);
        Tampilan.label(lblField5);
        Tampilan.label(lblField6);
        Tampilan.label(lblField7);
        Tampilan.label(lblField8);
        Tampilan.label(lblField9);
        Tampilan.label(lblField10);
        Tampilan.isianReadOnly(txtIdPembayaran);
        Tampilan.combo(cmbKendaraan);
        Tampilan.isianReadOnly(txtInfoPemilik);
        Tampilan.combo(cmbJenisPerpanjangan);
        Tampilan.dateChooser(dtMasaBerlakuStnk);
        Tampilan.dateChooser(dtTanggalPengurusan);
        Tampilan.isian(txtBiayaPajak);
        Tampilan.isian(txtBiayaJasa);
        Tampilan.isianReadOnly(txtTotal);
        Tampilan.combo(cmbStatus);
        Tampilan.tombolKecil(btnTambah);
        Tampilan.tombolKecil(btnUbah);
        Tampilan.tombolKecil(btnHapus);
        Tampilan.tombolKecil(btnBersihkan);
        Tampilan.tombolKecil(btnKembali);
        Tampilan.tabel(tblData);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(
                new Object[] { "Id", "No. Polisi", "Pemilik", "Jenis", "Masa STNK", "Tgl Pengurusan",
                        "Biaya Pajak", "Biaya Jasa", "Total", "Status", "Operator" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData.setModel(tableModel);
    }

    private void reloadKendaraanCombo() {
        cmbKendaraan.removeAllItems();
        kendaraanIds.clear();
        cmbKendaraan.addItem("-- Pilih nomor polisi --");
        kendaraanIds.add("");
        for (LinkedHashMap<String, Object> k : KendaraanStore.INSTANCE.all()) {
            String id = String.valueOf(k.get("id"));
            String pol = String.valueOf(k.getOrDefault(KendaraanStore.COL_NO_POLISI, ""));
            String merk = String.valueOf(k.getOrDefault(KendaraanStore.COL_MERK, ""));
            cmbKendaraan.addItem(pol + " — " + merk);
            kendaraanIds.add(id);
        }
    }

    private String getSelectedKendaraanId() {
        int i = cmbKendaraan.getSelectedIndex();
        if (i < 0 || i >= kendaraanIds.size()) {
            return "";
        }
        return kendaraanIds.get(i);
    }

    private void selectKendaraanById(String id) {
        if (id == null) {
            cmbKendaraan.setSelectedIndex(0);
            return;
        }
        String key = id.trim();
        for (int i = 0; i < kendaraanIds.size(); i++) {
            if (kendaraanIds.get(i).equals(key)) {
                cmbKendaraan.setSelectedIndex(i);
                return;
            }
        }
        cmbKendaraan.setSelectedIndex(0);
    }

    private void syncPemilikInfo() {
        String kid = getSelectedKendaraanId();
        if (kid.isEmpty()) {
            txtInfoPemilik.setText("— Pilih kendaraan terdaftar —");
            return;
        }
        LinkedHashMap<String, Object> p = DataLookup.pelangganForKendaraan(kid);
        if (p == null) {
            txtInfoPemilik.setText("Kendaraan belum terhubung ke pelanggan.");
            return;
        }
        String nama = String.valueOf(p.getOrDefault(PelangganStore.COL_NAMA, ""));
        String alamat = String.valueOf(p.getOrDefault(PelangganStore.COL_ALAMAT, ""));
        String hp = String.valueOf(p.getOrDefault(PelangganStore.COL_NO_HP, ""));
        txtInfoPemilik.setText("Nama: " + nama + "\nAlamat: " + alamat + "\nNo. HP: " + hp);
    }

    private void attachAutoTotal() {
        DocumentListener dl = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { recalcTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { recalcTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { recalcTotal(); }
        };
        txtBiayaPajak.getDocument().addDocumentListener(dl);
        txtBiayaJasa.getDocument().addDocumentListener(dl);
    }

    private double parseDouble(String s) {
        if (s == null) return 0d;
        String cleaned = s.replaceAll("[^0-9.\\-]", "");
        if (cleaned.isEmpty()) return 0d;
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException ex) {
            return 0d;
        }
    }

    private void recalcTotal() {
        double total = parseDouble(txtBiayaPajak.getText()) + parseDouble(txtBiayaJasa.getText());
        txtTotal.setText(formatRupiah(total));
    }

    private static String formatRupiah(double v) {
        return RUPIAH.format(v);
    }

    private static String fmtDate(Date d) {
        return d == null ? "" : DATE_FMT.format(d);
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (LinkedHashMap<String, Object> r : PembayaranPajakStore.INSTANCE.all()) {
            String kid = String.valueOf(r.getOrDefault(PembayaranPajakStore.COL_KENDARAAN_ID, ""));
            String pol = DataLookup.kendaraanNoPolisi(kid);
            String pemilik = DataLookup.namaPemilikKendaraan(kid);
            tableModel.addRow(new Object[] {
                    r.get("id"),
                    pol,
                    pemilik,
                    r.get(PembayaranPajakStore.COL_JENIS),
                    fmtDate((Date) r.get(PembayaranPajakStore.COL_MASA_STNK)),
                    fmtDate((Date) r.get(PembayaranPajakStore.COL_TGL_PENGURUSAN)),
                    formatRupiah(((Number) r.getOrDefault(PembayaranPajakStore.COL_BIAYA_PAJAK, 0)).doubleValue()),
                    formatRupiah(((Number) r.getOrDefault(PembayaranPajakStore.COL_BIAYA_JASA, 0)).doubleValue()),
                    formatRupiah(((Number) r.getOrDefault(PembayaranPajakStore.COL_TOTAL, 0)).doubleValue()),
                    r.get(PembayaranPajakStore.COL_STATUS),
                    r.get(PembayaranPajakStore.COL_OPERATOR)
            });
        }
    }

    private void clearInput() {
        txtIdPembayaran.setText("");
        reloadKendaraanCombo();
        cmbJenisPerpanjangan.setSelectedIndex(0);
        dtMasaBerlakuStnk.setDate(null);
        dtTanggalPengurusan.setDate(null);
        txtBiayaPajak.setText("");
        txtBiayaJasa.setText("");
        txtTotal.setText("");
        cmbStatus.setSelectedIndex(0);
        syncPemilikInfo();
        tblData.clearSelection();
    }

    private boolean validateInput() {
        if (getSelectedKendaraanId().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih kendaraan (nomor polisi) dari daftar.",
                    "Validasi Input", JOptionPane.WARNING_MESSAGE);
            cmbKendaraan.requestFocusInWindow();
            return false;
        }
        return Validasi.tanggal(this, dtMasaBerlakuStnk.getDate(), dtMasaBerlakuStnk, "Masa berlaku STNK")
                && Validasi.tanggal(this, dtTanggalPengurusan.getDate(), dtTanggalPengurusan, "Tanggal pengurusan")
                && Validasi.biaya(this, txtBiayaPajak, "Biaya pajak")
                && Validasi.biaya(this, txtBiayaJasa, "Biaya jasa");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblLoginUser = new javax.swing.JLabel();
        lblField1 = new javax.swing.JLabel();
        txtIdPembayaran = new javax.swing.JTextField();
        lblField2 = new javax.swing.JLabel();
        cmbKendaraan = new javax.swing.JComboBox<>();
        lblField3 = new javax.swing.JLabel();
        jScrollPanePemilik = new javax.swing.JScrollPane();
        txtInfoPemilik = new javax.swing.JTextArea();
        lblField4 = new javax.swing.JLabel();
        cmbJenisPerpanjangan = new javax.swing.JComboBox<>();
        lblField5 = new javax.swing.JLabel();
        dtMasaBerlakuStnk = new com.toedter.calendar.JDateChooser();
        lblField6 = new javax.swing.JLabel();
        dtTanggalPengurusan = new com.toedter.calendar.JDateChooser();
        lblField7 = new javax.swing.JLabel();
        txtBiayaPajak = new javax.swing.JTextField();
        lblField8 = new javax.swing.JLabel();
        txtBiayaJasa = new javax.swing.JTextField();
        lblField9 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        lblField10 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersihkan = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pembayaran Pajak Kendaraan");
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Pembayaran Pajak Kendaraan");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 20, 1100, 50);

        lblLoginUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLoginUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoginUser.setText("Login sebagai: -");
        getContentPane().add(lblLoginUser);
        lblLoginUser.setBounds(820, 32, 260, 24);

        lblField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField1.setText("Id Pembayaran");
        getContentPane().add(lblField1);
        lblField1.setBounds(60, 90, 160, 28);

        getContentPane().add(txtIdPembayaran);
        txtIdPembayaran.setBounds(225, 92, 380, 28);

        lblField2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField2.setText("Kendaraan");
        getContentPane().add(lblField2);
        lblField2.setBounds(60, 130, 160, 28);

        getContentPane().add(cmbKendaraan);
        cmbKendaraan.setBounds(225, 132, 380, 28);

        lblField3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField3.setText("Pemilik (otomatis)");
        getContentPane().add(lblField3);
        lblField3.setBounds(60, 170, 200, 28);

        txtInfoPemilik.setColumns(20);
        txtInfoPemilik.setRows(3);
        jScrollPanePemilik.setViewportView(txtInfoPemilik);

        getContentPane().add(jScrollPanePemilik);
        jScrollPanePemilik.setBounds(225, 172, 380, 80);

        lblField4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField4.setText("Jenis Perpanjangan");
        getContentPane().add(lblField4);
        lblField4.setBounds(60, 268, 160, 28);

        cmbJenisPerpanjangan.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Tahunan", "5 Tahunan" }));
        getContentPane().add(cmbJenisPerpanjangan);
        cmbJenisPerpanjangan.setBounds(225, 270, 220, 28);

        lblField5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField5.setText("Masa Berlaku STNK");
        getContentPane().add(lblField5);
        lblField5.setBounds(60, 308, 160, 28);

        getContentPane().add(dtMasaBerlakuStnk);
        dtMasaBerlakuStnk.setBounds(225, 310, 220, 28);

        lblField6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField6.setText("Tanggal Pengurusan");
        getContentPane().add(lblField6);
        lblField6.setBounds(60, 348, 160, 28);

        getContentPane().add(dtTanggalPengurusan);
        dtTanggalPengurusan.setBounds(225, 350, 220, 28);

        lblField7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField7.setText("Biaya Pajak");
        getContentPane().add(lblField7);
        lblField7.setBounds(640, 90, 160, 28);

        getContentPane().add(txtBiayaPajak);
        txtBiayaPajak.setBounds(810, 92, 220, 28);

        lblField8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField8.setText("Biaya Jasa");
        getContentPane().add(lblField8);
        lblField8.setBounds(640, 130, 160, 28);

        getContentPane().add(txtBiayaJasa);
        txtBiayaJasa.setBounds(810, 132, 220, 28);

        lblField9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField9.setText("Total");
        getContentPane().add(lblField9);
        lblField9.setBounds(640, 170, 160, 28);

        getContentPane().add(txtTotal);
        txtTotal.setBounds(810, 172, 220, 28);

        lblField10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblField10.setText("Status");
        getContentPane().add(lblField10);
        lblField10.setBounds(640, 210, 160, 28);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Draft", "Diproses", "Selesai" }));
        getContentPane().add(cmbStatus);
        cmbStatus.setBounds(810, 212, 220, 28);

        btnTambah.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambah);
        btnTambah.setBounds(60, 405, 120, 38);

        btnUbah.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.setBorderPainted(false);
        btnUbah.setFocusPainted(false);
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        getContentPane().add(btnUbah);
        btnUbah.setBounds(195, 405, 120, 38);

        btnHapus.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.setBorderPainted(false);
        btnHapus.setFocusPainted(false);
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus);
        btnHapus.setBounds(330, 405, 120, 38);

        btnBersihkan.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnBersihkan.setText("Bersihkan");
        btnBersihkan.setBorderPainted(false);
        btnBersihkan.setFocusPainted(false);
        btnBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihkanActionPerformed(evt);
            }
        });
        getContentPane().add(btnBersihkan);
        btnBersihkan.setBounds(465, 405, 120, 38);

        btnKembali.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnKembali.setText("Kembali");
        btnKembali.setBorderPainted(false);
        btnKembali.setFocusPainted(false);
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btnKembali);
        btnKembali.setBounds(910, 405, 120, 38);

        tblData.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {

                }));
        tblData.setRowHeight(26);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblData);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(60, 460, 980, 200);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if (!validateInput()) {
            return;
        }
        PembayaranPajakStore.INSTANCE.add(PembayaranPajakStore.newRow(
                getSelectedKendaraanId(),
                String.valueOf(cmbJenisPerpanjangan.getSelectedItem()),
                dtMasaBerlakuStnk.getDate(),
                dtTanggalPengurusan.getDate(),
                parseDouble(txtBiayaPajak.getText()),
                parseDouble(txtBiayaJasa.getText()),
                String.valueOf(cmbStatus.getSelectedItem()),
                Session.getCurrentUsername()));
        reloadTable();
        clearInput();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        int selectedRow = tblData.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu.");
            return;
        }
        if (!validateInput()) {
            return;
        }
        String id = txtIdPembayaran.getText().trim();
        double biayaPajak = parseDouble(txtBiayaPajak.getText());
        double biayaJasa = parseDouble(txtBiayaJasa.getText());
        LinkedHashMap<String, Object> existing = PembayaranPajakStore.INSTANCE.findById(id);
        String originalOperator = existing == null ? Session.getCurrentUsername()
                : String.valueOf(existing.getOrDefault(PembayaranPajakStore.COL_OPERATOR, ""));

        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put("id", id);
        r.put(PembayaranPajakStore.COL_KENDARAAN_ID, getSelectedKendaraanId());
        r.put(PembayaranPajakStore.COL_JENIS, String.valueOf(cmbJenisPerpanjangan.getSelectedItem()));
        r.put(PembayaranPajakStore.COL_MASA_STNK, dtMasaBerlakuStnk.getDate());
        r.put(PembayaranPajakStore.COL_TGL_PENGURUSAN, dtTanggalPengurusan.getDate());
        r.put(PembayaranPajakStore.COL_BIAYA_PAJAK, biayaPajak);
        r.put(PembayaranPajakStore.COL_BIAYA_JASA, biayaJasa);
        r.put(PembayaranPajakStore.COL_TOTAL, biayaPajak + biayaJasa);
        r.put(PembayaranPajakStore.COL_STATUS, String.valueOf(cmbStatus.getSelectedItem()));
        r.put(PembayaranPajakStore.COL_OPERATOR, originalOperator);
        PembayaranPajakStore.INSTANCE.update(id, r);
        reloadTable();
        clearInput();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int selectedRow = tblData.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu.");
            return;
        }
        String id = String.valueOf(tableModel.getValueAt(selectedRow, 0));
        String noPolisi = String.valueOf(tableModel.getValueAt(selectedRow, 1));
        if (!Validasi.konfirmasiHapus(this, "pembayaran pajak kendaraan '" + noPolisi + "'")) {
            return;
        }
        PembayaranPajakStore.INSTANCE.delete(id);
        reloadTable();
        clearInput();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihkanActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBersihkanActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        new FormMenu().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        int row = tblData.getSelectedRow();
        if (row < 0) return;
        String id = String.valueOf(tableModel.getValueAt(row, 0));
        LinkedHashMap<String, Object> r = PembayaranPajakStore.INSTANCE.findById(id);
        if (r == null) return;

        txtIdPembayaran.setText(id);
        reloadKendaraanCombo();
        selectKendaraanById(String.valueOf(r.getOrDefault(PembayaranPajakStore.COL_KENDARAAN_ID, "")));
        syncPemilikInfo();
        cmbJenisPerpanjangan.setSelectedItem(String.valueOf(r.getOrDefault(PembayaranPajakStore.COL_JENIS, "Tahunan")));
        dtMasaBerlakuStnk.setDate((Date) r.get(PembayaranPajakStore.COL_MASA_STNK));
        dtTanggalPengurusan.setDate((Date) r.get(PembayaranPajakStore.COL_TGL_PENGURUSAN));
        Object bp = r.getOrDefault(PembayaranPajakStore.COL_BIAYA_PAJAK, 0);
        Object bj = r.getOrDefault(PembayaranPajakStore.COL_BIAYA_JASA, 0);
        txtBiayaPajak.setText(String.valueOf(((Number) bp).longValue()));
        txtBiayaJasa.setText(String.valueOf(((Number) bj).longValue()));
        cmbStatus.setSelectedItem(String.valueOf(r.getOrDefault(PembayaranPajakStore.COL_STATUS, "Draft")));
        recalcTotal();
    }//GEN-LAST:event_tblDataMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBersihkan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbJenisPerpanjangan;
    private javax.swing.JComboBox<String> cmbKendaraan;
    private javax.swing.JComboBox<String> cmbStatus;
    private com.toedter.calendar.JDateChooser dtMasaBerlakuStnk;
    private com.toedter.calendar.JDateChooser dtTanggalPengurusan;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPanePemilik;
    private javax.swing.JLabel lblField1;
    private javax.swing.JLabel lblField10;
    private javax.swing.JLabel lblField2;
    private javax.swing.JLabel lblField3;
    private javax.swing.JLabel lblField4;
    private javax.swing.JLabel lblField5;
    private javax.swing.JLabel lblField6;
    private javax.swing.JLabel lblField7;
    private javax.swing.JLabel lblField8;
    private javax.swing.JLabel lblField9;
    private javax.swing.JLabel lblLoginUser;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtBiayaJasa;
    private javax.swing.JTextField txtBiayaPajak;
    private javax.swing.JTextField txtIdPembayaran;
    private javax.swing.JTextArea txtInfoPemilik;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
