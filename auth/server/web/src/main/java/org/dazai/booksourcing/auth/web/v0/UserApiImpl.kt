package org.dazai.booksourcing.auth.web.v0

import org.dazai.booksourcing.auth.api.v0.UserApi
import org.dazai.booksourcing.auth.api.v0.dto.JwtTokensDto
import org.dazai.booksourcing.auth.api.v0.dto.RoleDto
import org.dazai.booksourcing.auth.api.v0.dto.UserDto
import org.dazai.booksourcing.auth.application.service.AuthService
import org.dazai.booksourcing.auth.application.service.UserDetailsServiceImpl
import org.dazai.booksourcing.auth.application.service.models.JwtTokens
import org.dazai.booksourcing.shared.auth.models.JwtAuthentication
import org.dazai.booksourcing.shared.auth.models.User
import org.dazai.booksourcing.shared.auth.models.Role
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.slf4j.LoggerFactory

@Controller
class UserApiImpl(
    private val userDetailsServiceImpl: UserDetailsServiceImpl,
    private val authService: AuthService,
) : UserApi {
    override fun register(userDto: UserDto): ResponseEntity<Int> {
        LOG.info("Register $userDto")
        try {
            userDetailsServiceImpl.saveUser(userDto.toModel())
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.CONFLICT)
        }
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    override fun auth(userDto: UserDto): JwtTokensDto {
        LOG.info("Login $userDto")
        val jwt = authService.login(userDto.username, userDto.password)

        return jwt.toDto()
    }

    override fun refresh(refreshToken: String): JwtTokensDto {
        val jwt = authService.refresh(refreshToken)

        return jwt.toDto()
    }

    override fun me(): UserDto {
        val username = SecurityContextHolder.getContext().authentication.name
        LOG.info("Username $username")
        return userDetailsServiceImpl.getUser(username).toDto()
    }

    override fun disable(): ResponseEntity<String> {
        val username = SecurityContextHolder.getContext().authentication.name
        LOG.info("Disable $username")
        userDetailsServiceImpl.disableUser(userDetailsServiceImpl.getUser(username).id!!)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun dropAllSessions(): ResponseEntity<String> {
        val auth = SecurityContextHolder.getContext().authentication
        val id = when (auth) {
            is JwtAuthentication -> auth.userId
            else -> userDetailsServiceImpl.getUser(SecurityContextHolder.getContext().authentication.name).id!!
        }
        authService.revokeTokensByUserId(id)
        return ResponseEntity(
            HttpStatus.OK,
        )
    }

    override fun allUsers(): List<UserDto> {
        val username = SecurityContextHolder.getContext().authentication.name
        LOG.info("All users $username")
        return userDetailsServiceImpl.getAllUsers().map { it.toDto() }
    }

    override fun addRole(userId: Long, roleDto: RoleDto): ResponseEntity<String> {
        val username = SecurityContextHolder.getContext().authentication.name
        LOG.info("Adding role $roleDto to $username")
        userDetailsServiceImpl.addRole(userDetailsServiceImpl.getUser(username).id!!, Role.valueOf(roleDto.role.name))
        return ResponseEntity(HttpStatus.OK)
    }

    override fun removeRole(userId: Long, roleDto: RoleDto): ResponseEntity<String> {
        val username = SecurityContextHolder.getContext().authentication.name
        LOG.info("Removing role $roleDto to $username")
        userDetailsServiceImpl.removeRole(userDetailsServiceImpl.getUser(username).id!!, Role.valueOf(roleDto.role.name))
        return ResponseEntity(HttpStatus.OK)
    }

    private fun UserDto.toModel(): User = userDetailsServiceImpl.createUserModel(id, username, password)

    companion object {
        private val LOG = LoggerFactory.getLogger(UserApiImpl::class.java)

        private fun User.toDto(): UserDto = UserDto(
            id,
            username,
            password,
        )

        private fun JwtTokens.toDto(): JwtTokensDto = JwtTokensDto(accessToken, refreshToken)
    }
}