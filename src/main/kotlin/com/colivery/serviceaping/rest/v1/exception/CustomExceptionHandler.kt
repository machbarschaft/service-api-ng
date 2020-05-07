package com.colivery.serviceaping.rest.v1.exception

import com.colivery.serviceaping.rest.v1.resources.ErrorResource
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Mono
import javax.validation.ConstraintViolationException


@RestControllerAdvice
class CustomExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(CustomExceptionHandler::class.java)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: Exception): ResponseEntity<Mono<ErrorResource>> {
        //For ConstraintViolationException, we return a BAD REQUEST
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                //and remove the method name from the message..
                .body(Mono.just(ErrorResource(e.localizedMessage.substringAfter("."))))
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebExchangeBindException(e: Exception): ResponseEntity<Mono<ErrorResource>> {
        //For WebExchangeBindException, we return a BAD REQUEST
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Mono.just(ErrorResource(e.localizedMessage)))
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDeniedException(e: Exception): ResponseEntity<Mono<ErrorResource>> {
        //For AccessDeniedException, we return an UNAUTHORIZED
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Mono.empty())
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(e: Exception): ResponseEntity<Mono<ErrorResource>> {
        //For all other errors, we log
        logger.error("ERROR!", e)
        //and return a INTERNAL SERVER ERROR, with a small message, hiding the exception from the User
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(ErrorResource("We are sorry, there was an error at the server. " +
                        "Please contact the administrators.")))
    }
}