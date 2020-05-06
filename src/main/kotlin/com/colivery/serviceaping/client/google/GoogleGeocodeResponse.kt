package com.colivery.serviceaping.client.google

import com.colivery.serviceaping.business.spatial.GeoHash.Companion.encode
import com.colivery.serviceaping.client.Address
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import com.colivery.serviceaping.rest.v1.resources.LocationResource
import com.neovisionaries.i18n.CountryCode

class GoogleGeocodeResponse(val results: List<GoogleAddress>, val status: String)

data class GoogleAddress(
        val geometry: Geometry,
        val addressComponents: List<AddressComponent>
) : Address {

    fun cityName() = addressComponents.find { it.types.contains("locality") }?.longName.orEmpty()

    fun postalCode() = addressComponents.find { it.types.contains("postal_code") }?.longName.orEmpty()

    fun country() = addressComponents.find { it.types.contains("country") }?.shortName.orEmpty()

    override fun toLocationResource(): LocationResource =
            LocationResource(
                    street = null,
                    streetNo = null,
                    city = cityName(),
                    zipCode = postalCode(),
                    countryCode = CountryCode.getByAlpha2Code(country()),
                    location = geometry.location.toGeoPointResource(),
                    locationGeoHash = encode(geometry.location.lat, geometry.location.lng)
            )
}

data class Geometry(val location: Location)

data class Location(val lat: Double, val lng: Double)

data class AddressComponent(val longName: String, val shortName: String, val types: List<String>)

fun Location.toGeoPointResource() = GeoPointResource(latitude = lat, longitude = lng)