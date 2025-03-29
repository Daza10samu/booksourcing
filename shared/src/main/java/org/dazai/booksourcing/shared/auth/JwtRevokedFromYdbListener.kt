package org.dazai.booksourcing.shared.auth

import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import org.dazai.booksourcing.shared.auth.models.JwtToken
import org.dazai.booksourcing.shared.ydb.topics.AbstractYdbTopicListener
import tech.ydb.topic.TopicClient
import tech.ydb.topic.read.Message

class JwtRevokedFromYdbListener(
    private val jwtRevokedCacheFromYdbTopics: JwtRevokedCacheFromYdbTopics,
    topicClient: TopicClient,
    override val listenerName: String,
    override val topicPath: String,
) : AbstractYdbTopicListener<JwtToken>(topicClient) {
    @PostConstruct
    override fun init() {
        super.init()
    }

    override fun doJobWithObject(message: Message, obj: JwtToken) {
        log.info("Received revoked jwt token: $obj")
        jwtRevokedCacheFromYdbTopics.addRevoked(obj)
    }

    override fun deserialize(data: ByteArray): JwtToken = objectMapper.readValue(data.toString(Charsets.UTF_8))

}