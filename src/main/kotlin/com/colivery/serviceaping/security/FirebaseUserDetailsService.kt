package com.colivery.serviceaping.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FirebaseUserDetailsService : ReactiveUserDetailsService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun findByUsername(username: String?): Mono<UserDetails> {
        return when {
            username != null -> Mono.just(
                    User.withUsername(username).password(passwordEncoder.encode(username)).roles("USER").build())
            else -> Mono.empty()
        }
    }
}
