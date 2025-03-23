package org.dazai.booksourcing.main.application.service.exceptions

abstract class ApplicationException(
    message: String? = null,
    throwable: Throwable? = null,
) : Exception(message, throwable)