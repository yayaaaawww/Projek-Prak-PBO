/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.AuthController;
import Model.ModelSupermarket.User;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author LENOVO
 */


public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AuthController authController;

    public LoginFrame() {
        authController = new AuthController();

        setTitle("Login - Manajemen Supermarket");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(0x1a5276));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTitle = new JLabel("🛒 MANAJEMEN SUPERMARKET", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // Form
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        pnlForm.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(new JLabel("Username:"), gbc);
        gbc.gridy = 1;
        txtUsername = new JTextField(18);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlForm.add(txtUsername, gbc);

        gbc.gridy = 2;
        pnlForm.add(new JLabel("Password:"), gbc);
        gbc.gridy = 3;
        txtPassword = new JPasswordField(18);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlForm.add(txtPassword, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(16, 5, 6, 5);
        btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(0x1a5276));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnlForm.add(btnLogin, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Info akun
        JLabel lblInfo = new JLabel("manajer: admin/admin123 | kasir: kasir1/kasir123", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(lblInfo, BorderLayout.SOUTH);

        // Enter key juga bisa login
        txtPassword.addActionListener(e -> aksiLogin());
        btnLogin.addActionListener(e -> aksiLogin());

        setVisible(true);
    }

    private void aksiLogin() {
        try {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            User user = authController.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                    "Username atau password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                return;
            }

            dispose(); // tutup login

            if ("manajer".equals(user.getRole())) {
                new ManajerFrame(user);
            } else {
                new KasirFrame(user);
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}
