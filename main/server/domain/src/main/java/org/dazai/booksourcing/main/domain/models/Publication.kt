package org.dazai.booksourcing.main.domain.model

import java.time.Instant

data class Publication(
    val id: Long?,
    val offeredBookId: Long,
    val ownerId: Long,
    val offerDetails: String,
    val status: PublicationStatus,
    val createdDate: Instant
) {
    enum class PublicationStatus {
        ACTIVE, EXPIRED, WITHDRAWN
    }
}