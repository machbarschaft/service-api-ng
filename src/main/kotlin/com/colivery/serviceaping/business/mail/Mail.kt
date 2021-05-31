package com.colivery.serviceaping.business.mail

data class Mail(
    val recipient: String,
    val subject: String,
    val htmlBody: String
)
