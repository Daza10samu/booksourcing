package org.dazai.booksourcing.auth.application.service.models

data class JwtTokens(
    val accessToken: String?,
    val refreshToken: String?,
)