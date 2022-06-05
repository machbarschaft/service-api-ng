package com.colivery.serviceaping.util

import com.colivery.serviceaping.rest.v1.responses.PassbaseResponse
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class PassbaseUtil {
    companion object Passbase {
        fun getPassbaseUserById(id: String, passbaseAPIKey: String): Mono<PassbaseResponse> {
            return WebClient.create().get()
                .uri("https://api.passbase.com/verification/v1/identities/$id")
                .header("X-API-KEY", passbaseAPIKey)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(PassbaseResponse::class.java)
        }
    }
}