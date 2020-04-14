package com.colivery.serviceaping.rest

import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserRestService {

    @GetMapping
    fun test(auth: Authentication): Mono<String> {
        return Mono.just(auth.toString())
    }

}
