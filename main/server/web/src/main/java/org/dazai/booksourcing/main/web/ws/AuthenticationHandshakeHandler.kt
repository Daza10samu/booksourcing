package org.dazai.booksourcing.main.web.ws

import org.dazai.booksourcing.shared.auth.BasicJwtProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal
import org.springframework.http.server.ServletServerHttpRequest

class AuthenticationHandshakeHandler(private val jwtProvider: BasicJwtProvider) : DefaultHandshakeHandler() {
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: Map<String, Any>
    ): Principal? {
        val servletRequest = (request as ServletServerHttpRequest).servletRequest
        val token = servletRequest.getHeader("Authorization")?.removePrefix("Bearer ")
            ?: servletRequest.getParameter("token")

        if (token != null && jwtProvider.validateAccessToken(token)) {
            val claims = jwtProvider.getAccessClaims(token)
            val userId = claims.subject

            return Principal { userId }
        }

        return null // Если аутентификация не прошла, возвращаем null
    }
}