ALTER TABLE agreement
ADD CONSTRAINT status
CHECK (
    status in ('NEW', 'ACTIVE', 'CLOSED')
);