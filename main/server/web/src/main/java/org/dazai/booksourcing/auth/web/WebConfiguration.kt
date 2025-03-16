package org.dazai.booksourcing.auth.web

import org.dazai.booksourcing.auth.application.ApplicationConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan
@Import(ApplicationConfiguration::class)
@EnableAutoConfiguration
class WebConfiguration