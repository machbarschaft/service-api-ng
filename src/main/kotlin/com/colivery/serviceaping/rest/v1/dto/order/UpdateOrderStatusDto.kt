package com.colivery.serviceaping.rest.v1.dto.order

import com.colivery.serviceaping.persistence.OrderStatus
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
data class UpdateOrderStatusDto(
        @field:NotNull
        val status: OrderStatus,

        @field:NotNull
        val userId: Int
)
