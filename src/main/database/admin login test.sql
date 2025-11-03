USE dealership_inventory;
SELECT user_id, username, LENGTH(password_hash) AS len, HEX(SUBSTRING(password_hash, -2)) AS last2hex
FROM Users
WHERE username='admin';
-- Expect len around 60; last2hex should be valid hex (no 0A for newline).