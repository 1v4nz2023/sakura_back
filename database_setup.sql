-- Sakura Dental Clinic Database Setup Script
-- Run this script in MySQL to create the database and initial data

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS sakura_database
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE sakura_database;

-- Create tables (these will be created automatically by JPA, but here for reference)
-- The application will create all tables automatically when it starts

-- Insert initial data for payment methods
INSERT INTO payment_methods (name) VALUES 
('Efectivo'),
('Tarjeta de Crédito'),
('Tarjeta de Débito'),
('Transferencia Bancaria'),
('Yape'),
('Plin')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert initial admin user (password: admin123)
INSERT INTO users (username, password_hash, role, is_active, created_at) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', true, NOW())
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- Insert sample dentists
INSERT INTO dentists (first_name, last_name, specialty, active) VALUES 
('Dr. María', 'González', 'Ortodoncista', true),
('Dr. Carlos', 'Rodríguez', 'Endodoncista', true),
('Dra. Ana', 'López', 'Periodoncista', true),
('Dr. Luis', 'Martínez', 'Cirujano Oral', true)
ON DUPLICATE KEY UPDATE first_name = VALUES(first_name);

-- Insert sample services
INSERT INTO services (name, description, base_price) VALUES 
('Limpieza Dental', 'Limpieza profesional y profilaxis', 80.00),
('Empaste Dental', 'Tratamiento de caries con composite', 120.00),
('Extracción Simple', 'Extracción de diente sin complicaciones', 150.00),
('Extracción Compleja', 'Extracción de diente con cirugía', 300.00),
('Ortodoncia Inicial', 'Consulta y plan de tratamiento', 200.00),
('Blanqueamiento Dental', 'Tratamiento de blanqueamiento profesional', 250.00),
('Endodoncia', 'Tratamiento de conducto', 400.00),
('Corona Dental', 'Corona de porcelana', 800.00),
('Implante Dental', 'Implante con corona', 2500.00),
('Radiografía Panorámica', 'Radiografía completa de la boca', 100.00)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert sample promos
INSERT INTO promos (name, discount_pct, active_from, active_to) VALUES 
('Descuento Estudiantes', 15.00, '2024-01-01', '2024-12-31'),
('Primera Visita', 20.00, '2024-01-01', '2024-12-31'),
('Pacientes Frecuentes', 10.00, '2024-01-01', '2024-12-31')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Show confirmation
SELECT 'Database sakura_database created successfully!' as message;
SELECT 'Initial data inserted successfully!' as message; 