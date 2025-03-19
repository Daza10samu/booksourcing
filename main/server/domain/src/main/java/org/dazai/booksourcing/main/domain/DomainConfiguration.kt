package org.dazai.booksourcing.main.domain

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.time.Clock

@Configuration
@ComponentScan
@Import(
    DataSourceAutoConfiguration::class,
    FlywayAutoConfiguration::class,
    JooqAutoConfiguration::class,
)
@EnableAutoConfiguration
class DomainConfiguration {
    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}