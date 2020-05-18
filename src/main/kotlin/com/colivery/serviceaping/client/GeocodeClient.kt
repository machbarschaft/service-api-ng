package com.colivery.serviceaping.client

import com.neovisionaries.i18n.CountryCode
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

interface GeocodeClient {

    fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<Address>

    fun errorToException(response: ClientResponse): Mono<GeocodeClientException> =
            response.bodyToMono(String::class.java)
                    .flatMap { Mono.error<GeocodeClientException>(GeocodeClientException(it, response.statusCode().name)) }
}