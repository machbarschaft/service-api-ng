package com.colivery.serviceaping.client.google

import com.colivery.serviceaping.client.Address
import com.colivery.serviceaping.client.GeocodeClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.neovisionaries.i18n.CountryCode
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
@Primary
class GoogleGeocodeClient(val configuration: GoogleConfiguration, val objectMapper: ObjectMapper) : GeocodeClient {




    override fun findAddresses(zipCode: String, countryCode: CountryCode): Mono<Address> {

        val newMapper = objectMapper.copy()
        newMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE;

        val exchangeStrategies: ExchangeStrategies = ExchangeStrategies.builder()
                .codecs{it.defaultCodecs().jackson2JsonDecoder( Jackson2JsonDecoder(newMapper))}.build()

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(configuration.geocodeUrl).build()
                .get()
                .uri { uriBuilder ->
                    uriBuilder
                            .queryParam("key", configuration.apiKey)
                            .queryParam("address", zipCode.plus("+").plus(countryCode))
                            .build()
                }.retrieve()
                .onStatus(HttpStatus::isError, this::errorToException)
                .bodyToMono(GoogleGeocodeResponse::class.java)
                .flatMap { it.results.first().toMono() }
    }
}

