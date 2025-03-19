package org.dazai.booksourcing.main.web

import org.dazai.booksourcing.main.web.ws.AuthenticationHandshakeHandler
import org.dazai.booksourcing.main.web.ws.AuthenticationHandshakeInterceptor
import org.dazai.booksourcing.main.web.ws.MyWebSocketHandler
import org.dazai.booksourcing.shared.auth.BasicJwtProvider
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val myWebSocketHandler: MyWebSocketHandler,
    private val jwtProvider: BasicJwtProvider,
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(myWebSocketHandler, "/websocket-endpoint")
            .setHandshakeHandler(AuthenticationHandshakeHandler(jwtProvider))
            .addInterceptors(AuthenticationHandshakeInterceptor(jwtProvider))
    }
}
