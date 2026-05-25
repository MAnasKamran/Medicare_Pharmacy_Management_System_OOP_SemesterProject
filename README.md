# MediCare Pharmacy Management System

A comprehensive desktop application for pharmacy operations, built with Java Swing and OOP principles. It manages inventory, sales, patients, prescriptions, suppliers, users, and generates reports.

## Features

- 🔐 **User Authentication** – Role-based login (Admin, Pharmacist, Cashier)
- 📦 **Inventory Management** – Add, edit, delete medicines; track stock levels, expiry dates, and low-stock alerts
- 💰 **Point of Sale (POS)** – Create sales carts, apply discounts/tax, process cash/card payments, print receipts
- 👥 **Patient Management** – Register patients, view prescription history, track loyalty points
- 📋 **Prescription Handling** – Create and fulfill prescriptions, link medicines to patients
- 🏢 **Supplier Management** – Manage vendor details and lead times
- 📊 **Sales Reports** – Visualize daily revenue, payment method splits, top-selling medicines
- 👤 **User Management (Admin)** – Add/edit/delete users, reset passwords, assign roles
- 📝 **Audit Log** – Track system events (logins, sales, inventory changes)

## Tech Stack

- **Language**: Java (JDK 17 or higher)
- **GUI Framework**: Swing (custom-styled with `SwingUtils`)
- **Persistence**: File-based serialization (binary `.dat` files + plain text logs)
- **Build**: Manual compilation (or IntelliJ IDEA)

## OOP Concepts Demonstrated

- **Inheritance** – `Entity` abstract class extended by all domain models; `Medicine` subclasses (`OTCMedicine`, `PrescriptionMedicine`, `NarcoticMedicine`)
- **Polymorphism** – `Payable` interface implemented by `CashPayment` and `CardPayment`; `Searchable` and `Deactivatable` interfaces
- **Encapsulation** – Private fields with public getters/setters; validation in setters
- **Abstraction** – Abstract `FileRepository<T>` with template methods; `Repository` interface
- **Composition** – `Sale` contains `SaleItem` list; `Patient` contains `Prescription` list
- **Aggregation** – `Medicine` references `Supplier` by ID; `Sale` references `User` (cashier)
- **Generics** – `Result<T>`, `BoundedStack<T>`, `Pair<K,V>`
- **Singleton** – All DAOs and service classes
- **DAO Pattern** – Separate layer for data access (`MedicineDAO`, `SaleDAO`, etc.)

## Project Structure
