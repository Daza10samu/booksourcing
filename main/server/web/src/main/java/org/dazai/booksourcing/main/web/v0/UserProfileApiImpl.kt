package org.dazai.booksourcing.main.web.v0

import org.dazai.booksourcing.main.api.v0.UserProfileApi
import org.dazai.booksourcing.main.api.v0.dto.UserProfileDto
import org.dazai.booksourcing.main.application.service.UserProfileService
import org.dazai.booksourcing.main.application.service.exceptions.EntityNotFoundException
import org.dazai.booksourcing.main.domain.models.UserProfile
import org.dazai.booksourcing.shared.authInfo
import org.springframework.stereotype.Controller

@Controller
class UserProfileApiImpl(
    private val userProfileService: UserProfileService
) : UserProfileApi {
    override fun createUserProfile(userProfileDto: UserProfileDto): UserProfileDto {
        return userProfileService.createUserProfile(
            userProfileDto.toModel(userId = authInfo.userId)
        ).toDto()
    }

    override fun getUserProfile(): UserProfileDto {
        return userProfileService.getUserProfileByUserId(authInfo.userId)?.toDto() ?: throw EntityNotFoundException("User profile not found")
    }

    override fun getUserProfileById(id: Long): UserProfileDto {
        return userProfileService.getUserProfileByUserId(id)?.toDto() ?: throw EntityNotFoundException("User profile not found")
    }

    override fun updateUserProfile(userProfileDto: UserProfileDto): UserProfileDto {
        return userProfileService.updateUserProfile(userProfileDto.toModel(authInfo.userId).copy(userId = authInfo.userId)).toDto()
    }

    companion object {
        private fun UserProfileDto.toModel(userId: Long? = null) = UserProfile(
            userId = (userId ?: this@toModel.userId)!!,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phone = phone,
            bio = bio,
        )

        private fun UserProfile.toDto() = UserProfileDto(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phone = phone,
            bio = bio,
        )
    }
}