package org.dazai.booksourcing.auth.domain.repository.impl

import org.dazai.booksourcing.auth.domain.db.Indexes
import org.dazai.booksourcing.auth.domain.db.Tables.USERS
import org.dazai.booksourcing.auth.domain.db.tables.records.UsersRecord
import org.dazai.booksourcing.shared.auth.models.User
import org.dazai.booksourcing.auth.domain.repository.UserRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.support.TransactionTemplate
import tech.ydb.jooq.impl.YdbDSLContextImpl

@Repository
class UserRepositoryImpl(
    private val dsl: YdbDSLContextImpl,
    private val roleRepository: RoleRepositoryImpl,
    private val transactionTemplate: TransactionTemplate,
) : UserRepository {
    override fun get(id: Long, forUpdate: Boolean): User? {
        val user = dsl.selectFrom(USERS)
            .where(USERS.ID.eq(id))
            .let {
                if (forUpdate) {
                    it.forUpdate()
                } else {
                    it
                }
            }
            .fetchOne()?.toModel()

        return user?.copy(roles = roleRepository.getRoles(id))
    }

    override fun findByUsername(username: String): User? {
        val user = dsl.selectFrom(USERS.useIndex(Indexes.USERS__DISABLED_USERNAME__IDX.name))
            .where(USERS.IS_DISABLED.isFalse)
            .and(USERS.USERNAME.eq(username))
            .fetchOne()?.toModel()

        return user?.copy(roles = roleRepository.getRoles(user.id!!))
    }

    override fun saveUser(user: User) {
        dsl.insertInto(USERS)
            .set(user.toRecord().apply { changed(USERS.ID, false) })
            .execute()
    }

    override fun updateUser(id: Long, user: User) {
        transactionTemplate.execute {
            val oldUser = get(id, false) ?: throw IllegalArgumentException("user not found")
            if (oldUser.username != user.username) {
                throw IllegalArgumentException("username cannot be changed")
            }

            dsl.upsertInto(USERS)
                .set(
                    user.copy(id = id).toRecord()
                )
        }
    }

    override fun getAllUsers(): List<User> {
        return dsl.selectFrom(USERS.useIndex(Indexes.USERS__DISABLED_USERNAME__IDX.name))
            .where(USERS.IS_DISABLED.isFalse)
            .fetch()
            .map { it.toModel().copy(roles = roleRepository.getRoles(it.id)) }
    }

    override fun disableUser(id: Long) {
        dsl.update(USERS)
            .set(USERS.IS_DISABLED, true)
            .where(USERS.ID.eq(id))
            .execute()
    }

    companion object {
        private fun User.toRecord(): UsersRecord = UsersRecord(
            id,
            username,
            password,
            isDisabled,
        )

        private fun UsersRecord.toModel(): User = User(
            id,
            username,
            password,
            emptySet(),
            isDisabled,
        )
    }
}