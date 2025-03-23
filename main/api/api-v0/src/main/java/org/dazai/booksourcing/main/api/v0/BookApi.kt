package org.dazai.booksourcing.main.api.v0

import org.dazai.booksourcing.main.api.v0.dto.BookDto
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(
    value = ["/api/v0/book"],
    produces = [APPLICATION_JSON_VALUE],
)
@RestController
interface BookApi {
    @PostMapping
    fun createBook(@RequestBody bookDto: BookDto): ResponseEntity<String>

    @GetMapping("/my")
    fun getMyBooks(): List<BookDto>

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): BookDto

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, bookDto: BookDto): BookDto
}