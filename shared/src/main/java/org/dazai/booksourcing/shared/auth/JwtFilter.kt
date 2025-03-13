package org.dazai.booksourcing.shared.auth

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.dazai.booksourcing.shared.auth.models.Role
import org.dazai.booksourcing.shared.auth.models.JwtAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

class JwtFilter(
    private val jwtProvider: BasicJwtProvider,
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse?, fc: FilterChain) {
        val token = getTokenFromRequest(request as HttpServletRequest)
        if (token != null && jwtProvider.validateAccessToken(token)) {
            val claims: Claims = jwtProvider.getAccessClaims(token)
            val jwtInfoToken: JwtAuthentication = claims.toAuthentication()
            jwtInfoToken.isAuthenticated = true
            SecurityContextHolder.getContext().authentication = jwtInfoToken
        }
        fc.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearer = request.getHeader(AUTHORIZATION)
        return if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            bearer.substring(7)
        } else null
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"

        private val json = ObjectMapper()

        private fun Claims.toAuthentication(): JwtAuthentication = JwtAuthentication(
            get("userId").toString().toLong(),
            subject,
            json.readValue(get("roles").toString(),  object : TypeReference<Set<Role>>() { }),
            null,
            null,
            subject,
            true
        )
    }
}