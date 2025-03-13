package org.dazai.booksourcing.shared

import com.fasterxml.jackson.module.kotlin.KotlinModule

fun createObjectMapper() = com.fasterxml.jackson.databind.ObjectMapper()
    .registerModules(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule(), KotlinModule.Builder().build())

fun Any.logger() = org.slf4j.LoggerFactory.getLogger(this::class.java)