package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private MainApplication mainApp;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        setBackground(Theme.BACKGROUND);
        setLayout(new GridBagLayout());

        JPanel loginBox = new JPanel();
        loginBox.setBackground(Theme.PANEL_BG);
        loginBox.setLayout(new GridBagLayout());
        loginBox.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Temperature Analytics Manager");
        lblTitle.setFont(Theme.FONT_TITLE);
        lblTitle.setForeground(Theme.ACCENT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginBox.add(lblTitle, gbc);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(Theme.FONT_REGULAR);
        lblUser.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        loginBox.add(lblUser, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setFont(Theme.FONT_REGULAR);
        gbc.gridx = 1; gbc.gridy = 1;
        loginBox.add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(Theme.FONT_REGULAR);
        lblPass.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 2;
        loginBox.add(lblPass, gbc);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(Theme.FONT_REGULAR);
        gbc.gridx = 1; gbc.gridy = 2;
        loginBox.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(Theme.FONT_HEADER);
        btnLogin.setBackground(Theme.ACCENT);
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> performLogin());

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        loginBox.add(btnLogin, gbc);

        add(loginBox);
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (authenticate(username, password)) {
            mainApp.showPanel("Dashboard");
            txtUsername.setText("");
            txtPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try admin/admin", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean authenticate(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database authentication error: " + e.getMessage());
            // Fallback for default admin if DB has a connection issue
            if ("admin".equals(username) && "admin".equals(password)) {
                return true;
            }
        }
        return false;
    }
}
