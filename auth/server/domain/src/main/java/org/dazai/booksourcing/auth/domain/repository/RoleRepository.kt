package org.dazai.booksourcing.auth.domain.repository

import org.dazai.booksourcing.shared.auth.models.Role

interface RoleRepository {
    fun getRoles(userId: Long): Set<Role>

    fun addRoles(userId: Long, roles: Set<Role>): Int

    fun removeRoles(userId: Long, roles: Set<Role>): Int
}