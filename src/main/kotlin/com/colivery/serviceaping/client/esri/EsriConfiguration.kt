package com.colivery.serviceaping.client.esri

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "esri")
class EsriConfiguration() {
    lateinit var findAddressesUrl: String
}