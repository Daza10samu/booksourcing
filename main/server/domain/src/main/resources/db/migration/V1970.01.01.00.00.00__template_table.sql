CREATE TABLE IF NOT EXISTS user_profile
(
    id         BigSerial NOT NULL,
    user_id    Int64 NOT NULL,
    first_name Utf8 NOT NULL,
    last_name  Utf8 NOT NULL,
    address    Utf8,
    phone      Utf8 NULL,
    bio        Utf8 NULL,

    INDEX user_profile__user_id__idx GLOBAL UNIQUE ON (user_id),

    PRIMARY KEY(id)
    );

-- Book table definition
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

-- Exchange Request table definition
CREATE TABLE IF NOT EXISTS exchange_request (
    id                BigSerial NOT NULL,
    requested_book_id Int64 NOT NULL, -- Foreign Key to book.id
    requestor_book_id Int64 NOT NULL, -- Foreign Key to book.id
    requestor_id      Int64 NOT NULL, -- Foreign Key to users.id
    owner_id          Int64 NOT NULL, -- Foreign Key to users.id
    status            Utf8 NOT NULL, -- Possible values: PENDING, ACCEPTED, REJECTED, COMPLETED
    request_date      Timestamp NOT NULL,
    response_date     Timestamp,
    completion_date   Timestamp,
    from_publication  Int64,
    message           Utf8,

    INDEX exchange_request__status__idx                GLOBAL ON (status),
    INDEX exchange_request__requestor_id__idx          GLOBAL ON (requestor_id),
    INDEX exchange_request__owner_id__idx              GLOBAL ON (owner_id),
    INDEX exchange_request__requested_book_id__idx     GLOBAL ON (requested_book_id),
    INDEX exchange_request__requestor_book_id__idx     GLOBAL ON (requestor_book_id),

    PRIMARY KEY (id)
);

-- publication to offer an exchange
CREATE TABLE IF NOT EXISTS publication (
    id              BigSerial NOT NULL,
    offered_book_id Int64 NOT NULL, -- Foreign Key to book.id, the book being offered
    owner_id        Int64 NOT NULL, -- Foreign Key to user_profile.id, the owner of the offered book
    offer_details   Utf8 NOT NULL, -- Details about the exchange offer
    status          Utf8 NOT NULL, -- Possible values: ACTIVE, EXPIRED, WITHDRAWN
    created_date    Timestamp NOT NULL,

    INDEX publication__offered_book_id__idx GLOBAL ON (offered_book_id),
    INDEX publication__owner_id__idx GLOBAL ON (owner_id),
    INDEX publication__status__idx GLOBAL ON (status),

    PRIMARY KEY (id),
);