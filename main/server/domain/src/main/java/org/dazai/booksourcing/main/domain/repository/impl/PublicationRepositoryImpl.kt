package org.dazai.booksourcing.main.domain.repository.impl

import org.dazai.booksourcing.main.domain.db.Indexes
import org.dazai.booksourcing.main.domain.db.tables.records.PublicationRecord
import org.dazai.booksourcing.main.domain.model.Publication
import org.dazai.booksourcing.main.domain.repository.PublicationRepository
import org.springframework.stereotype.Repository
import tech.ydb.jooq.impl.YdbDSLContextImpl
import org.dazai.booksourcing.main.domain.db.Tables.PUBLICATION

@Repository
class PublicationRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
) : PublicationRepository {

    override fun findById(id: Long): Publication? {
        return dsl.selectFrom(PUBLICATION)
            .where(PUBLICATION.ID.eq(org.jooq.types.ULong.valueOf(id)))
            .fetchOne()
            ?.toModel()
    }

    override fun findByOfferedBookId(bookId: Long): Publication? {
        return dsl.selectFrom(PUBLICATION.useIndex(Indexes.PUBLICATION__OFFERED_BOOK_ID__IDX.name))
            .where(PUBLICATION.OFFERED_BOOK_ID.eq(org.jooq.types.ULong.valueOf(bookId)))
            .fetchOne()
            ?.toModel()
    }

    override fun findByOwnerId(ownerId: Long): List<Publication> {
        return dsl.selectFrom(PUBLICATION.useIndex(Indexes.PUBLICATION__OWNER_ID__IDX.name))
            .where(PUBLICATION.OWNER_ID.eq(org.jooq.types.ULong.valueOf(ownerId)))
            .fetch()
            .map { it.toModel() }
    }

    override fun findByStatus(status: String): List<Publication> {
        return dsl.selectFrom(PUBLICATION.useIndex(Indexes.PUBLICATION__STATUS__IDX.name))
            .where(PUBLICATION.STATUS.eq(status))
            .fetch()
            .map { it.toModel() }
    }

    override fun save(publication: Publication) {
        val record = publication.toRecord()

        dsl.insertInto(PUBLICATION)
            .set(record)
            .execute()
    }

    override fun update(publication: Publication): Publication {
        val oldValue = findById(publication.id!!)
        if (oldValue == null || oldValue.id != publication.id) {
            throw IllegalArgumentException("The publication to update either does not exist or the IDs do not match.")
        }

        dsl.upsertInto(PUBLICATION)
            .set(publication.toRecord())
            .execute()

        return publication
    }

    override fun delete(id: Long) {
        dsl.deleteFrom(PUBLICATION)
            .where(PUBLICATION.ID.eq(org.jooq.types.ULong.valueOf(id)))
            .execute()
    }

    override fun findActivePublications(): List<Publication> {
        return dsl.selectFrom(PUBLICATION)
            .where(PUBLICATION.STATUS.eq(Publication.PublicationStatus.ACTIVE.name))
            .fetch()
            .map { it.toModel() }
    }

    companion object {
        private fun Publication.toRecord(): PublicationRecord {
            val record = PublicationRecord()

            id?.let { record.id = org.jooq.types.ULong.valueOf(it) }
            offeredBookId.let { record.offeredBookId = org.jooq.types.ULong.valueOf(it) }
            ownerId.let { record.ownerId = org.jooq.types.ULong.valueOf(it) }

            record.offerDetails = offerDetails
            record.status = status.name
            record.createdDate = createdDate

            return record

        }

        private fun PublicationRecord.toModel(): Publication = Publication(
            id.toLong(),
            offeredBookId.toLong(),
            ownerId.toLong(),
            offerDetails,
            Publication.PublicationStatus.valueOf(status),
            createdDate,
        )
    }
}