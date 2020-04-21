package com.colivery.serviceaping.rest.v1.responses

import com.colivery.serviceaping.rest.v1.resources.AnonymizedUserResource
import com.colivery.serviceaping.rest.v1.resources.OrderResource

data class UserOrderSearchResponse(
        val order: OrderResource,
        val user: AnonymizedUserResource
)