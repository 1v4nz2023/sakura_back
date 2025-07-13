# Sakura Dental Clinic - Physical Database Schema Diagram

## Database Configuration
- **Database Engine**: MySQL 8.0
- **Character Set**: utf8mb4
- **Collation**: utf8mb4_unicode_ci
- **Connection**: JDBC with Hibernate ORM
- **DDL Strategy**: update (auto-schema evolution)

## Complete Database Schema

### 1. User Management Tables

#### users
```sql
CREATE TABLE users (
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
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);
```

#### roles
```sql
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);
```

#### permissions
```sql
CREATE TABLE permissions (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);
```

#### employees
```sql
CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    job_title_id INT NOT NULL,
    hire_date DATE NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (job_title_id) REFERENCES job_titles(job_title_id)
);
```

#### job_titles
```sql
CREATE TABLE job_titles (
    job_title_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);
```

### 2. Patient Management Tables

#### patients
```sql
CREATE TABLE patients (
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
    FOREIGN KEY (document_type_id) REFERENCES document_types(document_type_id),
    FOREIGN KEY (gender_id) REFERENCES genders(gender_id),
    FOREIGN KEY (district_id) REFERENCES districts(district_id)
);
```

#### document_types
```sql
CREATE TABLE document_types (
    document_type_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);
```

#### genders
```sql
CREATE TABLE genders (
    gender_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);
```

#### districts
```sql
CREATE TABLE districts (
    district_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);
```

### 3. Service Catalog Tables

#### categorie_service
```sql
CREATE TABLE categorie_service (
    categorie_service_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE
);
```

#### services
```sql
CREATE TABLE services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10,2) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    categorie_service_id INT NOT NULL,
    FOREIGN KEY (categorie_service_id) REFERENCES categorie_service(categorie_service_id)
);
```

### 4. Clinical Management Tables

#### clinical_histories
```sql
CREATE TABLE clinical_histories (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    diagnosis TEXT,
    treatment TEXT,
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);
```

#### quotations
```sql
CREATE TABLE quotations (
    quotation_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    history_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDIENTE', 'ACEPTADA', 'PAGADA', 'ANULADA') DEFAULT 'PENDIENTE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (history_id) REFERENCES clinical_histories(history_id)
);
```

#### quotation_items
```sql
CREATE TABLE quotation_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    quotation_id INT NOT NULL,
    service_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (quotation_id) REFERENCES quotations(quotation_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);
```

### 5. Payment Management Tables

#### payment_methods
```sql
CREATE TABLE payment_methods (
    method_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);
```

#### payments
```sql
CREATE TABLE payments (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quotation_id INT NOT NULL,
    method_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    balance_remaining DECIMAL(10,2) NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDIENTE', 'CONFIRMADO', 'ANULADO') DEFAULT 'CONFIRMADO',
    created_by INT NOT NULL,
    canceled_by INT,
    FOREIGN KEY (quotation_id) REFERENCES quotations(quotation_id),
    FOREIGN KEY (method_id) REFERENCES payment_methods(method_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id),
    FOREIGN KEY (canceled_by) REFERENCES users(user_id)
);
```

#### receipts
```sql
CREATE TABLE receipts (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    receipt_number VARCHAR(20) NOT NULL UNIQUE,
    issue_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id)
);
```

## Entity Relationship Diagram (ERD)

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│     users       │     │     roles       │     │  permissions    │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ PK user_id      │     │ PK role_id      │     │ PK permission_id│
│ username        │     │ name            │     │ name            │
│ password_hash   │     │ description     │     │ description     │
│ email           │     │ status          │     │ status          │
│ FK role_id      │────▶│                 │     │                 │
│ FK employee_id  │     │                 │     │                 │
│ is_active       │     │                 │     │                 │
│ last_login      │     │                 │     │                 │
│ created_at      │     │                 │     │                 │
│ updated_at      │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │
         │ 1:1
         ▼
┌─────────────────┐     ┌─────────────────┐
│   employees     │     │  job_titles     │
├─────────────────┤     ├─────────────────┤
│ PK employee_id  │     │ PK job_title_id │
│ first_name      │     │ name            │
│ last_name       │     │ description     │
│ FK job_title_id │────▶│ status          │
│ hire_date       │     │                 │
│ status          │     │                 │
└─────────────────┘     └─────────────────┘

┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    patients     │     │ document_types  │     │     genders     │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ PK patient_id   │     │ PK doc_type_id  │     │ PK gender_id    │
│ first_name      │     │ name            │     │ name            │
│ last_name       │     │ description     │     │ description     │
│ doc_number      │     │ status          │     │ status          │
│ birth_date      │     │                 │     │                 │
│ FK doc_type_id  │────▶│                 │     │                 │
│ FK gender_id    │────▶│                 │     │                 │
│ FK district_id  │     │                 │     │                 │
│ phone           │     │                 │     │                 │
│ email           │     │                 │     │                 │
│ status          │     │                 │     │                 │
│ created_at      │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │
         │ N:1
         ▼
┌─────────────────┐
│    districts    │
├─────────────────┤
│ PK district_id  │
│ name            │
│ description     │
│ status          │
└─────────────────┘

┌─────────────────┐     ┌─────────────────┐
│    services     │     │ categorie_service│
├─────────────────┤     ├─────────────────┤
│ PK service_id   │     │ PK categorie_id │
│ name            │     │ name            │
│ description     │     │ description     │
│ base_price      │     │ status          │
│ status          │     │                 │
│ FK categorie_id │────▶│                 │
└─────────────────┘     └─────────────────┘
         │
         │ 1:N
         ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ quotation_items │     │   quotations    │     │ clinical_histories│
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ PK item_id      │     │ PK quotation_id │     │ PK history_id   │
│ FK quotation_id │────▶│ FK patient_id   │────▶│ FK patient_id   │
│ FK service_id   │     │ FK history_id   │     │ diagnosis       │
│ quantity        │     │ total_amount    │     │ treatment       │
│ unit_price      │     │ status          │     │ notes           │
│ subtotal        │     │ created_at      │     │ created_at      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                │
                                │ 1:N
                                ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    payments     │     │ payment_methods │     │    receipts     │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ PK payment_id   │     │ PK method_id    │     │ PK receipt_id   │
│ FK quotation_id │     │ name            │     │ FK payment_id   │
│ FK method_id    │────▶│ description     │     │ receipt_number  │
│ amount          │     │                 │     │ issue_date      │
│ balance_remaining│     │                 │     │ total_amount    │
│ payment_date    │     │                 │     │ status          │
│ status          │     │                 │     │                 │
│ FK created_by   │     │                 │     │                 │
│ FK canceled_by  │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## Indexes and Performance Optimization

### Primary Indexes
- All primary keys are auto-incrementing integers
- Unique constraints on business identifiers (username, email, doc_number)

### Foreign Key Indexes
- All foreign key columns are automatically indexed by MySQL
- Composite indexes for frequently queried combinations

### Recommended Additional Indexes
```sql
-- Patient search optimization
CREATE INDEX idx_patients_dni ON patients(doc_number);
CREATE INDEX idx_patients_name ON patients(first_name, last_name);
CREATE INDEX idx_patients_status ON patients(status);

-- Quotation status tracking
CREATE INDEX idx_quotations_status ON quotations(status);
CREATE INDEX idx_quotations_patient ON quotations(patient_id, status);

-- Payment tracking
CREATE INDEX idx_payments_quotation ON payments(quotation_id);
CREATE INDEX idx_payments_date ON payments(payment_date);

-- Service catalog
CREATE INDEX idx_services_category ON services(categorie_service_id, status);
```

## Data Types and Constraints

### String Fields
- **Names**: VARCHAR(80) - Sufficient for most names
- **Emails**: VARCHAR(120) - RFC compliant email length
- **Phone**: VARCHAR(20) - International format support
- **Descriptions**: TEXT - Unlimited length for detailed descriptions

### Numeric Fields
- **Prices**: DECIMAL(10,2) - Precise currency handling
- **IDs**: INT/BIGINT - Auto-incrementing primary keys
- **Quantities**: INT - Whole number quantities

### Date/Time Fields
- **Timestamps**: DATETIME - Full date and time information
- **Dates**: DATE - Date only (birth dates, hire dates)

### Boolean Fields
- **Status flags**: BOOLEAN - TRUE/FALSE for active/inactive states

## Security Considerations

### Password Security
- Passwords are hashed using BCrypt
- Salt rounds: 10 (configurable)
- Hash length: 95 characters

### Data Encryption
- Sensitive data should be encrypted at rest (future enhancement)
- JWT tokens for API authentication
- HTTPS for all communications

### Access Control
- Role-based access control (RBAC)
- Permission-based authorization
- Session management with JWT

## Backup and Recovery Strategy

### Backup Strategy
- Daily automated backups
- Point-in-time recovery capability
- Offsite backup storage

### Recovery Procedures
- Database restore procedures documented
- Data validation after recovery
- Rollback procedures for failed deployments

## Monitoring and Maintenance

### Performance Monitoring
- Query performance tracking
- Index usage monitoring
- Connection pool monitoring

### Maintenance Tasks
- Regular index optimization
- Statistics updates
- Log rotation and cleanup 