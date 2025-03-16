package org.dazai.booksourcing.auth.domain

import org.jooq.ConnectionProvider
import org.jooq.ExecuteListenerProvider
import org.jooq.SQLDialect
import org.jooq.TransactionProvider
import org.jooq.conf.RenderTable
import org.jooq.conf.Settings
import org.jooq.impl.DefaultConfiguration
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.boot.autoconfigure.jooq.JooqProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.ydb.jooq.impl.YdbDSLContextImpl
import javax.sql.DataSource

@Configuration
class YdbJooqConfiguration {

    @Bean
    fun dslContext(configuration: org.jooq.Configuration): YdbDSLContextImpl {
        configuration.settings()
            .withRenderTable(RenderTable.NEVER)
            .withRenderCatalog(false)
            .withRenderSchema(false)
        return YdbDSLContextImpl(
            configuration,
            )
    }
}