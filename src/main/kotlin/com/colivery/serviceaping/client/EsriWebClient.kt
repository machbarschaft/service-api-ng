package com.colivery.serviceaping.client

import com.colivery.serviceaping.rest.v1.resources.LocationResource
import com.neovisionaries.i18n.CountryCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class EsriWebClient {

    @Value("\${esri.url}")
    private val esriUrl: String = " "

    @Value("\${esri.findAddresses.uri}")
    private val findAddressesUri: String = " "

    private val fixedAttributes = mapOf("f" to "json", "outFields" to "Match_addr,Addr_type")

    private val singleLineAttribute = "singleLine"

    fun findAddresses(zipCode: String, countryCode: CountryCode): LocationResource =
            WebClient.create(esriUrl)
                    .get()
                    .uri(findAddressesUri)
                    .attributes { attributes ->
                        attributes.putAll(fixedAttributes)
                        attributes.put(singleLineAttribute, zipCode.plus(" ").plus(countryCode))
                    }.retrieve()
                    

}