package org.dazai.booksourcing.main.api.v0.dto

data class UserProfileDto(
    val userId: Long? = null,
    val firstName: String,
    val lastName: String,
    val address: String? = null,
    val phone: String? = null,
    val bio: String? = null
)