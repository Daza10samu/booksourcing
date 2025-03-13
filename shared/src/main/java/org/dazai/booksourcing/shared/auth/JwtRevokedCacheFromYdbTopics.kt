package org.dazai.booksourcing.shared.auth

import org.dazai.booksourcing.shared.auth.models.JwtToken
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class JwtRevokedCacheFromYdbTopics(
    private val clock: Clock,
) : IJwtRevokedCache {
    private val cleanupExecutor = Executors.newSingleThreadScheduledExecutor()
    private val map = ConcurrentHashMap<String, JwtToken>()

    init {
        cleanupExecutor.scheduleAtFixedRate(::cleanup, 0, 300, java.util.concurrent.TimeUnit.SECONDS)
    }

    private fun cleanup() {
        val expired = map.entries
            .filter { it.value.expiration < clock.millis() }
            .map { it.key }

        expired.forEach { map.remove(it) }

    }

    override fun isRevoked(token: String): Boolean {
        return map.contains(token)
    }

    override fun allRevoked(): Set<String> {
        return map.keys
    }

    fun addRevoked(token: JwtToken) {
        map[token.token] = token
    }
}