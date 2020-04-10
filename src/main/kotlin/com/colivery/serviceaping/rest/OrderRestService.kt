package com.colivery.serviceaping.rest

import com.colivery.serviceaping.business.transformToDto
import com.colivery.serviceaping.persistence.OrderRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderRestService(
        private val orderRepository: OrderRepository
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllOrders() =
            this.orderRepository.findAll().map(::transformToDto)

}
