package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.business.spatial.Distance
import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toAnonymizedUserResource
import com.colivery.serviceaping.mapping.toOrderEntity
import com.colivery.serviceaping.mapping.toOrderItemEntity
import com.colivery.serviceaping.mapping.toOrderResource
import com.colivery.serviceaping.persistence.OrderStatus
import com.colivery.serviceaping.persistence.Source
import com.colivery.serviceaping.persistence.repository.OrderItemRepository
import com.colivery.serviceaping.persistence.repository.OrderRepository
import com.colivery.serviceaping.rest.v1.dto.App
import com.colivery.serviceaping.rest.v1.dto.Hotline
import com.colivery.serviceaping.rest.v1.dto.order.CreateOrderDto
import com.colivery.serviceaping.rest.v1.resources.OrderResource
import com.colivery.serviceaping.rest.v1.responses.UserOrderSearchResponse
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.util.GeometricShapeFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.SmartValidator
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min


@RestController
@Validated
@RequestMapping("/v1/order", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderRestService(
        private val orderRepository: OrderRepository,
        private val orderItemRepository: OrderItemRepository,
        private val geometryFactory: GeometryFactory,
        private val smartValidator: SmartValidator
) {

    @PatchMapping("/{orderId}/deliver")
    fun deliverOrder(@PathVariable orderId: UUID, authentication: Authentication):
            ResponseEntity<Mono<OrderResource>> {
        val user = authentication.getUser()
        val order = this.orderRepository.findByIdOrNull(orderId)
                ?: return ResponseEntity.notFound().build()

        if (order.status != OrderStatus.ACCEPTED || user != order.driverUser) {
            return ResponseEntity.badRequest().build()
        }

        order.status = OrderStatus.DELIVERED

        this.orderRepository.save(order)

        return ResponseEntity.ok(Mono.just(toOrderResource(order)))
    }

    @PatchMapping("/{orderId}/accept")
    fun acceptOrder(@PathVariable orderId: UUID, authentication: Authentication):
            ResponseEntity<Mono<OrderResource>> {
        val user = authentication.getUser()
        val order = this.orderRepository.findByIdOrNull(orderId)
                ?: return ResponseEntity.notFound().build()

        if (order.status != OrderStatus.TO_BE_DELIVERED) {
            return ResponseEntity.badRequest().build()
        }

        order.driverUser = user
        order.status = OrderStatus.ACCEPTED

        this.orderRepository.save(order)

        return ResponseEntity.ok(Mono.just(toOrderResource(order)))
    }

    @PatchMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: UUID, authentication: Authentication):
            ResponseEntity<Mono<OrderResource>> {
        val user = authentication.getUser()
        val order = this.orderRepository.findByIdOrNull(orderId)
                ?: return ResponseEntity.notFound().build()

        if ((order.status != OrderStatus.ACCEPTED && order.status != OrderStatus.TO_BE_DELIVERED)
                || user != order.user) {
            return ResponseEntity.badRequest().build()
        }

        order.driverUser = null
        order.status = OrderStatus.CONSUMER_CANCELLED

        this.orderRepository.save(order)

        return ResponseEntity.ok(Mono.just(toOrderResource(order)))
    }

    @PatchMapping("/{orderId}/abort")
    fun abortOrderDelivery(@PathVariable orderId: UUID, authentication: Authentication):
            ResponseEntity<Mono<OrderResource>> {
        val user = authentication.getUser()
        val order = this.orderRepository.findByIdOrNull(orderId)
                ?: return ResponseEntity.notFound().build()

        if (order.status != OrderStatus.ACCEPTED || user != order.driverUser) {
            return ResponseEntity.badRequest().build()
        }

        order.driverUser = null
        order.status = OrderStatus.TO_BE_DELIVERED

        this.orderRepository.save(order)

        return ResponseEntity.ok(Mono.just(toOrderResource(order)))
    }

    @PostMapping
    fun createOrder(@RequestBody order: CreateOrderDto, authentication: Authentication):
            ResponseEntity<Mono<OrderResource>> {

        val errors: Errors = BeanPropertyBindingResult(order, "order")

        if(order.source == Source.APP) {
            smartValidator.validate(order, errors, App::class.java)
        } else if(order.source == Source.HOTLINE) {
            smartValidator.validate(order, errors, Hotline::class.java)
        }

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }

        val user = authentication.getUser()

        val orderEntity = this.orderRepository.save(toOrderEntity(order, user))

        order.items?.let { this.orderItemRepository.saveAll(it.map {
            toOrderItemEntity(it, orderEntity)
        }) }

        return ResponseEntity.ok(Mono.just(toOrderResource(orderEntity)))
    }

    @GetMapping
    fun searchOrdersInRange(@RequestParam @Min(-90) @Max(90) latitude: Double,
                            @RequestParam @Min(-180) @Max(80) longitude: Double,
                            @RequestParam @Min(1) @Max(20) range: Int): Flux<UserOrderSearchResponse> {
        var shapeFactory = GeometricShapeFactory(geometryFactory)
        //simply defining how many points the circle will have..
        shapeFactory.setNumPoints(32)
        shapeFactory.setCentre(Coordinate(longitude, latitude))
        val rangeInDegrees = Distance.coordinatesWhenTravelingInDirectionForDistance(
                com.colivery.serviceaping.business.spatial.Coordinate(latitude, longitude),
                range.toFloat(), //range in Km!
                0f).latitude - latitude //delta in latitude after walking range KM at an angle of 0°
        //size is the diameter of the circle (in "coordinate degrees")..
        shapeFactory.setSize(2.0 * rangeInDegrees)

        return Flux.fromIterable(this.orderRepository.searchOpenOrdersInRange(shapeFactory.createCircle())
                .map { order ->
                    UserOrderSearchResponse(
                            order = toOrderResource(order),
                            user = toAnonymizedUserResource(order.user)
                    )
                }
        )
    }
}
