package org.dazai.booksourcing.main.web.ws

import org.dazai.booksourcing.shared.auth.BasicJwtProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

class AuthenticationHandshakeInterceptor(private val jwtProvider: BasicJwtProvider) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val servletRequest = (request as ServletServerHttpRequest).servletRequest
        val token = servletRequest.getHeader("Authorization")?.removePrefix("Bearer ")
            ?: servletRequest.getParameter("token")

        if (token == null || !jwtProvider.validateAccessToken(token)) {
            return false // Отклоняем соединение
        }

        try {
            val claims = jwtProvider.getAccessClaims(token)
            val userId = claims.subject
            attributes["userId"] = userId
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
    }
}