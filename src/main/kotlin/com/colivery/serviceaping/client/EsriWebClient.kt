package com.colivery.serviceaping.client

import com.colivery.serviceaping.business.spatial.EsriConfiguration
import com.colivery.serviceaping.rest.v1.resources.LocationResource
import com.neovisionaries.i18n.CountryCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class EsriWebClient(val configuration: EsriConfiguration) {

    private val postal = "Postal"
    private val city = "City"
    private val country = "Country"

    private val fixedAttributes = mapOf(
            "f" to "json",
            "outFields" to listOf(postal, city, country).joinToString(separator = ","),
            "forStorage" to "true")

    public fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<AddressCandidate> {
        return WebClient.create(configuration.url)
                .get()
                .uri(configuration.findAddressesUri)
                .attributes { attributes ->
                    attributes.putAll(fixedAttributes)
                    attributes.put("token", configuration.token)
                    attributes.put("singleLine", zipCode.plus(" ").plus(countryCode))
                }.exchange()
                .flatMap { response -> response.bodyToMono(FindAddressesResponse::class.java) }
                .flatMap { response -> response.candidates.maxBy { it.score }?.toMono() }
    }


}