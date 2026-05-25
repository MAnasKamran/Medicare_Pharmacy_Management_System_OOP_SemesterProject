# PharmacyPMS — Pharmacy Management & POS System

A desktop-based **Pharmacy Management and Point-of-Sale (POS) System** developed in **Java Swing**.
The application simulates a real-world pharmacy workflow including inventory management, prescription handling, patient records, supplier tracking, billing, reporting, and role-based access control.

---

# Features

## Authentication & Access Control

* Secure login system
* Role-based access:

  * Admin
  * Pharmacist
  * Cashier
* Account lockout after multiple failed attempts
* Session management

---

# Inventory Management

* Add, edit, update, and remove medicines
* Stock quantity management
* Low-stock alerts
* Barcode lookup support
* Medicine categorization
* Narcotic medicine restrictions

---

# Point of Sale (POS)

* Cart-based checkout system
* Real-time stock validation
* Discount and tax calculations
* Cash and card payment support
* Receipt generation

---

# Patient Management

* Patient registration
* Allergy tracking
* Prescription history

---

# Prescription Management

* Prescription creation and verification
* Pharmacist approval workflow
* Fulfillment tracking

---

# Supplier Management

* Supplier records management
* Contact information tracking
* Lead-time management

---

# Sales Reporting & Analytics

* Revenue reports
* Date-range filtering
* Transaction history
* Top-selling medicines analytics
* CSV export support

---

# Audit Logging

* Login/logout tracking
* Sales activity logs
* Inventory action logs
* System-wide audit trail

---

# Technologies Used

* Java 17
* Java Swing
* Java Collections Framework
* Java Streams API
* Java Serialization
* File Handling
* Object-Oriented Programming

---

# OOP Concepts Implemented

* Encapsulation
* Inheritance
* Polymorphism
* Abstraction
* Method Overloading
* Method Overriding
* Interfaces
* Abstract Classes
* Dynamic Binding
* Composition
* Generics

---

# Design Patterns Used

* Singleton Pattern
* Repository Pattern
* Strategy Pattern
* Template Method Pattern

---

# System Architecture

The application follows a layered architecture:

```text
UI Layer
   ↓
Service Layer
   ↓
DAO Layer
   ↓
Model Layer
```

### Package Structure

```text
com.pharmacy.model
com.pharmacy.dao
com.pharmacy.service
com.pharmacy.ui
com.pharmacy.util
com.pharmacy.generics
```

---

# Main Modules

* Login Panel
* Dashboard Panel
* POS Panel
* Inventory Panel
* Patient Panel
* Supplier Panel
* User Management Panel
* Sales Report Panel
* Prescription Panel
* Audit Log Panel

---

# Data Persistence

The system uses:

* Java Object Serialization (`.dat` files)
* Generic file-backed repositories
* Automatic persistence after updates
* Log-based audit storage

No external database is required.

---

# Key Technical Highlights

* Generic `Repository<T, ID>` implementation
* Generic `Result<T>` wrapper
* File-backed DAO architecture
* Role-based narcotic dispensing validation
* Real-time stock synchronization during checkout
* Thread-safe ID generation using `AtomicInteger`

---

# Default Accounts

| Role       | Username     | Password     |
| ---------- | ------------ | ------------ |
| Admin      | `admin`      | `admin123`   |
| Pharmacist | `pharmacist` | `pharma123`  |
| Cashier    | `cashier`    | `cashier123` |

---


# How to Run

## Requirements

* Java JDK 17 or above
* Any Java IDE:

  * IntelliJ IDEA
  * Eclipse
  * NetBeans

---

## Steps

1. Clone the repository

```bash
git clone https://github.com/your-username/PharmacyPMS.git
```

2. Open the project in your IDE

3. Run:

```text
com.pharmacy.ui.PharmacyApp
```

---

# Suggested Repository Structure

```text
PharmacyPMS/
│
├── src/
├── screenshots/
├── uml/
├── report/
├── README.md
└── .gitignore
```

---

# Future Improvements

* SQLite/MySQL integration
* REST API backend
* Web-based frontend
* Barcode scanner integration
* Drug interaction checking
* Email/SMS receipts
* BCrypt password hashing
* Automated JUnit testing

---

