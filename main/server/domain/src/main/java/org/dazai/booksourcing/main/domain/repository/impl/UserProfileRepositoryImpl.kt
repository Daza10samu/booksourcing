package org.dazai.booksourcing.main.domain.repository.impl

import org.dazai.booksourcing.main.domain.db.Indexes
import org.dazai.booksourcing.main.domain.db.tables.records.UserProfileRecord
import org.dazai.booksourcing.main.domain.models.UserProfile
import org.dazai.booksourcing.main.domain.repository.UserProfileRepository
import org.springframework.stereotype.Repository
import tech.ydb.jooq.impl.YdbDSLContextImpl
import org.dazai.booksourcing.main.domain.db.Tables.USER_PROFILE

@Repository
class UserProfileRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
) : UserProfileRepository {

    override fun findByUserId(userId: Long): UserProfile? {
        return dsl.selectFrom(USER_PROFILE)
            .where(USER_PROFILE.USER_ID.eq(userId))
            .fetchOne()
            ?.toModel()
    }

    override fun save(userProfile: UserProfile): UserProfile {
        val record = userProfile.toRecord()

        dsl.insertInto(USER_PROFILE)
            .set(record)
            .execute()

        return findByUserId(userProfile.userId) ?: throw IllegalStateException("The saved user profile is not found in the database")
    }

    override fun update(userProfile: UserProfile): UserProfile {
        val record = userProfile.toRecord()

        dsl.upsertInto(USER_PROFILE)
            .set(record)
            .execute()

        return userProfile
    }

    override fun delete(id: Long) {
        dsl.deleteFrom(USER_PROFILE)
            .where(USER_PROFILE.USER_ID.eq(id))
            .execute()
    }

    companion object {
        private fun UserProfile.toRecord(): UserProfileRecord {
            val record = UserProfileRecord()

            userId.let { record.userId = it }

            record.firstName = firstName
            record.lastName = lastName

            record.address = address
            record.phone = phone
            record.bio = bio

            return record
        }

        private fun UserProfileRecord.toModel(): UserProfile = UserProfile(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phone = phone,
            bio = bio,
        )
    }
}