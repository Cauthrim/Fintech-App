CREATE TABLE IF NOT EXISTS payment_gate.disbursement_payments
(
    id                      VARCHAR(255) PRIMARY KEY,
    email                   VARCHAR(255) NOT NULL,
    disbursement_amount     INT          NOT NULL,
    status                  VARCHAR(255) NOT NULL
        CHECK ( status in ('UNPROCESSED', 'PROCESSED') )
)