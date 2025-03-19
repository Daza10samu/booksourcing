package org.dazai.booksourcing.main.standalone

import org.dazai.booksourcing.main.web.WebConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan
@Import(WebConfiguration::class)
@EnableAutoConfiguration
class StandaloneConfiguration