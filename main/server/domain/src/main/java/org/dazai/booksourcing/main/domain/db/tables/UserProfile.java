/*
 * This file is generated by jOOQ.
 */
package org.dazai.booksourcing.main.domain.db.tables;


import java.util.Collection;

import org.dazai.booksourcing.main.domain.db.DefaultSchema;
import org.dazai.booksourcing.main.domain.db.Keys;
import org.dazai.booksourcing.main.domain.db.tables.records.UserProfileRecord;
import org.jooq.Condition;
import org.jooq.Field;
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


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class UserProfile extends TableImpl<UserProfileRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA.user_profile</code>
     */
    public static final UserProfile USER_PROFILE = new UserProfile();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserProfileRecord> getRecordType() {
        return UserProfileRecord.class;
    }

    /**
     * The column <code>DEFAULT_SCHEMA.user_profile.user_id</code>.
     */
    public final TableField<UserProfileRecord, Long> USER_ID = createField(DSL.name("user_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.user_profile.first_name</code>.
     */
    public final TableField<UserProfileRecord, String> FIRST_NAME = createField(DSL.name("first_name"), SQLDataType.VARCHAR(4194304).nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.user_profile.last_name</code>.
     */
    public final TableField<UserProfileRecord, String> LAST_NAME = createField(DSL.name("last_name"), SQLDataType.VARCHAR(4194304).nullable(false), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.user_profile.address</code>.
     */
    public final TableField<UserProfileRecord, String> ADDRESS = createField(DSL.name("address"), SQLDataType.VARCHAR(4194304), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.user_profile.phone</code>.
     */
    public final TableField<UserProfileRecord, String> PHONE = createField(DSL.name("phone"), SQLDataType.VARCHAR(4194304), this, "");

    /**
     * The column <code>DEFAULT_SCHEMA.user_profile.bio</code>.
     */
    public final TableField<UserProfileRecord, String> BIO = createField(DSL.name("bio"), SQLDataType.VARCHAR(4194304), this, "");

    private UserProfile(Name alias, Table<UserProfileRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private UserProfile(Name alias, Table<UserProfileRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>DEFAULT_SCHEMA.user_profile</code> table
     * reference
     */
    public UserProfile(String alias) {
        this(DSL.name(alias), USER_PROFILE);
    }

    /**
     * Create an aliased <code>DEFAULT_SCHEMA.user_profile</code> table
     * reference
     */
    public UserProfile(Name alias) {
        this(alias, USER_PROFILE);
    }

    /**
     * Create a <code>DEFAULT_SCHEMA.user_profile</code> table reference
     */
    public UserProfile() {
        this(DSL.name("user_profile"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<UserProfileRecord> getPrimaryKey() {
        return Keys.PK_USER_PROFILE;
    }

    @Override
    public UserProfile as(String alias) {
        return new UserProfile(DSL.name(alias), this);
    }

    @Override
    public UserProfile as(Name alias) {
        return new UserProfile(alias, this);
    }

    @Override
    public UserProfile as(Table<?> alias) {
        return new UserProfile(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserProfile rename(String name) {
        return new UserProfile(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserProfile rename(Name name) {
        return new UserProfile(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserProfile rename(Table<?> name) {
        return new UserProfile(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserProfile where(Condition condition) {
        return new UserProfile(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserProfile where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserProfile where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserProfile where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserProfile where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserProfile where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserProfile where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserProfile where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserProfile whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserProfile whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
