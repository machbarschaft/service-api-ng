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

        val token: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.let { getToken(it) }

        return when (val firebaseUser = token?.let { getFirebaseUser(it) }) {
            null -> Mono.empty()
            else -> Mono.just(UsernamePasswordAuthenticationToken(firebaseUser.name, token))
        }
    }

    private fun getToken(authHeader: String?) = authHeader?.let {
        when {
            it.startsWith("Bearer ") -> it.drop(    7)
            else -> null
        }
    }

    private fun getFirebaseUser(token: String?) = token?.let {
        FirebaseAuth.getInstance().verifyIdToken(it)
    }

}