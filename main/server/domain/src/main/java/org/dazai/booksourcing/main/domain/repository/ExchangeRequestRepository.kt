package org.dazai.booksourcing.main.domain.repository

import org.dazai.booksourcing.main.domain.model.ExchangeRequest

interface ExchangeRequestRepository {
    fun findById(id: Long): ExchangeRequest?
    fun findByRequestorId(requestorId: Long): List<ExchangeRequest>
    fun findByOwnerId(ownerId: Long): List<ExchangeRequest>
    fun findByStatus(status: String): List<ExchangeRequest>
    fun findByRequestedBookId(bookId: Long): List<ExchangeRequest>
    fun findByRequestorBookId(bookId: Long): List<ExchangeRequest>
    fun save(exchangeRequest: ExchangeRequest)
    fun update(exchangeRequest: ExchangeRequest): ExchangeRequest
    fun delete(id: Long)
}