package com.colivery.serviceaping.rest.v1.resources

import com.colivery.serviceaping.persistence.OrderStatus
import com.colivery.serviceaping.persistence.Source
import java.time.LocalDateTime
import java.util.*

class OrderResource(
        val id: UUID?,
        val hint: String?,
        val maxPrice: Int?,
        val status: OrderStatus,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val items: Set<OrderItemResource>,
        val source: Source
)
