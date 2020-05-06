package com.colivery.serviceaping.client

import com.colivery.serviceaping.client.esri.EsriAddress
import com.colivery.serviceaping.client.esri.EsriConfiguration
import com.neovisionaries.i18n.CountryCode
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

interface GeocodeClient {

    fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<Address>

    fun errorToException(response: ClientResponse): Mono<GeocodeClientException> =
            response.bodyToMono(String::class.java)
                    .flatMap{ Mono.error<GeocodeClientException>(GeocodeClientException(it, response.statusCode().name)) }
}