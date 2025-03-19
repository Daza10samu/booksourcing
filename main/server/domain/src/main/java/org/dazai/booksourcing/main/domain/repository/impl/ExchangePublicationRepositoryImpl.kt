package org.dazai.booksourcing.main.domain.repository.impl

import org.dazai.booksourcing.main.domain.db.Indexes
import org.dazai.booksourcing.main.domain.db.tables.records.PublicationRecord
import org.dazai.booksourcing.main.domain.models.ExchangePublication
import org.dazai.booksourcing.main.domain.repository.ExchangePublicationRepository
import org.springframework.stereotype.Repository
import tech.ydb.jooq.impl.YdbDSLContextImpl
import org.dazai.booksourcing.main.domain.db.Tables.PUBLICATION

@Repository
class ExchangePublicationRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
) : ExchangePublicationRepository {

    override fun findById(id: Long): ExchangePublication? {
        return dsl.selectFrom(PUBLICATION)
            .where(PUBLICATION.ID.eq(id))
            .fetchOne()
            ?.toModel()
    }

    override fun findByOfferedBookId(bookId: Long): ExchangePublication? {
        return dsl.selectFrom(PUBLICATION.useIndex(Indexes.PUBLICATION__OFFERED_BOOK_ID__IDX.name))
            .where(PUBLICATION.OFFERED_BOOK_ID.eq(bookId))
            .fetchOne()
            ?.toModel()
    }

    override fun findByOwnerId(ownerId: Long): List<ExchangePublication> {
        return dsl.selectFrom(PUBLICATION.useIndex(Indexes.PUBLICATION__OWNER_ID__IDX.name))
            .where(PUBLICATION.OWNER_ID.eq(ownerId))
            .fetch()
            .map { it.toModel() }
    }

    override fun findByStatus(status: String): List<ExchangePublication> {
        return dsl.selectFrom(PUBLICATION.useIndex(Indexes.PUBLICATION__STATUS__IDX.name))
            .where(PUBLICATION.STATUS.eq(status))
            .fetch()
            .map { it.toModel() }
    }

    override fun save(exchangePublication: ExchangePublication) {
        val record = exchangePublication
            .copy(id = null).toRecord()

        dsl.insertInto(PUBLICATION)
            .set(record)
            .execute()
    }

    override fun update(exchangePublication: ExchangePublication): ExchangePublication {
        val oldValue = findById(exchangePublication.id!!)
        if (oldValue == null || oldValue.id != exchangePublication.id) {
            throw IllegalArgumentException("The publication to update either does not exist or the IDs do not match.")
        }

        dsl.upsertInto(PUBLICATION)
            .set(exchangePublication.toRecord())
            .execute()

        return exchangePublication
    }

    override fun delete(id: Long) {
        dsl.deleteFrom(PUBLICATION)
            .where(PUBLICATION.ID.eq(id))
            .execute()
    }

    override fun findActivePublications(): List<ExchangePublication> {
        return dsl.selectFrom(PUBLICATION)
            .where(PUBLICATION.STATUS.eq(ExchangePublication.PublicationStatus.ACTIVE.name))
            .fetch()
            .map { it.toModel() }
    }

    companion object {
        private fun ExchangePublication.toRecord(): PublicationRecord {
            val record = PublicationRecord()

            id?.let { record.id = it }
            offeredBookId.let { record.offeredBookId = it }
            ownerId.let { record.ownerId = it }

            record.offerDetails = offerDetails
            record.status = status.name
            record.createdDate = createdDate

            return record

        }

        private fun PublicationRecord.toModel(): ExchangePublication = ExchangePublication(
            id,
            offeredBookId,
            ownerId,
            offerDetails,
            ExchangePublication.PublicationStatus.valueOf(status),
            createdDate,
        )
    }
}