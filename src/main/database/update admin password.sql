-- Replace USERS table name/columns if different
USE Dealership_inventory;
UPDATE Users
SET password_hash = '$2a$10$5lbbV9z/nbVFuw8pxBhw0ejq59GZaOZCVB.T6gO4m4lvrbFMvejWa'
WHERE username = 'jsmith';
UPDATE Users
SET password_hash = '$2a$10$5lbbV9z/nbVFuw8pxBhw0ejq59GZaOZCVB.T6gO4m4lvrbFMvejWa'
WHERE username = 'admin';
