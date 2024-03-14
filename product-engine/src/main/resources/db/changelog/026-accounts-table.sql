CREATE TABLE IF NOT EXISTS accounts
(
    id                  VARCHAR(255) PRIMARY KEY,
    agreement_id        VARCHAR(255) NOT NULL references agreements(id),
    code                VARCHAR(255) NOT NULL,
    balance             NUMERIC(20, 4) NOT NULL
);