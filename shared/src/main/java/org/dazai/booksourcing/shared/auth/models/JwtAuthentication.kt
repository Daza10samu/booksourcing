package org.dazai.booksourcing.shared.auth.models

import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

class JwtAuthentication(
    val userId: Long,
    private val name: String,
    private val authorities: Set<Role>,
    private val credentials: Any?,
    private val details: UserDetails?,
    private val principal: String,
    private var authenticated: Boolean,
) : Authentication {
    override fun getName(): String = name

    override fun getAuthorities(): Set<Role> = authorities

    override fun getCredentials(): Any? = credentials

    override fun getDetails(): UserDetails? = details

    override fun getPrincipal(): Any = principal

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated()
    }
}