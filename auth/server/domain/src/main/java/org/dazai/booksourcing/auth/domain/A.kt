package org.dazai.booksourcing.auth.domain

import tech.ydb.auth.iam.CloudAuthHelper
import tech.ydb.core.grpc.GrpcTransport
import tech.ydb.topic.TopicClient
import tech.ydb.topic.description.Codec
import tech.ydb.topic.read.events.AbstractReadEventHandler
import tech.ydb.topic.read.events.DataReceivedEvent
import tech.ydb.topic.read.events.StartPartitionSessionEvent
import tech.ydb.topic.settings.*
import tech.ydb.topic.write.Message
import tech.ydb.topic.write.WriteAck
import java.util.*
import java.util.concurrent.Executors

fun main() {
    val transport =
        GrpcTransport.forConnectionString("grpcs://ydb.serverless.yandexcloud.net:2135/ru-central1/b1g04hhqmil1qg0qfg0k/etnqc6hm82q25its2mfh")
            .withAuthProvider(CloudAuthHelper.getServiceAccountFileAuthProvider("/home/dazai/Downloads/authorized_key.json"))
            .build()
    val topicClient = TopicClient.newClient(transport)
        .build()

//    val producerAndGroupId = UUID.randomUUID().toString()
//    val writerSettings = WriterSettings.newBuilder()
//        .setTopicPath("my-test-topic")
//        .setProducerId(producerAndGroupId)
//        .setMessageGroupId(producerAndGroupId)
//        .setCodec(Codec.RAW)
//        .build()
//
//    val writer = topicClient.createAsyncWriter(writerSettings)
//
//    writer.init()
//        .thenRun { println("Init completed") }
//        .exceptionally {
//            println("Init failed: $it")
//
//            it.printStackTrace()
//
//            null
//        }
//        .join()
//
//    writer.send(
//        Message.of(
//            "Hello, YDB!".toByteArray()
//        )
//    )
//        .whenComplete { result, throwable ->
//            if (throwable != null) {
//                println("Send failed: $throwable")
//                throwable.printStackTrace()
//            } else {
//                when (result.state) {
//                    WriteAck.State.WRITTEN -> {
//                        println("Written")
//                    }
//                    WriteAck.State.ALREADY_WRITTEN -> {
//                        println("Already written")
//                    }
//                    else -> {}
//                }
//            }
//        }
//        .join()

    val readerSettings = ReaderSettings.newBuilder()
        .setConsumerName("tmp")
        .addTopic(
            TopicReadSettings.newBuilder()
                .setPath("my-test-topic")
                .build()
        )
        .build()

    val handlerSettings = ReadEventHandlersSettings.newBuilder()
        .setEventHandler(object : AbstractReadEventHandler() {
            override fun onMessages(event: DataReceivedEvent) {
                event.messages.map { message ->
                    println("Message received: ${String(message.data)}")
                    message.commit()
                }
            }
        })
        .setExecutor(
            Executors
                .newFixedThreadPool(2) { r -> Thread(r, "reader-executor-${Thread.currentThread().threadId()}")}
        )
        .build()

    val reader = topicClient.createAsyncReader(readerSettings, handlerSettings)

    reader.init()

    Thread.sleep(300_000)
}