package org.dazai.booksourcing.main.api.v0

import org.dazai.booksourcing.main.api.v0.dto.AcceptDto
import org.dazai.booksourcing.main.api.v0.dto.ExchangePublicationDto
import org.dazai.booksourcing.main.api.v0.dto.ExchangeRequestDto
import org.springframework.data.annotation.Id
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
    fun createPublication(@RequestBody exchangePublicationDto: ExchangePublicationDto): ExchangePublicationDto

    @GetMapping
    fun getAllPublications(): List<ExchangePublicationDto>

    @GetMapping("/my")
    fun getMyPublications(): List<ExchangePublicationDto>

    @GetMapping("/{id}")
    fun getPublicationById(@PathVariable id: Long): ExchangePublicationDto

    @PutMapping("/{id}")
    fun updatePublication(@PathVariable id: Long, exchangePublicationDto: ExchangePublicationDto): ExchangePublicationDto

    @DeleteMapping("{id}")
    fun deletePublication(@PathVariable id: Long): ResponseEntity<String>

    @PostMapping("/{id}/accept")
    fun acceptPublication(@PathVariable id: Long, @RequestBody acceptDto: AcceptDto): ExchangeRequestDto
}