-- 1. Create user
CREATE USER pizzeria_admin WITH PASSWORD 'mypassword';

-- 2. Create database owned by that user
CREATE DATABASE db_pizzeria OWNER pizzeria_admin;

-- 3. (Optional) Grant all privileges on the DB to the user
GRANT ALL PRIVILEGES ON DATABASE db_pizzeria TO pizzeria_admin;





