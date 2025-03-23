package org.dazai.booksourcing.main.application.service

import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.application.service.exceptions.ExchangeRequestPublicationIsAlreadyTaken
import org.dazai.booksourcing.main.application.service.exceptions.OperationNotPermittedException
import org.dazai.booksourcing.main.application.service.model.Accept
import org.dazai.booksourcing.main.domain.models.Book
import org.dazai.booksourcing.main.domain.models.ExchangePublication
import org.dazai.booksourcing.main.domain.models.ExchangeRequest
import org.dazai.booksourcing.main.domain.repository.ExchangePublicationRepository
import org.dazai.booksourcing.shared.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.Clock

@Service
class ExchangePublicationService(
    private val exchangePublicationRepository: ExchangePublicationRepository,
    private val exchangeService: ExchangeService,
    private val userProfileService: UserProfileService,
    private val transactionTemplate: TransactionTemplate,
    private val bookService: BookService,
    private val clock: Clock,
) {
    private val log = logger()

    fun getPublicationById(id: Long): ExchangePublication? {
        return exchangePublicationRepository.findById(id)
    }

    fun getPublicationByOfferedBookId(bookId: Long): ExchangePublication? {
        return exchangePublicationRepository.findByOfferedBookId(bookId)
    }

    fun getPublicationsByOwnerId(ownerId: Long): List<ExchangePublication> {
        return exchangePublicationRepository.findByOwnerId(ownerId)
    }

    fun getPublicationsByStatus(status: String): List<ExchangePublication> {
        return exchangePublicationRepository.findByStatus(status)
    }

    fun getActivePublications(): List<ExchangePublication> {
        return exchangePublicationRepository.findActivePublications()
    }

    fun createPublication(exchangePublication: ExchangePublication): ExchangePublication {
        return exchangePublicationRepository.save(
            exchangePublication
                .copy(createdDate = clock.instant())
        )
    }

    fun updatePublication(exchangePublication: ExchangePublication): ExchangePublication {
        return exchangePublicationRepository.update(exchangePublication)
    }

    fun deletePublication(id: Long) {
        exchangePublicationRepository.delete(id)
    }

    fun updatePublicationValidatingUserProfile(exchangePublication: ExchangePublication): ExchangePublication {
        val userProfile = userProfileService.getCurrentUserProfileOrThrow()
        val currentExchangePublication = getPublicationById(exchangePublication.id!!)
            ?: throw EntityNotFoundException("ExchangePublication not found.")

        if (userProfile.userId != currentExchangePublication.ownerId) {
            throw OperationNotPermittedException("You are not allowed to update this publication because you are not its owner.")
        }

        if (currentExchangePublication.ownerId != exchangePublication.ownerId) {
            throw OperationNotPermittedException("You are not allowed to modify the owner of the publication.")
        }

        return updatePublication(exchangePublication)
    }

    fun accept(id: Long, accept: Accept): ExchangeRequest {
        return try {
            transactionTemplate.execute {
                val exchangePublication =
                    getPublicationById(id) ?: throw EntityNotFoundException("ExchangePublication not found.")
                validateActivePublicationStatus(exchangePublication)

                val exchangeRequest = accept.constructRequest(exchangePublication, clock)

                val ownerBook = bookService.getBookById(exchangeRequest.ownerBookId) ?: throw EntityNotFoundException("Book not found.")
                val requestorBook = bookService.getBookById(exchangeRequest.requestorBookId) ?: throw EntityNotFoundException("Book not found.")

                processExchangePublication(exchangePublication, ownerBook, requestorBook)

                exchangeService.createExchangeRequest(exchangeRequest)

                return@execute exchangeRequest
            }!!
        } catch (e: EntityNotFoundException) {
            throw e
        } catch (e: Exception) {
            log.error("Failed to accept ExchangePublication.", e)
            throw ExchangeRequestPublicationIsAlreadyTaken("ExchangePublication is not active.", e)
        }
    }

    private fun validateActivePublicationStatus(exchangePublication: ExchangePublication) {
        if (exchangePublication.status != ExchangePublication.PublicationStatus.ACTIVE) throw IllegalStateException(
            "ExchangePublication is not active."
        )
    }

    private fun validateBookStatus(book: Book) {
        if (book.status != Book.BookStatus.AVAILABLE) throw IllegalStateException(
            "Book is not available."
        )
    }

    private fun processExchangePublication(
        exchangePublication: ExchangePublication,
        ownerBook: Book,
        requestorBook: Book
    ) {
        try {
            validateBookStatus(ownerBook)
            validateBookStatus(requestorBook)

            exchangePublicationRepository.update(exchangePublication.copy(status = ExchangePublication.PublicationStatus.CLOSED))
            bookService.updateBook(ownerBook.copy(status = Book.BookStatus.REQUESTED))
            bookService.updateBook(requestorBook.copy(status = Book.BookStatus.REQUESTED))
        } catch (e: Exception) {
            log.error("Failed to update ExchangePublication status to CLOSED.", e)
            throw IllegalStateException("ExchangePublication is not active.", e)
        }
    }
}
