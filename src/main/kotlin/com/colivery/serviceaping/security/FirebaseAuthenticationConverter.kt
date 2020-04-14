package com.colivery.serviceaping.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Callable


class FirebaseAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {

        val token: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.let { getToken(it) }

        val fireBaseToken = token?.let { getFirebaseUser(it) } ?: Mono.empty()

        return fireBaseToken.map { UsernamePasswordAuthenticationToken(it.uid, token) }

    }

    private fun getToken(authHeader: String?) = authHeader?.let {
        when {
            it.startsWith("Bearer ") -> it.drop(7)
            else -> null
        }
    }

    private fun getFirebaseUser(token: String): Mono<FirebaseToken> {
        return Mono.fromCallable(Callable {
            FirebaseAuth.getInstance().verifyIdToken(token)
        }).publishOn(Schedulers.elastic())

    }

}