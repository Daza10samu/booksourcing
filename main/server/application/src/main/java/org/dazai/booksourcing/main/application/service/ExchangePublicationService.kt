package org.dazai.booksourcing.main.application.service

import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.application.service.exceptions.OperationNotPermittedException
import org.dazai.booksourcing.main.domain.models.ExchangePublication
import org.dazai.booksourcing.main.domain.repository.ExchangePublicationRepository
import org.springframework.stereotype.Service

@Service
class ExchangePublicationService(
    private val exchangePublicationRepository: ExchangePublicationRepository,
    private val userProfileService: UserProfileService,
) {
    fun getPublicationById(id: Long): ExchangePublication? {
        return exchangePublicationRepository.findById(id)
    }

    fun getPublicationByOfferedBookId(bookId: Long): ExchangePublication? {
        return exchangePublicationRepository.findByOfferedBookId(bookId)
    }

    fun getPublicationsByOwnerId(ownerId: Long): List<ExchangePublication> {
        return exchangePublicationRepository.findByOwnerId(ownerId)
    }

    fun getPublicationsByStatus(status: String): List<ExchangePublication> {
        return exchangePublicationRepository.findByStatus(status)
    }

    fun getActivePublications(): List<ExchangePublication> {
        return exchangePublicationRepository.findActivePublications()
    }

    fun createPublication(exchangePublication: ExchangePublication) {
        exchangePublicationRepository.save(exchangePublication)
    }

    fun updatePublication(exchangePublication: ExchangePublication): ExchangePublication {
        return exchangePublicationRepository.update(exchangePublication)
    }

    fun deletePublication(id: Long) {
        exchangePublicationRepository.delete(id)
    }

    fun updatePublicationValidatingUserProfile(exchangePublication: ExchangePublication): ExchangePublication {
        val userProfile = userProfileService.getCurrentUserProfileOrThrow()
        val currentExchangePublication = getPublicationById(exchangePublication.id!!)
            ?: throw EntityNotFoundException("ExchangePublication not found.")

        if (userProfile.id != currentExchangePublication.ownerId) {
            throw OperationNotPermittedException("You are not allowed to update this publication because you are not its owner.")
        }

        if (currentExchangePublication.ownerId != exchangePublication.ownerId) {
            throw OperationNotPermittedException("You are not allowed to modify the owner of the publication.")
        }

        return updatePublication(exchangePublication)
    }
}
