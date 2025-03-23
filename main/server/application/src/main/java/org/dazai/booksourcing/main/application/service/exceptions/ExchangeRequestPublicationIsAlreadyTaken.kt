package org.dazai.booksourcing.main.application.service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class ExchangeRequestPublicationIsAlreadyTaken(
    message: String? = null,
    cause: Throwable? = null,
) : ApplicationException(message, cause)