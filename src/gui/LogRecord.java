package gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogRecord {
    private int id;
    private String recordName;
    private double inputTemp;
    private String inputUnit;
    private double outputTemp;
    private String outputUnit;
    private String timestamp;

    public LogRecord(int id, String recordName, double inputTemp, String inputUnit, double outputTemp, String outputUnit) {
        this.id = id;
        this.recordName = recordName;
        this.inputTemp = inputTemp;
        this.inputUnit = inputUnit;
        this.outputTemp = outputTemp;
        this.outputUnit = outputUnit;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(formatter);
    }
    
    // Additional constructor for mock data with specific timestamp
    public LogRecord(int id, String recordName, double inputTemp, String inputUnit, double outputTemp, String outputUnit, String timestamp) {
        this.id = id;
        this.recordName = recordName;
        this.inputTemp = inputTemp;
        this.inputUnit = inputUnit;
        this.outputTemp = outputTemp;
        this.outputUnit = outputUnit;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getRecordName() { return recordName; }
    public double getInputTemp() { return inputTemp; }
    public String getInputUnit() { return inputUnit; }
    public double getOutputTemp() { return outputTemp; }
    public String getOutputUnit() { return outputUnit; }
    public String getTimestamp() { return timestamp; }
}
