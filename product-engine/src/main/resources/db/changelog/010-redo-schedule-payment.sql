DROP TABLE IF EXISTS schedulepayment;

CREATE TABLE schedule_payment(
    payment_schedule_id BIGSERIAL,
    status VARCHAR(255) CHECK (status ~ 'PAID' || status ~ 'OVERDUE' || status ~ 'FUTURE'),
    payment_date DATE,
    period_payment INT,
    interest_payment INT,
    principal_payment INT,
    period_number INT,
    CONSTRAINT fk_schedule
        FOREIGN KEY (payment_schedule_id)
            REFERENCES payment_schedule(id)
);