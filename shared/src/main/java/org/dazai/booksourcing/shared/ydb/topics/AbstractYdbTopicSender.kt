package org.dazai.booksourcing.shared.ydb.topics

import com.github.rholder.retry.RetryerBuilder
import com.github.rholder.retry.StopStrategies
import com.github.rholder.retry.WaitStrategies
import org.dazai.booksourcing.shared.createObjectMapper
import org.dazai.booksourcing.shared.logger
import tech.ydb.topic.TopicClient
import tech.ydb.topic.description.Codec
import tech.ydb.topic.description.SupportedCodecs
import tech.ydb.topic.settings.CreateTopicSettings
import tech.ydb.topic.settings.PartitioningSettings
import tech.ydb.topic.settings.WriterSettings
import tech.ydb.topic.write.AsyncWriter
import tech.ydb.topic.write.Message
import tech.ydb.topic.write.WriteAck
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

abstract class AbstractYdbTopicSender<T>(
    private val topicClient: TopicClient,
) {
    protected val logger = logger()

    protected abstract val topicPath: String

    private lateinit var producerAndGroupId: String
    private lateinit var writer: AsyncWriter
    protected open val retriesCount: Int = 5

    private val retryer = RetryerBuilder.newBuilder<Unit>()
        .retryIfException()
        .withStopStrategy(StopStrategies.stopAfterAttempt(retriesCount))
        .withWaitStrategy(WaitStrategies.exponentialWait())
        .build()

    private val backgroundExecutor = Executors.newFixedThreadPool(4) {
        Thread(it, "ydb-sender-thread-${Thread.currentThread().threadId()}").apply {
            isDaemon = true
        }
    }

    protected open fun serialize(data: T): ByteArray = objectMapper.writeValueAsBytes(data)

    private fun createIfNotExists() {
        val topicDescriptionResult = topicClient.describeTopic(topicPath)
            .join()

        if (!topicDescriptionResult.isSuccess) {
            topicClient.createTopic(
                topicPath, CreateTopicSettings.newBuilder()
                    .setSupportedCodecs(
                        SupportedCodecs.newBuilder()
                            .addCodec(Codec.RAW)
                            .build()
                    )
                    .setPartitioningSettings(
                        PartitioningSettings.newBuilder()
                            .setMinActivePartitions(1)
                            .build()
                    )
                    .setRetentionPeriod(Duration.ofHours(1))
                    .build()
            )
        }
    }

    fun send(message: T) {
        CompletableFuture.runAsync( { sendInternal(message) }, backgroundExecutor)
    }

    fun send(messages: Collection<T>) {
        messages.forEach { send(it) }
    }

    private fun sendInternal(message: T) {
        retryer.call {
            writer.send(Message.of(serialize(message)))
                .whenComplete { result, ex ->
                    if (ex != null) {
                        logger.error("Exception on writing message message: ", ex)
                        throw ex
                    } else {
                        when (result.state) {
                            WriteAck.State.WRITTEN -> {
                                val details = result.details
                                val str = StringBuilder("Message was written successfully")
                                if (details != null) {
                                    str.append(", offset: ").append(details.offset)
                                }
                                logger.debug(str.toString())
                            }

                            WriteAck.State.ALREADY_WRITTEN -> {
                                logger.warn("Message has already been written")
                            }

                            else -> {}
                        }
                    }
                }
                .join()
        }
    }

    protected open fun init() {
        createIfNotExists()
        producerAndGroupId = UUID.randomUUID().toString()

        val settings = WriterSettings.newBuilder()
            .setTopicPath(topicPath)
            .setProducerId(producerAndGroupId)
            .setMessageGroupId(producerAndGroupId)
            .setCodec(Codec.RAW)
            .build()

        writer = topicClient.createAsyncWriter(settings)

        writer.init()
            .thenRun { logger.info("Init finished successfully") }
            .exceptionally { ex ->
                logger.error("Init failed with ex: ", ex)
                null
            }
            .join()
    }

    companion object {
        protected val objectMapper = createObjectMapper()
    }
}