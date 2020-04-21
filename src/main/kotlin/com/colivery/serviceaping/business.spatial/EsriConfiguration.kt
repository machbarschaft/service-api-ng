package com.colivery.serviceaping.business.spatial

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "esri")
data class EsriConfiguration(
        val url: String = " ",
        val findAddressesUri: String = " ",
        val token: String = " "
)