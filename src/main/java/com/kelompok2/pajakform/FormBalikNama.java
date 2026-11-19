package com.kelompok2.pajakform;

import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FormBalikNama extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy");
    private static final NumberFormat RUPIAH = NumberFormat.getInstance(new Locale("id", "ID"));

    private String fileKtp = "";
    private String fileStnk = "";
    private String fileBpkb = "";
    private String fileKwitansi = "";

    public FormBalikNama() {
        initComponents();
        applyStyle();
        setupTable();
        attachAutoTotal();
        reloadTable();
        clearInput();

        Tampilan.window(this);
        setLocationRelativeTo(null);
        Responsive.enable(this);
    }

    private void applyStyle() {
        getContentPane().setBackground(Tampilan.BACKGROUND);
        Tampilan.judul(lblTitle, Tampilan.SIZE_PAGE_TITLE);
        Tampilan.labelKecil(lblLoginUser);
        lblLoginUser.setText("Login sebagai: " + Session.getCurrentUsername());

        Tampilan.label(lblNoTransaksi);
        Tampilan.label(lblNoPolisi);
        Tampilan.label(lblPemilikLama);
        Tampilan.label(lblPemilikBaru);
        Tampilan.label(lblNik);
        Tampilan.label(lblAlamat);
        Tampilan.label(lblJenisKendaraan);
        Tampilan.label(lblMerk);
        Tampilan.label(lblTipe);
        Tampilan.label(lblTahun);
        Tampilan.label(lblRangka);
        Tampilan.label(lblMesin);
        Tampilan.label(lblTgl);

        Tampilan.label(lblBiayaPajak);
        Tampilan.label(lblBiayaBbnkb);
        Tampilan.label(lblBiayaJasa);
        Tampilan.label(lblBiayaLain);
        Tampilan.label(lblTotal);
        Tampilan.label(lblStatus);
        Tampilan.label(lblFileKtp);
        Tampilan.label(lblFileStnk);
        Tampilan.label(lblFileBpkb);
        Tampilan.label(lblFileKwitansi);
        Tampilan.label(lblCatatan);

        Tampilan.isianReadOnly(txtNoTransaksi);
        Tampilan.isian(txtNoPolisi);
        Tampilan.isian(txtPemilikLama);
        Tampilan.isian(txtPemilikBaru);
        Tampilan.isian(txtNik);
        Tampilan.isian(txtAlamat);
        Tampilan.combo(cmbJenisKendaraan);
        cmbJenisKendaraan.setEditable(true);
        Tampilan.combo(cmbMerk);
        cmbMerk.setEditable(true);
        Tampilan.isian(txtTipe);
        Tampilan.isian(txtTahun);
        Tampilan.isian(txtRangka);
        Tampilan.isian(txtMesin);
        Tampilan.dateChooser(dtTanggalPengurusan);

        Tampilan.isian(txtBiayaPajak);
        Tampilan.isian(txtBiayaBbnkb);
        Tampilan.isian(txtBiayaJasa);
        Tampilan.isian(txtBiayaLain);
        Tampilan.isianReadOnly(txtTotalBiaya);
        Tampilan.combo(cmbStatus);
        Tampilan.isian(txtCatatan);

        Tampilan.tombolKecil(btnUploadKtp);
        Tampilan.tombolKecil(btnUploadStnk);
        Tampilan.tombolKecil(btnUploadBpkb);
        Tampilan.tombolKecil(btnUploadKwitansi);
        Tampilan.labelKecil(lblFileKtpVal);
        Tampilan.labelKecil(lblFileStnkVal);
        Tampilan.labelKecil(lblFileBpkbVal);
        Tampilan.labelKecil(lblFileKwitansiVal);

        FileUploader.pasangTombolLihat(this, btnUploadKtp, lblFileKtpVal, () -> fileKtp);
        FileUploader.pasangTombolLihat(this, btnUploadStnk, lblFileStnkVal, () -> fileStnk);
        FileUploader.pasangTombolLihat(this, btnUploadBpkb, lblFileBpkbVal, () -> fileBpkb);
        FileUploader.pasangTombolLihat(this, btnUploadKwitansi, lblFileKwitansiVal, () -> fileKwitansi);

        Tampilan.tombolKecil(btnTambah);
        Tampilan.tombolKecil(btnUbah);
        Tampilan.tombolKecil(btnHapus);
        Tampilan.tombolKecil(btnBersihkan);
        Tampilan.tombolKecil(btnKembali);
        Tampilan.tabel(tblData);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(
                new Object[] { "No Transaksi", "No. Polisi", "Pemilik Baru", "Tgl Pengurusan", "Total Biaya", "Status", "Operator" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData.setModel(tableModel);
    }

    private void attachAutoTotal() {
        DocumentListener dl = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { recalcTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { recalcTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { recalcTotal(); }
        };
        txtBiayaPajak.getDocument().addDocumentListener(dl);
        txtBiayaBbnkb.getDocument().addDocumentListener(dl);
        txtBiayaJasa.getDocument().addDocumentListener(dl);
        txtBiayaLain.getDocument().addDocumentListener(dl);
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
        double total = parseDouble(txtBiayaPajak.getText())
                + parseDouble(txtBiayaBbnkb.getText())
                + parseDouble(txtBiayaJasa.getText())
                + parseDouble(txtBiayaLain.getText());
        txtTotalBiaya.setText(formatRupiah(total));
    }

    private static String formatRupiah(double v) {
        return RUPIAH.format(v);
    }

    private static String fmtDate(Date d) {
        return d == null ? "" : DATE_FMT.format(d);
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (LinkedHashMap<String, Object> r : BalikNamaStore.INSTANCE.all()) {
            tableModel.addRow(new Object[] {
                    r.get("id"),
                    r.get(BalikNamaStore.COL_NO_POLISI),
                    r.get(BalikNamaStore.COL_PEMILIK_BARU),
                    fmtDate((Date) r.get(BalikNamaStore.COL_TGL_PENGURUSAN)),
                    formatRupiah(((Number) r.getOrDefault(BalikNamaStore.COL_TOTAL, 0)).doubleValue()),
                    r.get(BalikNamaStore.COL_STATUS),
                    r.get(BalikNamaStore.COL_OPERATOR)
            });
        }
    }

    private void clearInput() {
        txtNoTransaksi.setText("");
        txtNoPolisi.setText("");
        txtPemilikLama.setText("");
        txtPemilikBaru.setText("");
        txtNik.setText("");
        txtAlamat.setText("");
        cmbJenisKendaraan.setSelectedIndex(0);
        cmbMerk.setSelectedIndex(0);
        txtTipe.setText("");
        txtTahun.setText("");
        txtRangka.setText("");
        txtMesin.setText("");
        dtTanggalPengurusan.setDate(null);
        txtBiayaPajak.setText("");
        txtBiayaBbnkb.setText("");
        txtBiayaJasa.setText("");
        txtBiayaLain.setText("");
        txtTotalBiaya.setText("");
        cmbStatus.setSelectedIndex(0);
        txtCatatan.setText("");
        fileKtp = "";
        fileStnk = "";
        fileBpkb = "";
        fileKwitansi = "";
        lblFileKtpVal.setText("(kosong)");
        lblFileStnkVal.setText("(kosong)");
        lblFileBpkbVal.setText("(kosong)");
        lblFileKwitansiVal.setText("(kosong)");
        tblData.clearSelection();
    }

    private boolean validateInput() {
        return Validasi.noPolisi(this, txtNoPolisi)
                && Validasi.wajib(this, txtPemilikBaru, "Nama pemilik baru")
                && Validasi.nik(this, txtNik)
                && Validasi.tahun(this, txtTahun)
                && Validasi.tanggal(this, dtTanggalPengurusan.getDate(), dtTanggalPengurusan, "Tanggal pengurusan")
                && Validasi.biaya(this, txtBiayaPajak, "Biaya pajak")
                && Validasi.biaya(this, txtBiayaBbnkb, "Biaya BBNKB")
                && Validasi.biaya(this, txtBiayaJasa, "Biaya jasa")
                && Validasi.biaya(this, txtBiayaLain, "Biaya lain");
    }

    private int parseInt(String s) {
        return Validasi.parseTahun(s);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblLoginUser = new javax.swing.JLabel();

        lblNoTransaksi = new javax.swing.JLabel();
        txtNoTransaksi = new javax.swing.JTextField();
        lblNoPolisi = new javax.swing.JLabel();
        txtNoPolisi = new javax.swing.JTextField();
        lblPemilikLama = new javax.swing.JLabel();
        txtPemilikLama = new javax.swing.JTextField();
        lblPemilikBaru = new javax.swing.JLabel();
        txtPemilikBaru = new javax.swing.JTextField();
        lblNik = new javax.swing.JLabel();
        txtNik = new javax.swing.JTextField();
        lblAlamat = new javax.swing.JLabel();
        jScrollPaneAlamat = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        lblJenisKendaraan = new javax.swing.JLabel();
        cmbJenisKendaraan = new javax.swing.JComboBox<>();
        lblMerk = new javax.swing.JLabel();
        cmbMerk = new javax.swing.JComboBox<>();
        lblTipe = new javax.swing.JLabel();
        txtTipe = new javax.swing.JTextField();
        lblTahun = new javax.swing.JLabel();
        txtTahun = new javax.swing.JTextField();
        lblRangka = new javax.swing.JLabel();
        txtRangka = new javax.swing.JTextField();
        lblMesin = new javax.swing.JLabel();
        txtMesin = new javax.swing.JTextField();
        lblTgl = new javax.swing.JLabel();
        dtTanggalPengurusan = new com.toedter.calendar.JDateChooser();

        lblBiayaPajak = new javax.swing.JLabel();
        txtBiayaPajak = new javax.swing.JTextField();
        lblBiayaBbnkb = new javax.swing.JLabel();
        txtBiayaBbnkb = new javax.swing.JTextField();
        lblBiayaJasa = new javax.swing.JLabel();
        txtBiayaJasa = new javax.swing.JTextField();
        lblBiayaLain = new javax.swing.JLabel();
        txtBiayaLain = new javax.swing.JTextField();
        lblTotal = new javax.swing.JLabel();
        txtTotalBiaya = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();

        lblFileKtp = new javax.swing.JLabel();
        btnUploadKtp = new javax.swing.JButton();
        lblFileKtpVal = new javax.swing.JLabel();
        lblFileStnk = new javax.swing.JLabel();
        btnUploadStnk = new javax.swing.JButton();
        lblFileStnkVal = new javax.swing.JLabel();
        lblFileBpkb = new javax.swing.JLabel();
        btnUploadBpkb = new javax.swing.JButton();
        lblFileBpkbVal = new javax.swing.JLabel();
        lblFileKwitansi = new javax.swing.JLabel();
        btnUploadKwitansi = new javax.swing.JButton();
        lblFileKwitansiVal = new javax.swing.JLabel();

        lblCatatan = new javax.swing.JLabel();
        jScrollPaneCatatan = new javax.swing.JScrollPane();
        txtCatatan = new javax.swing.JTextArea();

        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersihkan = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Balik Nama Kendaraan");
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Balik Nama Kendaraan");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 10, 1240, 40);

        lblLoginUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLoginUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoginUser.setText("Login sebagai: -");
        getContentPane().add(lblLoginUser);
        lblLoginUser.setBounds(940, 25, 270, 24);

        // === KOLOM KIRI ===
        lblNoTransaksi.setText("No Transaksi");
        getContentPane().add(lblNoTransaksi);
        lblNoTransaksi.setBounds(40, 65, 170, 28);
        getContentPane().add(txtNoTransaksi);
        txtNoTransaksi.setBounds(215, 67, 200, 28);

        lblNoPolisi.setText("No. Polisi");
        getContentPane().add(lblNoPolisi);
        lblNoPolisi.setBounds(40, 100, 170, 28);
        getContentPane().add(txtNoPolisi);
        txtNoPolisi.setBounds(215, 102, 200, 28);

        lblPemilikLama.setText("Pemilik Lama");
        getContentPane().add(lblPemilikLama);
        lblPemilikLama.setBounds(40, 135, 170, 28);
        getContentPane().add(txtPemilikLama);
        txtPemilikLama.setBounds(215, 137, 200, 28);

        lblPemilikBaru.setText("Pemilik Baru");
        getContentPane().add(lblPemilikBaru);
        lblPemilikBaru.setBounds(40, 170, 170, 28);
        getContentPane().add(txtPemilikBaru);
        txtPemilikBaru.setBounds(215, 172, 200, 28);

        lblNik.setText("NIK Pemilik Baru");
        getContentPane().add(lblNik);
        lblNik.setBounds(40, 205, 170, 28);
        getContentPane().add(txtNik);
        txtNik.setBounds(215, 207, 200, 28);

        lblAlamat.setText("Alamat Baru");
        getContentPane().add(lblAlamat);
        lblAlamat.setBounds(40, 240, 170, 28);
        txtAlamat.setRows(3);
        jScrollPaneAlamat.setViewportView(txtAlamat);
        getContentPane().add(jScrollPaneAlamat);
        jScrollPaneAlamat.setBounds(215, 240, 200, 60);

        lblJenisKendaraan.setText("Jenis Kendaraan");
        getContentPane().add(lblJenisKendaraan);
        lblJenisKendaraan.setBounds(40, 305, 170, 28);
        cmbJenisKendaraan.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Mobil", "Motor", "Truk", "Bus" }));
        getContentPane().add(cmbJenisKendaraan);
        cmbJenisKendaraan.setBounds(215, 307, 200, 28);

        lblMerk.setText("Merk Kendaraan");
        getContentPane().add(lblMerk);
        lblMerk.setBounds(40, 340, 170, 28);
        cmbMerk.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Honda", "Toyota", "Yamaha", "Suzuki", "Mitsubishi", "Daihatsu" }));
        getContentPane().add(cmbMerk);
        cmbMerk.setBounds(215, 342, 200, 28);

        lblTipe.setText("Tipe Kendaraan");
        getContentPane().add(lblTipe);
        lblTipe.setBounds(40, 375, 170, 28);
        getContentPane().add(txtTipe);
        txtTipe.setBounds(215, 377, 200, 28);

        lblTahun.setText("Tahun Kendaraan");
        getContentPane().add(lblTahun);
        lblTahun.setBounds(40, 410, 170, 28);
        getContentPane().add(txtTahun);
        txtTahun.setBounds(215, 412, 200, 28);

        lblRangka.setText("Nomor Rangka");
        getContentPane().add(lblRangka);
        lblRangka.setBounds(40, 445, 170, 28);
        getContentPane().add(txtRangka);
        txtRangka.setBounds(215, 447, 200, 28);

        lblMesin.setText("Nomor Mesin");
        getContentPane().add(lblMesin);
        lblMesin.setBounds(40, 480, 170, 28);
        getContentPane().add(txtMesin);
        txtMesin.setBounds(215, 482, 200, 28);

        lblTgl.setText("Tgl Pengurusan");
        getContentPane().add(lblTgl);
        lblTgl.setBounds(40, 515, 170, 28);
        getContentPane().add(dtTanggalPengurusan);
        dtTanggalPengurusan.setBounds(215, 517, 200, 28);

        // === KOLOM TENGAH (Biaya & Status) ===
        lblBiayaPajak.setText("Biaya Pajak");
        getContentPane().add(lblBiayaPajak);
        lblBiayaPajak.setBounds(450, 65, 150, 28);
        getContentPane().add(txtBiayaPajak);
        txtBiayaPajak.setBounds(605, 67, 200, 28);

        lblBiayaBbnkb.setText("Biaya BBNKB");
        getContentPane().add(lblBiayaBbnkb);
        lblBiayaBbnkb.setBounds(450, 100, 150, 28);
        getContentPane().add(txtBiayaBbnkb);
        txtBiayaBbnkb.setBounds(605, 102, 200, 28);

        lblBiayaJasa.setText("Biaya Jasa");
        getContentPane().add(lblBiayaJasa);
        lblBiayaJasa.setBounds(450, 135, 150, 28);
        getContentPane().add(txtBiayaJasa);
        txtBiayaJasa.setBounds(605, 137, 200, 28);

        lblBiayaLain.setText("Biaya Lain");
        getContentPane().add(lblBiayaLain);
        lblBiayaLain.setBounds(450, 170, 150, 28);
        getContentPane().add(txtBiayaLain);
        txtBiayaLain.setBounds(605, 172, 200, 28);

        lblTotal.setText("Total Biaya");
        getContentPane().add(lblTotal);
        lblTotal.setBounds(450, 205, 150, 28);
        getContentPane().add(txtTotalBiaya);
        txtTotalBiaya.setBounds(605, 207, 200, 28);

        lblStatus.setText("Status");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(450, 240, 150, 28);
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Draft", "Diproses", "Selesai" }));
        getContentPane().add(cmbStatus);
        cmbStatus.setBounds(605, 242, 200, 28);

        // === KOLOM KANAN (Upload + Catatan) ===
        lblFileKtp.setText("Upload KTP Baru");
        getContentPane().add(lblFileKtp);
        lblFileKtp.setBounds(450, 295, 150, 28);
        btnUploadKtp.setText("Pilih File...");
        btnUploadKtp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadKtpActionPerformed(evt);
            }
        });
        getContentPane().add(btnUploadKtp);
        btnUploadKtp.setBounds(605, 297, 110, 28);
        lblFileKtpVal.setText("(kosong)");
        getContentPane().add(lblFileKtpVal);
        lblFileKtpVal.setBounds(725, 297, 480, 28);

        lblFileStnk.setText("Upload STNK");
        getContentPane().add(lblFileStnk);
        lblFileStnk.setBounds(450, 330, 150, 28);
        btnUploadStnk.setText("Pilih File...");
        btnUploadStnk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadStnkActionPerformed(evt);
            }
        });
        getContentPane().add(btnUploadStnk);
        btnUploadStnk.setBounds(605, 332, 110, 28);
        lblFileStnkVal.setText("(kosong)");
        getContentPane().add(lblFileStnkVal);
        lblFileStnkVal.setBounds(725, 332, 480, 28);

        lblFileBpkb.setText("Upload BPKB");
        getContentPane().add(lblFileBpkb);
        lblFileBpkb.setBounds(450, 365, 150, 28);
        btnUploadBpkb.setText("Pilih File...");
        btnUploadBpkb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadBpkbActionPerformed(evt);
            }
        });
        getContentPane().add(btnUploadBpkb);
        btnUploadBpkb.setBounds(605, 367, 110, 28);
        lblFileBpkbVal.setText("(kosong)");
        getContentPane().add(lblFileBpkbVal);
        lblFileBpkbVal.setBounds(725, 367, 480, 28);

        lblFileKwitansi.setText("Upload Kwitansi");
        getContentPane().add(lblFileKwitansi);
        lblFileKwitansi.setBounds(450, 400, 150, 28);
        btnUploadKwitansi.setText("Pilih File...");
        btnUploadKwitansi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadKwitansiActionPerformed(evt);
            }
        });
        getContentPane().add(btnUploadKwitansi);
        btnUploadKwitansi.setBounds(605, 402, 110, 28);
        lblFileKwitansiVal.setText("(kosong)");
        getContentPane().add(lblFileKwitansiVal);
        lblFileKwitansiVal.setBounds(725, 402, 480, 28);

        lblCatatan.setText("Catatan");
        getContentPane().add(lblCatatan);
        lblCatatan.setBounds(450, 440, 150, 28);
        txtCatatan.setRows(3);
        jScrollPaneCatatan.setViewportView(txtCatatan);
        getContentPane().add(jScrollPaneCatatan);
        jScrollPaneCatatan.setBounds(605, 440, 600, 70);

        // === TOMBOL AKSI ===
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambah);
        btnTambah.setBounds(40, 555, 120, 36);

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        getContentPane().add(btnUbah);
        btnUbah.setBounds(175, 555, 120, 36);

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus);
        btnHapus.setBounds(310, 555, 120, 36);

        btnBersihkan.setText("Bersihkan");
        btnBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihkanActionPerformed(evt);
            }
        });
        getContentPane().add(btnBersihkan);
        btnBersihkan.setBounds(445, 555, 120, 36);

        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btnKembali);
        btnKembali.setBounds(1085, 555, 120, 36);

        // === TABEL ===
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
        jScrollPane1.setBounds(40, 605, 1165, 155);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUploadKtpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadKtpActionPerformed
        String p = FileUploader.upload(this, "ktp");
        if (p != null) { fileKtp = p; lblFileKtpVal.setText(p); }
    }//GEN-LAST:event_btnUploadKtpActionPerformed

    private void btnUploadStnkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadStnkActionPerformed
        String p = FileUploader.upload(this, "stnk");
        if (p != null) { fileStnk = p; lblFileStnkVal.setText(p); }
    }//GEN-LAST:event_btnUploadStnkActionPerformed

    private void btnUploadBpkbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadBpkbActionPerformed
        String p = FileUploader.upload(this, "bpkb");
        if (p != null) { fileBpkb = p; lblFileBpkbVal.setText(p); }
    }//GEN-LAST:event_btnUploadBpkbActionPerformed

    private void btnUploadKwitansiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadKwitansiActionPerformed
        String p = FileUploader.upload(this, "kwitansi");
        if (p != null) { fileKwitansi = p; lblFileKwitansiVal.setText(p); }
    }//GEN-LAST:event_btnUploadKwitansiActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if (!validateInput()) return;
        BalikNamaStore.INSTANCE.add(BalikNamaStore.newRow(
                txtNoPolisi.getText().trim(),
                txtPemilikLama.getText().trim(),
                txtPemilikBaru.getText().trim(),
                txtNik.getText().trim(),
                txtAlamat.getText().trim(),
                String.valueOf(cmbJenisKendaraan.getSelectedItem()),
                String.valueOf(cmbMerk.getSelectedItem()),
                txtTipe.getText().trim(),
                parseInt(txtTahun.getText()),
                txtRangka.getText().trim(),
                txtMesin.getText().trim(),
                dtTanggalPengurusan.getDate(),
                parseDouble(txtBiayaPajak.getText()),
                parseDouble(txtBiayaBbnkb.getText()),
                parseDouble(txtBiayaJasa.getText()),
                parseDouble(txtBiayaLain.getText()),
                String.valueOf(cmbStatus.getSelectedItem()),
                fileKtp, fileStnk, fileBpkb, fileKwitansi,
                txtCatatan.getText().trim(),
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
        if (!validateInput()) return;
        String id = txtNoTransaksi.getText().trim();
        double bp = parseDouble(txtBiayaPajak.getText());
        double bb = parseDouble(txtBiayaBbnkb.getText());
        double bj = parseDouble(txtBiayaJasa.getText());
        double bl = parseDouble(txtBiayaLain.getText());

        LinkedHashMap<String, Object> existing = BalikNamaStore.INSTANCE.findById(id);
        String originalOperator = existing == null ? Session.getCurrentUsername()
                : String.valueOf(existing.getOrDefault(BalikNamaStore.COL_OPERATOR, ""));

        LinkedHashMap<String, Object> r = new LinkedHashMap<>();
        r.put("id", id);
        r.put(BalikNamaStore.COL_NO_POLISI, txtNoPolisi.getText().trim());
        r.put(BalikNamaStore.COL_PEMILIK_LAMA, txtPemilikLama.getText().trim());
        r.put(BalikNamaStore.COL_PEMILIK_BARU, txtPemilikBaru.getText().trim());
        r.put(BalikNamaStore.COL_NIK_BARU, txtNik.getText().trim());
        r.put(BalikNamaStore.COL_ALAMAT_BARU, txtAlamat.getText().trim());
        r.put(BalikNamaStore.COL_JENIS_KENDARAAN, String.valueOf(cmbJenisKendaraan.getSelectedItem()));
        r.put(BalikNamaStore.COL_MERK, String.valueOf(cmbMerk.getSelectedItem()));
        r.put(BalikNamaStore.COL_TIPE, txtTipe.getText().trim());
        r.put(BalikNamaStore.COL_TAHUN, parseInt(txtTahun.getText()));
        r.put(BalikNamaStore.COL_NO_RANGKA, txtRangka.getText().trim());
        r.put(BalikNamaStore.COL_NO_MESIN, txtMesin.getText().trim());
        r.put(BalikNamaStore.COL_TGL_PENGURUSAN, dtTanggalPengurusan.getDate());
        r.put(BalikNamaStore.COL_BIAYA_PAJAK, bp);
        r.put(BalikNamaStore.COL_BIAYA_BBNKB, bb);
        r.put(BalikNamaStore.COL_BIAYA_JASA, bj);
        r.put(BalikNamaStore.COL_BIAYA_LAIN, bl);
        r.put(BalikNamaStore.COL_TOTAL, bp + bb + bj + bl);
        r.put(BalikNamaStore.COL_STATUS, String.valueOf(cmbStatus.getSelectedItem()));
        r.put(BalikNamaStore.COL_FILE_KTP, fileKtp);
        r.put(BalikNamaStore.COL_FILE_STNK, fileStnk);
        r.put(BalikNamaStore.COL_FILE_BPKB, fileBpkb);
        r.put(BalikNamaStore.COL_FILE_KWITANSI, fileKwitansi);
        r.put(BalikNamaStore.COL_CATATAN, txtCatatan.getText().trim());
        r.put(BalikNamaStore.COL_OPERATOR, originalOperator);

        BalikNamaStore.INSTANCE.update(id, r);
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
        if (!Validasi.konfirmasiHapus(this, "transaksi balik nama '" + noPolisi + "'")) {
            return;
        }
        BalikNamaStore.INSTANCE.delete(id);
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
        LinkedHashMap<String, Object> r = BalikNamaStore.INSTANCE.findById(id);
        if (r == null) return;

        txtNoTransaksi.setText(id);
        txtNoPolisi.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_NO_POLISI, "")));
        txtPemilikLama.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_PEMILIK_LAMA, "")));
        txtPemilikBaru.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_PEMILIK_BARU, "")));
        txtNik.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_NIK_BARU, "")));
        txtAlamat.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_ALAMAT_BARU, "")));
        cmbJenisKendaraan.setSelectedItem(String.valueOf(r.getOrDefault(BalikNamaStore.COL_JENIS_KENDARAAN, "Mobil")));
        cmbMerk.setSelectedItem(String.valueOf(r.getOrDefault(BalikNamaStore.COL_MERK, "Honda")));
        txtTipe.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_TIPE, "")));
        txtTahun.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_TAHUN, "")));
        txtRangka.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_NO_RANGKA, "")));
        txtMesin.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_NO_MESIN, "")));
        dtTanggalPengurusan.setDate((Date) r.get(BalikNamaStore.COL_TGL_PENGURUSAN));
        txtBiayaPajak.setText(String.valueOf(((Number) r.getOrDefault(BalikNamaStore.COL_BIAYA_PAJAK, 0)).longValue()));
        txtBiayaBbnkb.setText(String.valueOf(((Number) r.getOrDefault(BalikNamaStore.COL_BIAYA_BBNKB, 0)).longValue()));
        txtBiayaJasa.setText(String.valueOf(((Number) r.getOrDefault(BalikNamaStore.COL_BIAYA_JASA, 0)).longValue()));
        txtBiayaLain.setText(String.valueOf(((Number) r.getOrDefault(BalikNamaStore.COL_BIAYA_LAIN, 0)).longValue()));
        cmbStatus.setSelectedItem(String.valueOf(r.getOrDefault(BalikNamaStore.COL_STATUS, "Draft")));
        fileKtp = String.valueOf(r.getOrDefault(BalikNamaStore.COL_FILE_KTP, ""));
        fileStnk = String.valueOf(r.getOrDefault(BalikNamaStore.COL_FILE_STNK, ""));
        fileBpkb = String.valueOf(r.getOrDefault(BalikNamaStore.COL_FILE_BPKB, ""));
        fileKwitansi = String.valueOf(r.getOrDefault(BalikNamaStore.COL_FILE_KWITANSI, ""));
        lblFileKtpVal.setText(fileKtp.isEmpty() ? "(kosong)" : fileKtp);
        lblFileStnkVal.setText(fileStnk.isEmpty() ? "(kosong)" : fileStnk);
        lblFileBpkbVal.setText(fileBpkb.isEmpty() ? "(kosong)" : fileBpkb);
        lblFileKwitansiVal.setText(fileKwitansi.isEmpty() ? "(kosong)" : fileKwitansi);
        txtCatatan.setText(String.valueOf(r.getOrDefault(BalikNamaStore.COL_CATATAN, "")));
    }//GEN-LAST:event_tblDataMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBersihkan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JButton btnUploadBpkb;
    private javax.swing.JButton btnUploadKtp;
    private javax.swing.JButton btnUploadKwitansi;
    private javax.swing.JButton btnUploadStnk;
    private javax.swing.JComboBox<String> cmbJenisKendaraan;
    private javax.swing.JComboBox<String> cmbMerk;
    private javax.swing.JComboBox<String> cmbStatus;
    private com.toedter.calendar.JDateChooser dtTanggalPengurusan;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneAlamat;
    private javax.swing.JScrollPane jScrollPaneCatatan;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblBiayaBbnkb;
    private javax.swing.JLabel lblBiayaJasa;
    private javax.swing.JLabel lblBiayaLain;
    private javax.swing.JLabel lblBiayaPajak;
    private javax.swing.JLabel lblCatatan;
    private javax.swing.JLabel lblFileBpkb;
    private javax.swing.JLabel lblFileBpkbVal;
    private javax.swing.JLabel lblFileKtp;
    private javax.swing.JLabel lblFileKtpVal;
    private javax.swing.JLabel lblFileKwitansi;
    private javax.swing.JLabel lblFileKwitansiVal;
    private javax.swing.JLabel lblFileStnk;
    private javax.swing.JLabel lblFileStnkVal;
    private javax.swing.JLabel lblJenisKendaraan;
    private javax.swing.JLabel lblLoginUser;
    private javax.swing.JLabel lblMerk;
    private javax.swing.JLabel lblMesin;
    private javax.swing.JLabel lblNik;
    private javax.swing.JLabel lblNoPolisi;
    private javax.swing.JLabel lblNoTransaksi;
    private javax.swing.JLabel lblPemilikBaru;
    private javax.swing.JLabel lblPemilikLama;
    private javax.swing.JLabel lblRangka;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTahun;
    private javax.swing.JLabel lblTgl;
    private javax.swing.JLabel lblTipe;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblData;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtBiayaBbnkb;
    private javax.swing.JTextField txtBiayaJasa;
    private javax.swing.JTextField txtBiayaLain;
    private javax.swing.JTextField txtBiayaPajak;
    private javax.swing.JTextArea txtCatatan;
    private javax.swing.JTextField txtMesin;
    private javax.swing.JTextField txtNik;
    private javax.swing.JTextField txtNoPolisi;
    private javax.swing.JTextField txtNoTransaksi;
    private javax.swing.JTextField txtPemilikBaru;
    private javax.swing.JTextField txtPemilikLama;
    private javax.swing.JTextField txtRangka;
    private javax.swing.JTextField txtTahun;
    private javax.swing.JTextField txtTipe;
    private javax.swing.JTextField txtTotalBiaya;
    // End of variables declaration//GEN-END:variables
}
