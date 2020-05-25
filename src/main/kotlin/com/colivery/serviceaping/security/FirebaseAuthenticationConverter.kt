package com.colivery.serviceaping.security

import com.colivery.serviceaping.persistence.repository.UserRepository
import com.colivery.serviceaping.util.extractBearerToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${chatbot.api-key}")
    lateinit var apiKey: String

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return if (isFirebaseAuthenticationRequest(exchange)) {
            getFirebaseUserAuthentication(exchange)
        } else {
            getApiUserAuthentication(exchange)
        }
    }

    private fun isFirebaseAuthenticationRequest(exchange: ServerWebExchange): Boolean =
            exchange.request.headers
                    .getFirst(HttpHeaders.AUTHORIZATION)
                    .orEmpty()
                    .startsWith("bearer-")

    private fun getFirebaseUserAuthentication(exchange: ServerWebExchange): Mono<Authentication> {
        val token: String? = extractBearerToken(exchange.request.headers)

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

    private fun getApiUserAuthentication(exchange: ServerWebExchange?): Mono<Authentication> {

        val key: String? = exchange?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION)
        val firebaseUid: String? = exchange?.request?.headers?.getFirst("firebase-uid")

        if (key === null || firebaseUid === null || key != apiKey) {
            return Mono.empty()
        }

        return Mono.justOrEmpty(userRepository.findByFirebaseUid(firebaseUid))
                .map {
                    PreAuthenticatedAuthenticationToken(it.firebaseUid, it)
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
