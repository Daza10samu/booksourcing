package org.dazai.booksourcing.main.application.service

import org.dazai.booksourcing.main.domain.models.ExchangeRequest
import org.dazai.booksourcing.main.domain.repository.ExchangeRequestRepository
import org.springframework.stereotype.Service

@Service
class ExchangeService(
    private val exchangeRequestRepository: ExchangeRequestRepository
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

    fun createExchangeRequest(exchangeRequest: ExchangeRequest) {
        exchangeRequestRepository.save(exchangeRequest)
    }

    fun updateExchangeRequest(exchangeRequest: ExchangeRequest): ExchangeRequest {
        return exchangeRequestRepository.update(exchangeRequest)
    }
}
