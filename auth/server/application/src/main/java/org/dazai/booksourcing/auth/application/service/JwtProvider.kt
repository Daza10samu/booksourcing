package org.dazai.booksourcing.auth.application.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.dazai.booksourcing.shared.auth.BasicJwtProvider
import org.dazai.booksourcing.shared.auth.models.JwtToken
import org.dazai.booksourcing.shared.auth.models.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    revokedCache: JwtRevokedCache,
    private val clock: Clock,
    @Value("\${jwt.secret.access}") jwtAccessSecret: String,
    @Value("\${jwt.secret.refresh}") jwtRefreshSecret: String,
    @Value("\${jwt.access.time:5}") private val jwtAccessTime: Long,
    @Value("\${jwt.access.time.unit:MINUTES}") private val jwtAccessTimeUnit: ChronoUnit,
    @Value("\${jwt.refresh.time:30}") private val jwtRefreshTime: Long,
    @Value("\${jwt.refresh.time.unit:DAYS}") private val jwtRefreshTimeUnit: ChronoUnit,
) : BasicJwtProvider(
    revokedCache,
    jwtAccessSecret,
) {
    private val jwtRefreshSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret))

    fun generateAccessToken(user: User): JwtToken {
        val now: LocalDateTime = LocalDateTime.now(clock)
        val accessExpirationInstant: Instant = now.plus(Duration.of(jwtAccessTime, jwtAccessTimeUnit))
            .atZone(ZoneId.systemDefault())
            .toInstant()
        val accessExpiration: Date = Date.from(accessExpirationInstant)
        return JwtToken(
            Jwts.builder()
                .setSubject(user.username)
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", user.roles)
                .claim("userId", user.id)
                .compact(),
            accessExpirationInstant.toEpochMilli(),
            user.id!!,
            JwtToken.Type.ACCESS,
            false,
        )
    }

    fun generateRefreshToken(user: User): JwtToken {
        val now: LocalDateTime = LocalDateTime.now(clock)
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