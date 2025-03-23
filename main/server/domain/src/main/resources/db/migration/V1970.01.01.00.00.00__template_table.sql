CREATE TABLE IF NOT EXISTS user_profile
(
    user_id    Int64 NOT NULL,
    first_name Utf8 NOT NULL,
    last_name  Utf8 NOT NULL,
    address    Utf8,
    phone      Utf8 NULL,
    bio        Utf8 NULL,

    PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS book (
    id          BigSerial NOT NULL,
    owner_id    Int64 NOT NULL,
    title       Utf8 NOT NULL,
    author      Utf8 NOT NULL,
    genre       Utf8 NOT NULL,
    description Utf8,
    condition   Utf8 NOT NULL,
    image       Utf8,
    status      Utf8 NOT NULL, -- Possible values: AVAILABLE, REQUESTED, EXCHANGED
    added_date  Timestamp NOT NULL,

    INDEX book__owner_id__idx GLOBAL ON (owner_id),
    INDEX book__status__idx   GLOBAL ON (status),
    INDEX book__genre__idx    GLOBAL ON (genre),
    INDEX book__author__idx   GLOBAL ON (author),


    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS exchange_request (
    id                BigSerial NOT NULL,
    owner_id          Int64 NOT NULL,
    owner_book_id     Int64 NOT NULL,
    requestor_book_id Int64 NOT NULL,
    requestor_id      Int64 NOT NULL,
    status            Utf8 NOT NULL,
    request_date      Timestamp NOT NULL,
    response_date     Timestamp,
    completion_date   Timestamp,
    from_publication  Int64,
    message           Utf8,

    INDEX exchange_request__status__idx                GLOBAL ON (status),
    INDEX exchange_request__requestor_id__idx          GLOBAL ON (requestor_id),
    INDEX exchange_request__owner_id__idx              GLOBAL ON (owner_id),
    INDEX exchange_request__owner_book_id__idx         GLOBAL ON (owner_book_id),
    INDEX exchange_request__requestor_book_id__idx     GLOBAL ON (requestor_book_id),
    INDEX exchange_request__uidx                       GLOBAL SYNC ON (requestor_id, owner_id, owner_book_id, requestor_book_id, status),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS publication (
    id              BigSerial NOT NULL,
    offered_book_id Int64 NOT NULL,
    owner_id        Int64 NOT NULL,
    offer_details   Utf8 NOT NULL,
    status          Utf8 NOT NULL,
    created_date    Timestamp NOT NULL,

    INDEX publication__offered_book_id__idx GLOBAL ON (offered_book_id),
    INDEX publication__owner_id__idx GLOBAL ON (owner_id),
    INDEX publication__status__idx GLOBAL ON (status),
    INDEX publication__offered_book_id__owner_id__status__idx GLOBAL SYNC ON (offered_book_id, owner_id, status),

    PRIMARY KEY (id),
);