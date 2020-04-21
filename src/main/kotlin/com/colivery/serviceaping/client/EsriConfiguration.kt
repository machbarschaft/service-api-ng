package com.colivery.serviceaping.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "esri")
class EsriConfiguration() {
    lateinit var url: String
    lateinit var findAddressesUri: String
    lateinit var token: String
}