package com.colivery.serviceaping.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "mbs.mail.mailjet")
data class MailjetConfig(
    val apiKey: String,
    val apiSecret: String,
    val senderAddress: String,
    val senderName: String
)
