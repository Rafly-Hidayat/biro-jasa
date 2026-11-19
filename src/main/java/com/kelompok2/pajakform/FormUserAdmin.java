package com.kelompok2.pajakform;

import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormUserAdmin extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private String selectedId = null;

    public FormUserAdmin() {
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
        Tampilan.label(lblField5);
        Tampilan.isianReadOnly(txtIdUser);
        Tampilan.isian(txtUsername);
        Tampilan.isian(txtPassword);
        Tampilan.isian(txtNamaLengkap);
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
                new Object[] { "Id User", "Username", "Nama Lengkap", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData.setModel(tableModel);
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (UserStore.User u : UserStore.all()) {
            tableModel.addRow(new Object[] { u.id, u.username, u.namaLengkap, u.status });
        }
    }

    private void clearInput() {
        selectedId = null;
        txtIdUser.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtNamaLengkap.setText("");
        cmbStatus.setSelectedIndex(0);
        tblData.clearSelection();
    }

    private boolean validateInput(boolean requirePassword) {
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username wajib diisi.");
            return false;
        }
        if (requirePassword && new String(txtPassword.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password wajib diisi.");
            return false;
        }
        if (UserStore.isUsernameTaken(txtUsername.getText().trim(), selectedId)) {
            JOptionPane.showMessageDialog(this, "Username sudah digunakan user lain.");
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblField1 = new javax.swing.JLabel();
        txtIdUser = new javax.swing.JTextField();
        lblField2 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblField3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblField4 = new javax.swing.JLabel();
        txtNamaLengkap = new javax.swing.JTextField();
        lblField5 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersihkan = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User Admin");
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 38)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("User Admin");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 60, 1100, 60);

        lblField1.setText("Id User");
        getContentPane().add(lblField1);
        lblField1.setBounds(130, 175, 170, 38);
        getContentPane().add(txtIdUser);
        txtIdUser.setBounds(315, 180, 220, 28);

        lblField2.setText("Username");
        getContentPane().add(lblField2);
        lblField2.setBounds(130, 230, 170, 38);
        getContentPane().add(txtUsername);
        txtUsername.setBounds(315, 235, 220, 28);

        lblField3.setText("Password");
        getContentPane().add(lblField3);
        lblField3.setBounds(130, 285, 170, 38);
        getContentPane().add(txtPassword);
        txtPassword.setBounds(315, 290, 220, 28);

        lblField4.setText("Nama Lengkap");
        getContentPane().add(lblField4);
        lblField4.setBounds(590, 175, 170, 38);
        getContentPane().add(txtNamaLengkap);
        txtNamaLengkap.setBounds(770, 180, 220, 28);

        lblField5.setText("Status");
        getContentPane().add(lblField5);
        lblField5.setBounds(590, 230, 170, 38);
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Aktif", "Nonaktif" }));
        getContentPane().add(cmbStatus);
        cmbStatus.setBounds(770, 235, 220, 28);

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambah);
        btnTambah.setBounds(130, 355, 120, 38);

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        getContentPane().add(btnUbah);
        btnUbah.setBounds(265, 355, 120, 38);

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus);
        btnHapus.setBounds(400, 355, 120, 38);

        btnBersihkan.setText("Bersihkan");
        btnBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihkanActionPerformed(evt);
            }
        });
        getContentPane().add(btnBersihkan);
        btnBersihkan.setBounds(535, 355, 120, 38);

        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btnKembali);
        btnKembali.setBounds(830, 355, 120, 38);

        tblData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {

                }));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblData);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(130, 430, 820, 165);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        selectedId = null;
        if (!validateInput(true)) return;
        UserStore.add(
                txtUsername.getText().trim(),
                new String(txtPassword.getPassword()).trim(),
                txtNamaLengkap.getText().trim(),
                String.valueOf(cmbStatus.getSelectedItem()));
        reloadTable();
        clearInput();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Pilih user di tabel terlebih dahulu.");
            return;
        }
        if (!validateInput(false)) return;

        String username = txtUsername.getText().trim();
        String namaLengkap = txtNamaLengkap.getText().trim();
        String status = String.valueOf(cmbStatus.getSelectedItem());
        String password = new String(txtPassword.getPassword()).trim();

        if (UserStore.isDefaultAdmin(selectedId)) {
            UserStore.User existing = UserStore.findById(selectedId);
            if (existing != null) {
                username = existing.username;
                status = UserStore.STATUS_AKTIF;
            }
        }

        UserStore.update(selectedId, username, password, namaLengkap, status);
        reloadTable();
        clearInput();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Pilih user di tabel terlebih dahulu.");
            return;
        }
        if (UserStore.isDefaultAdmin(selectedId)) {
            JOptionPane.showMessageDialog(this,
                    "User admin default tidak boleh dihapus.",
                    "Operasi Ditolak", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String username = txtUsername.getText().trim();
        if (!Validasi.konfirmasiHapus(this, "user '" + username + "'")) {
            return;
        }
        try {
            UserStore.delete(selectedId);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Operasi Ditolak", JOptionPane.WARNING_MESSAGE);
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
        if (row < 0) return;
        String id = String.valueOf(tableModel.getValueAt(row, 0));
        UserStore.User u = UserStore.findById(id);
        if (u == null) return;
        selectedId = u.id;
        txtIdUser.setText(u.id);
        txtUsername.setText(u.username);
        txtPassword.setText("");
        txtNamaLengkap.setText(u.namaLengkap);
        cmbStatus.setSelectedItem(u.status);
    }//GEN-LAST:event_tblDataMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBersihkan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblField1;
    private javax.swing.JLabel lblField2;
    private javax.swing.JLabel lblField3;
    private javax.swing.JLabel lblField4;
    private javax.swing.JLabel lblField5;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtIdUser;
    private javax.swing.JTextField txtNamaLengkap;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
