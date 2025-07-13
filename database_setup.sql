-- Sakura Dental Clinic Database Setup Script
-- Run this script in MySQL to create the database and initial data

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS sakura_database
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE sakura_database;

-- 1. Create roles table
CREATE TABLE IF NOT EXISTS roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    status BOOLEAN DEFAULT TRUE
);

-- 2. Create permissions table
CREATE TABLE IF NOT EXISTS permissions (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    status BOOLEAN DEFAULT TRUE
);

-- 3. Create role_permissions junction table
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id)
);

-- 4. Create document_type table
CREATE TABLE IF NOT EXISTS document_type (
    document_type_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    status BOOLEAN DEFAULT TRUE
);

-- 5. Create gender table
CREATE TABLE IF NOT EXISTS gender (
    gender_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    status BOOLEAN DEFAULT TRUE
);

-- 6. Create district table
CREATE TABLE IF NOT EXISTS district (
    district_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status BOOLEAN DEFAULT TRUE
);

-- 7. Create job_titles table
CREATE TABLE IF NOT EXISTS job_titles (
    job_title_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    status BOOLEAN DEFAULT TRUE
);

-- 8. Create employee table
CREATE TABLE IF NOT EXISTS employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    doc_number INT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100),
    hired_at DATETIME,
    phone VARCHAR(20),
    email VARCHAR(120),
    status BOOLEAN DEFAULT TRUE,
    document_type_id INT NOT NULL,
    job_title_id INT NOT NULL,
    gender_id INT NOT NULL,
    district_id INT NOT NULL,
    FOREIGN KEY (document_type_id) REFERENCES document_type(document_type_id),
    FOREIGN KEY (job_title_id) REFERENCES job_titles(job_title_id),
    FOREIGN KEY (gender_id) REFERENCES gender(gender_id),
    FOREIGN KEY (district_id) REFERENCES district(district_id)
);

-- 9. Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    password_hash VARCHAR(95) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    role_id INT NOT NULL,
    employee_id INT,
    is_active BOOLEAN DEFAULT TRUE,
    last_login DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);

-- 10. Create patients table
CREATE TABLE IF NOT EXISTS patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    doc_number VARCHAR(8) UNIQUE,
    birth_date DATETIME,
    document_type_id INT NOT NULL,
    gender_id INT NOT NULL,
    district_id INT NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(120),
    status BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_type_id) REFERENCES document_type(document_type_id),
    FOREIGN KEY (gender_id) REFERENCES gender(gender_id),
    FOREIGN KEY (district_id) REFERENCES district(district_id)
);

-- 11. Create payment_methods table
CREATE TABLE IF NOT EXISTS payment_methods (
    method_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL UNIQUE
);

-- 12. Create categorie_service table
CREATE TABLE IF NOT EXISTS categorie_service (
    categorie_service_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);

-- 13. Create services table
CREATE TABLE IF NOT EXISTS services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10,2) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    categorie_service_id INT NOT NULL,
    FOREIGN KEY (categorie_service_id) REFERENCES categorie_service(categorie_service_id)
);

-- 14. Create clinical_histories table
CREATE TABLE IF NOT EXISTS clinical_histories (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- 15. Create quotations table
CREATE TABLE IF NOT EXISTS quotations (
    quotation_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    history_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDIENTE', 'ACEPTADA', 'PAGADA', 'ANULADA') DEFAULT 'PENDIENTE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (history_id) REFERENCES clinical_histories(history_id)
);

-- 16. Create quotation_items table
CREATE TABLE IF NOT EXISTS quotation_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    quotation_id INT NOT NULL,
    service_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2),
    FOREIGN KEY (quotation_id) REFERENCES quotations(quotation_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);

-- 17. Create payments table
CREATE TABLE IF NOT EXISTS payments (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quotation_id INT NOT NULL,
    method_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    balance_remaining DECIMAL(10,2) NOT NULL,
    status ENUM('PENDIENTE', 'CONFIRMADO', 'ANULADO') DEFAULT 'PENDIENTE',
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    canceled_by INT,
    FOREIGN KEY (quotation_id) REFERENCES quotations(quotation_id),
    FOREIGN KEY (method_id) REFERENCES payment_methods(method_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id),
    FOREIGN KEY (canceled_by) REFERENCES users(user_id)
);

