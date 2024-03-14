CREATE TABLE IF NOT EXISTS origination.loan_clients
(
    id         VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    salary     INT          NOT NULL
)