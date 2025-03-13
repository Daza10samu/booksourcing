package org.dazai.booksourcing.auth.application

import org.dazai.booksourcing.auth.domain.DomainConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import tech.ydb.auth.iam.CloudAuthHelper
import tech.ydb.core.grpc.GrpcTransport
import tech.ydb.topic.TopicClient

@Configuration
@ComponentScan
@Import(DomainConfiguration::class)
@EnableAutoConfiguration
class ApplicationConfiguration {
    @Bean
    fun transport(
        @Value("\${ydb.auth.service-account-service-key-path}") serviceAccountKeyPath: String,
        @Value("\${ydb.auth.service-connection-string}") connectionString: String,
    ): GrpcTransport {
        val transport =
            GrpcTransport.forConnectionString(connectionString)
                .withAuthProvider(CloudAuthHelper.getServiceAccountFileAuthProvider(serviceAccountKeyPath))
                .build()

        return transport
    }

    @Bean
    fun topicClient(transport: GrpcTransport): TopicClient {
        return TopicClient
            .newClient(transport)
            .build()
    }
}