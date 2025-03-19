package org.dazai.booksourcing.main.web.v0

import org.dazai.booksourcing.main.api.v0.BookApi
import org.dazai.booksourcing.main.api.v0.dto.BookDto
import org.dazai.booksourcing.main.application.service.BookService
import org.dazai.booksourcing.main.application.service.UserProfileService
import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.domain.models.Book
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class BookApiImpl(
    private val bookService: BookService,
    private val userProfileService: UserProfileService,
) : BookApi {
    override fun createBook(bookDto: BookDto): ResponseEntity<String> {
        val userProfile = userProfileService.getCurrentUserProfileOrThrow()
        bookService.createBook(bookDto.toModel().copy(ownerId = userProfile.id!!))

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    override fun getMyBooks(): List<BookDto> {
        val userProfile = userProfileService.getCurrentUserProfileOrThrow()

        return bookService.getBooksByOwnerId(userProfile.id!!)
            .map { it.toDto() }
    }

    override fun getBookById(id: Long): BookDto {
        return bookService.getBookById(id)?.toDto() ?: throw EntityNotFoundException("Book not found")
    }

    override fun updateBook(id: Long, bookDto: BookDto): BookDto =
        bookService.updateBookValidatingUserProfile(bookDto.toModel().copy(id = id)).toDto()


    companion object {
        private fun BookDto.toModel() = Book(
            id = id,
            ownerId = ownerId,
            title = title,
            genre = genre,
            author = author,
            status = Book.BookStatus.valueOf(status.name),
            addedDate = addedDate,
            condition = Book.BookCondition.valueOf(condition.name),
            description = description,
            imageUrl = imageUrl,
        )

        private fun Book.toDto() = BookDto(
            id = id,
            ownerId = ownerId,
            title = title,
            genre = genre,
            author = author,
            status = BookDto.BookStatus.valueOf(status.name),
            addedDate = addedDate,
            condition = BookDto.BookCondition.valueOf(condition.name),
        )
    }
}