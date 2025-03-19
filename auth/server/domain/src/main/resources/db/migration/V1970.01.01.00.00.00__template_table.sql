CREATE TABLE IF NOT EXISTS users (
    id             BigSerial            NOT NULL,
    username       Utf8                 NOT NULL,
    password       Utf8                 NOT NULL,
    is_disabled    Bool                 NOT NULL,

    INDEX users__disabled_username__idx GLOBAL ON        (is_disabled, username),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_roles (
      user_id Int64 NOT NULL,
      role    Utf8   NOT NULL,

      PRIMARY KEY (user_id, role)
);

CREATE TABLE IF NOT EXISTS jwt_tokens (
    jwt_token     Utf8             NOT NULL,
    expiration_ts Int64            NOT NULL,
    user_id       Int64           NOT NULL,
    type          Utf8             NOT NULL,
    revoked       Bool             NOT NULL,

    INDEX jwt_tokens__revoked__idx GLOBAL ON        (revoked),
    INDEX jwt_tokens__user_id__idx GLOBAL ON        (user_id),

    PRIMARY KEY (jwt_token)
);