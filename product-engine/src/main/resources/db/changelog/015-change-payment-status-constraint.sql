ALTER TABLE schedule_payment DROP CONSTRAINT schedule_payment_status_check RESTRICT;

ALTER TABLE schedule_payment
ADD CONSTRAINT status
CHECK (
    status in ('PAID', 'OVERDUE', 'FUTURE')
);