package org.dazai.booksourcing.main.web.v0

import org.dazai.booksourcing.main.api.v0.ExchangeRequestApi
import org.dazai.booksourcing.main.api.v0.dto.ExchangeRequestDto
import org.dazai.booksourcing.main.application.service.ExchangeService
import org.dazai.booksourcing.main.domain.models.ExchangeRequest
import org.dazai.booksourcing.shared.authInfo
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class ExchangeRequestApiImpl(
    private val exchangeService: ExchangeService,
) : ExchangeRequestApi {
    override fun getMyExchangeRequests(): ExchangeRequestApi.ExchangeRequestSplitDto {
        val outgoing = exchangeService.getExchangeRequestsByOwnerId(authInfo.userId)
        val incoming = exchangeService.getExchangeRequestsByRequestorId(authInfo.userId)

        return ExchangeRequestApi.ExchangeRequestSplitDto(
            incoming = incoming.map { it.toDto() },
            outgoing = outgoing.map { it.toDto() },
        )
    }

    override fun acceptExchangeRequest(id: Long): ResponseEntity<String> {
        val userId = authInfo.userId

        exchangeService.accept(userId, id)

        return ResponseEntity.ok("Exchange request accepted.")
    }

    override fun rejectExchangeRequest(id: Long): ResponseEntity<String> {
        val userId = authInfo.userId

        exchangeService.reject(userId, id)

        return ResponseEntity.ok("Exchange request rejected.")
    }

    override fun completeExchangeRequest(id: Long): ResponseEntity<String> {
        val userId = authInfo.userId

        exchangeService.complete(userId, id)

        return ResponseEntity.ok("Exchange request completed.")
    }

    companion object {
        private fun ExchangeRequest.toDto() = ExchangeRequestDto (
            id = id,
            ownerId = ownerId,
            ownerBookId = ownerBookId,
            requestorBookId = requestorBookId,
            requestorId = requestorId,
            status = ExchangeRequestDto.ExchangeRequestStatus.valueOf(status.name),
            requestDate = requestDate,
            responseDate = responseDate,
            completionDate = completionDate,
            message = message,
        )
    }
}