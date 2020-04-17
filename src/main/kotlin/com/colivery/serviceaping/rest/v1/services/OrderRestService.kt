package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.business.spatial.Distance
import com.colivery.serviceaping.dto.UserOrderAcceptedResponse
import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toOrderEntity
import com.colivery.serviceaping.mapping.toOrderItemEntity
import com.colivery.serviceaping.mapping.toOrderResource
import com.colivery.serviceaping.mapping.toUserResource
import com.colivery.serviceaping.persistence.repository.OrderItemRepository
import com.colivery.serviceaping.persistence.repository.OrderRepository
import com.colivery.serviceaping.rest.v1.dto.order.CreateOrderDto
import com.colivery.serviceaping.rest.v1.dto.order.UpdateOrderStatusDto
import com.colivery.serviceaping.rest.v1.resources.OrderResource
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.util.GeometricShapeFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/v1/order", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderRestService(
        private val orderRepository: OrderRepository,
        private val orderItemRepository: OrderItemRepository,
        private val geometryFactory: GeometryFactory
) {

    @PatchMapping("/{orderId}/status")
    fun updateOrderStatus(@PathVariable orderId: UUID, @RequestBody request:
    UpdateOrderStatusDto): ResponseEntity<Mono<Void>> {
        val order = this.orderRepository.findByIdOrNull(orderId)
        if (order === null) {
            return ResponseEntity.notFound()
                    .build()
        }

        order.status = request.status

        this.orderRepository.save(order)
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

    @GetMapping
    fun searchOrdersInRange(@RequestParam latitude: Double, @RequestParam longitude: Double,
                            @RequestParam range: Int): Flux<UserOrderAcceptedResponse> {
        var shapeFactory = GeometricShapeFactory(geometryFactory)
        //simply defining how many points the circle will have..
        shapeFactory.setNumPoints(32)
        shapeFactory.setCentre(Coordinate(longitude, latitude))
        val rangeInDegrees = Distance.coordinatesWhenTravelingInDirectionForDistance(
                com.colivery.serviceaping.business.spatial.Coordinate(latitude, longitude),
                (range.toFloat() / 1000.0).toFloat(), //range needs to be in Km
                0f).latitude - latitude //delta in latitude after walking range degrees at an angle of 0°
        //size is the diameter of the circle (in "coordinate degrees")..
        shapeFactory.setSize(2.0 * rangeInDegrees)

        return Flux.fromIterable(this.orderRepository.searchOrdersInRange(shapeFactory.createCircle())
                .map { order ->
                    UserOrderAcceptedResponse(
                            toOrderResource(order),
                            toUserResource(order.user)
                    )
                }
        )
    }
}
