package org.dazai.booksourcing.main.domain.repository

import org.dazai.booksourcing.main.domain.models.ExchangePublication

interface ExchangePublicationRepository {
    fun findById(id: Long): ExchangePublication?
    fun findByOfferedBookId(bookId: Long): ExchangePublication?
    fun findByOwnerId(ownerId: Long): List<ExchangePublication>
    fun findByStatus(status: String): List<ExchangePublication>
    fun save(exchangePublication: ExchangePublication): ExchangePublication
    fun update(exchangePublication: ExchangePublication): ExchangePublication
    fun delete(id: Long)
    fun findActivePublications(): List<ExchangePublication>
}