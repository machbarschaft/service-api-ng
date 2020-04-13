package com.colivery.serviceaping.dto

import com.colivery.serviceaping.rest.v1.resources.OrderResource
import com.colivery.serviceaping.rest.v1.resources.UserResource

data class UserOrderResponse(
        val order: OrderResource,
        val driver: UserResource?
)
