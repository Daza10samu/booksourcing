package org.dazai.booksourcing.main.domain.repository

import org.dazai.booksourcing.main.domain.models.UserProfile

interface UserProfileRepository {
    fun findById(id: Long): UserProfile?
    fun findByUserId(userId: Long): UserProfile?
    fun save(userProfile: UserProfile): UserProfile
    fun update(userProfile: UserProfile): UserProfile
    fun delete(id: Long)
}