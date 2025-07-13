# Sakura Dental Clinic - Updated Logical Architecture Diagram

## System Overview
The Sakura Dental Clinic backend is a Spring Boot application that manages dental clinic operations including patient management, service catalog, quotations, payments, and clinical history.

## Core Business Domains

### 1. User Management & Security
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      User       │    │      Rol        │    │   Permission    │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ - userId        │    │ - roleId        │    │ - permissionId  │
│ - username      │    │ - name          │    │ - name          │
│ - passwordHash  │    │ - descripcion   │    │ - description   │
│ - email         │    │ - status        │    │ - status        │
│ - rol           │    └─────────────────┘    └─────────────────┘
│ - employee      │            ▲                    ▲
│ - isActive      │            │                    │
│ - lastLogin     │            │                    │
│ - createdAt     │            │                    │
│ - updatedAt     │            │                    │
└─────────────────┘            │                    │
        │                      │                    │
        │ 1:1                  │                    │
        ▼                      │                    │
┌─────────────────┐            │                    │
│    Employee     │            │                    │
├─────────────────┤            │                    │
│ - doc_number    │            │                    │
│ - first_name    │            │                    │
│ - last_name     │            │                    │
│ - specialty     │            │                    │
│ - hired_at      │            │                    │
│ - phone         │            │                    │
│ - email         │            │                    │
│ - status        │            │                    │
│ - tipoDocumento │            │                    │
│ - jobTitle      │            │                    │
│ - gender        │            │                    │
│ - district      │            │                    │
└─────────────────┘            │                    │
        │                      │                    │
        │ N:1                  │                    │
        ▼                      │                    │
┌─────────────────┐            │                    │
│   JobTitles     │            │                    │
├─────────────────┤            │                    │
│ - jobTitleId    │            │                    │
│ - name          │            │                    │
│ - description   │            │                    │
│ - status        │            │                    │
└─────────────────┘            │                    │
                               │                    │
                               └────────────────────┘
```

### 2. Patient Management
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Patient     │    │  DocumentType   │    │     Gender      │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ - patientId     │    │ - documentTypeId│    │ - genderId      │
│ - firstName     │    │ - name          │    │ - name          │
│ - lastName      │    │ - description   │    │ - description   │
│ - dni           │    │ - status        │    │ - status        │
│ - birthDate     │    └─────────────────┘    └─────────────────┘
│ - tipoDocumento │            ▲                    ▲
│ - gender        │            │                    │
│ - district      │            │                    │
│ - phoneNumber   │            │                    │
│ - email         │            │                    │
│ - status        │            │                    │
│ - createdAt     │            │                    │
└─────────────────┘            │                    │
        │                      │                    │
        │ N:1                  │                    │
        ▼                      │                    │
┌─────────────────┐            │                    │
│     District    │            │                    │
├─────────────────┤            │                    │
│ - districtId    │            │                    │
│ - name          │            │                    │
│ - description   │            │                    │
│ - status        │            │                    │
└─────────────────┘            │                    │
                               │                    │
                               └────────────────────┘
```

### 3. Service Catalog Management
```
┌─────────────────┐    ┌─────────────────┐
│     Service     │    │ CategorieService│
├─────────────────┤    ├─────────────────┤
│ - serviceId     │    │ - categorieServiceId│
│ - name          │    │ - name          │
│ - description   │    │ - description   │
│ - basePrice     │    │ - status        │
│ - status        │    └─────────────────┘
│ - categorieService│          ▲
└─────────────────┘            │
        │                      │
        │ N:1                  │
        └──────────────────────┘
```

### 4. Clinical Process Flow
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Clinical      │    │   Quotation     │    │  QuotationItem  │
│   History       │    ├─────────────────┤    ├─────────────────┤
├─────────────────┤    │ - quotationId   │    │ - itemId        │
│ - historyId     │    │ - patient       │    │ - quotation     │
│ - patient       │    │ - history       │    │ - service       │
│ - createdAt     │    │ - totalAmount   │    │ - quantity      │
│ - notes         │    │ - status        │    │ - unitPrice     │
└─────────────────┘    │ - createdAt     │    │ - subtotal      │
        ▲              └─────────────────┘    └─────────────────┘
        │                      │                      │
        │                      │ 1:N                  │
        │                      ▼                      │
        │              ┌─────────────────┐            │
        │              │     Payment     │            │
        │              ├─────────────────┤            │
        │              │ - paymentId     │            │
        │              │ - quotation     │            │
        │              │ - method        │            │
        │              │ - amount        │            │
        │              │ - balanceRemaining│          │
        │              │ - paymentDate   │            │
        │              │ - status        │            │
        │              │ - createdBy     │            │
        │              │ - canceledBy    │            │
        │              └─────────────────┘            │
        │                      │                      │
        │                      │ N:1                  │
        │                      ▼                      │
        │              ┌─────────────────┐            │
        │              │ PaymentMethod   │            │
        │              ├─────────────────┤            │
        │              │ - methodId      │            │
        │              │ - name          │            │
        │              │ - description   │            │
        │              └─────────────────┘            │
        │                                              │
        └──────────────────────────────────────────────┘
