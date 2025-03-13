package org.dazai.booksourcing.shared.ydb.topics

import com.fasterxml.jackson.core.type.TypeReference
import org.dazai.booksourcing.shared.createObjectMapper
import org.dazai.booksourcing.shared.logger
import tech.ydb.topic.TopicClient
import tech.ydb.topic.read.Message
import tech.ydb.topic.read.events.AbstractReadEventHandler
import tech.ydb.topic.read.events.DataReceivedEvent
import tech.ydb.topic.settings.ReadEventHandlersSettings
import tech.ydb.topic.settings.ReaderSettings
import tech.ydb.topic.settings.TopicReadSettings

abstract class AbstractYdbTopicListener<T>(
    private val topicClient: TopicClient,
) {
    protected val log = logger()

    protected abstract val listenerName: String
    protected abstract val topicPath: String
    protected open val shouldCommit: Boolean = false
    protected open val shouldCommitOnFailure: Boolean = true
    protected val typeReference = object : TypeReference<T>() {}

    protected open fun deserialize(data: ByteArray) = objectMapper.readValue(data, typeReference)

    fun init() {
        val readerSettings = ReaderSettings.newBuilder()
            .setConsumerName(listenerName)
            .addTopic(
                TopicReadSettings.newBuilder()
                    .setPath(topicPath)
                    .build()
            )
            .build()

        val handlerSettings = ReadEventHandlersSettings.newBuilder()
            .setEventHandler(object : AbstractReadEventHandler() {
                override fun onMessages(event: DataReceivedEvent) {
                    event.messages.map { message ->
                        log.debug("Message received: ${String(message.data)}")

                        var ok = true
                        try {
                            doJob(message)
                        } catch (e: Exception) {
                            log.error("Exception on doJob: ", e)
                            ok = false
                            if (shouldCommitOnFailure && shouldCommit) {
                                message.commit()
                            }
                        }
                        if (ok && shouldCommit) {
                            message.commit()
                        }
                    }
                }
            })
            .build()

        val reader = topicClient.createAsyncReader(readerSettings, handlerSettings)

        reader.init()
            .join()
    }

    protected open fun doJob(message: Message) {
        doJobWithObject(message, deserialize(message.data))
    }

    protected open fun doJobWithObject(message: Message, obj: T) {

    }

    companion object {
        protected val objectMapper = createObjectMapper()
    }
}