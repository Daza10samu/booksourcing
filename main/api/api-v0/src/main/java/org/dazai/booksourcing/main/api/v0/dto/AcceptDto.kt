package org.dazai.booksourcing.main.api.v0.dto

data class AcceptDto(
    val offeredBookId: Long,
    val requestDetails: String,
)