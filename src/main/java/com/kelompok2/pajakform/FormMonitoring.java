package com.kelompok2.pajakform;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.swing.table.DefaultTableModel;

public class FormMonitoring extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy");

    public FormMonitoring() {
        initComponents();
        applyStyle();
        setupTable();
        reloadTable();
        Tampilan.window(this);
        setLocationRelativeTo(null);
        Responsive.enable(this);
    }

    private void applyStyle() {
        getContentPane().setBackground(Tampilan.BACKGROUND);
        Tampilan.judul(lblTitle, Tampilan.SIZE_DASHBOARD);
        Tampilan.labelKecil(lblLoginUser);
        lblLoginUser.setText("Login sebagai: " + Session.getCurrentUsername());
        Tampilan.tombolKecil(btnKembali);
        Tampilan.tabel(tblData);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(
                new Object[] { "ID Transaksi", "Jenis", "No Polisi", "Nama Pemilik", "Tanggal Pengurusan", "Status", "Operator" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData.setModel(tableModel);
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (LinkedHashMap<String, Object> r : PembayaranPajakStore.INSTANCE.all()) {
            Date d = (Date) r.get(PembayaranPajakStore.COL_TGL_PENGURUSAN);
            String kid = String.valueOf(r.getOrDefault(PembayaranPajakStore.COL_KENDARAAN_ID, ""));
            tableModel.addRow(new Object[] {
                    r.get("id"),
                    "Pajak",
                    DataLookup.kendaraanNoPolisi(kid),
                    DataLookup.namaPemilikKendaraan(kid),
                    d != null ? DATE_FMT.format(d) : "",
                    r.get(PembayaranPajakStore.COL_STATUS),
                    r.get(PembayaranPajakStore.COL_OPERATOR)
            });
        }
        for (LinkedHashMap<String, Object> r : BalikNamaStore.INSTANCE.all()) {
            Date d = (Date) r.get(BalikNamaStore.COL_TGL_PENGURUSAN);
            tableModel.addRow(new Object[] {
                    r.get("id"),
                    "Balik Nama",
                    r.get(BalikNamaStore.COL_NO_POLISI),
                    r.get(BalikNamaStore.COL_PEMILIK_BARU),
                    d != null ? DATE_FMT.format(d) : "",
                    r.get(BalikNamaStore.COL_STATUS),
                    r.get(BalikNamaStore.COL_OPERATOR)
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblLoginUser = new javax.swing.JLabel();
        btnKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pemantauan Proses");
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 38)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Pemantauan Proses");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 60, 1100, 60);

        lblLoginUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLoginUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoginUser.setText("Login sebagai: -");
        getContentPane().add(lblLoginUser);
        lblLoginUser.setBounds(820, 20, 260, 28);

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
        btnKembali.setBounds(470, 155, 160, 38);

        tblData.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {}));
        tblData.setRowHeight(28);
        jScrollPane1.setViewportView(tblData);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(80, 215, 940, 370);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        new FormMenu().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnKembaliActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLoginUser;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblData;
    // End of variables declaration//GEN-END:variables
}
