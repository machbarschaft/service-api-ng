package com.colivery.serviceaping.mapping

import com.colivery.serviceaping.persistence.OrderStatus
import com.colivery.serviceaping.persistence.entity.OrderEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.rest.v1.dto.CreateOrderDto
import com.colivery.serviceaping.rest.v1.resources.OrderResource

fun toOrderResource(order: OrderEntity) =
        OrderResource(
                id = order.id,
                hint = order.hint,
                maxPrice = order.maxPrice,
                status = order.status,
                createdAt = order.createdAt,
                updatedAt = order.updatedAt,
                items = toItemSet(order.items)
        )

fun toOrderEntity(order: CreateOrderDto, user: UserEntity) =
        OrderEntity(
                user = user,
                hint = order.hint,
                maxPrice = order.maxPrice,
                status = OrderStatus.TO_BE_DELIVERED
        )
