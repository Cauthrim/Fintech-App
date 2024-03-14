CREATE TABLE Product
(
    id                   varchar(255) PRIMARY KEY,
    minLoanTerm          INT,
    maxLoanTerm          INT,
    minPrincipalAmount   INT,
    maxPrincipalAmount   INT,
    minInterest          INT,
    maxInterest          INT,
    minOriginationAmount INT,
    maxOriginationAmount INT
);

INSERT INTO Product
VALUES
(
    'Cl1.0',
    '3',
    '24',
    '50000',
    '500000',
    '8',
    '15',
    '2000',
    '10000'
);