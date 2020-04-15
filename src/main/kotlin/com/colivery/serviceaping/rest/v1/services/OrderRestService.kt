package com.colivery.serviceaping.rest.v1.v1

import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toOrderEntity
import com.colivery.serviceaping.mapping.toOrderItemEntity
import com.colivery.serviceaping.mapping.toOrderResource
import com.colivery.serviceaping.persistence.repository.OrderItemRepository
import com.colivery.serviceaping.persistence.repository.OrderRepository
import com.colivery.serviceaping.rest.v1.dto.order.CreateOrderDto
import com.colivery.serviceaping.rest.v1.dto.order.UpdateOrderStatusDto
import com.colivery.serviceaping.rest.v1.resources.OrderResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/v1/order", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderRestService(
        private val orderRepository: OrderRepository,
        private val orderItemRepository: OrderItemRepository
) {

    @PatchMapping("/{orderId}/status")
    fun updateOrderStatus(@PathVariable orderId: UUID, @RequestBody request:
    UpdateOrderStatusDto): ResponseEntity<Mono<Void>> {
        val order = this.orderRepository.findByIdOrNull(orderId)
        if (order === null) {
            return ResponseEntity.notFound()
                    .build()
        }

        this.orderRepository.save(order.copy(status = request.status))
        return ResponseEntity.ok(Mono.empty())
    }

    @PostMapping
    fun createOrder(@RequestBody order: CreateOrderDto, authentication: Authentication): Mono<OrderResource> {
        val user = authentication.getUser()

        val orderEntity = this.orderRepository.save(toOrderEntity(order, user))
        this.orderItemRepository.saveAll(order.items.map {
            toOrderItemEntity(it, orderEntity)
        })

        return Mono.just(toOrderResource(orderEntity))
    }

    /*@GetMapping
    fun searchOrdersInRange(@RequestParam latitude: Double, @RequestParam longitude: Double,
                            @RequestParam range: Int): Flux<UserOrderAcceptedResponse> {

    }*/

}
