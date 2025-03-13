package org.dazai.booksourcing.auth.application.service

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.dazai.booksourcing.shared.auth.BasicJwtProvider
import org.dazai.booksourcing.shared.auth.models.JwtToken
import org.dazai.booksourcing.shared.auth.models.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    revokedCache: JwtRevokedCache,
    @Value("\${jwt.secret.access}") jwtAccessSecret: String,
    @Value("\${jwt.secret.refresh}") jwtRefreshSecret: String,
    @Value("\${jwt.access.time:5}") jwtAccessTime: Long,
    @Value("\${jwt.access.time.unit:MINUTES}") jwtAccessTimeUnit: ChronoUnit,
    @Value("\${jwt.refresh.time:30}") private val jwtRefreshTime: Long,
    @Value("\${jwt.refresh.time.unit:DAYS}") private val jwtRefreshTimeUnit: ChronoUnit,
) : BasicJwtProvider(
    revokedCache,
    jwtAccessSecret,
    jwtAccessTime,
    jwtAccessTimeUnit,
) {
    private val jwtRefreshSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret))

    fun generateRefreshToken(user: User): JwtToken {
        val now: LocalDateTime = LocalDateTime.now()
        val refreshExpirationInstant: Instant = now.plus(Duration.of(jwtRefreshTime, jwtRefreshTimeUnit))
            .atZone(ZoneId.systemDefault())
            .toInstant()
        val refreshExpiration: Date = Date.from(refreshExpirationInstant)
        return JwtToken(
            Jwts.builder()
                .setSubject(user.username)
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact(),
            refreshExpirationInstant.toEpochMilli(),
            user.id!!,
            JwtToken.Type.REFRESH,
            false,
        )
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        return validateToken(refreshToken, jwtRefreshSecret)
    }

    fun getRefreshClaims(token: String): Claims {
        return getClaims(token, jwtRefreshSecret)
    }
}