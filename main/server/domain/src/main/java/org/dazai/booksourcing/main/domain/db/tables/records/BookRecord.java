/*
 * This file is generated by jOOQ.
 */
package org.dazai.booksourcing.main.domain.db.tables.records;


import java.time.Instant;

import org.dazai.booksourcing.main.domain.db.tables.Book;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class BookRecord extends UpdatableRecordImpl<BookRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.owner_id</code>.
     */
    public void setOwnerId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.owner_id</code>.
     */
    public Long getOwnerId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.title</code>.
     */
    public void setTitle(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.title</code>.
     */
    public String getTitle() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.author</code>.
     */
    public void setAuthor(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.author</code>.
     */
    public String getAuthor() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.genre</code>.
     */
    public void setGenre(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.genre</code>.
     */
    public String getGenre() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.description</code>.
     */
    public void setDescription(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.description</code>.
     */
    public String getDescription() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.condition</code>.
     */
    public void setCondition(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.condition</code>.
     */
    public String getCondition() {
        return (String) get(6);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.image</code>.
     */
    public void setImage(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.image</code>.
     */
    public String getImage() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.status</code>.
     */
    public void setStatus(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.status</code>.
     */
    public String getStatus() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DEFAULT_SCHEMA.book.added_date</code>.
     */
    public void setAddedDate(Instant value) {
        set(9, value);
    }

    /**
     * Getter for <code>DEFAULT_SCHEMA.book.added_date</code>.
     */
    public Instant getAddedDate() {
        return (Instant) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BookRecord
     */
    public BookRecord() {
        super(Book.BOOK);
    }

    /**
     * Create a detached, initialised BookRecord
     */
    public BookRecord(Long id, Long ownerId, String title, String author, String genre, String description, String condition, String image, String status, Instant addedDate) {
        super(Book.BOOK);

        setId(id);
        setOwnerId(ownerId);
        setTitle(title);
        setAuthor(author);
        setGenre(genre);
        setDescription(description);
        setCondition(condition);
        setImage(image);
        setStatus(status);
        setAddedDate(addedDate);
        resetChangedOnNotNull();
    }
}
