package com.colivery.serviceaping.security

import com.google.firebase.auth.FirebaseAuth
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class FirebaseAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {

        val authHeader: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        return when (
            val firebaseUser = getFirebaseUser(authHeader)) {
            null -> Mono.empty()
            else -> Mono.just(UsernamePasswordAuthenticationToken(firebaseUser.name, authHeader?.drop(7)))
        }
    }

    private fun isValidHeader(authHeader: String?) = authHeader != null && authHeader.startsWith(prefix = "Bearer ")

    private fun getFirebaseUser(authHeader: String?) = when {
        isValidHeader(authHeader) -> FirebaseAuth.getInstance().verifyIdToken(authHeader?.drop(7))
        else -> null
    }

}