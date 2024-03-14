CREATE TYPE statusEnum AS ENUM ('ACTIVE', 'PENDING');

CREATE TABLE Agreement
(
    agreementNumber    INT PRIMARY KEY,
    clientId           INT,
    loanTerm           INT,
    disbursementAmount INT,
    originationAmount  INT,
    interest           INT,
    productCode        VARCHAR(255),
    status             statusEnum
);
