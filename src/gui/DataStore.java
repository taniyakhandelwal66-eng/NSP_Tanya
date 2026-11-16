package gui;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;

    private DataStore() {
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public List<LogRecord> getLogs() {
        List<LogRecord> logs = new ArrayList<>();
        String sql = "SELECT id, sensor_or_record_name, input_temp, input_unit, output_temp, output_unit, recorded_at " +
                     "FROM temperature_logs ORDER BY recorded_at ASC, id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("sensor_or_record_name");
                double inTemp = rs.getDouble("input_temp");
                String inUnit = rs.getString("input_unit");
                double outTemp = rs.getDouble("output_temp");
                String outUnit = rs.getString("output_unit");
                String timestamp = rs.getString("recorded_at");
                if (timestamp == null) {
                    timestamp = "";
                }

                logs.add(new LogRecord(id, name, inTemp, inUnit, outTemp, outUnit, timestamp));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching logs from MySQL: " + e.getMessage());
        }
        return logs;
    }

    public boolean addLog(String recordName, double inputTemp, String inputUnit, double outputTemp, String outputUnit) {
        String sql = "INSERT INTO temperature_logs (sensor_or_record_name, input_temp, input_unit, output_temp, output_unit) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, recordName);
            stmt.setDouble(2, inputTemp);
            stmt.setString(3, inputUnit);
            stmt.setDouble(4, outputTemp);
            stmt.setString(5, outputUnit);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving log to MySQL: " + e.getMessage());
            return false;
        }
    }
}
