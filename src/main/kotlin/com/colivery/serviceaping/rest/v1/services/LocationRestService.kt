package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.client.FindAdressesClient
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

class LocationRestService(val findAdressesClient: FindAdressesClient) {

    @GetMapping
    fun geoCodeAddress(@RequestParam zipCode: String, @RequestParam countryCode: CountryCode): Mono<LocationResource> =
            findAdressesClient.findAddresses(zipCode = zipCode, countryCode = countryCode)
                    .filter { candidate -> candidate.score == 100 }
                    .map { it.toLocationResource() }

}