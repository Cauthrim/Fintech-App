ALTER TABLE payment_schedule
    ALTER COLUMN version TYPE INT USING version::integer;