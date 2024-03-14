ALTER TABLE agreements
    ALTER COLUMN origination_amount TYPE NUMERIC(20, 4),
    ALTER COLUMN disbursement_amount TYPE NUMERIC(20, 4);

ALTER TABLE schedule_payments
    ALTER COLUMN period_payment TYPE NUMERIC(20, 4),
    ALTER COLUMN interest_payment TYPE NUMERIC(20, 4),
    ALTER COLUMN principal_payment TYPE NUMERIC(20, 4);