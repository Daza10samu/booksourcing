package org.dazai.booksourcing.main.application.service.model

import org.dazai.booksourcing.main.domain.models.ExchangePublication
import org.dazai.booksourcing.main.domain.models.ExchangeRequest
import java.time.Clock
import java.time.Instant

data class Accept(
    val offeredBookId: Long,
    val requesterId: Long,
    val requestDetails: String,
) {
    fun constructRequest(publication: ExchangePublication, clock: Clock) = ExchangeRequest(
        id = null,
        ownerId = requesterId,
        ownerBookId = offeredBookId,
        requestorBookId = publication.offeredBookId,
        requestorId = publication.ownerId,
        status = ExchangeRequest.ExchangeRequestStatus.PENDING,
        requestDate = Instant.now(clock),
        fromPublicationId = publication.id,
    )
}