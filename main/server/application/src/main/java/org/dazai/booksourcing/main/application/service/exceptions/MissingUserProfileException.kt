package org.dazai.booksourcing.main.application.service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class MissingUserProfileException(
    message: String? = null,
    throwable: Throwable? = null,
): Exception(
    message,
    throwable
)