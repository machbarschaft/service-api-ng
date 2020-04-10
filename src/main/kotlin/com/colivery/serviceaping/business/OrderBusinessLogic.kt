package com.colivery.serviceaping.business

import com.colivery.serviceaping.persistence.OrderEntity
import com.colivery.serviceaping.dto.OrderDto

fun transformToDto(orderEntity: OrderEntity) =
        OrderDto(orderEntity.id, orderEntity.userId, orderEntity.driverUserId)
