package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.client.GeocodeClient
import com.colivery.serviceaping.rest.v1.resources.LocationResource
import com.neovisionaries.i18n.CountryCode
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/location", produces = [MediaType.APPLICATION_JSON_VALUE])

class LocationRestService(val geocodeClient: GeocodeClient) {

    @GetMapping
    fun geoCodeAddress(@RequestParam zipCode: String, @RequestParam countryCode: CountryCode): Mono<LocationResource> =
            geocodeClient.findAddresses(zipCode = zipCode, countryCode = countryCode)
                    .map { it.toLocationResource() }

}