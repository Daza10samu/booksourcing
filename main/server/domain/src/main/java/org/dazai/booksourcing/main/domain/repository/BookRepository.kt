package org.dazai.booksourcing.main.domain.repository

import org.dazai.booksourcing.main.domain.models.Book

interface BookRepository {
    fun findById(id: Long): Book?
    fun findByOwnerId(ownerId: Long): List<Book>
    fun findByStatus(status: String): List<Book>
    fun findByGenre(genre: String): List<Book>
    fun findByAuthor(author: String): List<Book>
    fun findAll(): List<Book>
    fun save(book: Book)
    fun update(book: Book): Book
    fun delete(id: Long)
}