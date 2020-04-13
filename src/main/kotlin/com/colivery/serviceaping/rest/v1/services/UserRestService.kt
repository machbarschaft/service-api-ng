package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.dto.UserOrderAcceptedResponse
import com.colivery.serviceaping.dto.UserOrderResponse
import com.colivery.serviceaping.mapping.toOrderResource
import com.colivery.serviceaping.mapping.toUserResource
import com.colivery.serviceaping.persistence.repository.OrderRepository
import com.colivery.serviceaping.persistence.repository.UserRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/v1/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserRestService(
        val orderRepository: OrderRepository,
        val userRepository: UserRepository
) {

    @GetMapping("/orders")
    fun getOrdersForUser(): Flux<UserOrderResponse> {
        // TODO: Replace this by getting the user from the auth context
        val user = this.userRepository.findFirst()

        return Flux.fromIterable(this.orderRepository.findAllByUser(user)
                .map { order ->
                    UserOrderResponse(
                            toOrderResource(order),
                            order.driverUser?.let { toUserResource(it) }
                    )
                }
        )
    }

    @GetMapping("/orders-accepted")
    fun getDriverOrders(): Flux<UserOrderAcceptedResponse> {
        // TODO: Replace this by getting the user from the auth context
        val user = this.userRepository.findFirst()

        return Flux.fromIterable(this.orderRepository.findAllByDriverUser(user)
                .map { order ->
                    UserOrderAcceptedResponse(
                            toOrderResource(order),
                            toUserResource(order.user)
                    )
                }
        )
    }

    @DeleteMapping
    fun deleteOwnUser() {

    }

}
