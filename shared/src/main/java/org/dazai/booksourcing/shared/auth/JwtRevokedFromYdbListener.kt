package org.dazai.booksourcing.shared.auth

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
    override fun doJobWithObject(message: Message, obj: JwtToken) {
        jwtRevokedCacheFromYdbTopics.addRevoked(obj)
    }
}