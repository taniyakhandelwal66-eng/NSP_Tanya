-- Database Schema for Temperature Log & Analytics Manager

CREATE DATABASE IF NOT EXISTS temp_analytics_db;
USE temp_analytics_db;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Default admin user (password is 'admin' - hashed or plain depending on implementation, for prototype we'll assume plain text for simplicity)
INSERT IGNORE INTO users (username, password_hash) VALUES ('admin', 'admin');

-- Temperature logs table
CREATE TABLE IF NOT EXISTS temperature_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sensor_or_record_name VARCHAR(100) NOT NULL,
    input_temp DECIMAL(10, 2) NOT NULL,
    input_unit VARCHAR(20) NOT NULL,
    output_temp DECIMAL(10, 2) NOT NULL,
    output_unit VARCHAR(20) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert some mock data for initial dashboard rendering
INSERT IGNORE INTO temperature_logs (sensor_or_record_name, input_temp, input_unit, output_temp, output_unit) VALUES
('Boiler Room A', 100.00, 'Celsius', 212.00, 'Fahrenheit'),
('Server Rack 1', 75.50, 'Fahrenheit', 24.17, 'Celsius'),
('Lab Equipment', 298.15, 'Kelvin', 25.00, 'Celsius'),
('Freezer Unit B', -10.00, 'Celsius', 14.00, 'Fahrenheit');
