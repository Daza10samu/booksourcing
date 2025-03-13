package org.dazai.booksourcing.auth.application.service.topics

import org.dazai.booksourcing.shared.auth.models.JwtToken
import org.dazai.booksourcing.shared.ydb.topics.AbstractYdbTopicSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tech.ydb.topic.TopicClient
import javax.annotation.PostConstruct

@Component
class RevokedAccessSender(
    @Value("\${ydb.topic.revokedAccess}")
    override val topicPath: String,
    topicClient: TopicClient,
) : AbstractYdbTopicSender<JwtToken>(topicClient) {
    @PostConstruct
    override fun init() {
        super.init()
    }
}