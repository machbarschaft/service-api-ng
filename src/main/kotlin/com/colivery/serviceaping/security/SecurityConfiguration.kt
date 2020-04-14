package com.colivery.serviceaping.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun filterChain(http: ServerHttpSecurity, authenticationWebFilter: AuthenticationWebFilter):
            SecurityWebFilterChain =
            http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).authorizeExchange()
                    // By default, we want everything to be authenticated (via Firebase)
                    .anyExchange().authenticated()
                    .and().build()

    @Bean
    @Autowired
    fun authenticationWebFilter(
            reactiveAuthenticationManager: ReactiveAuthenticationManager,
            serverAuthenticationConverter: ServerAuthenticationConverter) =
            AuthenticationWebFilter(reactiveAuthenticationManager).apply {
                setServerAuthenticationConverter(serverAuthenticationConverter)
            }

    @Bean
    @Profile("!development")
    fun fireBaseAuthenticationConverter(): FirebaseAuthenticationConverter {
        return FirebaseAuthenticationConverter()
    }

    @Bean
    @Profile("development")
    fun dummyAuthenticationConverter(): DummyAuthenticationConverter {
        return DummyAuthenticationConverter()
    }

    @Bean
    @Autowired
    fun reactiveAuthenticationManager(firebaseUserDetailsService: FirebaseUserDetailsService,
                                      passwordEncoder: PasswordEncoder): ReactiveAuthenticationManager? {
        val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(firebaseUserDetailsService)
        authenticationManager.setPasswordEncoder(passwordEncoder)
        return authenticationManager
    }


}
