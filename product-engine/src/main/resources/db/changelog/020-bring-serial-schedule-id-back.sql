DROP SEQUENCE IF EXISTS payment_schedule_seq CASCADE;

DROP TABLE IF EXISTS payment_schedule CASCADE;

CREATE TABLE payment_schedule
(
    id           SERIAL PRIMARY KEY,
    agreement_id VARCHAR(255),
    version      VARCHAR(255),
    CONSTRAINT fk_agreement
        FOREIGN KEY (agreement_id)
            REFERENCES agreement (id)
);