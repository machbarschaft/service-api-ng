package com.colivery.serviceaping.security

import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FirebaseUserDetailsService : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        return when {
            username != null -> Mono.just(
                    User.withUsername(username).password("").roles("USER").build())
            else -> Mono.empty()
        }
    }
}
