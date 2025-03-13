package org.dazai.booksourcing.auth.api.v0.dto

data class JwtTokensDto(
    val access: String?,
    val refresh: String?,
)