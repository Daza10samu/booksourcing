package org.dazai.booksourcing.main.api.v0.dto

import java.time.Instant

data class ExchangeRequestDto(
    val id: Long?,
    val ownerBookId: Long,
    val requestorBookId: Long,
    val requestorId: Long,
    val ownerId: Long,
    val status: ExchangeRequestStatus,
    val requestDate: Instant,
    val responseDate: Instant? = null,
    val completionDate: Instant? = null,
    val message: String? = null,
    val fromPublicationId: Long? = null,
) {
    enum class ExchangeRequestStatus {
        PENDING, ACCEPTED, REJECTED, COMPLETED
    }
}