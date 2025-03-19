package org.dazai.booksourcing.main.api.v0

import org.dazai.booksourcing.main.api.v0.dto.ExchangePublicationDto
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(
    value = ["/api/v0/exchange-publications"],
    produces = [APPLICATION_JSON_VALUE],
)
@RestController
interface ExchangePublicationApi {
    @PostMapping
    fun createPublication(exchangePublicationDto: ExchangePublicationDto): ResponseEntity<String>

    @GetMapping
    fun getPublications(): List<ExchangePublicationDto>

    @GetMapping("/{id}")
    fun getPublicationById(@PathVariable id: Long): ExchangePublicationDto

    @PutMapping("/{id}")
    fun updatePublication(@PathVariable id: Long, exchangePublicationDto: ExchangePublicationDto): ExchangePublicationDto

    @DeleteMapping("{id}")
    fun deletePublication(@PathVariable id: Long): ResponseEntity<String>
}