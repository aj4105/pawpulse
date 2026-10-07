-- PawPulse Database Schema
-- Run this script in MySQL to create database, tables, procedures, and triggers.

CREATE DATABASE IF NOT EXISTS pawpulse;
USE pawpulse;

-- ---------------------------------------------------------------------------
-- Tables
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) DEFAULT '',
    phone VARCHAR(50) DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pets (
    pet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    breed VARCHAR(100) DEFAULT '',
    age INT DEFAULT NULL,
    weight DECIMAL(5,2) DEFAULT NULL,
    gender VARCHAR(20) DEFAULT '',
    species VARCHAR(50) DEFAULT 'Dog',
    status VARCHAR(50) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_pets_user (user_id)
);

CREATE TABLE IF NOT EXISTS health_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    pet_id INT NOT NULL,
    record_type VARCHAR(50) DEFAULT 'Checkup',
    description TEXT,
    record_date DATE NOT NULL,
    vet_name VARCHAR(255) DEFAULT '',
    next_due_date VARCHAR(20) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE,
    INDEX idx_health_pet (pet_id)
);

CREATE TABLE IF NOT EXISTS reminders (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    pet_id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    reminder_type VARCHAR(50) DEFAULT 'General',
    is_completed TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_reminders_user (user_id),
    INDEX idx_reminders_due (due_date)
);

CREATE TABLE IF NOT EXISTS vets (
    vet_id INT AUTO_INCREMENT PRIMARY KEY,
    clinic_name VARCHAR(255) NOT NULL,
    address VARCHAR(500) DEFAULT '',
    phone VARCHAR(50) DEFAULT '',
    emergency_phone VARCHAR(50) DEFAULT '',
    email VARCHAR(255) DEFAULT '',
    working_hours VARCHAR(255) DEFAULT ''
);

-- Sample vet data (optional)
INSERT INTO vets (clinic_name, address, phone, emergency_phone, email, working_hours) VALUES
('Happy Paws Veterinary Clinic', '123 Pet Care Lane', '555-0100', '555-0199', 'contact@happypaws.example.com', 'Mon-Fri 8am-6pm, Sat 9am-2pm'),
('Emergency Pet Hospital', '456 Emergency Ave', '555-0200', '555-0200', 'emergency@petemergency.example.com', '24/7');
