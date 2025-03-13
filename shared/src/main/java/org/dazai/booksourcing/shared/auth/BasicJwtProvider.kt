package org.dazai.booksourcing.shared.auth

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.dazai.booksourcing.shared.auth.models.JwtToken
import org.dazai.booksourcing.shared.auth.models.User
import org.dazai.booksourcing.shared.logger
import org.springframework.beans.factory.annotation.Value
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

open class BasicJwtProvider(
    protected val revokedCache: IJwtRevokedCache,
    @Value("\${jwt.secret.access}") jwtAccessSecret: String,
    @Value("\${jwt.access.time:5}") protected val jwtAccessTime: Long,
    @Value("\${jwt.access.time.unit:MINUTES}") protected val jwtAccessTimeUnit: ChronoUnit,
) {
    protected val log = logger()
    protected val jwtAccessSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret))

    fun generateAccessToken(user: User): JwtToken {
        val now: LocalDateTime = LocalDateTime.now()
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

    fun validateAccessToken(accessToken: String): Boolean {
        return validateToken(accessToken, jwtAccessSecret)
    }

    protected fun validateToken(token: String, secret: SecretKey): Boolean {
        try {
            if (revokedCache.isRevoked(token)) {
                log.error("Token revoked")
                return false
            }
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
            return true
        } catch (expEx: ExpiredJwtException) {
            log.error("Token expired", expEx)
        } catch (unsEx: UnsupportedJwtException) {
            log.error("Unsupported jwt", unsEx)
        } catch (mjEx: MalformedJwtException) {
            log.error("Malformed jwt", mjEx)
        } catch (sEx: SignatureException) {
            log.error("Invalid signature", sEx)
        } catch (e: Exception) {
            log.error("invalid token", e)
        }
        return false
    }

    fun getAccessClaims(token: String): Claims {
        return getClaims(token, jwtAccessSecret)
    }

    protected fun getClaims(token: String, secret: SecretKey): Claims {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    }
}