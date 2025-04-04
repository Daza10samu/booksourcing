/*
 * This file is generated by jOOQ.
 */
package org.dazai.booksourcing.auth.domain.db;


import org.dazai.booksourcing.auth.domain.db.tables.FlywaySchemaHistory;
import org.dazai.booksourcing.auth.domain.db.tables.JwtTokens;
import org.dazai.booksourcing.auth.domain.db.tables.UserRoles;
import org.dazai.booksourcing.auth.domain.db.tables.Users;


/**
 * Convenience access to all tables in DEFAULT_SCHEMA.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Tables {

    /**
     * The table <code>DEFAULT_SCHEMA.flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>DEFAULT_SCHEMA.jwt_tokens</code>.
     */
    public static final JwtTokens JWT_TOKENS = JwtTokens.JWT_TOKENS;

    /**
     * The table <code>DEFAULT_SCHEMA.user_roles</code>.
     */
    public static final UserRoles USER_ROLES = UserRoles.USER_ROLES;

    /**
     * The table <code>DEFAULT_SCHEMA.users</code>.
     */
    public static final Users USERS = Users.USERS;
}
