package org.dazai.booksourcing.main.domain.repository.impl

import org.dazai.booksourcing.main.domain.db.Indexes
import org.dazai.booksourcing.main.domain.db.Tables.BOOK
import org.dazai.booksourcing.main.domain.db.tables.records.BookRecord
import org.dazai.booksourcing.main.domain.model.Book
import org.dazai.booksourcing.main.domain.model.Book.BookStatus
import org.dazai.booksourcing.main.domain.repository.BookRepository
import org.springframework.stereotype.Repository
import tech.ydb.jooq.impl.YdbDSLContextImpl

@Repository
class BookRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
) : BookRepository {

    override fun findById(id: Long): Book? {
        return dsl.selectFrom(BOOK)
            .where(BOOK.ID.eq(org.jooq.types.ULong.valueOf(id)))
            .fetchOne()?.toModel()
    }

    override fun findByOwnerId(ownerId: Long): List<Book> {
        return dsl.selectFrom(BOOK.useIndex(Indexes.BOOK__OWNER_ID__IDX.name))
            .where(BOOK.OWNER_ID.eq(org.jooq.types.ULong.valueOf(ownerId)))
            .map { it.toModel() }
    }

    override fun findByStatus(status: String): List<Book> {
        return dsl.selectFrom(BOOK.useIndex(Indexes.BOOK__STATUS__IDX.name))
            .where(BOOK.STATUS.eq(status))
            .fetch()
            .map { it.toModel() }
    }

    override fun findByGenre(genre: String): List<Book> {
        return dsl.selectFrom(BOOK.useIndex(Indexes.BOOK__GENRE__IDX.name))
            .where(BOOK.GENRE.eq(genre))
            .fetch()
            .map { it.toModel() }
    }

    override fun findByAuthor(author: String): List<Book> {
        return dsl.selectFrom(BOOK.useIndex(Indexes.BOOK__AUTHOR__IDX.name))
            .where(BOOK.AUTHOR.eq(author))
            .fetch()
            .map { it.toModel() }
    }

    override fun findAll(): List<Book> {
        return dsl.selectFrom(BOOK)
            .fetch()
            .map { it.toModel() }
    }

    override fun save(book: Book) {
        dsl.insertInto(BOOK)
            .set(book.toRecord())
            .execute()
    }

    override fun update(book: Book): Book {
        val oldValue = findById(book.id!!)!!
        if (oldValue.id != book.id) {
            throw IllegalArgumentException("The id of the book to update is different from the id of the book in the database.")
        }
        val record = book.toRecord()

        dsl.upsertInto(BOOK)
            .set(record)
            .execute()

        return book
    }

    override fun delete(id: Long) {
        dsl.deleteFrom(BOOK)
            .where(BOOK.ID.eq(org.jooq.types.ULong.valueOf(id)))
            .execute()
    }

    companion object {
        private fun Book.toRecord(): BookRecord {
            val record = BookRecord()

            id?.let { record.id = org.jooq.types.ULong.valueOf(it) }
            ownerId.let { record.ownerId = org.jooq.types.ULong.valueOf(it) }

            record.title = title
            record.author = author
            record.genre = genre
            record.description = description
            record.condition = condition
            record.image = image
            record.status = status.name

            record.addedDate = addedDate

            return record
        }

        private fun BookRecord.toModel(): Book = Book(
            id = id!!.toBigInteger()!!.longValueExact(),
            ownerId = ownerId!!.toBigInteger()!!.longValueExact(),
            title = title,
            author = author,
            genre = genre,
            description = description,
            condition = condition,
            image = image,
            status = BookStatus.valueOf(status),
            addedDate = addedDate,
        )
    }
}