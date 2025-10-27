/*
===========================================================
Dealership Inventory Assistant - Verification & Row Count Script
Author: Kenneth Nimmo
Description:
  Queries each table to verify schema creation, seed data,
  and record counts for the Dealership Inventory Assistant.
===========================================================
*/

USE dealership_inventory;

-- ===== USERS =====
SELECT '=== USERS TABLE ===' AS section;
SELECT * FROM Users;
-- SELECT COUNT(*) AS 'User Count' FROM Users;

-- ===== SALESPERSONS =====
SELECT '=== SALESPERSONS TABLE ===' AS section;
SELECT * FROM Salespersons;
-- SELECT COUNT(*) AS 'Salesperson Count' FROM Salespersons;

-- ===== VEHICLES =====
SELECT '=== VEHICLES TABLE ===' AS section;
SELECT * FROM Vehicles;
-- SELECT COUNT(*) AS 'Vehicle Count' FROM Vehicles;

-- ===== PROSPECTS =====
SELECT '=== PROSPECTS TABLE ===' AS section;
SELECT * FROM Prospects;
-- SELECT COUNT(*) AS 'Prospect Count' FROM Prospects;

-- ===== SALES =====
SELECT '=== SALES TABLE ===' AS section;
SELECT * FROM Sales;
-- SELECT COUNT(*) AS 'Sales Record Count' FROM Sales;

-- ===== SUMMARY =====
-- SELECT 
    -- (SELECT COUNT(*) FROM Users) AS users,
    -- (SELECT COUNT(*) FROM Salespersons) AS salespersons,
    -- (SELECT COUNT(*) FROM Vehicles) AS vehicles,
    -- (SELECT COUNT(*) FROM Prospects) AS prospects,
    -- (SELECT COUNT(*) FROM Sales) AS sales,
    -- 'All table row counts summarized above' AS summary_message;

-- Final confirmation message
SELECT 'Verification complete — all tables queried successfully!' AS message;
