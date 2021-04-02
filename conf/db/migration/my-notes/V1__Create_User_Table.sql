DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    email              TEXT UNIQUE NOT NULL,
    password           TEXT        NOT NULL,
    is_email_confirmed BOOLEAN   DEFAULT FALSE,
    created_at         TIMESTAMP DEFAULT now(),
    updated_at         TIMESTAMP DEFAULT now()
);