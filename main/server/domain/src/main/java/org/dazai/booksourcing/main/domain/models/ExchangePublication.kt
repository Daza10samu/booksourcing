package org.dazai.booksourcing.main.domain.models

import java.time.Instant

data class ExchangePublication(
    val id: Long?,
    val offeredBookId: Long,
    val ownerId: Long,
    val offerDetails: String,
    val status: PublicationStatus,
    val createdDate: Instant
) {
    enum class PublicationStatus {
        ACTIVE, EXPIRED, CLOSED,
    }
}