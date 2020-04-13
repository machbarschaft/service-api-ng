package com.colivery.serviceaping.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class DummyAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        val authHeader: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        return Mono.just(UsernamePasswordAuthenticationToken(authHeader, authHeader))
    }
}