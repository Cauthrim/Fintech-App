CREATE TYPE STATUS AS ENUM ('UPCOMING', 'PAID', 'MISSED');

CREATE TABLE SchedulePayment
(
    id              INT PRIMARY KEY,
    agreementNumber INT,
    period          INT,
    payment         NUMERIC,
    interest        NUMERIC,
    principal       NUMERIC,
    balance         NUMERIC,
    dueDate         DATE,
    status          STATUS
);
