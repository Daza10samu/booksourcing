package org.dazai.booksourcing.auth.api.v0.dto
data class RoleDto(
    val role: Role,
) {
    enum class Role {
        ADMIN,
        USER,
    }
}