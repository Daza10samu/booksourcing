package org.dazai.booksourcing.auth.application.service

import org.dazai.booksourcing.shared.auth.models.Role
import org.dazai.booksourcing.shared.auth.models.User
import org.dazai.booksourcing.auth.domain.repository.RoleRepository
import org.dazai.booksourcing.auth.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return org.springframework.security.core.userdetails.User(user.username, user.password, emptyList())
    }

    fun getUser(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
    }

    fun saveUser(user: User) {
        userRepository.saveUser(user)
    }

    fun updateUser(userId: Long, user: User) {
        userRepository.updateUser(userId, user)
    }

    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    fun disableUser(userId: Long) {
        userRepository.disableUser(userId)
    }

    fun addRole(userId: Long, role: Role) {
        roleRepository.addRoles(
            userId,
            setOf(role)
        )
    }

    fun removeRole(userId: Long, role: Role) {
        roleRepository.removeRoles(
            userId,
            setOf(role)
        )
    }

    fun createUserModel(
        id: Long?,
        username: String,
        password: String,
    ): User = User(
        id,
        username,
        passwordEncoder.encode(password),
        setOf(Role.USER),
        false,
    )
}