package com.colivery.serviceaping.client

import com.neovisionaries.i18n.CountryCode
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class EsriWebClient(val configuration: EsriConfiguration) {

    private val postal = "Postal"
    private val city = "City"
    private val country = "Country"

    private val fixedParameters = mapOf(
            "f" to listOf("json"),
            "outFields" to listOf(listOf(postal, city, country).joinToString(separator = ","))
            // "forStorage" to listOf("true")
    )

    public fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<AddressCandidate> {

        return WebClient.create(configuration.url)
                .get()
                .uri { uriBuilder -> uriBuilder
                        .path(configuration.findAddressesUri)
                        .queryParams(LinkedMultiValueMap(fixedParameters))
                        .queryParam("singleLine", zipCode.plus(" ").plus(countryCode))
                        .build()
                }.exchange()
                .flatMap { it.bodyToMono(FindAddressesResponse::class.java) }
                .flatMap { it.candidates.maxBy { it.score }?.toMono() }
    }
}