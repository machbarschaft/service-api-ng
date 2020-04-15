package com.colivery.serviceaping.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration(
        @Value("\${colivery.frontend.url}")
        private val frontendUrl: String
) {

    @Bean
    fun filterChain(http: ServerHttpSecurity, authenticationWebFilter: AuthenticationWebFilter):
            SecurityWebFilterChain =
            http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).authorizeExchange()
                    // Allow unauthorized access to createUser endpoint, since the user cant be
                    // validated against our own DB before creating. This endpoint performs own
                    // validation.
                    .pathMatchers(HttpMethod.POST, "/v1/user").permitAll()
                    // By default, we want everything to be authenticated (via Firebase)
                    .anyExchange().authenticated()
                    .and().csrf().disable()
                    .cors().configurationSource {
                        val corsConfig = CorsConfiguration()
                        corsConfig.allowedOrigins = listOf(this.frontendUrl)
                        corsConfig.allowedHeaders = listOf("*")
                        corsConfig.allowedMethods = listOf("*")

                        corsConfig
                    }.and().build()

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

}
