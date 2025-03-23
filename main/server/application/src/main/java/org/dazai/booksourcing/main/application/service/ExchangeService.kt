package org.dazai.booksourcing.main.application.service

import org.dazai.booksourcing.main.application.service.exceptions.ApplicationException
import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.application.service.exceptions.OperationNotPermittedException
import org.dazai.booksourcing.main.domain.models.ExchangeRequest
import org.dazai.booksourcing.main.domain.repository.ExchangeRequestRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class ExchangeService(
    private val exchangeRequestRepository: ExchangeRequestRepository,
    private val transactionTemplate: TransactionTemplate,
    private val bookService: BookService,
) {
    fun getExchangeRequestById(id: Long): ExchangeRequest? {
        return exchangeRequestRepository.findById(id)
    }

    fun getExchangeRequestsByRequestorId(requestorId: Long): List<ExchangeRequest> {
        return exchangeRequestRepository.findByRequestorId(requestorId)
    }

    fun getExchangeRequestsByOwnerId(ownerId: Long): List<ExchangeRequest> {
        return exchangeRequestRepository.findByOwnerId(ownerId)
    }

    fun getExchangeRequestsByStatus(status: String): List<ExchangeRequest> {
        return exchangeRequestRepository.findByStatus(status)
    }

    fun getExchangeRequestsByRequestedBookId(bookId: Long): List<ExchangeRequest> {
        return exchangeRequestRepository.findByRequestedBookId(bookId)
    }

    fun getExchangeRequestsByRequestorBookId(bookId: Long): List<ExchangeRequest> {
        return exchangeRequestRepository.findByRequestorBookId(bookId)
    }

    fun createExchangeRequest(exchangeRequest: ExchangeRequest): ExchangeRequest {
        return transactionTemplate.execute {
            exchangeRequestRepository.save(exchangeRequest.copy(status = ExchangeRequest.ExchangeRequestStatus.PENDING))
        }!!
    }

    fun updateExchangeRequest(exchangeRequest: ExchangeRequest): ExchangeRequest {
        return exchangeRequestRepository.update(exchangeRequest)
    }

    fun accept(userId: Long, exchangeRequestId: Long) {
        transactionTemplate.executeCatchingApplicationExceptions {
            val exchangeRequest = getExchangeRequestById(exchangeRequestId)
                ?: throw EntityNotFoundException("ExchangeRequest not found.")
            if (exchangeRequest.requestorId != userId) throw OperationNotPermittedException("You are not allowed to accept this request.")
            exchangeRequestRepository.update(exchangeRequest.copy(status = ExchangeRequest.ExchangeRequestStatus.ACCEPTED))
        }
    }

    fun reject(userId: Long, exchangeRequestId: Long) {
        transactionTemplate.executeCatchingApplicationExceptions {
            val exchangeRequest = getExchangeRequestById(exchangeRequestId)
                ?: throw EntityNotFoundException("ExchangeRequest not found.")
            if (exchangeRequest.requestorId != userId) throw OperationNotPermittedException("You are not allowed to reject this request.")
            bookService.freeBook(exchangeRequest.ownerBookId)
            bookService.freeBook(exchangeRequest.requestorBookId)
            exchangeRequestRepository.update(exchangeRequest.copy(status = ExchangeRequest.ExchangeRequestStatus.REJECTED))
        }
    }

    fun complete(userId: Long, exchangeRequestId: Long) {
        transactionTemplate.executeCatchingApplicationExceptions {
            val exchangeRequest = getExchangeRequestById(exchangeRequestId)
                ?: throw EntityNotFoundException("ExchangeRequest not found.")
            if (exchangeRequest.requestorId != userId && exchangeRequest.ownerId != userId) throw OperationNotPermittedException(
                "You are not allowed to reject this request."
            )
            bookService.exchangeBook(exchangeRequest.ownerBookId)
            bookService.exchangeBook(exchangeRequest.requestorBookId)
            exchangeRequestRepository.update(exchangeRequest.copy(status = ExchangeRequest.ExchangeRequestStatus.COMPLETED))
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ExchangeService::class.java)

        private fun TransactionTemplate.executeCatchingApplicationExceptions(block: () -> Unit) {
            execute {
                try {
                    block()
                } catch (e: ApplicationException) {
                    throw e
                } catch (e: Exception) {
                    log.error("Failed to execute transaction.", e)
                    throw e
                }
            }
        }
    }
}
