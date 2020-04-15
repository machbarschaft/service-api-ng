package com.colivery.serviceaping.security

import com.colivery.serviceaping.persistence.repository.UserRepository
import org.springframework.context.annotation.Profile
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import javax.transaction.Transactional

@Service
@Transactional
@Profile("!development")
class FirebaseUserDetailsService(
        private val userRepository: UserRepository
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        return when {
            username != null -> findInDatabase(username)
            else -> Mono.empty()
        }
    }

    private fun findInDatabase(username: String) =
            this.userRepository
                    .findByFirebaseUid(username)
                    ?.let {
                        Mono.just(User.withUsername(it.id.toString()).password("").roles("USER")
                                .build())
                    } ?: Mono.empty()
}
