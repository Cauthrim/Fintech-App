DROP TABLE IF EXISTS product;

CREATE TABLE product
(
    code                   VARCHAR(255) PRIMARY KEY,
    min_term               INT,
    max_term               INT,
    min_principal_amount   INT,
    max_principal_amount   INT,
    min_interest           NUMERIC(8, 4),
    max_interest           NUMERIC(8, 4),
    min_origination_amount INT,
    max_origination_amount INT
);