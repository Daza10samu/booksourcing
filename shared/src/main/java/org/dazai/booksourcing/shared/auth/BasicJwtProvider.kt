package org.dazai.booksourcing.shared.auth

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.dazai.booksourcing.shared.logger
import org.springframework.beans.factory.annotation.Value
import javax.crypto.SecretKey

open class BasicJwtProvider(
    protected val revokedCache: IJwtRevokedCache,
    @Value("\${jwt.secret.access}") jwtAccessSecret: String,
) {
    protected val log = logger()
    protected val jwtAccessSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret))

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