package org.dazai.booksourcing.main.api.v0

import org.dazai.booksourcing.main.api.v0.dto.UserProfileDto
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    value = ["/api/v0/user-profile"],
    produces = [APPLICATION_JSON_VALUE],
)
@RestController
interface UserProfileApi {
    @PostMapping
    fun createUserProfile(@RequestBody userProfileDto: UserProfileDto): UserProfileDto

    @GetMapping
    fun getUserProfile(): UserProfileDto

    @GetMapping("/{id}")
    fun getUserProfileById(@PathVariable id: Long): UserProfileDto

    @PutMapping
    fun updateUserProfile(@RequestBody userProfileDto: UserProfileDto): UserProfileDto
}