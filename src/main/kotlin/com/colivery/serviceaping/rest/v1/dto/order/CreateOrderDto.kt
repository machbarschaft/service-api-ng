package com.colivery.serviceaping.rest.v1.dto.order

import org.springframework.validation.annotation.Validated
import javax.annotation.Nullable
import javax.validation.constraints.NotEmpty

@Validated
data class CreateOrderDto(
        @Nullable
        val hint: String?,

        @Nullable
        val maxPrice: Int?,

        @NotEmpty
        val items: Set<CreateOrderItemDto>
)
