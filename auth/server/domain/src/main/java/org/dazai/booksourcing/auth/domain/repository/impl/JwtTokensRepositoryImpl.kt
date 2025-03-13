package org.dazai.booksourcing.auth.domain.repository.impl

import org.dazai.booksourcing.auth.domain.db.Indexes
import org.dazai.booksourcing.auth.domain.db.tables.JwtTokens
import org.dazai.booksourcing.auth.domain.db.tables.records.JwtTokensRecord
import org.dazai.booksourcing.shared.auth.models.JwtToken
import org.dazai.booksourcing.auth.domain.repository.JwtTokensRepository
import org.springframework.stereotype.Repository
import tech.ydb.jooq.impl.YdbDSLContextImpl

@Repository
class JwtTokensRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
) : JwtTokensRepository {
    override fun saveToken(token: JwtToken) {
        dsl.insertInto(JwtTokens.JWT_TOKENS)
            .set(token.toRecord())
            .execute()
    }

    override fun getToken(tokenString: String): JwtToken? {
        return dsl.selectFrom(JwtTokens.JWT_TOKENS)
            .where(JwtTokens.JWT_TOKENS.JWT_TOKEN.eq(tokenString))
            .and(JwtTokens.JWT_TOKENS.REVOKED.isFalse)
            .fetchOne()?.toModel()
    }

    override fun getTokensByUserId(userId: Long): Collection<JwtToken> {
        return dsl.selectFrom(JwtTokens.JWT_TOKENS.useIndex(Indexes.JWT_TOKENS__USER_ID__IDX.name))
            .where(JwtTokens.JWT_TOKENS.USER_ID.eq(org.jooq.types.ULong.valueOf(userId)))
            .map { it.toModel() }
    }

    override fun getRevokedTokens(type: JwtToken.Type?): Collection<JwtToken> {
        var condition = JwtTokens.JWT_TOKENS.REVOKED.isTrue

        if (type != null) {
            condition = condition.and(JwtTokens.JWT_TOKENS.TYPE.eq(type.toString()))
        }

        return dsl.selectFrom(JwtTokens.JWT_TOKENS)
            .where(condition)
            .map { it.toModel() }
    }

    override fun revokeTokens(tokens: Collection<JwtToken>) {
        dsl.upsertInto(JwtTokens.JWT_TOKENS)
            .set(
                tokens.map {
                    it.copy(isRevoked = true).toRecord()
                }
            )
            .execute()
    }

    override fun revokeTokensByUserId(userId: Long) {
//        dsl.update(JwtTokens.JWT_TOKENS)
//            .set(JwtTokens.JWT_TOKENS.REVOKED, true)
//            .where(JwtTokens.JWT_TOKENS.USER_ID.eq(org.jooq.types.ULong.valueOf(userId)))
//            .execute()
        dsl.query("""
            update ${JwtTokens.JWT_TOKENS.name}
            set revoked = true
            where user_id = $userId
        """.trimIndent())
    }

    override fun deleteExpiredTokens() {
        dsl.deleteFrom(JwtTokens.JWT_TOKENS)
            .where(JwtTokens.JWT_TOKENS.EXPIRATION_TS.lt(System.currentTimeMillis()))
            .execute()
    }

    companion object {
        private fun JwtToken.toRecord(): JwtTokensRecord = JwtTokensRecord(
            token,
            expiration,
            org.jooq.types.ULong.valueOf(userId),
            type.toString(),
            isRevoked,
        )

        private fun JwtTokensRecord.toModel(): JwtToken = JwtToken(
            jwtToken,
            expirationTs,
            userId.toLong(),
            JwtToken.Type.valueOf(type),
            revoked,
        )
    }
}