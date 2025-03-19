package org.dazai.booksourcing.main.api.v0.dto

import java.time.Instant

data class BookDto(
    val id: Long?,
    val ownerId: Long,
    val title: String,
    val author: String,
    val genre: String,
    val description: String? = null,
    val condition: BookCondition,
    val imageUrl: String? = null,
    val status: BookStatus,
    val addedDate: Instant
) {
    enum class BookStatus {
        AVAILABLE, REQUESTED, EXCHANGED
    }

    enum class BookCondition {
        NEW, USED, GOOD, BAD,
    }
}