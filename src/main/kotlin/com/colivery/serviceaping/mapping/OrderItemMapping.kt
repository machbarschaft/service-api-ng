package com.colivery.serviceaping.mapping

import com.colivery.serviceaping.persistence.entity.OrderEntity
import com.colivery.serviceaping.persistence.entity.OrderItemEntity
import com.colivery.serviceaping.rest.v1.dto.order.CreateOrderItemDto
import com.colivery.serviceaping.rest.v1.resources.OrderItemResource

fun toItemSet(items: List<OrderItemEntity>?) =
        items?.map(::toItemResource)?.toSet() ?: emptySet()

fun toItemResource(item: OrderItemEntity) =
        OrderItemResource(
                id = item.id,
                description = item.description,
                createdAt = item.createdAt,
                updatedAt = item.updatedAt
        )

fun toOrderItemEntity(orderItemDto: CreateOrderItemDto, order: OrderEntity) =
        OrderItemEntity(
                description = orderItemDto.description,
                order = order
        )
