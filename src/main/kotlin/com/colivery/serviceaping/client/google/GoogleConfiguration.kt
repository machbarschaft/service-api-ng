package com.colivery.serviceaping.client.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "google")
class GoogleConfiguration() {
    lateinit var geocodeUrl: String
    lateinit var apiKey: String
}