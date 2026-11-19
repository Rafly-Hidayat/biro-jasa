package com.kelompok2.pajakform;

import java.awt.Dimension;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormPelanggan extends javax.swing.JFrame {

    private DefaultTableModel tableModel;

    public FormPelanggan() {
        initComponents();
        applyStyle();
        setupTable();
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
        Tampilan.labelKecil(lblLoginUser);
        lblLoginUser.setText("Login sebagai: " + Session.getCurrentUsername());
        Tampilan.isianReadOnly(txtIdPelanggan);
        Tampilan.isian(txtNama);
        Tampilan.isian(txtAlamat);
        Tampilan.isian(txtNoHp);
        Tampilan.tombolKecil(btnTambah);
        Tampilan.tombolKecil(btnUbah);
        Tampilan.tombolKecil(btnHapus);
        Tampilan.tombolKecil(btnBersihkan);
        Tampilan.tombolKecil(btnKembali);
        Tampilan.tabel(tblData);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(new Object[] { "Id Pelanggan", "Nama", "Alamat", "No. HP", "Operator" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData.setModel(tableModel);
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (LinkedHashMap<String, Object> r : PelangganStore.INSTANCE.all()) {
            tableModel.addRow(new Object[] {
                    r.get("id"),
                    r.get(PelangganStore.COL_NAMA),
                    r.get(PelangganStore.COL_ALAMAT),
                    r.get(PelangganStore.COL_NO_HP),
                    r.get(PelangganStore.COL_OPERATOR)
            });
        }
    }

    private void clearInput() {
        txtIdPelanggan.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        txtNoHp.setText("");
        tblData.clearSelection();
    }

    private boolean validateInput() {
        return Validasi.wajib(this, txtNama, "Nama")
                && Validasi.noHp(this, txtNoHp);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblLoginUser = new javax.swing.JLabel();
        lblField1 = new javax.swing.JLabel();
        txtIdPelanggan = new javax.swing.JTextField();
        lblField2 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblField3 = new javax.swing.JLabel();
        lblField4 = new javax.swing.JLabel();
        txtNoHp = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersihkan = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Pelanggan");
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 38)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Pelanggan");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 60, 1100, 60);

        lblLoginUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLoginUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoginUser.setText("Login sebagai: -");
        getContentPane().add(lblLoginUser);
        lblLoginUser.setBounds(820, 20, 260, 28);

        lblField1.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField1.setText("Id Pelanggan");
        getContentPane().add(lblField1);
        lblField1.setBounds(130, 175, 170, 38);

        txtIdPelanggan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtIdPelanggan.setBorder(null);
        getContentPane().add(txtIdPelanggan);
        txtIdPelanggan.setBounds(315, 180, 180, 28);

        lblField2.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField2.setText("Nama");
        getContentPane().add(lblField2);
        lblField2.setBounds(130, 230, 170, 38);

        txtNama.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNama.setBorder(null);
        getContentPane().add(txtNama);
        txtNama.setBounds(315, 235, 180, 28);

        lblField3.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField3.setText("Alamat");
        getContentPane().add(lblField3);
        lblField3.setBounds(560, 170, 170, 38);

        lblField4.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblField4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblField4.setText("No. HP");
        getContentPane().add(lblField4);
        lblField4.setBounds(130, 290, 170, 38);

        txtNoHp.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNoHp.setBorder(null);
        getContentPane().add(txtNoHp);
        txtNoHp.setBounds(315, 290, 180, 28);

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
        btnTambah.setBounds(130, 355, 120, 38);

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
        btnUbah.setBounds(265, 355, 120, 38);

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
        btnHapus.setBounds(400, 355, 120, 38);

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
        btnBersihkan.setBounds(535, 355, 120, 38);

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
        btnKembali.setBounds(830, 355, 120, 38);

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
        jScrollPane1.setBounds(130, 430, 820, 165);

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(680, 170, 270, 150);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if (!validateInput()) {
            return;
        }
        PelangganStore.INSTANCE.add(PelangganStore.newRow(
                txtNama.getText().trim(),
                txtAlamat.getText().trim(),
                txtNoHp.getText().trim(),
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
        String id = txtIdPelanggan.getText().trim();
        LinkedHashMap<String, Object> existing = PelangganStore.INSTANCE.findById(id);
        String originalOperator = existing == null ? Session.getCurrentUsername()
                : String.valueOf(existing.getOrDefault(PelangganStore.COL_OPERATOR, ""));

        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put("id", id);
        r.put(PelangganStore.COL_NAMA, txtNama.getText().trim());
        r.put(PelangganStore.COL_ALAMAT, txtAlamat.getText().trim());
        r.put(PelangganStore.COL_NO_HP, txtNoHp.getText().trim());
        r.put(PelangganStore.COL_OPERATOR, originalOperator);
        PelangganStore.INSTANCE.update(id, r);
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
        String nama = String.valueOf(tableModel.getValueAt(selectedRow, 1));
        if (!Validasi.konfirmasiHapus(this, "pelanggan '" + nama + "'")) {
            return;
        }
        try {
            PelangganStore.INSTANCE.delete(id);
        } catch (StoreException ex) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat menghapus pelanggan: data masih dipakai oleh kendaraan.\n" + ex.getMessage(),
                    "Operasi Ditolak", JOptionPane.WARNING_MESSAGE);
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
            txtIdPelanggan.setText(String.valueOf(tableModel.getValueAt(row, 0)));
            txtNama.setText(String.valueOf(tableModel.getValueAt(row, 1)));
            txtAlamat.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            txtNoHp.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        }
    }//GEN-LAST:event_tblDataMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBersihkan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblField1;
    private javax.swing.JLabel lblField2;
    private javax.swing.JLabel lblField3;
    private javax.swing.JLabel lblField4;
    private javax.swing.JLabel lblLoginUser;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblData;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtIdPelanggan;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoHp;
    // End of variables declaration//GEN-END:variables
}
