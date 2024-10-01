DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'enum_role') THEN
        CREATE TYPE enum_role AS ENUM('USER', 'ADMIN');
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(255),
    password VARCHAR(100),
    email VARCHAR(50),
    phone VARCHAR(10),
    last_token VARCHAR(255) NULL,
    role enum_role,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at BOOLEAN DEFAULT false
);

CREATE TABLE categories(
    id INT primary key ,
    name varchar(255) not null,
    description varchar(255) default null,
    created_at timestamp default CURRENT_TIMESTAMP,
    deleted_at BOOLEAN default false);

CREATE TABLE product(
    id INT primary key ,
    name varchar(100) not null ,
    description varchar(255),
    price decimal(10,2) not null ,
    category_id int not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    foreign key (category_id) references categories(id),
    deleted_at BOOLEAN default false);

CREATE TABLE orders(
    id INT primary key ,
    user_id int not null,
    product_id int not null,
    total_price decimal(10,2),
    created_at timestamp default CURRENT_TIMESTAMP,
    foreign key (user_id) references users(id),
    foreign key (product_id) references product(id),
    deleted_at BOOLEAN default false);

CREATE TABLE blacklisted_tokens (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL
);


INSERT INTO users
VALUES (3, 'Duy', '$2a$10$wXj39iCw4YRKyIwhgfJ40uQ5IxZ4KaFVPAhR9rNpL85z.LaIg6hD2', 'duy@gmail.com', '0123456789',NULL,  'ADMIN');

INSERT INTO users
VALUES (4, 'Khanh', '$2a$10$wXj39iCw4YRKyIwhgfJ40uQ5IxZ4KaFVPAhR9rNpL85z.LaIg6hD2', 'khanh@gmail.com', '0987654321',NULL, 'USER');


