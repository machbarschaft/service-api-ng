package com.colivery.serviceaping.security

import com.colivery.serviceaping.persistence.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import javax.transaction.Transactional

@Transactional
@Profile("!development")
@Component
class FirebaseAuthenticationConverter(
        private val firebaseAuth: FirebaseAuth,
        private val userRepository: UserRepository
) : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        val token: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.let { getToken(it) }

        if (token === null) {
            return Mono.empty()
        }

        return this.getFirebaseUser(token)
                .flatMap {
                    Mono.justOrEmpty(this.userRepository.findByFirebaseUid(it.uid))
                }.map {
                    PreAuthenticatedAuthenticationToken(it.firebaseUid, it)
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
            Mono.justOrEmpty(this.firebaseAuth.verifyIdToken(token))
        } catch (exception: FirebaseAuthException) {
            Mono.empty()
        }
    }

}
