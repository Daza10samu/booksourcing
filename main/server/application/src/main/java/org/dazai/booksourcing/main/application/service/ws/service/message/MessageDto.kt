package org.dazai.booksourcing.main.application.service.ws.service.message

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageDto.PingMessageDto::class, name = "PING"),
    JsonSubTypes.Type(value = MessageDto.PongMessageDto::class, name = "PONG"),
    JsonSubTypes.Type(value = MessageDto.RawTextMessageDto::class, name = "RAW_TEXT_MESSAGE"),
)
sealed interface MessageDto {
    enum class Type {
        PING,
        PONG,
        RAW_TEXT_MESSAGE,
    }

    data class PingMessageDto(val text: String = "pint"): MessageDto

    data class PongMessageDto(val text: String? = null): MessageDto

    data class RawTextMessageDto(val text: String): MessageDto
}