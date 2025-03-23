package org.dazai.booksourcing.main.api.v0

import org.dazai.booksourcing.main.api.v0.dto.ExchangeRequestDto
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(
    value = ["/api/v0/exchange-requests"],
    produces = [APPLICATION_JSON_VALUE],
)
@RestController
interface ExchangeRequestApi {
    @GetMapping("/my")
    fun getMyExchangeRequests(): ExchangeRequestSplitDto

    @PutMapping("/{id}/accept")
    fun acceptExchangeRequest(@PathVariable id: Long): ResponseEntity<String>

    @PutMapping("/{id}/reject")
    fun rejectExchangeRequest(@PathVariable id: Long): ResponseEntity<String>

    @PutMapping("/{id}/complete")
    fun completeExchangeRequest(@PathVariable id: Long): ResponseEntity<String>

    data class ExchangeRequestSplitDto(
        private val incoming: List<ExchangeRequestDto>,
        private val outgoing: List<ExchangeRequestDto>,
    )
}