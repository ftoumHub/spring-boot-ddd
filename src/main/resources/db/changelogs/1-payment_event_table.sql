--liquibase formatted sql

--changeset gginon:1

DROP TYPE IF EXISTS payment_event_type CASCADE;
CREATE TYPE payment_event_type AS ENUM ('PAYMENT_REQUESTED', 'PAYMENT_AUTHORIZED', 'PAYMENT_CONFIRMED');

DROP TABLE IF EXISTS payment_event;
CREATE TABLE payment_event
(
    event_id    varchar(255) PRIMARY KEY,
    event_type  payment_event_type NOT NULL,
    payment_id  varchar(255) NOT NULL,
    timestamp TIMESTAMP,
    event_data  json NOT NULL
);

--comment: add report table