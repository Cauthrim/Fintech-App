CREATE TABLE IF NOT EXISTS origination.payments
(
    id                            VARCHAR(255) PRIMARY KEY,
    agreement_id                  VARCHAR(255) NOT NULL
)