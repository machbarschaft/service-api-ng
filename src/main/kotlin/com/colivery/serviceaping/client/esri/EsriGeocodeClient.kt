package com.colivery.serviceaping.client.esri

import com.colivery.serviceaping.client.Address
import com.colivery.serviceaping.client.GeocodeClient
import com.neovisionaries.i18n.CountryCode
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
@Profile("esri_api")
class EsriGeocodeClient(val configuration: EsriConfiguration) : GeocodeClient {

    private val postal = "Postal"
    private val city = "City"
    private val country = "Country"

    val fixedParameters = mapOf(
            "f" to listOf("json"),
            "outFields" to listOf(listOf(postal, city, country).joinToString(separator = ","))
    )

    override fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<Address> {

            return WebClient.create(configuration.findAddressesUrl)
                    .get()
                    .uri { uriBuilder ->
                        uriBuilder
                                .queryParams(LinkedMultiValueMap(fixedParameters))
                                .queryParam("singleLine", zipCode.plus(" ").plus(countryCode))
                                .build()
                    }.retrieve()
                    .onStatus(HttpStatus::isError, this::errorToException)
                    .bodyToMono(EsriGeocodeResponse::class.java)
                    .flatMap { it.candidates.maxBy { it.score }?.toMono() }
    }

}
