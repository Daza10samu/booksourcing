/*
 * This file is generated by jOOQ.
 */
package org.dazai.booksourcing.main.domain.db;


import java.util.Arrays;
import java.util.List;

import org.dazai.booksourcing.main.domain.db.tables.Book;
import org.dazai.booksourcing.main.domain.db.tables.ExchangeRequest;
import org.dazai.booksourcing.main.domain.db.tables.FlywaySchemaHistory;
import org.dazai.booksourcing.main.domain.db.tables.Publication;
import org.dazai.booksourcing.main.domain.db.tables.UserProfile;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>DEFAULT_SCHEMA.book</code>.
     */
    public final Book BOOK = Book.BOOK;

    /**
     * The table <code>DEFAULT_SCHEMA.exchange_request</code>.
     */
    public final ExchangeRequest EXCHANGE_REQUEST = ExchangeRequest.EXCHANGE_REQUEST;

    /**
     * The table <code>DEFAULT_SCHEMA.flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>DEFAULT_SCHEMA.publication</code>.
     */
    public final Publication PUBLICATION = Publication.PUBLICATION;

    /**
     * The table <code>DEFAULT_SCHEMA.user_profile</code>.
     */
    public final UserProfile USER_PROFILE = UserProfile.USER_PROFILE;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("DEFAULT_SCHEMA", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Book.BOOK,
            ExchangeRequest.EXCHANGE_REQUEST,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            Publication.PUBLICATION,
            UserProfile.USER_PROFILE
        );
    }
}
