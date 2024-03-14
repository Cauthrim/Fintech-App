ALTER TABLE product
    ALTER COLUMN mininterest TYPE numeric(8, 4),
    ALTER COLUMN maxinterest TYPE numeric(8, 4);

ALTER TABLE agreement
    ALTER COLUMN interest TYPE numeric(8, 4);