package org.dazai.booksourcing.main.api.v0.dto

data class UserProfileDto(
    val id: Long?,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val address: String? = null,
    val phone: String? = null,
    val bio: String? = null
)