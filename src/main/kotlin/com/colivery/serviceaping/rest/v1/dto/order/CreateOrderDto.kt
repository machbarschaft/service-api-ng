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
        @field:NotNull(groups = [Hotline::class])
        val hint: String?,

        @field:Nullable
        val maxPrice: Int?,

        @field:NotNull
        val userId: Int,

        @field:NotEmpty(groups = [App::class])
        val items: Set<CreateOrderItemDto>,

        @field:NotNull
        val source: Source
)
