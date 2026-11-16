package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainApplication extends JFrame {

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    
    // Keep references to panels to refresh them
    private DashboardPanel dashboardPanel;
    private HistoryPanel historyPanel;

    public MainApplication() {
        setTitle("Temperature Log & Analytics Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND);

        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(Theme.BACKGROUND);

        dashboardPanel = new DashboardPanel(this);
        historyPanel = new HistoryPanel(this);

        // Add Panels
        cardsPanel.add(new LoginPanel(this), "Login");
        cardsPanel.add(dashboardPanel, "Dashboard");
        cardsPanel.add(new DataEntryPanel(this), "DataEntry");
        cardsPanel.add(historyPanel, "History");

        createSidebar();
        
        setLayout(new BorderLayout());
        add(sidebarPanel, BorderLayout.WEST);
        add(cardsPanel, BorderLayout.CENTER);

        // Show Login initially
        showPanel("Login");
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Theme.PANEL_BG);
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel lblLogo = new JLabel("Temp Manager");
        lblLogo.setFont(Theme.FONT_TITLE.deriveFont(22f));
        lblLogo.setForeground(Theme.ACCENT);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebarPanel.add(lblLogo);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        sidebarPanel.add(createSidebarButton("Dashboard", "Dashboard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        sidebarPanel.add(createSidebarButton("Log Data", "DataEntry"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        sidebarPanel.add(createSidebarButton("History", "History"));
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        JButton btnLogout = createSidebarButton("Logout", null);
        btnLogout.addActionListener(e -> logout());
        sidebarPanel.add(btnLogout);
        
        sidebarPanel.setVisible(false);
    }

    private JButton createSidebarButton(String text, String targetPanel) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_HEADER);
        btn.setForeground(Theme.TEXT_PRIMARY);
        btn.setBackground(Theme.PANEL_BG);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.ACCENT);
                btn.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.PANEL_BG);
                btn.setForeground(Theme.TEXT_PRIMARY);
            }
        });

        if (targetPanel != null) {
            btn.addActionListener(e -> showPanel(targetPanel));
        }

        return btn;
    }

    public void showPanel(String panelName) {
        if (panelName.equals("Dashboard")) {
            dashboardPanel.refreshChart();
        } else if (panelName.equals("History")) {
            historyPanel.refreshTable();
        }
        
        cardLayout.show(cardsPanel, panelName);
        boolean isLoggedIn = !panelName.equals("Login");
        sidebarPanel.setVisible(isLoggedIn);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            showPanel("Login");
        }
    }

    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("com.formdev.flatlaf.themes.FlatMacDarkLaf");
            clazz.getMethod("setup").invoke(null);
            
            UIManager.put("Panel.background", Theme.PANEL_BG);
            UIManager.put("Component.accentColor", Theme.ACCENT);
        } catch (Exception ex) {
            System.err.println("FlatLaf not found in classpath. Falling back to default L&F.");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}
        }

        SwingUtilities.invokeLater(() -> {
            MainApplication app = new MainApplication();
            app.setVisible(true);
        });
    }
}
