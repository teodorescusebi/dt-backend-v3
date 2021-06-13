To run the container connected to an external h2 database file, run:

    ```docker run --name debts-tracker -p 4000:4000 -v /home/ubuntu/database/:/usr/app/data -d debts-tracker```