# AwesomePizza

It's an online Pizzeria where you can order pizza    

## Prepare Database
### create user  and database

```sql
-- 1. Create user

CREATE USER pizzeria_admin WITH PASSWORD 'mypassword';

-- 2. Create database owned by that user

CREATE DATABASE db_pizzeria OWNER pizzeria_admin;  

-- 3. (Optional) Grant all privileges on the DB to the user

GRANT ALL PRIVILEGES ON DATABASE db_pizzeria TO pizzeria_admin;
```

### create tables: pizzas, toppings, orders and relation tables

```sql
-- create table pizzas  
CREATE TABLE pizzas (  
    id SERIAL PRIMARY KEY,  
    name VARCHAR(50),  
    price NUMERIC(10, 2)  
);  
ALTER TABLE pizzas ADD COLUMN description varchar(1000);
  
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
  
-- create table order_items  
CREATE TABLE order_items (
	id SERIAL PRIMARY KEY,
	quantity INT,
	price NUMERIC(10, 2),
	order_id INT,
	pizza_id INT,
	FOREIGN KEY (order_id) REFERENCES orders(id),
	FOREIGN KEY (pizza_id) REFERENCES pizzas(id)
);
```

### add data for pizzas

```sql
INSERT INTO public.pizzas (id, "name", price, description) VALUES(1, 'MARINARA', 6.50, 'Aglio, pomodoro, prezzemolo, olio evo');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(2, 'MEDITERRANEA', 7.50, 'Olive nere*, cipolla rossa, pomodoro, origano');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(3, 'MARGHERITA', 8.50, 'Fiordilatte, pomodoro, basilico, olio evo');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(4, 'PROVOLA', 9.50, 'Provola affumicata al naturale, pomodoro bio, basilico, pepe nero, olio evo');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(5, 'BUFALA', 10.50, 'Mozzarella di bufala, pomodoro, basilico, olio evo');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(6, 'NAPOLI', 11.00, 'Acciughe siciliane di Aspra, capperi di Salina, fiordilatte campano, pomodoro bio, origano');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(8, 'SALSICCIA E PARMIGIANO', 11.00, 'Salsiccia , pomodoro, parmigiano, semi di finocchio');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(9, 'DIAVOLA', 11.50, 'Salamino piccante, pomodoro, fiordilatte, prezzemolo');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(10, 'ORTOLANA', 11.50, 'Zucchine al forno, patate schiacciate all’olio, provola affumicata al naturale, pesto di basilico con mandorle, pomodorini confit, basilico');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(11, 'BOLOGNA', 12.00, 'Mortadella , patate , fiordilatte, pesto');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(12, 'SPECK E GORGONZOLA', 12.00, 'Speck, gorgonzola , fiordilatte , miele di Acacia, noci');
INSERT INTO public.pizzas (id, "name", price, description) VALUES(13, 'TONNO', 11.50, 'Tonno Callipo, cipolla rossa saltata, fiordilatte campano, salsa verde, olive nere*, origano');

-- update sequence
SELECT pg_get_serial_sequence('pizzas', 'id');
SELECT setval('public.pizzas_id_seq', 14);
select nextval('public.pizzas_id_seq');


```

### add data for toppings

```sql

INSERT INTO public.toppings (id, "name") VALUES(1, 'pomodoro');
INSERT INTO public.toppings (id, "name") VALUES(2, 'basilico');
INSERT INTO public.toppings (id, "name") VALUES(3, 'cipolla rossa');
INSERT INTO public.toppings (id, "name") VALUES(4, 'fiordilatte');
INSERT INTO public.toppings (id, "name") VALUES(5, 'salsa verde');
INSERT INTO public.toppings (id, "name") VALUES(6, 'olive nere');
INSERT INTO public.toppings (id, "name") VALUES(7, 'origano');
INSERT INTO public.toppings (id, "name") VALUES(8, 'tonno');
INSERT INTO public.toppings (id, "name") VALUES(9, 'speck');
INSERT INTO public.toppings (id, "name") VALUES(10, 'gorgonzola');

-- update sequence
SELECT pg_get_serial_sequence('toppings', 'id');
SELECT setval('public.toppings_id_seq', 11);
select nextval('public.toppings_id_seq');


```

### Data Tables
##### Pizza

| id  | name                         | price | description                                                                                                                                 |
| --- | ---------------------------- | ----- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | MARINARA                     | 6.5   | Aglio, pomodoro, prezzemolo, olio evo                                                                                                       |
| 2   | MEDITERRANEA                 | 7.5   | Olive nere*, cipolla rossa, pomodoro, origano  <br>                                                                                         |
| 3   | MARGHERITA  <br>             | 8.5   | Fiordilatte, pomodoro, basilico, olio evo                                                                                                   |
| 4   | PROVOLA  <br>                | 9.5   | Provola affumicata al naturale, pomodoro bio, basilico, pepe nero, olio evo                                                                 |
| 5   | BUFALA                       | 10.5  | <br>Mozzarella di bufala, pomodoro, basilico, olio evo                                                                                      |
| 6   | NAPOLI  <br>                 | 11    | Acciughe siciliane di Aspra, capperi di Salina, fiordilatte campano, pomodoro bio, origano  <br>                                            |
| 8   | SALSICCIA E PARMIGIANO  <br> | 11    | Salsiccia , pomodoro, parmigiano, semi di finocchio  <br>                                                                                   |
| 9   | DIAVOLA  <br>                | 11.5  | Salamino piccante, pomodoro, fiordilatte, prezzemolo                                                                                        |
| 10  | ORTOLANA  <br>               | 11.5  | Zucchine al forno, patate schiacciate all’olio, provola affumicata al naturale, pesto di basilico con mandorle, pomodorini confit, basilico |
| 11  | BOLOGNA  <br>                | 12    | Mortadella , patate , fiordilatte, pesto                                                                                                    |
| 12  | SPECK E GORGONZOLA  <br>     | 12    | Speck, gorgonzola , fiordilatte , miele di Acacia, noci                                                                                     |
| 13  | TONNO  <br>                  | 11.5  | Tonno Callipo, cipolla rossa saltata, fiordilatte campano, salsa verde, olive nere*, origano                                                |


##### Toppings

| id  | name                  |     
| --- | --------------------- | 
| 1   | fiordilatte campano   |     
| 2   | pomodoro bio          |     
| 3   | basilico              |     
| 4   | olio evo              |     
| 5   | cipolla rossa saltata |     
| 6   | fiordilatte           |     
| 7   | salsa verde           |     
| 8   | olive nere            |     
| 9   | origano               |     
| 10  | tonno                 |     
| 11  | speck                 |     
| 12  | gorgonzola            |  
