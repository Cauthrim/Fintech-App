CREATE TABLE IF NOT EXISTS origination.applications
(
    id                            VARCHAR(255) PRIMARY KEY,
    client_id                     VARCHAR(255) NOT NULL REFERENCES origination.loan_clients (id),
    agreement_id                  VARCHAR(255) NOT NULL,
    requested_disbursement_amount INT          NOT NULL,
    status                        VARCHAR(255) NOT NULL
        CHECK ( status in ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED') )
)