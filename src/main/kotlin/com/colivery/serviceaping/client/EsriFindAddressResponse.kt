package com.colivery.serviceaping.client

import com.colivery.serviceaping.business.spatial.GeoHash.Companion.encode
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import com.colivery.serviceaping.rest.v1.resources.LocationResource
import com.neovisionaries.i18n.CountryCode

class FindAddressesResponse(val candidates: List<AddressCandidate>)

data class AddressCandidate(val address: String, val location: Location, val attributes: Attributes, val score: Int)

data class Location(val x: Double, val y: Double)

data class Attributes(val city: String, val postal: String, val country: String)

fun Location.toGeoPointResource() = GeoPointResource(y, x)

fun AddressCandidate.toLocationResource() =
        LocationResource(
                street =  null,
                streetNo = null,
                city = attributes.city,
                zipCode = attributes.postal,
                countryCode = CountryCode.getByAlpha3Code(attributes.country),
                location = location.toGeoPointResource(),
                locationGeoHash = encode(location.y, location.x)
                )

