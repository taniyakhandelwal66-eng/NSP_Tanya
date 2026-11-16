package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/temp_analytics_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "Taniya@2026";

    private static String url = DEFAULT_URL;
    private static String user = DEFAULT_USER;
    private static String password = DEFAULT_PASSWORD;

    static {
        loadProperties();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in classpath: " + e.getMessage());
        }
    }

    private static void loadProperties() {
        Properties props = new Properties();
        try {
            File propFile = new File("db.properties");
            if (propFile.exists()) {
                try (InputStream in = new FileInputStream(propFile)) {
                    props.load(in);
                }
            } else {
                try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (in != null) {
                        props.load(in);
                    }
                }
            }
            if (props.containsKey("db.url")) url = props.getProperty("db.url");
            if (props.containsKey("db.user")) user = props.getProperty("db.user");
            if (props.containsKey("db.password")) password = props.getProperty("db.password");
        } catch (Exception e) {
            System.err.println("Warning: Could not read db.properties, using default values: " + e.getMessage());
        } 
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing MySQL Connection...");
        if (testConnection()) {
            System.out.println("SUCCESS: Connected to MySQL database temp_analytics_db!");
        } else {
            System.out.println("FAILED: Could not connect to MySQL.");
        }
    }
}
