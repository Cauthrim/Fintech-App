CREATE TABLE payment_schedule
(
    id           BIGSERIAL PRIMARY KEY,
    agreement_id VARCHAR(255),
    version      VARCHAR(255),
    CONSTRAINT fk_agreement
        FOREIGN KEY (agreement_id)
            REFERENCES agreement (id)
);