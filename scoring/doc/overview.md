Application scoring endpoint is implemented: the scoring system is based on integer score, default is zero,
positive score results in approval. Current scoring system is as follows:
<ol>
    <li>
        If loanClient's salary is equal or greater than a third of a periodic payment on the loan,
        score is increased by 1.
    </li>
    <li>
        If the loanClient has an overdue payment with over 7 days of delay, score is reduced by,
        if the delay is less or equal to 7 days, the score is unchanged,
        and if there are no overdue payments, the score is increased by 1. 
    </li>
</ol>
For endpoint contract implementation, gRPC framework is used.

In order to run the application, first execute the docker-compose file,
then run the Application class in scoring module.