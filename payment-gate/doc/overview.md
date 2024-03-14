Selected database is PostgreSQl, port used is 5432, tables are placed in payment_gate schema.
For database migration purposes, Liquibase library is employed. Link to database structure: https://miro.com/app/board/uXjVNit0wI0=/?share_link_id=436619775876

One rest service is written for debit payment purposes and two services that work with disbursement payments:
one requesting a payment from the merchant provider and another one checking the status of unprocessed payments.

In order to run the application, first execute the docker-compose file, then run the Application class in origination module.