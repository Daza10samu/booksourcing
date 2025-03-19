package org.dazai.booksourcing.main.application

import org.dazai.booksourcing.main.domain.DomainConfiguration
import org.dazai.booksourcing.shared.auth.BasicJwtProvider
import org.dazai.booksourcing.shared.auth.JwtRevokedCacheFromYdbTopics
import org.dazai.booksourcing.shared.auth.JwtRevokedFromYdbListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import tech.ydb.auth.iam.CloudAuthHelper
import tech.ydb.core.grpc.GrpcTransport
import tech.ydb.topic.TopicClient
import java.time.Clock

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

    @Bean
    fun jwtRevokedCacheFromYdbTopic(clock: Clock): JwtRevokedCacheFromYdbTopics = JwtRevokedCacheFromYdbTopics(
        clock = clock,
    )

    @Bean
    fun jwtProvider(
        revokedCacheFromYdbTopics: JwtRevokedCacheFromYdbTopics,
        @Value("\${jwt.secret.access}") accessSecret: String
    ): BasicJwtProvider {
        return BasicJwtProvider(revokedCacheFromYdbTopics, accessSecret)
    }

    @Bean
    fun jwtRevokedFromYdbListener(
        jwtRevokedCacheFromYdbTopics: JwtRevokedCacheFromYdbTopics,
        topicClient: TopicClient,
        @Value("\${ydb.topic.revokedAccess.listener}") listenerName: String,
        @Value("\${ydb.topic.revokedAccess}") topicName: String,
    ): JwtRevokedFromYdbListener {
        return JwtRevokedFromYdbListener(
            jwtRevokedCacheFromYdbTopics,
            topicClient,
            listenerName,
            topicName
        )
    }
}