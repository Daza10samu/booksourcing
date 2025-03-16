package org.dazai.booksourcing.main.domain.repository.impl

import org.dazai.booksourcing.main.domain.db.Indexes
import org.dazai.booksourcing.main.domain.db.tables.records.UserProfileRecord
import org.dazai.booksourcing.main.domain.model.UserProfile
import org.dazai.booksourcing.main.domain.repository.UserProfileRepository
import org.springframework.stereotype.Repository
import tech.ydb.jooq.impl.YdbDSLContextImpl
import org.dazai.booksourcing.main.domain.db.Tables.USER_PROFILE

@Repository
class UserProfileRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
) : UserProfileRepository {

    override fun findById(id: Long): UserProfile? {
        return dsl.selectFrom(USER_PROFILE)
            .where(USER_PROFILE.ID.eq(org.jooq.types.ULong.valueOf(id)))
            .fetchOne()
            ?.toModel()
    }

    override fun findByUserId(userId: Long): UserProfile? {
        return dsl.selectFrom(USER_PROFILE.useIndex(Indexes.USER_PROFILE__USER_ID__IDX.name))
            .where(USER_PROFILE.USER_ID.eq(org.jooq.types.ULong.valueOf(userId)))
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
        val oldProfile = findByUserId(userProfile.userId)
        if (oldProfile != null && oldProfile.id != userProfile.id) {
            throw IllegalArgumentException("User profile ID mismatch: cannot update the profile with a different ID.")
        }

        val record = userProfile.toRecord()

        dsl.upsertInto(USER_PROFILE)
            .set(record)
            .execute()

        return userProfile
    }

    override fun delete(id: Long) {
        dsl.deleteFrom(USER_PROFILE)
            .where(USER_PROFILE.ID.eq(org.jooq.types.ULong.valueOf(id)))
            .execute()
    }

    companion object {
        private fun UserProfile.toRecord(): UserProfileRecord {
            val record = UserProfileRecord()

            id?.let { record.id = org.jooq.types.ULong.valueOf(it) }
            userId.let { record.userId = org.jooq.types.ULong.valueOf(it) }

            record.firstName = firstName
            record.lastName = lastName

            record.address = address
            record.phone = phone
            record.bio = bio

            return record
        }

        private fun UserProfileRecord.toModel(): UserProfile = UserProfile(
            id = id!!.toBigInteger()!!.longValueExact(),
            userId = userId!!.toBigInteger()!!.longValueExact(),
            firstName = firstName,
            lastName = lastName,
            address = address,
            phone = phone,
            bio = bio,
        )
    }
}