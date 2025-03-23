package org.dazai.booksourcing.main.application.service

import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.application.service.exceptions.InvalidUpdateProfileOperationException
import org.dazai.booksourcing.main.application.service.exceptions.MissingUserProfileException
import org.dazai.booksourcing.main.domain.models.UserProfile
import org.dazai.booksourcing.main.domain.repository.UserProfileRepository
import org.dazai.booksourcing.shared.authInfo
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
) {
    fun getUserProfileByUserId(userId: Long): UserProfile? {
        return userProfileRepository.findByUserId(userId)
    }

    fun createUserProfile(userProfile: UserProfile): UserProfile {
        return userProfileRepository.save(userProfile)
    }

    fun updateUserProfile(userProfile: UserProfile): UserProfile {
        val oldProfile = userProfileRepository.findByUserId(userProfile.userId)
            ?: throw EntityNotFoundException("User profile not found: cannot update the profile.")

        if (oldProfile.userId != userProfile.userId) {
            throw InvalidUpdateProfileOperationException("It looks like you are trying to update a different profile.")
        }

        return userProfileRepository.update(userProfile)
    }

    fun deleteUserProfile(id: Long) {
        userProfileRepository.delete(id)
    }

    fun getCurrentUserProfileOrThrow(): UserProfile {
        return getUserProfileByUserId(authInfo.userId) ?: throw MissingUserProfileException("User profile not found")
    }
}
