package com.colivery.serviceaping.rest.v1.dto.order

import com.colivery.serviceaping.persistence.Source
import com.colivery.serviceaping.rest.v1.dto.App
import com.colivery.serviceaping.rest.v1.dto.Hotline
import org.springframework.validation.annotation.Validated
import javax.annotation.Nullable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
data class CreateOrderDto(
        @NotNull(groups = [Hotline::class])
        val hint: String?,

        @Nullable
        val maxPrice: Int?,

        @NotNull
        val userId: Int,

        @NotEmpty(groups = [App::class])
        val items: Set<CreateOrderItemDto>,

        @NotNull
        val source: Source
)
