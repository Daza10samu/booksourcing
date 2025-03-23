package org.dazai.booksourcing.main.web.ws.service

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import org.dazai.booksourcing.main.web.ws.service.message.WsMessage
import org.dazai.booksourcing.shared.createObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import org.dazai.booksourcing.shared.logger
import org.springframework.web.socket.TextMessage
import java.util.*

@Service
class WsSessionsManager {
    private val logger = logger()

    private val sessions: MutableMap<String, WebSocketSession> = ConcurrentHashMap()
    val values: Collection<WebSocketSession>
        get() = sessions.values

    private val executor = Executors.newFixedThreadPool(16) {
        Thread(it, "WsSessionsManager-Thread-${Thread.currentThread().threadId()}").apply {
            isDaemon = true
        }
    }
    private val coroutineDispatcher = executor.asCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

    @PostConstruct
    fun init() {
        logger.info("WsSessionsManager init")

        startSessionMonitoring()
    }

    operator fun set(id: String, value: WebSocketSession) {
        sessions[id] = value
    }
    
    fun remove(id: String) {
        sessions.remove(id)
    }

    operator fun get(sessionId: String): WebSocketSession? = sessions[sessionId]

    fun broadcastMessage(message: WsMessage) {
        coroutineScope.launch {
            sessions.values.forEach { session ->
                if (session.isOpen) {
                    session.sendMessage(message)
                }
            }
        }
    }

    private fun startSessionMonitoring() {
        coroutineScope.launch {
            while (true) {
                try {
                    pingAndCleanSessions()
                    delay(30000)
                } catch (e: Exception) {
                    logger.error("Exception during processing sessions", e)
                }
            }
        }
    }

    private suspend fun pingAndCleanSessions() {
        logger.info("Checking sessions")

        val sessionsToRemove:MutableList<String> = Collections.synchronizedList(mutableListOf())

        sessions.forEach { (id, session) ->
            coroutineScope.launch {
                if (!session.isOpen) {
                    sessionsToRemove.add(id)
                } else {
                    try {
                        session.sendMessage(WsMessage.PingWsMessage())
                    } catch (e: Exception) {
                        logger.warn("Exception during ping session $id", e)
                        sessionsToRemove.add(id)
                    }
                }
            }
        }

        sessionsToRemove.forEach { id ->
            logger.info("Removing session $id")
            try {
                sessions[id]?.close()
            } catch (e: Exception) {
                logger.warn("Exception during closing session $id", e)
            }
            remove(id)
        }
    }

    private suspend fun WebSocketSession.sendMessage(message: WsMessage) {
        coroutineScope.launch {
            if (isOpen) {
                sendMessage(message.toTextMessage())
            }
        }
    }
}