-- 18. Create receipts table
CREATE TABLE IF NOT EXISTS receipts (
    receipt_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    receipt_number VARCHAR(30) NOT NULL UNIQUE,
    receipt_type ENUM('BOLETA', 'FACTURA') NOT NULL,
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id)
);

-- Insert initial data for roles
INSERT INTO roles (name, descripcion, status) VALUES
('ADMIN', 'Administrador del sistema', TRUE),
('DENTIST', 'Dentista', TRUE),
('ASSISTANT', 'Asistente dental', TRUE),
('RECEPTIONIST', 'Recepcionista', TRUE)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert initial data for permissions
INSERT INTO permissions (code, descripcion, status) VALUES
('USER_CREATE', 'Crear usuarios', TRUE),
('USER_READ', 'Ver usuarios', TRUE),
('USER_UPDATE', 'Actualizar usuarios', TRUE),
('USER_DELETE', 'Eliminar usuarios', TRUE),
('PATIENT_CREATE', 'Crear pacientes', TRUE),
('PATIENT_READ', 'Ver pacientes', TRUE),
('PATIENT_UPDATE', 'Actualizar pacientes', TRUE),
('PATIENT_DELETE', 'Eliminar pacientes', TRUE),
('QUOTATION_CREATE', 'Crear cotizaciones', TRUE),
('QUOTATION_READ', 'Ver cotizaciones', TRUE),
('QUOTATION_UPDATE', 'Actualizar cotizaciones', TRUE),
('QUOTATION_DELETE', 'Eliminar cotizaciones', TRUE)
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- Insert initial data for document types
INSERT INTO document_type (code, name, status) VALUES
('DNI', 'Documento Nacional de Identidad', TRUE),
('CE', 'Carné de Extranjería', TRUE),
('PASSPORT', 'Pasaporte', TRUE),
('RUC', 'Registro Único de Contribuyentes', TRUE)
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- Insert initial data for genders
INSERT INTO gender (code, name, status) VALUES
('M', 'Masculino', TRUE),
('F', 'Femenino', TRUE),
('O', 'Otro', TRUE)
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- Insert initial data for districts
INSERT INTO district (name, status) VALUES
('Lima', TRUE),
('Miraflores', TRUE),
('San Isidro', TRUE),
('Barranco', TRUE),
('Surco', TRUE)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert initial data for job titles
INSERT INTO job_titles (name, description, status) VALUES
('Dentista General', 'Dentista con práctica general', TRUE),
('Ortodoncista', 'Especialista en ortodoncia', TRUE),
('Endodoncista', 'Especialista en endodoncia', TRUE),
('Asistente Dental', 'Asistente de consultorio dental', TRUE),
('Recepcionista', 'Atención al público y citas', TRUE)
ON DUPLICATE KEY UPDATE name = VALUES(name);

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
INSERT INTO users (username, password_hash, email, role_id, is_active, created_at) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@sakura.com', (SELECT role_id FROM roles WHERE name='ADMIN'), true, NOW())
ON DUPLICATE KEY UPDATE username = VALUES(username), email = VALUES(email);

