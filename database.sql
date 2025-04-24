
CREATE DATABASE otp_system;


\c otp_system;


CREATE TABLE otp_config (
    id SERIAL PRIMARY KEY,
    code_length INT NOT NULL,
    expiration_time INT NOT NULL
);


CREATE TYPE code_status AS ENUM ('ACTIVE', 'EXPIRED', 'USED');


CREATE TABLE otp_codes (
    id SERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    status code_status NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIME NOT NULL,
    operation_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TYPE role AS ENUM ('ADMIN', 'USER', 'MODERATOR');


CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    tg_chat_id VARCHAR(255) UNIQUE NOT NULL,
    file_path VARCHAR(255) UNIQUE NOT NULL,
    role role NOT NULL
);