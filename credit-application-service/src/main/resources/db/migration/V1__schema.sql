-- V1__schema.sql

CREATE TABLE affiliates (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    salary NUMERIC(19, 2) NOT NULL,
    affiliation_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE risk_evaluations (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(255) NOT NULL,
    score INTEGER NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    detail TEXT NOT NULL
);

CREATE TABLE credit_applications (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    requested_amount NUMERIC(19, 2) NOT NULL,
    term_months INTEGER NOT NULL,
    proposed_rate NUMERIC(5, 2) NOT NULL,
    application_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    risk_evaluation_id BIGINT UNIQUE,
    FOREIGN KEY (affiliate_id) REFERENCES affiliates(id),
    FOREIGN KEY (risk_evaluation_id) REFERENCES risk_evaluations(id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    roles VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
