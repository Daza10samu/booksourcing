package org.dazai.booksourcing.auth.api.v0

import org.dazai.booksourcing.auth.api.v0.dto.JwtTokensDto
import org.dazai.booksourcing.auth.api.v0.dto.RoleDto
import org.dazai.booksourcing.auth.api.v0.dto.UserDto
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping(
    value = ["/api/v0/users"],
    produces = [APPLICATION_JSON_VALUE],
)
@RestController
interface UserApi {
    @PostMapping("/register")
    fun register(@RequestBody userDto: UserDto): ResponseEntity<Int>

    @PostMapping("/auth")
    fun auth(@RequestBody userDto: UserDto): JwtTokensDto

    @PostMapping("/refresh")
    fun refresh(@RequestBody refreshToken: String): JwtTokensDto

    @GetMapping("/me")
    fun me(): UserDto

    @GetMapping("/disable")
    fun disable(): ResponseEntity<String>

    @PutMapping("/dropAllSessions")
    fun dropAllSessions(): ResponseEntity<String>

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allUsers")
    fun allUsers(): List<UserDto>

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add_role")
    fun addRole(@RequestParam userId: Long, @RequestBody roleDto: RoleDto): ResponseEntity<String>

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/remove_role")
    fun removeRole(@RequestParam userId: Long, @RequestBody roleDto: RoleDto): ResponseEntity<String>
}