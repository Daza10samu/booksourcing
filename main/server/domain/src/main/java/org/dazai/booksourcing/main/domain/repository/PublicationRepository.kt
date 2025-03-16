package org.dazai.booksourcing.main.domain.repository

import org.dazai.booksourcing.main.domain.model.Publication

interface PublicationRepository {
    fun findById(id: Long): Publication?
    fun findByOfferedBookId(bookId: Long): Publication?
    fun findByOwnerId(ownerId: Long): List<Publication>
    fun findByStatus(status: String): List<Publication>
    fun save(publication: Publication)
    fun update(publication: Publication): Publication
    fun delete(id: Long)
    fun findActivePublications(): List<Publication>
}