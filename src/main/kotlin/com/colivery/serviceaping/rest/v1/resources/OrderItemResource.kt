package com.colivery.serviceaping.rest.v1.resources

import java.time.LocalDateTime
import java.util.*

data class OrderItemResource(
        val id: UUID?,
        val description: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
)
