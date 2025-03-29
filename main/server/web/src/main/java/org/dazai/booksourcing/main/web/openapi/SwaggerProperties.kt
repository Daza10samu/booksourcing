package org.dazai.booksourcing.main.web.openapi

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "app.swagger")
@Component
class SwaggerProperties {
    var baseUrl: String = ""
    var isProxyEnabled: Boolean = false
}
