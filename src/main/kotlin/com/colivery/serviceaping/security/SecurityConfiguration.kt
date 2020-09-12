package com.colivery.serviceaping.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ReactivePreAuthenticatedAuthenticationManager
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration(
        @Value("\${colivery.security.allowedOrigins}")
        private val corsAllowedOrigins: Array<String>
) {

    @Bean
    fun filterChain(http: ServerHttpSecurity, authenticationWebFilter: AuthenticationWebFilter):
            SecurityWebFilterChain =
            http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).authorizeExchange()
                    // Allow unauthorized access to createUser endpoint, since the user cant be
                    // validated against our own DB before creating. This endpoint performs own
                    // validation.
                    .pathMatchers(HttpMethod.GET, "/v1/location").permitAll()
                    .pathMatchers(HttpMethod.POST, "/v1/user").permitAll()
                    .pathMatchers(HttpMethod.GET, "/v2/api-docs",
                            "/swagger-resources/**",
                            "/swagger-ui.html**",
                            "/webjars/**",
                            "favicon.ico").permitAll()
                    // By default, we want everything to be authenticated (via Firebase)
                    .anyExchange().authenticated()
                    .and().csrf().disable()
                    .cors().configurationSource {
                        val corsConfig = CorsConfiguration()
                        // note: allowed origins have to be declared without / at the end
                        corsConfig.allowedOrigins = this.corsAllowedOrigins.toList()
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
    fun reactiveAuthenticationManager(reactiveUserDetailsService: ReactiveUserDetailsService) =
            ReactivePreAuthenticatedAuthenticationManager(reactiveUserDetailsService)

}
