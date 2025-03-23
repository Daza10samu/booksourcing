package org.dazai.booksourcing.main.web.ws.service.message

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.dazai.booksourcing.main.api.v0.dto.ExchangePublicationDto
import org.dazai.booksourcing.shared.createObjectMapper
import org.springframework.web.socket.TextMessage
import java.time.Instant

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = WsMessage.PingWsMessage::class, name = "PING"),
    JsonSubTypes.Type(value = WsMessage.PongWsMessage::class, name = "PONG"),
    JsonSubTypes.Type(value = WsMessage.RawTextWsMessage::class, name = "RAW_TEXT_MESSAGE"),
    JsonSubTypes.Type(
        value = WsMessage.AddNewExchangePublicationWsMessage::class,
        name = "ADD_NEW_EXCHANGE_PUBLICATION"
    ),
    JsonSubTypes.Type(
        value = WsMessage.DeleteExchangePublicationWsMessage::class,
        name = "DELETE_EXCHANGE_PUBLICATION"
    ),
)
sealed interface WsMessage {
    enum class Type {
        PING,
        PONG,
        RAW_TEXT_MESSAGE,
        ADD_NEW_EXCHANGE_PUBLICATION,
        DELETE_EXCHANGE_PUBLICATION,
    }

    data class PingWsMessage(val text: String = "pint") : WsMessage

    data class PongWsMessage(val text: String? = null) : WsMessage

    data class RawTextWsMessage(val text: String) : WsMessage

    data class AddNewExchangePublicationWsMessage(val exchangePublicationDto: ExchangePublicationDto) : WsMessage

    data class DeleteExchangePublicationWsMessage(val id: Long) : WsMessage

    fun toTextMessage(): TextMessage = TextMessage(objectMapper.writeValueAsString(this))

    companion object {
        private val objectMapper = createObjectMapper()
    }
}