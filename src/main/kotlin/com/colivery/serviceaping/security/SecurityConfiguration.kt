package com.colivery.serviceaping.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
            http.authorizeExchange()
                    // By default, we want everything to be authenticated (via Firebase)
                    .anyExchange().authenticated()
                    .and().build()

}
