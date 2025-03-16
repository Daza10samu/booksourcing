/*
 * This file is generated by jOOQ.
 */
package org.dazai.booksourcing.main.domain.db.tables;


import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dazai.booksourcing.main.domain.db.DefaultSchema;
import org.dazai.booksourcing.main.domain.db.Indexes;
import org.dazai.booksourcing.main.domain.db.Keys;
import org.dazai.booksourcing.main.domain.db.tables.records.BookRecord;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.types.ULong;

import tech.ydb.jooq.binding.TimestampBinding;
import tech.ydb.jooq.binding.Uint64Binding;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Book extends TableImpl<BookRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA.book</code>
     */
    public static final Book BOOK = new Book();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<BookRecord> getRecordType() {
        return BookRecord.class;
    }

    /**
     * The column <code>DEFAULT_SCHEMA.book.id</code>.
     */
    public final TableField<BookRecord, ULong> ID = createField(DSL.name("id"), SQLDataType.BIGINTUNSIGNED.nullable(false), this, "", new Uint64Binding());

    /**
     * The column <code>DEFAULT_SCHEMA.book.owner_id</code>.
     */
    public final TableField<BookRecord, ULong> OWNER_ID = createField(DSL.name("owner_id"), SQLDataType.BIGINTUNSIGNED.nullable(false), this, "", new Uint64Binding());

    /**
     * The column <code>DEFAULT_SCHEMA.book.title</code>.
     */
    public final TableField<BookRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(4194304).nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.book.author</code>.
     */
    public final TableField<BookRecord, String> AUTHOR = createField(DSL.name("author"), SQLDataType.VARCHAR(4194304).nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.book.genre</code>.
     */
    public final TableField<BookRecord, String> GENRE = createField(DSL.name("genre"), SQLDataType.VARCHAR(4194304).nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.book.description</code>.
     */
    public final TableField<BookRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(4194304), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.book.condition</code>.
     */
    public final TableField<BookRecord, String> CONDITION = createField(DSL.name("condition"), SQLDataType.VARCHAR(4194304).nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.book.image</code>.
     */
    public final TableField<BookRecord, String> IMAGE = createField(DSL.name("image"), SQLDataType.VARCHAR(4194304), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.book.status</code>.
     */
    public final TableField<BookRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(4194304).nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.book.added_date</code>.
     */
    public final TableField<BookRecord, Instant> ADDED_DATE = createField(DSL.name("added_date"), SQLDataType.LOCALDATETIME(26).nullable(false), this, "", new TimestampBinding());

    private Book(Name alias, Table<BookRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Book(Name alias, Table<BookRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>DEFAULT_SCHEMA.book</code> table reference
     */
    public Book(String alias) {
        this(DSL.name(alias), BOOK);
    }

    /**
     * Create an aliased <code>DEFAULT_SCHEMA.book</code> table reference
     */
    public Book(Name alias) {
        this(alias, BOOK);
    }

    /**
     * Create a <code>DEFAULT_SCHEMA.book</code> table reference
     */
    public Book() {
        this(DSL.name("book"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.BOOK__AUTHOR__IDX, Indexes.BOOK__GENRE__IDX, Indexes.BOOK__OWNER_ID__IDX, Indexes.BOOK__STATUS__IDX);
    }

    @Override
    public UniqueKey<BookRecord> getPrimaryKey() {
        return Keys.PK_BOOK;
    }

    @Override
    public Book as(String alias) {
        return new Book(DSL.name(alias), this);
    }

    @Override
    public Book as(Name alias) {
        return new Book(alias, this);
    }

    @Override
    public Book as(Table<?> alias) {
        return new Book(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Book rename(String name) {
        return new Book(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Book rename(Name name) {
        return new Book(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Book rename(Table<?> name) {
        return new Book(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Book where(Condition condition) {
        return new Book(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Book where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Book where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Book where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Book where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Book where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Book where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Book where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Book whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Book whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