-- Insert initial categories
INSERT INTO categorie_service (name, description, status) VALUES
('Diagnóstico', 'Servicios de diagnóstico dental', TRUE),
('Restaurativo', 'Servicios restaurativos', TRUE),
('Preventivo', 'Servicios preventivos', TRUE),
('Rehabilitación', 'Servicios de rehabilitación', TRUE),
('Endodoncia', 'Servicios de endodoncia', TRUE),
('Odontopediatría', 'Servicios para niños', TRUE),
('Cirugía', 'Servicios quirúrgicos', TRUE),
('Estética', 'Servicios estéticos', TRUE),
('Periodoncia', 'Servicios periodontales', TRUE),
('Otros', 'Servicios Otros', TRUE)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert initial services (examples)
INSERT INTO services (name, description, base_price, status, categorie_service_id) VALUES
('Mock Up', 'Prueba estética temporal para visualizar cambios.', 25, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Diagnóstico')),
('Diseño Digital', 'Planificación digital de la sonrisa.', 500, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Diagnóstico')),
('Resina Simple', 'Empaste básico para caries superficiales.', 140, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Resina Compuesta', 'Restauración de daño moderado con resina estética.', 220, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Resina Compleja', 'Reconstrucción dental con múltiples superficies.', 330, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Ionómero Simple', 'Relleno dental con liberación de flúor.', 150, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Ionómero Compuesto', 'Empaste resistente con componentes de ionómero.', 220, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Sellantes', 'Barreras protectoras en molares para prevenir caries.', 55, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Preventivo')),
('Profilaxis y Destartraje Simple', 'Limpieza dental para remover sarro leve.', 150, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Preventivo')),
('Profilaxis y destartraje Moderado (2 citas)', 'Limpieza dental profunda en dos sesiones.', 220, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Preventivo')),
('Profilaxis y destartraje Moderado (3citas)', 'Limpieza intensiva en tres citas.', 550, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Preventivo')),
('Aplicación de cloruro de amino de plata', 'Detención de caries activas en dientes temporales.', 80, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Preventivo')),
('Flúor Barniz', 'Refuerzo del esmalte dental con barniz fluorado.', 90, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Preventivo')),
('Perno Fibra de Vidrio', 'Refuerzo interno estético para dientes tratados.', 350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Perno Metálico', 'Refuerzo interno metálico para dientes debilitados.', 280, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Apertura Cameral', 'Acceso al interior dental para tratamiento.', 150, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('Apiceptomía de Dientes Anteriores', 'Cirugía para remover raíz infectada en dientes frontales.', 1350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('Apiceptomía de Dientes posteriores', 'Cirugía para remover raíz en molares afectados.', 1350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('5 anterior', 'Tratamiento de conducto en incisivos y caninos.', 470, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('5 Premolar', 'Tratamiento de conducto en premolares.', 600, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('5 Molar', 'Tratamiento de conducto en molares.', 650, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('Retratamiento Anterior', 'Repetición del tratamiento de conducto en dientes frontales.', 650, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('Retratamiento premolar', 'Repetición del tratamiento de conducto en premolares.', 720, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('Retratamiento Molar', 'Repetición del tratamiento de conducto en molares.', 820, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Endodoncia')),
('Pulpectomía', 'Eliminación de pulpa dental infectada en niños.', 380, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Odontopediatría')),
('Pulpotomía', 'Remoción parcial de la pulpa dental en niños.', 280, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Odontopediatría')),
('Incrustación Resina', 'Restauración estética incrustada con resina.', 470, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Incrustación Cerómero', 'Restauración estética con material cerómero.', 720, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Incrustación Zirconio', 'Restauración altamente estética y resistente.', 1320, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Mucocele', 'Extracción de quiste mucoso en la boca.', 350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Cirugía')),
('Curetaje de Alveolo', 'Limpieza del alveolo tras extracción dental.', 200, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Cirugía')),
('Exodoncia Simple', 'Extracción sin complicaciones de una pieza dental.', 220, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Cirugía')),
('Exodoncia diente deciduo', 'Extracción de diente temporal en niños.', 100, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Cirugía')),
('Exodoncia con Colgajo', 'Extracción quirúrgica con apertura de encía.', 300, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Cirugía')),
('Exodoncia Semi impactada', 'Extracción de diente parcialmente cubierto.', 380, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Cirugía')),
('Exodoncia Impactada', 'Extracción de diente totalmente cubierto o inclinado.', 660, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Cirugía')),
('Blanqueamiento unitario', 'Aclaramiento de un solo diente.', 450, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Estética')),
('Blanqueamiento con Cubetas', 'Aclaramiento con férulas en casa.', 350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Estética')),
('Blanqueamiento consultorio(2 sesiones)', 'Aclaramiento profesional en dos citas.', 500, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Estética')),
('Blanqueamiento Mixto', 'Combinación de blanqueamiento en casa y consultorio.', 660, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Estética')),
('Carilla Resina Directa', 'Lámina estética aplicada directamente en clínica.', 380, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Carilla Resina Indirecta', 'Carilla elaborada en laboratorio y luego adherida.', 660, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Carilla Cerómero', 'Carilla estética con cerómero de alta calidad.', 950, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Estética')),
('Carilla Cerámica Emax', 'Carilla estética de cerámica altamente resistente.', 1350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Estética')),
('Corona Acrílico Rápido', 'Corona provisional rápida en acrílico.', 150, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Corona Acrílico Lento', 'Corona provisional de mejor calidad.', 250, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Corona Veener', 'Corona estética con mínima reducción dental.', 530, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Corona Metal Cerámica', 'Corona resistente con estructura metálica.', 850, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Corona Metal Cerámica con atache', 'Corona con sistema de retención adicional.', 1250, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Corona Zirconio', 'Corona estética sin metal de alta resistencia.', 1550, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Prótesis Parcial Removible Wipla o Inmediata', 'Prótesis removible de uso inmediato tras extracciones.', 630, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Prótesis Parcial Removible Cromo cobalto con dientes de Acrilico', 'Prótesis parcial metálica con dientes acrílicos.', 1200, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Prótesis Parcial Removible Cromo cobalto con dientes de Resina', 'Prótesis parcial metálica con dientes de resina estética.', 1600, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Prótesis Parcial Removible Cromo cobalto con dientes de Atache', 'Prótesis parcial con sistema de retención tipo atache.', 1980, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Prótesis Parcial Removible Flexible', 'Prótesis removible sin metal, flexible y estética.', 1980, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Prótesis Total de Acrílico', 'Prótesis completa de acrílico para ambas arcadas.', 1200, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Prótesis Total de Resina', 'Prótesis total con mayor estética y resistencia.', 1600, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Prótesis Total Acrílico triplex, Dientes de Resina y Malla', 'Prótesis total reforzada con malla y dientes estéticos.', 1980, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Prótesis Total Rebase de Prótesis', 'Ajuste interno de prótesis para mejor adaptación.', 550, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Prótesis Total Overdent', 'Prótesis total anclada a implantes.', 2000, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Férula Miorrelajante Láminas', 'Férula para aliviar tensión mandibular leve.', 350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Férula Miorrelajante Acrílico', 'Férula rígida para trastornos de ATM.', 500, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Alargamiento de Corona Clínica con Osteotomía por pieza', 'Cirugía para exponer más superficie dental.', 550, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Colgajo Desplazado Coronal (por diente)', 'Cirugía periodontal para cubrir recesiones.', 900, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Rehabilitación')),
('Colgajo Desplazado Lateral (por diente)', 'Técnica quirúrgica para corregir encías retraídas.', 900, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia')),
('Colgajo Periodontal', 'Acceso quirúrgico para limpieza profunda.', 900, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia')),
('Fase de Mantenimiento periodontal', 'Control periódico para pacientes periodontales.', 550, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia')),
('Ferulizaciones con Resina (por arcada)', 'Unión de dientes móviles con resina.', 700, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Restaurativo')),
('Gingivectomía/Gingivoplastía por pieza', 'Remodelación de encía en un diente.', 700, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia')),
('Gingivectomía/Gingivoplastía por Sextante', 'Remodelación de encía en un grupo dental.', 2000, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia')),
('Injerto Gingival', 'Trasplante de encía para cubrir raíces.', 1200, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia')),
('Raspado y Alisado por cuadrante', 'Limpieza profunda de encías en un sector.', 1500, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia')),
('Regeneración Tisular Guiada', 'Técnica para recuperar tejido óseo o encía.', 2000, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Otros')),
('Recuperación de espacio biológico', 'Cirugía para restablecer el espacio encía-hueso.', 350, TRUE, (SELECT categorie_service_id FROM categorie_service WHERE name='Periodoncia'))
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Show confirmation
SELECT 'Database sakura_database created successfully!' as message;
SELECT 'All tables created successfully!' as message;
SELECT 'Initial data inserted successfully!' as message; 