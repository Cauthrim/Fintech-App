Selected database is PostgreSQl, port used is 5432, tables are placed in origination schema.
For database migration purposes, Liquibase library is employed.
Link to database structure: https://miro.com/app/board/uXjVNLXwvVQ=/?share_link_id=797820682251

Two endpoints are implemented: one for creating an application (also creating a loanClient entity if necessary)
and another for cancelling a newly created one. In addition, a scheduled task sends requests
to score the newly created applications in the scoring module, notifying the loanClient of the result by email.
For endpoint contract implementation, gRPC framework is used.

In order to run the application, first execute the docker-compose file,
then run the Application class in origination module.