```

### 5. Receipt Management
```
┌─────────────────┐    ┌─────────────────┐
│     Receipt     │    │     Payment     │
├─────────────────┤    ├─────────────────┤
│ - receiptId     │    │ - paymentId     │
│ - payment       │    │ - quotation     │
│ - receiptNumber │    │ - method        │
│ - receiptType   │    │ - amount        │
│ - generatedAt   │    │ - balanceRemaining│
│                 │    │ - paymentDate   │
│ ReceiptType:    │    │ - status        │
│ - BOLETA        │    │ - createdBy     │
│ - FACTURA       │    │ - canceledBy    │
└─────────────────┘    └─────────────────┘
        │                      ▲
        │ N:1                  │
        └──────────────────────┘
```

## Business Logic Flow

### 1. Patient Registration Process
```
1. User Input Patient Data
   ↓
2. Validate Document Number (DNI)
   ↓
3. Check for Duplicate Patient
   ↓
4. Create Patient Record
   ↓
5. Generate Clinical History
   ↓
6. Return Patient ID
```

### 2. Quotation Creation Process
```
1. Select Patient
   ↓
2. Choose Services from Catalog
   ↓
3. Calculate Total Amount (auto-calculated subtotals)
   ↓
4. Apply Discounts (if any)
   ↓
5. Generate Quotation
   ↓
6. Send to Patient
   ↓
7. Track Status (PENDIENTE → ACEPTADA → PAGADA)
```

### 3. Payment Processing Flow
```
1. Patient Accepts Quotation
   ↓
2. Select Payment Method
   ↓
3. Process Payment
   ↓
4. Update Balance
   ↓
5. Generate Receipt (BOLETA or FACTURA)
   ↓
6. Update Quotation Status
   ↓
7. Schedule Treatment
```

### 4. Clinical History Management
```
1. Patient Visit
   ↓
2. Update Clinical History
   ↓
3. Record Notes
   ↓
4. Plan Treatment
   ↓
5. Create Quotation
   ↓
6. Track Progress
   ↓
7. Update History
```

## Security Architecture

### Authentication & Authorization
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   JWT Token     │    │   Spring        │    │   Role-Based    │
│   Generation    │    │   Security      │    │   Access        │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ - Username      │    │ - Authentication│    │ - ADMIN         │
│ - Password      │    │ - Authorization │    │ - DOCTOR        │
│ - Role          │    │ - JWT Filter    │    │ - ASSISTANT     │
│ - Expiration    │    │ - CORS Config   │    │ - RECEPTIONIST  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Role-Permission Matrix
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    Rol      │    │Permission   │    │  User       │
│             │    │             │    │             │
│ • roleId    │    │ • permId    │    │ • userId    │
│ • name      │    │ • name      │    │ • username  │
│ • descripcion│   │ • description│   │ • password  │
│ • status    │    │ • status    │    │ • email     │
│ • permissions│   │             │    │ • rol       │
│ • users     │    │             │    │ • employee  │
└─────────────┘    └─────────────┘    └─────────────┘
        │                   ▲                   │
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
                            ▼
                    ┌─────────────┐
                    │role_permissions│
                    │             │
                    │ • role_id   │
                    │ • permission_id│
                    └─────────────┘
```

## Data Validation Rules

### Patient Validation
- DNI must be unique and 8 digits
- Email format validation
- Phone number format validation
- Required fields: firstName, lastName, tipoDocumento, gender, district

### Employee Validation
- Document number must be unique
- Email format validation
- Phone number format validation
- Required fields: first_name, last_name, tipoDocumento, jobTitle, gender, district

### Quotation Validation
- Patient must exist and be active
- At least one service must be selected
- Total amount must be greater than 0
- Status transitions must be valid

### Payment Validation
- Payment amount cannot exceed quotation balance
- Payment method must be valid
- Payment cannot be made on canceled quotations

### Receipt Validation
- Receipt number must be unique
- Receipt type must be BOLETA or FACTURA
- Payment must exist and be confirmed

## Error Handling Strategy

### Business Logic Errors
- Duplicate patient/employee registration
- Invalid quotation status transitions
- Insufficient payment amounts
- Service not found
- Invalid receipt type

### System Errors
- Database connection issues
- JWT token expiration
- Invalid request format
- Authorization failures

## Integration Points

### External Systems
- Payment gateways (future)
- Email notification service (future)
- SMS notification service (future)
- Laboratory management system (future)

### Frontend Integration
- RESTful API endpoints
- CORS configuration for Next.js frontend
- JWT token-based authentication
- Real-time updates (WebSocket - future) 