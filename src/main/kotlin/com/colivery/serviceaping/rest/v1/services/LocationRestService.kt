package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import com.colivery.serviceaping.rest.v1.resources.LocationResource
import com.neovisionaries.i18n.CountryCode
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/location", produces = [MediaType.APPLICATION_JSON_VALUE])
class LocationRestService {

    @GetMapping
    fun geoCodeAddress(@RequestParam zipCode: String, @RequestParam countryCode: CountryCode): LocationResource =
            LocationResource(
                    zipCode = "",
                    countryCode = CountryCode.AC,
                    city = "",
                    locationGeoHash = "",
                    location = GeoPointResource(0.0, 0.0),
                    street = null,
                    streetNo = null
            )
}