package com.colivery.serviceaping.client.esri

import com.colivery.serviceaping.business.spatial.GeoHash.Companion.encode
import com.colivery.serviceaping.client.AddressCandidate
import com.colivery.serviceaping.client.FindAddressesResponse
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import com.colivery.serviceaping.rest.v1.resources.LocationResource
import com.neovisionaries.i18n.CountryCode

class EsriFindAddressesResponse(override val candidates: List<EsriAddressCandidate>) : FindAddressesResponse

data class EsriAddressCandidate(val address: String, val esriLocation: EsriLocation, val esriAttributes: EsriAttributes, override val score: Int) : AddressCandidate {
    override fun toLocationResource(): LocationResource =
            LocationResource(
                    street = null,
                    streetNo = null,
                    city = esriAttributes.City,
                    zipCode = esriAttributes.Postal,
                    countryCode = CountryCode.getByAlpha3Code(esriAttributes.Country),
                    location = esriLocation.toGeoPointResource(),
                    locationGeoHash = encode(esriLocation.y, esriLocation.x)
            )
}

data class EsriLocation(val x: Double, val y: Double)

data class EsriAttributes(val City: String, val Postal: String, val Country: String)

fun EsriLocation.toGeoPointResource() = GeoPointResource(y, x)


