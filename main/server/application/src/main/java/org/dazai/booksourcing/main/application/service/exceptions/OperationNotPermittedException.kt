package org.dazai.booksourcing.main.application.service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class OperationNotPermittedException(
    message: String? = null,
    throwable: Throwable? = null,
) : ApplicationException(message, throwable)