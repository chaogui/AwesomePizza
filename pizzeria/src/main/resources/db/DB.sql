-- 1. Create user
CREATE USER pizzeria_admin WITH PASSWORD 'mypassword';

-- 2. Create database owned by that user
CREATE DATABASE db_pizzeria OWNER pizzeria_admin;

-- 3. (Optional) Grant all privileges on the DB to the user
GRANT ALL PRIVILEGES ON DATABASE db_pizzeria TO pizzeria_admin;



-- create table pizzas
CREATE TABLE pizzas (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    price NUMERIC(10, 2)
);

-- create table toppings
CREATE TABLE toppings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

-- create table pizza_toppings
CREATE TABLE pizza_toppings (
    pizza_id INT,
    topping_id INT,
    PRIMARY KEY (pizza_id, topping_id),
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id),
    FOREIGN KEY (topping_id) REFERENCES toppings(id)
);


-- create table orders
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    created_time TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    total_price NUMERIC(10, 2),
    completed_time TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- create table order_pizzas
CREATE TABLE order_pizzas (
    order_id INT,
    pizza_id INT,
    PRIMARY KEY (order_id, pizza_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id)

);

-- create table users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50),
    email VARCHAR(50),
    phone_number VARCHAR(50),
    active VARCHAR(10),
    register_time TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    last_modification_time TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);