package com.colivery.serviceaping.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("colivery.firebase")
class FirebaseProperties {
    lateinit var apiUrl: String
    lateinit var keyLocation: String
    lateinit var projectId: String
}
