package com.colivery.serviceaping.rest.v1.resources

import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import com.neovisionaries.i18n.CountryCode

data class LocationResource(
        val street: String?,
        val streetNo: String?,
        val zipCode: String,
        val city: String,
        val countryCode: CountryCode,
        val location: GeoPointResource,
        val locationGeoHash: String
)