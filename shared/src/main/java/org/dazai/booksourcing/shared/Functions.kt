package org.dazai.booksourcing.shared

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.dazai.booksourcing.shared.auth.models.JwtAuthentication
import org.springframework.security.core.context.SecurityContextHolder

fun createObjectMapper() = com.fasterxml.jackson.databind.ObjectMapper()
    .registerModules(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule(), KotlinModule.Builder().build())

fun Any.logger() = org.slf4j.LoggerFactory.getLogger(this::class.java)

val authInfo: JwtAuthentication
    get() = SecurityContextHolder.getContext().authentication as JwtAuthentication