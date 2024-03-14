Selected database is PostgreSQl, port used is 5432. For database migration purposes, Liquibase library is employed.
Link to database structure: https://miro.com/app/board/uXjVNPV2NqI=/?share_link_id=153825994188

Four endpoints are implemented:
<ol>
    <li>
        Create an agreement.
    </li>
    <li>
        Activate an agreement along with providing the first version of a payment schedule.
    </li>
    <li>
        Send the period payment of a loan with given parameters for scoring purposes.
    </li>
    <li>
        Send the earliest overdue payments' dates of a loanClient's agreements for scoring evaluation.
    </li>
</ol>

For endpoint contract implementation, gRPC framework is used.

In order to run the application, first execute the docker-compose file,
then run the Application class in product-engine module.