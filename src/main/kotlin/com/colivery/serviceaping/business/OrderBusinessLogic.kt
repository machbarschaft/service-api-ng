package com.colivery.serviceaping.business

import com.colivery.serviceaping.dto.OrderDto
import com.colivery.serviceaping.persistence.entity.OrderEntity

fun transformToDto(orderEntity: OrderEntity) =
        OrderDto(orderEntity.id.toString(), orderEntity.user.id.toString(), orderEntity.driverUser?.id.toString())
