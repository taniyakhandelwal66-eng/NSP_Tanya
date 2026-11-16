package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class DashboardPanel extends JPanel {
    private MainApplication mainApp;
    private ChartPanel chartPanel;
    private DefaultCategoryDataset dataset;

    private JTextField txtInputTemp;
    private JComboBox<String> cmbInputUnit;
    private JTextField txtOutputTemp;
    private JComboBox<String> cmbOutputUnit;

    public DashboardPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("Analytics Dashboard");
        lblHeader.setFont(Theme.FONT_TITLE);
        lblHeader.setForeground(Theme.ACCENT);
        add(lblHeader, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(createConverterPanel());
        centerPanel.add(createChartPanel());
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createConverterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.PANEL_BG);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.ACCENT), "Live Conversion Tool",
                0, 0, Theme.FONT_HEADER, Theme.TEXT_PRIMARY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] units = {"Celsius", "Fahrenheit", "Kelvin"};

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblIn = new JLabel("Input:");
        lblIn.setForeground(Theme.TEXT_PRIMARY);
        panel.add(lblIn, gbc);

        txtInputTemp = new JTextField(10);
        gbc.gridx = 1;
        panel.add(txtInputTemp, gbc);

        cmbInputUnit = new JComboBox<>(units);
        gbc.gridx = 2;
        panel.add(cmbInputUnit, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblOut = new JLabel("Output:");
        lblOut.setForeground(Theme.TEXT_PRIMARY);
        panel.add(lblOut, gbc);

        txtOutputTemp = new JTextField(10);
        txtOutputTemp.setEditable(false);
        gbc.gridx = 1;
        panel.add(txtOutputTemp, gbc);

        cmbOutputUnit = new JComboBox<>(units);
        cmbOutputUnit.setSelectedIndex(1);
        gbc.gridx = 2;
        panel.add(cmbOutputUnit, gbc);

        KeyAdapter liveUpdate = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { updateConversion(); }
        };
        txtInputTemp.addKeyListener(liveUpdate);
        cmbInputUnit.addActionListener(e -> updateConversion());
        cmbOutputUnit.addActionListener(e -> updateConversion());

        return panel;
    }

    private void updateConversion() {
        try {
            double input = Double.parseDouble(txtInputTemp.getText());
            String fromUnit = (String) cmbInputUnit.getSelectedItem();
            String toUnit = (String) cmbOutputUnit.getSelectedItem();
            
            double output = convertTemperature(input, fromUnit, toUnit);
            txtOutputTemp.setText(String.format("%.2f", output));
        } catch (NumberFormatException e) {
            txtOutputTemp.setText("");
        }
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

    private JPanel createChartPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.PANEL_BG);
        wrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.ACCENT), "Recent Trends (Input vs Output)",
                0, 0, Theme.FONT_HEADER, Theme.TEXT_PRIMARY));

        dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "Temperature Over Time",
                "Record",
                "Value",
                dataset
        );

        chart.setBackgroundPaint(Theme.PANEL_BG);
        chart.getTitle().setPaint(Theme.TEXT_PRIMARY);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Theme.BACKGROUND);
        plot.getDomainAxis().setTickLabelPaint(Theme.TEXT_SECONDARY);
        plot.getDomainAxis().setLabelPaint(Theme.TEXT_PRIMARY);
        plot.getRangeAxis().setTickLabelPaint(Theme.TEXT_SECONDARY);
        plot.getRangeAxis().setLabelPaint(Theme.TEXT_PRIMARY);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        wrapper.add(chartPanel, BorderLayout.CENTER);

        refreshChart();

        return wrapper;
    }

    public void refreshChart() {
        if (dataset == null) return;
        dataset.clear();
        List<LogRecord> logs = DataStore.getInstance().getLogs();
        
        // Show up to the last 10 records
        int start = Math.max(0, logs.size() - 10);
        for (int i = start; i < logs.size(); i++) {
            LogRecord log = logs.get(i);
            String label = "Log " + log.getId();
            dataset.addValue(log.getInputTemp(), "Input Temp", label);
            dataset.addValue(log.getOutputTemp(), "Output Temp", label);
        }
    }
}
