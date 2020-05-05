package com.colivery.serviceaping.client.esri

import com.colivery.serviceaping.client.FindAdressesClient
import com.neovisionaries.i18n.CountryCode
import org.springframework.stereotype.Service

@Service
class EsriFindAddressesClient(override val configuration: EsriConfiguration) : FindAdressesClient {

    private val postal = "Postal"
    private val city = "City"
    private val country = "Country"

    override val fixedParameters = mapOf(
            "f" to listOf("json"),
            "outFields" to listOf(listOf(postal, city, country).joinToString(separator = ","))
    )

    override val responseClass: Class<EsriFindAddressesResponse> = EsriFindAddressesResponse::class.java

    override fun queryString(zipCode: String, countryCode: CountryCode): Pair<String, String> =
            Pair("singleLine", zipCode.plus(" ").plus(countryCode))

}
