package org.dazai.booksourcing.auth.domain.repository

import org.dazai.booksourcing.shared.auth.models.User

interface UserRepository {
    fun get(id: Long, forUpdate: Boolean = false): User?
    fun findByUsername(username: String): User?
    fun saveUser(user: User)
    fun updateUser(id: Long, user: User)
    fun getAllUsers(): List<User>
    fun disableUser(id: Long)
}