/*
===========================================================
Dealership Inventory Assistant - Database Setup Script
Author: Kenneth Nimmo
Course: CIS 175 Final Project
Description:
  Creates the database schema for the Dealership Inventory Assistant
  web application. Includes tables for vehicles, prospects (customers),
  salespersons, sales transactions, and users for authentication.
===========================================================
*/

-- Drop and recreate the database
DROP DATABASE IF EXISTS dealership_inventory;
CREATE DATABASE dealership_inventory;
USE dealership_inventory;

-- ==========================
-- Table: Users
-- ==========================
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('admin', 'sales') DEFAULT 'sales',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- Table: Salespersons
-- ==========================
CREATE TABLE Salespersons (
    salesperson_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    commission_rate DECIMAL(5,2) DEFAULT 0.00
);

-- ==========================
-- Table: Vehicles
-- ==========================
CREATE TABLE Vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    color VARCHAR(30),
    category ENUM('Car','Truck','SUV','Van','Other') DEFAULT 'Car',
    cost DECIMAL(10,2) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status ENUM('Available','Sold','Reserved') DEFAULT 'Available',
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- Table: Prospects
-- ==========================
CREATE TABLE Prospects (
    prospect_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    desired_make VARCHAR(50),
    desired_model VARCHAR(50),
    desired_color VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- Table: Sales
-- ==========================
CREATE TABLE Sales (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    salesperson_id INT NOT NULL,
    prospect_id INT,
    sale_date DATE NOT NULL,
    sale_price DECIMAL(10,2) NOT NULL,
    commission_earned DECIMAL(10,2),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id),
    FOREIGN KEY (salesperson_id) REFERENCES Salespersons(salesperson_id),
    FOREIGN KEY (prospect_id) REFERENCES Prospects(prospect_id)
);

-- ==========================
-- Seed Data
-- ==========================

-- Users
INSERT INTO Users (username, password_hash, role) VALUES
('admin', '$2a$10$samplehashedadminpass', 'admin'),
('jsmith', '$2a$10$samplehashedsalespass', 'sales');

-- Salespersons
INSERT INTO Salespersons (first_name, last_name, email, phone, commission_rate) VALUES
('John', 'Smith', 'jsmith@example.com', '555-1234', 5.00),
('Maria', 'Lopez', 'mlopez@example.com', '555-5678', 6.50);

-- Vehicles
INSERT INTO Vehicles (make, model, year, color, category, cost, price, status) VALUES
('Toyota', 'Camry', 2022, 'Silver', 'Car', 21000, 25999, 'Available'),
('Ford', 'F-150', 2023, 'Blue', 'Truck', 34000, 38999, 'Reserved'),
('Honda', 'Civic', 2021, 'White', 'Car', 18500, 22999, 'Sold');

-- Prospects
INSERT INTO Prospects (first_name, last_name, email, phone, desired_make, desired_model, desired_color) VALUES
('Alex', 'Turner', 'alex.t@example.com', '555-3456', 'Toyota', 'Camry', 'Silver'),
('Samantha', 'Reed', 'sam.reed@example.com', '555-7890', 'Ford', 'F-150', 'Blue');

-- Sales
INSERT INTO Sales (vehicle_id, salesperson_id, prospect_id, sale_date, sale_price, commission_earned) VALUES
(3, 1, 2, '2025-10-01', 22999, 1150.00);

-- ==========================
-- Verification
-- ==========================
SELECT 'Database and tables created successfully!' AS message;
