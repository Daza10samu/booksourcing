package org.dazai.booksourcing.auth.application.service

import org.dazai.booksourcing.auth.application.service.models.JwtTokens
import org.dazai.booksourcing.auth.application.service.topics.RevokedAccessSender
import org.dazai.booksourcing.shared.auth.models.JwtToken
import org.dazai.booksourcing.shared.auth.models.User
import org.dazai.booksourcing.auth.domain.repository.JwtTokensRepository
import org.dazai.booksourcing.shared.auth.models.JwtAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class AuthService(
    private val userService: UserDetailsServiceImpl,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokensRepository: JwtTokensRepository,
    private val transactionTemplate: TransactionTemplate,
    private val revokedAccessSender: RevokedAccessSender,
) {
    fun login(username: String, password: String): JwtTokens {
        val user: User = userService.getUser(username)
        if (passwordEncoder.matches(password, user.password)) {
            val accessToken = jwtProvider.generateAccessToken(user)
            val refreshToken = jwtProvider.generateRefreshToken(user)

            return transactionTemplate.execute {
                saveTokens(accessToken, refreshToken)

                JwtTokens(accessToken.token, refreshToken.token)
            }!!
        } else {
            throw IllegalArgumentException("Wrong password")
        }
    }

    fun refresh(refreshToken: String): JwtTokens {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            val saveRefreshToken = jwtTokensRepository.getToken(refreshToken)
            if (saveRefreshToken != null && saveRefreshToken.type == JwtToken.Type.REFRESH) {
                return transactionTemplate.execute {
                    val claims = jwtProvider.getRefreshClaims(refreshToken)
                    val login = claims.subject
                    val user: User = userService.getUser(login)

                    val accessToken = jwtProvider.generateAccessToken(user)
                    val newRefreshToken = jwtProvider.generateRefreshToken(user)

                    jwtTokensRepository.revokeToken(JwtToken(refreshToken, 0L, user.id!!, JwtToken.Type.REFRESH, true))
                    saveTokens(accessToken, newRefreshToken)

                    JwtTokens(accessToken.token, newRefreshToken.token)
                }!!
            }
        }
        throw IllegalArgumentException("Illegal refresh token")
    }

    fun revokeTokensByUserId(userId: Long) {
        transactionTemplate.execute {
            val userTokens = jwtTokensRepository.getTokensByUserId(userId)

            revokedAccessSender.send(userTokens)

            jwtTokensRepository.revokeTokens(userTokens)
        }
    }

    private fun saveTokens(accessToken: JwtToken, refreshToken: JwtToken) {
        transactionTemplate.execute {
            jwtTokensRepository.saveToken(accessToken)
            jwtTokensRepository.saveToken(refreshToken)
        }
    }

    val authInfo: JwtAuthentication
        get() = SecurityContextHolder.getContext().authentication as JwtAuthentication
}