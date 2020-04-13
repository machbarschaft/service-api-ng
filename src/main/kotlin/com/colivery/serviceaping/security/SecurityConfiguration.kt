package com.colivery.serviceaping.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter


@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Autowired
    private lateinit var firebaseUserDetailsService: FirebaseUserDetailsService

    @Bean
    fun filterChain(http: ServerHttpSecurity, authenticationManager: ReactiveAuthenticationManager):
            SecurityWebFilterChain =
            http.addFilterAt(authenticationWebFilter(authenticationManager),SecurityWebFiltersOrder.AUTHENTICATION).authorizeExchange()
                    // By default, we want everything to be authenticated (via Firebase)
                    .anyExchange().authenticated()
                    .and().build()

    private fun authenticationWebFilter(reactiveAuthenticationManager: ReactiveAuthenticationManager) =
            AuthenticationWebFilter(reactiveAuthenticationManager).apply {
                setServerAuthenticationConverter(FirebaseAuthenticationConverter())
            }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager? {
        return UserDetailsRepositoryReactiveAuthenticationManager(firebaseUserDetailsService)
    }


}
