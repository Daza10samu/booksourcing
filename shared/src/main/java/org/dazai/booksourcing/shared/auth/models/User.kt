package org.dazai.booksourcing.shared.auth.models

data class User(
    val id: Long?,
    val username: String,
    val password: String,
    val roles: Set<Role>,
    val isDisabled: Boolean = false,
) {
    override fun toString(): String {
        return "User(id=$id, username=$username, password=\"HIDDEN\", roles=$roles)"
    }
}