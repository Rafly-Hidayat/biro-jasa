package com.kelompok2.pajakform;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormKendaraan extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private final List<String> pelangganIds = new ArrayList<>();
    private String fileStnk = "";

    public FormKendaraan() {
        initComponents();
        applyStyle();
        setupTable();
        reloadPelangganCombo();
        reloadTable();
        clearInput();
        Tampilan.window(this);
        setLocationRelativeTo(null);
        Responsive.enable(this);
    }

    private void applyStyle() {
        getContentPane().setBackground(Tampilan.BACKGROUND);
        Tampilan.judul(lblTitle, Tampilan.SIZE_DASHBOARD);
        Tampilan.label(lblField1);
        Tampilan.label(lblField2);
        Tampilan.label(lblField3);
        Tampilan.label(lblField4);
        Tampilan.label(lblField5);
        Tampilan.label(lblStnk);
        Tampilan.labelKecil(lblLoginUser);
        lblLoginUser.setText("Login sebagai: " + Session.getCurrentUsername());
        Tampilan.isianReadOnly(txtIdKendaraan);
        Tampilan.isian(txtNoPolisi);
        Tampilan.isian(txtMerk);
        Tampilan.isian(txtTahun);
        Tampilan.combo(cmbPelanggan);
        Tampilan.tombolKecil(btnUploadStnk);
        Tampilan.labelKecil(lblFileStnkVal);
        FileUploader.pasangTombolLihat(this, btnUploadStnk, lblFileStnkVal, () -> fileStnk);
        Tampilan.tombolKecil(btnTambah);
        Tampilan.tombolKecil(btnUbah);
        Tampilan.tombolKecil(btnHapus);
        Tampilan.tombolKecil(btnBersihkan);
        Tampilan.tombolKecil(btnKembali);
        Tampilan.tabel(tblData);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(
                new Object[] { "Id Kendaraan", "No. Polisi", "Merk", "Tahun", "Pelanggan", "File STNK", "Operator" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData.setModel(tableModel);
    }

    private void reloadPelangganCombo() {
        cmbPelanggan.removeAllItems();
        pelangganIds.clear();
        cmbPelanggan.addItem("-- Pilih pelanggan --");
        pelangganIds.add("");
        for (LinkedHashMap<String, Object> p : PelangganStore.INSTANCE.all()) {
            String id = String.valueOf(p.get("id"));
            String nama = String.valueOf(p.getOrDefault(PelangganStore.COL_NAMA, ""));
            cmbPelanggan.addItem(nama + " (" + id + ")");
            pelangganIds.add(id);
        }
    }

    private String getSelectedPelangganId() {
        int i = cmbPelanggan.getSelectedIndex();
        if (i < 0 || i >= pelangganIds.size()) {
            return "";
        }
        return pelangganIds.get(i);
    }

    private void selectPelangganById(String id) {
        if (id == null) {
            cmbPelanggan.setSelectedIndex(0);
            return;
        }
        String key = id.trim();
        for (int i = 0; i < pelangganIds.size(); i++) {
            if (pelangganIds.get(i).equals(key)) {
                cmbPelanggan.setSelectedIndex(i);
                return;
            }
        }
        cmbPelanggan.setSelectedIndex(0);
    }

    private static String namaPelanggan(String idPelanggan) {
        if (idPelanggan == null || idPelanggan.isBlank()) {
            return "";
        }
        LinkedHashMap<String, Object> p = PelangganStore.INSTANCE.findById(idPelanggan.trim());
        if (p == null) {
            return idPelanggan;
        }
        return String.valueOf(p.getOrDefault(PelangganStore.COL_NAMA, ""));
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (LinkedHashMap<String, Object> r : KendaraanStore.INSTANCE.all()) {
            String idPlg = String.valueOf(r.getOrDefault(KendaraanStore.COL_ID_PELANGGAN, ""));
            tableModel.addRow(new Object[] {
                    r.get("id"),
                    r.get(KendaraanStore.COL_NO_POLISI),
                    r.get(KendaraanStore.COL_MERK),
                    r.get(KendaraanStore.COL_TAHUN),
                    namaPelanggan(idPlg),
                    r.get(KendaraanStore.COL_FILE_STNK),
                    r.get(KendaraanStore.COL_OPERATOR)
            });
        }
    }

    private void clearInput() {
        txtIdKendaraan.setText("");
        txtNoPolisi.setText("");
        txtMerk.setText("");
        txtTahun.setText("");
        selectPelangganById("");
        fileStnk = "";
        lblFileStnkVal.setText("(kosong)");
        tblData.clearSelection();
    }

    private boolean validateInput() {
        if (!Validasi.noPolisi(this, txtNoPolisi)) {
            return false;
        }
        if (!Validasi.wajib(this, txtMerk, "Merk kendaraan")) {
            return false;
        }
        if (!Validasi.tahun(this, txtTahun)) {
            return false;
        }
        if (getSelectedPelangganId().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan terdaftar.",
                    "Validasi Input", JOptionPane.WARNING_MESSAGE);
            cmbPelanggan.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblLoginUser = new javax.swing.JLabel();
        lblField1 = new javax.swing.JLabel();
        txtIdKendaraan = new javax.swing.JTextField();
        lblField2 = new javax.swing.JLabel();
        txtNoPolisi = new javax.swing.JTextField();
        lblField3 = new javax.swing.JLabel();
        txtMerk = new javax.swing.JTextField();
        lblField4 = new javax.swing.JLabel();
        txtTahun = new javax.swing.JTextField();
        lblField5 = new javax.swing.JLabel();
        cmbPelanggan = new javax.swing.JComboBox<>();
        lblStnk = new javax.swing.JLabel();
        btnUploadStnk = new javax.swing.JButton();
        lblFileStnkVal = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersihkan = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Kendaraan");
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 38)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Kendaraan");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 60, 1100, 60);

        lblLoginUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLoginUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoginUser.setText("Login sebagai: -");
        getContentPane().add(lblLoginUser);
        lblLoginUser.setBounds(820, 20, 260, 28);

        lblField1.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField1.setText("Id Kendaraan");
        getContentPane().add(lblField1);
        lblField1.setBounds(130, 175, 170, 38);

        txtIdKendaraan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtIdKendaraan.setBorder(null);
        getContentPane().add(txtIdKendaraan);
        txtIdKendaraan.setBounds(315, 180, 180, 28);

        lblField2.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField2.setText("No. Polisi");
        getContentPane().add(lblField2);
        lblField2.setBounds(130, 230, 170, 38);

        txtNoPolisi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNoPolisi.setBorder(null);
        getContentPane().add(txtNoPolisi);
        txtNoPolisi.setBounds(315, 235, 180, 28);

        lblField3.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField3.setText("Merk");
        getContentPane().add(lblField3);
        lblField3.setBounds(130, 285, 170, 38);

        txtMerk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMerk.setBorder(null);
        getContentPane().add(txtMerk);
        txtMerk.setBounds(315, 290, 180, 28);

        lblField4.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField4.setText("Tahun");
        getContentPane().add(lblField4);
        lblField4.setBounds(590, 175, 170, 38);

        txtTahun.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTahun.setBorder(null);
        getContentPane().add(txtTahun);
        txtTahun.setBounds(770, 180, 180, 28);

        lblField5.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField5.setText("Pelanggan");
        getContentPane().add(lblField5);
        lblField5.setBounds(590, 230, 170, 38);

        getContentPane().add(cmbPelanggan);
        cmbPelanggan.setBounds(770, 235, 280, 28);

        lblStnk.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblStnk.setText("Upload STNK");
        getContentPane().add(lblStnk);
        lblStnk.setBounds(590, 280, 170, 28);

        btnUploadStnk.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUploadStnk.setText("Pilih File...");
        btnUploadStnk.setBorderPainted(false);
        btnUploadStnk.setFocusPainted(false);
        btnUploadStnk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadStnkActionPerformed(evt);
            }
        });
        getContentPane().add(btnUploadStnk);
        btnUploadStnk.setBounds(770, 282, 110, 28);

        lblFileStnkVal.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblFileStnkVal.setText("(kosong)");
        getContentPane().add(lblFileStnkVal);
        lblFileStnkVal.setBounds(890, 282, 380, 28);

        btnTambah.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambah);
        btnTambah.setBounds(130, 345, 120, 38);

        btnUbah.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnUbah.setBorderPainted(false);
        btnUbah.setFocusPainted(false);
        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        getContentPane().add(btnUbah);
        btnUbah.setBounds(265, 345, 120, 38);

        btnHapus.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnHapus.setBorderPainted(false);
        btnHapus.setFocusPainted(false);
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus);
        btnHapus.setBounds(400, 345, 120, 38);

        btnBersihkan.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnBersihkan.setBorderPainted(false);
        btnBersihkan.setFocusPainted(false);
        btnBersihkan.setText("Bersihkan");
        btnBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihkanActionPerformed(evt);
            }
        });
        getContentPane().add(btnBersihkan);
        btnBersihkan.setBounds(535, 345, 120, 38);

        btnKembali.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnKembali.setBorderPainted(false);
        btnKembali.setFocusPainted(false);
        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btnKembali);
        btnKembali.setBounds(830, 345, 120, 38);

        tblData.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {

                }));
        tblData.setRowHeight(28);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblData);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(130, 420, 820, 200);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUploadStnkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadStnkActionPerformed
        String p = FileUploader.upload(this, "stnk");
        if (p != null) {
            fileStnk = p;
            lblFileStnkVal.setText(p);
        }
    }//GEN-LAST:event_btnUploadStnkActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if (!validateInput()) {
            return;
        }
        KendaraanStore.INSTANCE.add(KendaraanStore.newRow(
                txtNoPolisi.getText().trim(),
                txtMerk.getText().trim(),
                txtTahun.getText().trim(),
                getSelectedPelangganId(),
                fileStnk,
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
        String id = txtIdKendaraan.getText().trim();
        LinkedHashMap<String, Object> existing = KendaraanStore.INSTANCE.findById(id);
        String originalOperator = existing == null ? Session.getCurrentUsername()
                : String.valueOf(existing.getOrDefault(KendaraanStore.COL_OPERATOR, ""));

        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put("id", id);
        r.put(KendaraanStore.COL_NO_POLISI, txtNoPolisi.getText().trim());
        r.put(KendaraanStore.COL_MERK, txtMerk.getText().trim());
        r.put(KendaraanStore.COL_TAHUN, txtTahun.getText().trim());
        r.put(KendaraanStore.COL_ID_PELANGGAN, getSelectedPelangganId());
        r.put(KendaraanStore.COL_FILE_STNK, fileStnk);
        r.put(KendaraanStore.COL_OPERATOR, originalOperator);
        KendaraanStore.INSTANCE.update(id, r);
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
        if (!Validasi.konfirmasiHapus(this, "kendaraan '" + noPolisi + "'")) {
            return;
        }
        try {
            KendaraanStore.INSTANCE.delete(id);
        } catch (StoreException ex) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat menghapus kendaraan: masih dipakai data pembayaran pajak.\n" + ex.getMessage());
            return;
        }
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
        if (row >= 0) {
            String id = String.valueOf(tableModel.getValueAt(row, 0));
            LinkedHashMap<String, Object> r = KendaraanStore.INSTANCE.findById(id);
            if (r == null) {
                return;
            }
            reloadPelangganCombo();
            txtIdKendaraan.setText(id);
            txtNoPolisi.setText(String.valueOf(r.getOrDefault(KendaraanStore.COL_NO_POLISI, "")));
            txtMerk.setText(String.valueOf(r.getOrDefault(KendaraanStore.COL_MERK, "")));
            txtTahun.setText(String.valueOf(r.getOrDefault(KendaraanStore.COL_TAHUN, "")));
            selectPelangganById(String.valueOf(r.getOrDefault(KendaraanStore.COL_ID_PELANGGAN, "")));
            fileStnk = String.valueOf(r.getOrDefault(KendaraanStore.COL_FILE_STNK, ""));
            lblFileStnkVal.setText(fileStnk.isEmpty() ? "(kosong)" : fileStnk);
        }
    }//GEN-LAST:event_tblDataMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBersihkan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JButton btnUploadStnk;
    private javax.swing.JComboBox<String> cmbPelanggan;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblField1;
    private javax.swing.JLabel lblField2;
    private javax.swing.JLabel lblField3;
    private javax.swing.JLabel lblField4;
    private javax.swing.JLabel lblField5;
    private javax.swing.JLabel lblFileStnkVal;
    private javax.swing.JLabel lblLoginUser;
    private javax.swing.JLabel lblStnk;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtIdKendaraan;
    private javax.swing.JTextField txtMerk;
    private javax.swing.JTextField txtNoPolisi;
    private javax.swing.JTextField txtTahun;
    // End of variables declaration//GEN-END:variables
}
