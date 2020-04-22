package com.colivery.serviceaping.client

import com.neovisionaries.i18n.CountryCode
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.ClientResponse
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
            "outFields" to listOf(listOf(postal, city, country).joinToString(separator = ",")),
            "forStorage" to listOf("true")
    )

    //TODO error handling
    fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<AddressCandidate> {

        return WebClient.create(configuration.url)
                .get()
                .uri { uriBuilder ->
                    uriBuilder
                            .path(configuration.findAddressesUri)
                            .queryParams(LinkedMultiValueMap(fixedParameters))
                            .queryParam("token", configuration.token)
                            .queryParam("singleLine", zipCode.plus(" ").plus(countryCode))
                            .build()
                }.retrieve()
                .onStatus(HttpStatus::isError, this::errorToException)
                .bodyToMono(FindAddressesResponse::class.java)
                .flatMap { it.candidates.maxBy { it.score }?.toMono() }
    }

    fun errorToException(response: ClientResponse): Mono<EsriClientException> =
            response.bodyToMono(String::class.java)
                    .flatMap{ Mono.error<EsriClientException>(EsriClientException(it, response.statusCode().name)) }
}