package org.dazai.booksourcing.main.web.openapi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SwaggerProperties::class)
class OpenApiConfig(private val swaggerProperties: SwaggerProperties) {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Booksourcing Main")
                    .description("API Documentation")
                    .version("1.0")
            )
            .servers(
                listOf(
                    Server()
                        .url(swaggerProperties.baseUrl)
                        .description("API Server")
                )
            )
    }
}
