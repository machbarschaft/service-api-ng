package com.colivery.serviceaping.client

import com.colivery.serviceaping.client.esri.EsriAddressCandidate
import com.colivery.serviceaping.client.esri.EsriConfiguration
import com.colivery.serviceaping.client.esri.EsriFindAddressesResponse
import com.neovisionaries.i18n.CountryCode
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

interface FindAdressesClient {
    val configuration: EsriConfiguration
    val fixedParameters: Map<String, List<String>>

    fun queryString(zipCode: String, countryCode: CountryCode): Pair<String,String>
    val responseClass: Class<out FindAddressesResponse>

    //TODO error handling
    fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<EsriAddressCandidate> {

        val (queryKey, queryValue) = queryString(zipCode, countryCode)

        return WebClient.create(configuration.url)
                .get()
                .uri { uriBuilder ->
                    uriBuilder
                            .queryParams(LinkedMultiValueMap(fixedParameters))
                            .queryParam(queryKey, queryValue)
                            .build()
                }.retrieve()
                .onStatus(HttpStatus::isError, this::errorToException)
                .bodyToMono(responseClass)
                .flatMap { it.candidates.maxBy { it.score }?.toMono() }
    }

    fun errorToException(response: ClientResponse): Mono<FindAdressesClientException> =
            response.bodyToMono(String::class.java)
                    .flatMap{ Mono.error<FindAdressesClientException>(FindAdressesClientException(it, response.statusCode().name)) }
}