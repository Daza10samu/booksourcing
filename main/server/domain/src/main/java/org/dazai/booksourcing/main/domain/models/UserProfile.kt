package org.dazai.booksourcing.main.domain.models

data class UserProfile(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val address: String? = null,
    val phone: String? = null,
    val bio: String? = null
)