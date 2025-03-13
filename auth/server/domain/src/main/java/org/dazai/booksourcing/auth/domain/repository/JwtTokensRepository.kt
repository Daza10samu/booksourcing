package org.dazai.booksourcing.auth.domain.repository

import org.dazai.booksourcing.shared.auth.models.JwtToken

interface JwtTokensRepository {
    fun saveToken(token: JwtToken)

    fun getToken(tokenString: String): JwtToken?

    fun getTokensByUserId(userId: Long): Collection<JwtToken>

    fun getRevokedTokens(
        type: JwtToken.Type? = null
    ): Collection<JwtToken>

    fun revokeToken(tokenString: JwtToken) = revokeTokens(listOf(tokenString))

    fun revokeTokens(tokens: Collection<JwtToken>)

    fun revokeTokensByUserId(userId: Long)

    fun deleteExpiredTokens()
}