package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DataEntryPanel extends JPanel {
    private MainApplication mainApp;

    public DataEntryPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Log New Temperature");
        lblTitle.setFont(Theme.FONT_TITLE);
        lblTitle.setForeground(Theme.ACCENT);
        lblTitle.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.PANEL_BG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblName = new JLabel("Sensor/Record Name:");
        lblName.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(lblName, gbc);

        gbc.gridx = 1; 
        JTextField txtName = new JTextField(20);
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblInTemp = new JLabel("Input Temperature:");
        lblInTemp.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(lblInTemp, gbc);

        gbc.gridx = 1; 
        JPanel inPanel = new JPanel(new BorderLayout(5, 0));
        inPanel.setOpaque(false);
        JTextField txtInTemp = new JTextField(10);
        JComboBox<String> cmbInUnit = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        inPanel.add(txtInTemp, BorderLayout.CENTER);
        inPanel.add(cmbInUnit, BorderLayout.EAST);
        formPanel.add(inPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblOutUnit = new JLabel("Target Unit:");
        lblOutUnit.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(lblOutUnit, gbc);

        gbc.gridx = 1;
        JComboBox<String> cmbOutUnit = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        formPanel.add(cmbOutUnit, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnSave = new JButton("Save Record");
        btnSave.setBackground(Theme.ACCENT);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty() || txtInTemp.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double inputTemp = Double.parseDouble(txtInTemp.getText());
                String inUnit = (String) cmbInUnit.getSelectedItem();
                String outUnit = (String) cmbOutUnit.getSelectedItem();
                
                double outTemp = convertTemperature(inputTemp, inUnit, outUnit);
                
                // Add to DataStore
                DataStore.getInstance().addLog(txtName.getText().trim(), inputTemp, inUnit, outTemp, outUnit);
                
                JOptionPane.showMessageDialog(this, "Record saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields and return to Dashboard
                txtName.setText("");
                txtInTemp.setText("");
                mainApp.showPanel("Dashboard");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input Temperature must be numeric.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(btnSave, gbc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.add(formPanel);
        
        add(wrapper, BorderLayout.CENTER);
    }
    
    private double convertTemperature(double value, String from, String to) {
        if (from.equals(to)) return value;
        double celsius = value;
        if (from.equals("Fahrenheit")) celsius = (value - 32) * 5 / 9;
        else if (from.equals("Kelvin")) celsius = value - 273.15;
        
        if (to.equals("Fahrenheit")) return (celsius * 9 / 5) + 32;
        else if (to.equals("Kelvin")) return celsius + 273.15;
        return celsius;
    }
}
