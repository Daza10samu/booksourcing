package org.dazai.booksourcing.shared.auth

interface IJwtRevokedCache {
    fun isRevoked(token: String): Boolean
    fun allRevoked(): Set<String>
}