package com.colivery.serviceaping.security

import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ReactivePreAuthenticatedAuthenticationManager
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
                    // Allow unauthorized access to createUser endpoint, since the user cant be
                    // validated against our own DB before creating. This endpoint performs own
                    // validation.
                    .pathMatchers(HttpMethod.POST, "/v1/user").permitAll()
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
    @Autowired
    fun reactiveAuthenticationManager(firebaseUserDetailsService: FirebaseUserDetailsService) =
            ReactivePreAuthenticatedAuthenticationManager(firebaseUserDetailsService)

    @Bean
    fun firebaseAuth(): FirebaseAuth =
            FirebaseAuth.getInstance()

}
