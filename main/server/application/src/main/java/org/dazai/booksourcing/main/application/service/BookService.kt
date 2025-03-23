package org.dazai.booksourcing.main.application.service

import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.application.service.exceptions.OperationNotPermittedException
import org.dazai.booksourcing.main.domain.models.Book
import org.dazai.booksourcing.main.domain.repository.BookRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userProfileService: UserProfileService,
    private val clock: Clock,
) {
    fun getBookById(id: Long): Book? {
        return bookRepository.findById(id)
    }

    fun getBooksByOwnerId(ownerId: Long): List<Book> {
        return bookRepository.findByOwnerId(ownerId)
    }

    fun getBooksByStatus(status: String): List<Book> {
        return bookRepository.findByStatus(status)
    }

    fun getBooksByGenre(genre: String): List<Book> {
        return bookRepository.findByGenre(genre)
    }

    fun getBooksByAuthor(author: String): List<Book> {
        return bookRepository.findByAuthor(author)
    }

    fun getAllBooks(): List<Book> {
        return bookRepository.findAll()
    }

    fun createBook(book: Book) {
        bookRepository.save(
            book
                .copy(addedDate = Instant.now(clock))
        )
    }

    fun updateBook(book: Book): Book {
        return bookRepository.update(book)
    }

    fun freeBook(bookId: Long) {
        bookRepository.update(bookRepository.findById(bookId)!!.copy(status = Book.BookStatus.AVAILABLE))
    }

    fun exchangeBook(bookId: Long) {
        bookRepository.update(bookRepository.findById(bookId)!!.copy(status = Book.BookStatus.EXCHANGED))
    }

    fun updateBookValidatingUserProfile(book: Book): Book {
        val userProfile = userProfileService.getCurrentUserProfileOrThrow()
        val actualBookById = getBookById(book.id!!) ?: throw EntityNotFoundException("Book not found.")

        if (userProfile.userId != actualBookById.ownerId) {
            throw OperationNotPermittedException("You are not allowed to update this book because you are not its owner.")
        }

        if (actualBookById.ownerId == book.ownerId) {
            throw OperationNotPermittedException("You are not allowed to update the owner of the book.")
        }

        return updateBook(book)
    }

    fun deleteBook(id: Long) {
        bookRepository.delete(id)
    }
}
