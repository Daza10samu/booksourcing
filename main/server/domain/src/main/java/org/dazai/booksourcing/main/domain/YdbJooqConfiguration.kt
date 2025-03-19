package org.dazai.booksourcing.main.domain

import org.jooq.conf.RenderTable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.ydb.jooq.impl.YdbDSLContextImpl

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