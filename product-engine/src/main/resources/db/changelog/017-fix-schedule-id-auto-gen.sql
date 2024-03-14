CREATE SEQUENCE payment_schedule_seq;

ALTER TABLE payment_schedule
ALTER COLUMN id SET DEFAULT nextval('payment_schedule_seq');

ALTER TABLE payment_schedule
ALTER COLUMN id TYPE INT;

ALTER SEQUENCE payment_schedule_seq
OWNED BY payment_schedule.id;

DROP SEQUENCE IF EXISTS payment_schedule_id_seq;