DROP TABLE IF EXISTS token;
CREATE TABLE token
(
    id                 BIGSERIAL PRIMARY KEY,
    token_type         TEXT        NOT NULL,
    token              TEXT UNIQUE NOT NULL,
    user_id            BIGINT REFERENCES users ON DELETE CASCADE,
    created_at         TIMESTAMP DEFAULT now()
);