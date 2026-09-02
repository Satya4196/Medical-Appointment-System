# 🏥 Medical Appointment System

> A modern Java-based application for managing patients, doctors, and medical appointments efficiently.

---

## 📌 Project Overview

The **Medical Appointment System** is a Java application designed to simplify the process of managing medical appointments.

The system provides functionality for:

- 👨‍⚕️ Managing doctors
- 🧑‍🤝‍🧑 Managing patients
- 📅 Booking appointments
- 🔐 User authentication
- 🗄️ Database connectivity
- 🖥️ Interactive graphical user interface

---

## ✨ Features

### 🔐 Authentication
- Secure user login
- User authentication management

### 👨‍⚕️ Doctor Management
- Add doctor details
- Manage doctor information
- View available doctors

### 🧑 Patient Management
- Add patient details
- Manage patient information
- View patient records

### 📅 Appointment Management
- Create medical appointments
- Manage appointment details
- Connect patients with doctors

### 🖥️ User Interface
- Modern Java GUI
- Login interface
- Admin interface
- Patient interface
- Main menu navigation

### 🗄️ Database Integration
- Database connectivity using Java
- SQL database schema files included
- DAO architecture for database operations

---

## 🛠️ Technologies Used

| Technology | Purpose |
|-----------|---------|
| ☕ Java | Core application development |
| 🖥️ Java GUI | User interface |
| 🗄️ SQL | Database management |
| 🔗 JDBC | Database connectivity |
| 📂 DAO Pattern | Data access operations |
| 🧠 OOP | Application architecture |

---

## 📁 Project Structure

```text
Medical-Appointment-System/
│
├── 📁 dao/
│   ├── AppointmentDAO.java
│   ├── DoctorDAO.java
│   └── UserDAO.java
│
├── 📁 db/
│   └── Database connection files
│
├── 📁 gui/
│   ├── AdminGUI.java
│   ├── LoginGUI.java
│   ├── MainMenuUI.java
│   └── PatientUI.java
│
├── 📁 model/
│   ├── Appointment.java
│   └── Doctor.java
│
├── 📁 service/
│   ├── AdminService.java
│   ├── AuthService.java
│   ├── DoctorService.java
│   └── PatientService.java
│
├── 📁 ui/
│   └── User interface components
│
├── 📄 Main.java
├── 📄 README.md
├── 📄 schema.sql
├── 📄 schema_database.sql
└── 📄 run.bat
