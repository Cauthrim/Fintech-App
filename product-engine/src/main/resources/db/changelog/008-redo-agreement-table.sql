DROP TABLE IF EXISTS agreement;

CREATE TABLE agreement
(
    id                  VARCHAR(255) PRIMARY KEY,
    product_code        VARCHAR(255),
    client_id           VARCHAR(255),
    interest            NUMERIC(8, 4),
    term                INT,
    disbursement_amount INT,
    origination_amount  INT,
    status              VARCHAR(255) CHECK (status ~ 'NEW' || status ~ 'ACTIVE' || status ~ 'CLOSED'),
    disbursement_date   DATE,
    next_payment_date   DATE,
    CONSTRAINT fk_product
        FOREIGN KEY (product_code)
            REFERENCES product (code)
);