ALTER TABLE products ALTER COLUMN code SET NOT NULL;
ALTER TABLE products ALTER COLUMN min_term SET NOT NULL;
ALTER TABLE products ALTER COLUMN max_term SET NOT NULL;
ALTER TABLE products ALTER COLUMN min_principal_amount SET NOT NULL;
ALTER TABLE products ALTER COLUMN max_principal_amount SET NOT NULL;
ALTER TABLE products ALTER COLUMN min_interest SET NOT NULL;
ALTER TABLE products ALTER COLUMN max_interest SET NOT NULL;
ALTER TABLE products ALTER COLUMN min_origination_amount SET NOT NULL;
ALTER TABLE products ALTER COLUMN max_origination_amount SET NOT NULL;

ALTER TABLE agreements ALTER COLUMN id SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN product_code SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN client_id SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN interest SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN term SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN disbursement_amount SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN origination_amount SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN status SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN disbursement_date SET NOT NULL;
ALTER TABLE agreements ALTER COLUMN next_payment_date SET NOT NULL;

ALTER TABLE payment_schedules ALTER COLUMN id SET NOT NULL;
ALTER TABLE payment_schedules ALTER COLUMN agreement_id SET NOT NULL;
ALTER TABLE payment_schedules ALTER COLUMN version SET NOT NULL;

ALTER TABLE schedule_payments ALTER COLUMN payment_schedule_id SET NOT NULL;
ALTER TABLE schedule_payments ALTER COLUMN status SET NOT NULL;
ALTER TABLE schedule_payments ALTER COLUMN payment_date SET NOT NULL;
ALTER TABLE schedule_payments ALTER COLUMN period_payment SET NOT NULL;
ALTER TABLE schedule_payments ALTER COLUMN interest_payment SET NOT NULL;
ALTER TABLE schedule_payments ALTER COLUMN principal_payment SET NOT NULL;
ALTER TABLE schedule_payments ALTER COLUMN period_number SET NOT NULL;