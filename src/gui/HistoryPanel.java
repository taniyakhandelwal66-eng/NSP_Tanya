package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    private MainApplication mainApp;
    private JTable table;
    private DefaultTableModel tableModel;

    public HistoryPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Conversion History");
        lblTitle.setFont(Theme.FONT_TITLE);
        lblTitle.setForeground(Theme.ACCENT);
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Record Name", "Input Temp", "Input Unit", "Output Temp", "Output Unit", "Recorded At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        
        table.setBackground(Theme.PANEL_BG);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(30);
        table.setSelectionBackground(Theme.ACCENT.darker().darker());
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Theme.BACKGROUND);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.BACKGROUND);
        header.setForeground(Theme.ACCENT);
        header.setFont(Theme.FONT_HEADER);
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Theme.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.PANEL_BG, 2));

        add(scrollPane, BorderLayout.CENTER);
        
        refreshTable();
    }

    public void refreshTable() {
        if (tableModel == null) return;
        tableModel.setRowCount(0); // Clear existing rows
        
        List<LogRecord> logs = DataStore.getInstance().getLogs();
        for (LogRecord log : logs) {
            tableModel.addRow(new Object[]{
                log.getId(),
                log.getRecordName(),
                log.getInputTemp(),
                log.getInputUnit(),
                log.getOutputTemp(),
                log.getOutputUnit(),
                log.getTimestamp()
            });
        }
    }
}
