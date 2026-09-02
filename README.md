# Medical Appointment System

A Java-based desktop application for managing medical appointments. Allows patients to book appointments, doctors to manage their schedules, and admins to manage the system.

## Features

- **User Registration & Authentication**: Secure login for Admin, Doctor, and Patient roles
- **Appointment Management**: Patients can book, view, and cancel appointments
- **Doctor Management**: Admin can manage doctor profiles and specializations
- **Patient Management**: View and manage patient information
- **Database Integration**: MySQL database for persistent data storage
- **GUI Interface**: User-friendly Swing-based GUI

## Prerequisites

- **Java**: JDK 25 or higher
- **MySQL Server**: Version 8.0 or higher
- **MySQL Connector**: MySQL JDBC Driver (included in `/lib/mysql-connector-j-9.2.0.jar`)

## Installation & Setup

### 1. Install MySQL

Ensure MySQL Server 8.0 is installed on your system. The default credentials used are:
- **Username**: `root`
- **Password**: `root123` (or whatever password you set during MySQL installation)

If your credentials are different, update them in:
```
db/DBConnection.java
Lines 9-10
```

### 2. Create Database & Tables

Run the following SQL commands in MySQL to set up the database:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS medical_system;
USE medical_system;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20)
);

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    time_slot VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### 3. Insert Sample Data (Optional)

```sql
-- Sample admin user
INSERT INTO users (name, password, role, specialization, phone) 
VALUES ('Admin', 'admin123', 'admin', NULL, NULL);

-- Sample doctors
INSERT INTO users (name, password, role, specialization, phone) 
VALUES ('Dr. John', 'doctor123', 'doctor', 'Cardiology', '9876543210');
INSERT INTO users (name, password, role, specialization, phone) 
VALUES ('Dr. Sarah', 'doctor123', 'doctor', 'Neurology', '9876543211');
INSERT INTO users (name, password, role, specialization, phone) 
VALUES ('Dr. Mike', 'doctor123', 'doctor', 'Orthopedics', '9876543212');

-- Sample patients
INSERT INTO users (name, password, role, specialization, phone) 
VALUES ('Patient1', 'patient123', 'patient', NULL, '8765432100');
INSERT INTO users (name, password, role, specialization, phone) 
VALUES ('Patient2', 'patient123', 'patient', NULL, '8765432101');
INSERT INTO users (name, password, role, specialization, phone) 
VALUES ('Patient3', 'patient123', 'patient', NULL, '8765432102');

-- Sample appointments
INSERT INTO appointments (patient_id, doctor_id, time_slot, status) 
VALUES (5, 2, '2024-03-15 10:00 AM', 'pending');
INSERT INTO appointments (patient_id, doctor_id, time_slot, status) 
VALUES (6, 3, '2024-03-15 02:00 PM', 'pending');
INSERT INTO appointments (patient_id, doctor_id, time_slot, status) 
VALUES (7, 4, '2024-03-16 09:00 AM', 'pending');
```

## How to Compile

Navigate to the project directory and run:

```bash
javac -cp "lib\mysql-connector-j-9.2.0.jar" *.java db/*.java gui/*.java service/*.java model/*.java dao/*.java
```

**For Windows (PowerShell):**
```powershell
javac -cp "lib\mysql-connector-j-9.2.0.jar" *.java db\*.java gui\*.java service\*.java model\*.java dao\*.java
```

## How to Run

Execute the application with:

```bash
java -cp "lib\mysql-connector-j-9.2.0.jar;." Main
```

**For Windows (PowerShell):**
```powershell
java -cp "lib\mysql-connector-j-9.2.0.jar;." Main
```

The application will launch with a main menu where you can:
- **Login**: Use existing credentials
- **Register**: Create a new account

## Test Credentials

Use these credentials to test the application:

### Admin
- **Username**: `Admin`
- **Password**: `admin123`

### Doctor
- **Username**: `Dr. John`
- **Password**: `doctor123`

### Patient
- **Username**: `Patient1`
- **Password**: `patient123`

## Project Structure

```
medicalAppointmentSystem/
├── Main.java                    # Entry point
├── README.md                    # This file
├── db/
│   ├── DBConnection.java       # Database connection utility
│   └── schema.sql              # Database schema
├── dao/
│   ├── UserDAO.java            # User data access object
│   ├── DoctorDAO.java          # Doctor data access object
│   └── AppointmentDAO.java     # Appointment data access object
├── model/
│   ├── User.java               # User model
│   ├── Doctor.java             # Doctor model
│   └── Appointment.java        # Appointment model
├── service/
│   ├── AuthService.java        # Authentication service
│   ├── AdminService.java       # Admin operations
│   └── PatientService.java     # Patient operations
├── gui/
│   ├── MainMenuGUI.java        # Main menu interface
│   ├── LoginGUI.java           # Login/Registration interface
│   ├── AdminGUI.java           # Admin dashboard
│   ├── DoctorGUI.java          # Doctor dashboard
│   └── PatientGUI.java         # Patient dashboard
└── lib/
    └── mysql-connector-j-9.2.0.jar  # MySQL JDBC driver
```

## Troubleshooting

### Database Connection Failed
- Ensure MySQL Server is running
- Check username and password in `db/DBConnection.java`
- Verify the `medical_system` database exists

### Compilation Errors
- Ensure MySQL JDBC driver is in the `lib/` directory
- Use the correct compiler with `-cp` flag pointing to the JAR file

### Login Failed After Registration
- Make sure the database connection is working
- Verify that the user data was inserted into the database correctly
- Check the console for detailed error messages

## Supported Operations

### Admin
- Manage doctors (add, update, delete)
- View all patients
- View all appointments
- System administration

### Doctor
- View appointments
- Manage own profile
- Accept/Reject appointments
- View patient information

### Patient
- Book appointments
- View appointments
- Cancel appointments
- Update profile

## Technical Stack

- **Language**: Java (JDK 25)
- **GUI Framework**: Swing
- **Database**: MySQL 8.0
- **JDBC Driver**: MySQL Connector/J 9.2.0
- **Architecture**: MVC (Model-View-Controller) with DAO pattern

## License

This project is open source and available for educational purposes.

---

**For further assistance or issues, check the console output for detailed error messages.**
