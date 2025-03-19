package org.dazai.booksourcing.main.domain.repository.impl

import org.dazai.booksourcing.main.domain.db.Indexes
import org.dazai.booksourcing.main.domain.db.tables.records.ExchangeRequestRecord
import org.dazai.booksourcing.main.domain.models.ExchangeRequest
import org.dazai.booksourcing.main.domain.repository.ExchangeRequestRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.support.TransactionTemplate
import tech.ydb.jooq.impl.YdbDSLContextImpl
import org.dazai.booksourcing.main.domain.db.Tables.EXCHANGE_REQUEST

@Repository
class ExchangeRequestRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
    private val transactionTemplate: TransactionTemplate
) : ExchangeRequestRepository {

    override fun findById(id: Long): ExchangeRequest? {
        return dsl.selectFrom(EXCHANGE_REQUEST)
            .where(EXCHANGE_REQUEST.ID.eq(id))
            .fetchOne()
            ?.toModel()
    }

    override fun findByRequestorId(requestorId: Long): List<ExchangeRequest> {
        return dsl.selectFrom(EXCHANGE_REQUEST.useIndex(Indexes.EXCHANGE_REQUEST__REQUESTOR_ID__IDX.name))
            .where(
                EXCHANGE_REQUEST.REQUESTOR_ID.eq(requestorId)
            )
            .fetch()
            .map { it.toModel() }
    }

    override fun findByOwnerId(ownerId: Long): List<ExchangeRequest> {
        return dsl.selectFrom(EXCHANGE_REQUEST.useIndex(Indexes.EXCHANGE_REQUEST__OWNER_ID__IDX.name))
            .where(
                EXCHANGE_REQUEST.OWNER_ID.eq(ownerId)
            )
            .fetch()
            .map { it.toModel() }
    }

    override fun findByStatus(status: String): List<ExchangeRequest> {
        return dsl.selectFrom(EXCHANGE_REQUEST.useIndex(Indexes.EXCHANGE_REQUEST__STATUS__IDX.name))
            .where(EXCHANGE_REQUEST.STATUS.eq(status))
            .fetch()
            .map { it.toModel() }
    }

    override fun findByRequestedBookId(bookId: Long): List<ExchangeRequest> {
        return dsl.selectFrom(EXCHANGE_REQUEST.useIndex(Indexes.EXCHANGE_REQUEST__REQUESTED_BOOK_ID__IDX.name))
            .where(
                EXCHANGE_REQUEST.REQUESTED_BOOK_ID.eq(bookId)
            )
            .fetch()
            .map { it.toModel() }
    }

    override fun findByRequestorBookId(bookId: Long): List<ExchangeRequest> {
        return dsl.selectFrom(EXCHANGE_REQUEST.useIndex(Indexes.EXCHANGE_REQUEST__REQUESTOR_BOOK_ID__IDX.name))
            .where(
                EXCHANGE_REQUEST.REQUESTOR_BOOK_ID.eq(bookId)
            )
            .fetch()
            .map { it.toModel() }
    }

    override fun save(exchangeRequest: ExchangeRequest) {
        val record = exchangeRequest
            .copy(id = null).toRecord()

        dsl.insertInto(EXCHANGE_REQUEST)
            .set(record)
            .execute()
    }

    override fun update(exchangeRequest: ExchangeRequest): ExchangeRequest {
        val oldValue = findById(exchangeRequest.id!!)!!
        if (oldValue.id != exchangeRequest.id) {
            throw IllegalArgumentException("The id of the book to update is different from the id of the book in the database.")
        }
        val record = exchangeRequest.toRecord()

        return transactionTemplate.execute {
            dsl.upsertInto(EXCHANGE_REQUEST)
                .set(record)
                .execute()

            exchangeRequest
        }!!
    }

    override fun delete(id: Long) {
        transactionTemplate.execute {
            dsl.deleteFrom(EXCHANGE_REQUEST)
                .where(
                    EXCHANGE_REQUEST.ID.eq(id)
                )
                .execute()
        }
    }

    companion object {
        private fun ExchangeRequest.toRecord(): ExchangeRequestRecord {
            val record = ExchangeRequestRecord()

            id?.let { record.id = it }
            requestedBookId.let { record.requestedBookId = it }
            requestorBookId.let { record.requestorBookId = it }
            requestorId.let { record.requestorId = it }
            ownerId.let { record.ownerId = it }

            record.status = status.name
            record.requestDate = requestDate
            record.responseDate = responseDate
            record.completionDate = completionDate
            record.message = message
            record.fromPublication = fromPublicationId

            return record
        }

        private fun ExchangeRequestRecord.toModel(): ExchangeRequest = ExchangeRequest(
            id,
            requestedBookId,
            requestorBookId,
            requestorId,
            ownerId,
            ExchangeRequest.ExchangeRequestStatus.valueOf(status),
            requestDate,
            responseDate,
            completionDate,
            message,
            fromPublication
        )
    }
}