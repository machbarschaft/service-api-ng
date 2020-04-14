package com.colivery.serviceaping.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class FirebaseAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {

        val token: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.let { getToken(it) }

        return when (token) {
            null -> Mono.empty()
            else -> getFirebaseUser(token)
        }.map {
            val authenticationContext = UsernamePasswordAuthenticationToken(
                    it.uid,
                    "",
                    setOf(SimpleGrantedAuthority("USER"))
            )

            authenticationContext
        }

    }

    private fun getToken(authHeader: String?) =
            authHeader?.let {
                when {
                    it.startsWith("Bearer ") -> it.drop(7)
                    else -> null
                }
            }

    private fun getFirebaseUser(token: String): Mono<FirebaseToken> {
        return try {
            Mono.just(FirebaseAuth.getInstance().verifyIdToken(token))
        } catch (exception: FirebaseAuthException) {
            Mono.empty()
        }
    }

}
