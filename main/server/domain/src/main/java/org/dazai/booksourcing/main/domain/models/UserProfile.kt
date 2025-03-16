package org.dazai.booksourcing.main.domain.model

data class UserProfile(
    val id: Long?,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val address: String? = null,
    val phone: String? = null,
    val bio: String? = null
)