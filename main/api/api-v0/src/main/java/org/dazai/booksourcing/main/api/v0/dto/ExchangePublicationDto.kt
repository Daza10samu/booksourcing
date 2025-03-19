package org.dazai.booksourcing.main.api.v0.dto

import java.time.Instant

data class ExchangePublicationDto(
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