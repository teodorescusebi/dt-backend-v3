FROM openjdk:16

COPY ./target/debts-tracker.jar /usr/app/

WORKDIR /usr/app/

RUN sh -c 'touch debts-tracker.jar'

ENTRYPOINT java -jar debts-tracker.jar
