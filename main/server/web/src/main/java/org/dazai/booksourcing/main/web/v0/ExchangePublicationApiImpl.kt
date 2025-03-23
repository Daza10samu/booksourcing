package org.dazai.booksourcing.main.web.v0

import org.dazai.booksourcing.main.api.v0.ExchangePublicationApi
import org.dazai.booksourcing.main.api.v0.dto.AcceptDto
import org.dazai.booksourcing.main.api.v0.dto.ExchangePublicationDto
import org.dazai.booksourcing.main.api.v0.dto.ExchangeRequestDto
import org.dazai.booksourcing.main.application.service.ExchangePublicationService
import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.application.service.model.Accept
import org.dazai.booksourcing.main.domain.models.ExchangePublication
import org.dazai.booksourcing.main.domain.models.ExchangeRequest
import org.dazai.booksourcing.main.web.ws.service.WsSessionsManager
import org.dazai.booksourcing.main.web.ws.service.message.WsMessage
import org.dazai.booksourcing.shared.authInfo
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class ExchangePublicationApiImpl(
    private val exchangePublicationService: ExchangePublicationService,
    private val wsSessionsManager: WsSessionsManager,
) : ExchangePublicationApi {
    override fun createPublication(exchangePublicationDto: ExchangePublicationDto): ExchangePublicationDto {
        val publication = exchangePublicationService.createPublication(
            exchangePublicationDto.toModel().copy(ownerId = authInfo.userId)
        )

        wsSessionsManager.broadcastMessage(WsMessage.AddNewExchangePublicationWsMessage(publication.toDto()))

        return publication.toDto()
    }

    override fun getAllPublications(): List<ExchangePublicationDto> {
        return exchangePublicationService.getActivePublications().map { it.toDto() }
    }

    override fun getMyPublications(): List<ExchangePublicationDto> {
        return exchangePublicationService.getPublicationsByOwnerId(authInfo.userId)
            .map { it.toDto() }
    }

    override fun getPublicationById(id: Long): ExchangePublicationDto {
        return exchangePublicationService.getPublicationById(id)?.toDto()
            ?: throw EntityNotFoundException("Publication not found")
    }

    override fun updatePublication(id: Long, exchangePublicationDto: ExchangePublicationDto): ExchangePublicationDto {
        return exchangePublicationService.updatePublicationValidatingUserProfile(exchangePublicationDto.toModel())
            .toDto()
    }

    override fun deletePublication(id: Long): ResponseEntity<String> {
        val publication =
            exchangePublicationService.getPublicationById(id) ?: throw EntityNotFoundException("Publication not found")
        exchangePublicationService.updatePublicationValidatingUserProfile(publication.copy(status = ExchangePublication.PublicationStatus.CLOSED))
        return ResponseEntity.ok("Publication status updated to CLOSED.")
    }

    override fun acceptPublication(id: Long, acceptDto: AcceptDto): ExchangeRequestDto {
        val exchangeRequest = exchangePublicationService.accept(
            id,
            acceptDto.toModel(authInfo.userId)
        ).toDto()

        wsSessionsManager.broadcastMessage(WsMessage.DeleteExchangePublicationWsMessage(id))

        return exchangeRequest
    }

    companion object {
        private fun ExchangePublicationDto.toModel() = ExchangePublication(
            id = id,
            offeredBookId = offeredBookId,
            ownerId = ownerId,
            offerDetails = offerDetails,
            status = ExchangePublication.PublicationStatus.valueOf(status.name),
            createdDate = createdDate,
        )

        private fun ExchangePublication.toDto() = ExchangePublicationDto(
            id = id,
            offeredBookId = offeredBookId,
            ownerId = ownerId,
            offerDetails = offerDetails,
            status = ExchangePublicationDto.PublicationStatus.valueOf(status.name),
            createdDate = createdDate,
        )

        private fun AcceptDto.toModel(requesterId: Long) = Accept(
            offeredBookId = offeredBookId,
            requesterId = requesterId,
            requestDetails = requestDetails,
        )

        private fun ExchangeRequest.toDto() = ExchangeRequestDto(
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
            fromPublicationId = fromPublicationId,
        )
    }
}