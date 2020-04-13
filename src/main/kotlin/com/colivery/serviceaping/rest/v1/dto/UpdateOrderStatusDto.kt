package com.colivery.serviceaping.rest.v1.dto

import com.colivery.serviceaping.persistence.OrderStatus
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
data class UpdateOrderStatusDto(
        @NotNull
        val status: OrderStatus
)
