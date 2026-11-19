package com.kelompok2.pajakform;

import javax.swing.JOptionPane;

public class FormLogin extends javax.swing.JFrame {

    public FormLogin() {
        initComponents();
        applyStyle();

        Tampilan.window(this);

        setLocationRelativeTo(null);
        Responsive.enable(this);
    }

    private void applyStyle() {
        getContentPane().setBackground(Tampilan.BACKGROUND);
        Tampilan.logo(lblLogo, 150);
        Tampilan.judul(lblTitle, Tampilan.SIZE_DASHBOARD);
        Tampilan.label(lblUsername);
        Tampilan.label(lblPassword);
        Tampilan.isian(txtUsername);
        Tampilan.isian(txtPassword);
        Tampilan.tombol(btnMasuk);
        Tampilan.tombol(btnBatal);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLogo = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnMasuk = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Masuk");
        setResizable(false);
        getContentPane().setLayout(null);

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblLogo);
        lblLogo.setBounds(475, 35, 150, 150);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 38)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Selamat Datang!");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 190, 1100, 55);

        lblUsername.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUsername.setText("Nama Pengguna");
        getContentPane().add(lblUsername);
        lblUsername.setBounds(340, 295, 185, 40);

        lblPassword.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPassword.setText("Kata Sandi");
        getContentPane().add(lblPassword);
        lblPassword.setBounds(340, 360, 185, 40);

        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtUsername.setBorder(null);
        getContentPane().add(txtUsername);
        txtUsername.setBounds(535, 296, 260, 36);

        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPassword.setBorder(null);
        getContentPane().add(txtPassword);
        txtPassword.setBounds(535, 361, 260, 36);

        btnMasuk.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnMasuk.setText("Masuk");
        btnMasuk.setBorderPainted(false);
        btnMasuk.setFocusPainted(false);
        btnMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasukActionPerformed(evt);
            }
        });
        getContentPane().add(btnMasuk);
        btnMasuk.setBounds(395, 435, 170, 48);

        btnBatal.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.setBorderPainted(false);
        btnBatal.setFocusPainted(false);
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        getContentPane().add(btnBatal);
        btnBatal.setBounds(585, 435, 170, 48);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasukActionPerformed
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        UserStore.User user = UserStore.authenticate(username, password);
        if (user != null) {
            Session.setCurrentUser(user);
            new FormMenu().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Nama Pengguna atau Kata Sandi salah, atau user nonaktif.",
                    "Gagal Masuk",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnMasukActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        txtUsername.setText("");
        txtPassword.setText("");
    }//GEN-LAST:event_btnBatalActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnMasuk;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
