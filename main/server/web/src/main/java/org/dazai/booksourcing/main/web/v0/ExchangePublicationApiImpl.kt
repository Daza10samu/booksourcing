package org.dazai.booksourcing.main.web.v0

import org.dazai.booksourcing.main.api.v0.ExchangePublicationApi
import org.dazai.booksourcing.main.api.v0.dto.ExchangePublicationDto
import org.dazai.booksourcing.main.application.service.ExchangePublicationService
import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.domain.models.ExchangePublication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class ExchangePublicationApiImpl(
    private val exchangePublicationService: ExchangePublicationService,
    private val userProfileApiImpl: UserProfileApiImpl,
) : ExchangePublicationApi {
    override fun createPublication(exchangePublicationDto: ExchangePublicationDto): ResponseEntity<String> {
        exchangePublicationService.createPublication(exchangePublicationDto.toModel().copy(ownerId = userProfileApiImpl.getUserProfile().id!!))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    override fun getPublications(): List<ExchangePublicationDto> {
        val userProfile = userProfileApiImpl.getUserProfile()

        return exchangePublicationService.getPublicationsByOwnerId(userProfile.id!!)
            .map { it.toDto() }
    }

    override fun getPublicationById(id: Long): ExchangePublicationDto {
        return exchangePublicationService.getPublicationById(id)?.toDto() ?: throw EntityNotFoundException("Publication not found")
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
    }
}