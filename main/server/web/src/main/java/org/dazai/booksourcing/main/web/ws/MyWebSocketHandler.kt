package org.dazai.booksourcing.main.web.ws

import org.dazai.booksourcing.main.application.service.ws.service.WsSessionsManager
import org.dazai.booksourcing.shared.logger
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class MyWebSocketHandler(
    private val wsSessionsManager: WsSessionsManager,
) : TextWebSocketHandler() {
    private val logger = logger()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        wsSessionsManager[session.id] = session
        logger.info("New WebSocket connection: " + session.id)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        logger.info("Received message: " + payload + " from " + session.id)


        session.sendMessage(TextMessage("Server received: $payload"))


        wsSessionsManager.broadcastMessage("Broadcast: $payload")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        wsSessionsManager.remove(session.id)
        logger.info("WebSocket connection closed: " + session.id + ", status: " + status)
    }
}