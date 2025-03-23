package org.dazai.booksourcing.main.domain.models

import java.time.Instant

data class ExchangeRequest(
    val id: Long?,
    val ownerId: Long,
    val ownerBookId: Long,
    val requestorBookId: Long,
    val requestorId: Long,
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