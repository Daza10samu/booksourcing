package org.dazai.booksourcing.main.application.service.exceptions

import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(org.springframework.http.HttpStatus.CONFLICT)
class InvalidUpdateProfileOperationException(
    message: String? = null,
    throwable: Throwable? = null,
): ApplicationException(
    message,
    throwable
)