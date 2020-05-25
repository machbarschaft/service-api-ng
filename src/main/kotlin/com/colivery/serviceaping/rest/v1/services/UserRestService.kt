package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.business.spatial.encodeGeoHash
import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.extensions.toGeoPoint
import com.colivery.serviceaping.mapping.toOrderResource
import com.colivery.serviceaping.mapping.toUserResource
import com.colivery.serviceaping.persistence.Source
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.persistence.repository.OrderRepository
import com.colivery.serviceaping.persistence.repository.UserRepository
import com.colivery.serviceaping.rest.v1.dto.App
import com.colivery.serviceaping.rest.v1.dto.Hotline
import com.colivery.serviceaping.rest.v1.dto.user.CreateUserDto
import com.colivery.serviceaping.rest.v1.dto.user.UpdateUserDto
import com.colivery.serviceaping.rest.v1.resources.UserResource
import com.colivery.serviceaping.rest.v1.responses.UserOrderAcceptedResponse
import com.colivery.serviceaping.rest.v1.responses.UserOrderResponse
import com.colivery.serviceaping.util.extractBearerToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@Transactional
@RequestMapping("/v1/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserRestService(
        private val orderRepository: OrderRepository,
        private val userRepository: UserRepository,
        private val firebaseAuth: FirebaseAuth,
        private val geometryFactory: GeometryFactory,
        private val smartValidator: SmartValidator
) {

    @GetMapping("/orders")
    fun getOrdersForUser(authentication: Authentication): Flux<UserOrderResponse> {
        val user = authentication.getUser()

        return Flux.fromIterable(this.orderRepository.findAllByUser(user)
                .map { order ->
                    UserOrderResponse(
                            toOrderResource(order),
                            order.driverUser?.let { toUserResource(it) }
                    )
                }
        )
    }

    @PutMapping
    fun updateOwnUser(@RequestBody updateUserDto: UpdateUserDto, authentication: Authentication):
            ResponseEntity<Mono<UserResource>> {

        val errors: Errors = BeanPropertyBindingResult(updateUserDto, "updateUserDto")

        if(updateUserDto.source == Source.APP) {
            smartValidator.validate(updateUserDto, errors, App::class.java)
        } else if(updateUserDto.source == Source.HOTLINE) {
            smartValidator.validate(updateUserDto, errors, Hotline::class.java)
        }

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }

        return ResponseEntity.ok(Mono.fromCallable {
            val user = authentication.getUser()
            user.firstName = updateUserDto.firstName
            user.lastName = updateUserDto.lastName
            user.street = updateUserDto.street
            user.streetNo = updateUserDto.streetNo
            user.zipCode = updateUserDto.zipCode
            user.city = updateUserDto.city
            user.phone = updateUserDto.phone
            user.locationGeoHash = encodeGeoHash(updateUserDto.location)
            user.location = updateUserDto.location.toGeoPoint(this.geometryFactory)

            this.userRepository.save(user)
        }.map(::toUserResource))
    }

    @GetMapping("/orders-accepted")
    fun getDriverOrders(authentication: Authentication): Flux<UserOrderAcceptedResponse> {
        val user = authentication.getUser()

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
    fun deleteOwnUser(authentication: Authentication) {
        val user = authentication.getUser()
        this.orderRepository.updateCreatedOrdersToConsumerCancelled(user)
        this.firebaseAuth.deleteUser(user.firebaseUid)
        this.userRepository.delete(user)
    }

    @PostMapping
    fun createUser(@RequestBody createUserDto: CreateUserDto, @RequestHeader headers: HttpHeaders):
            ResponseEntity<Mono<UserResource>> {
        val unauthorized = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build<Mono<UserResource>>()
        // Get the bearer token
        val token = extractBearerToken(headers)
                ?: return unauthorized

        val errors: Errors = BeanPropertyBindingResult(createUserDto, "createUserDto")

        if(createUserDto.source == Source.APP) {
            smartValidator.validate(createUserDto, errors, App::class.java)
        } else if(createUserDto.source == Source.HOTLINE) {
            smartValidator.validate(createUserDto, errors, Hotline::class.java)
        }

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }

        try {
            val firebaseToken = this.firebaseAuth.verifyIdToken(token)
            // First, make sure that the user wasnt created before
            if (this.userRepository.existsByFirebaseUid(firebaseToken.uid)) {
                return ResponseEntity.badRequest()
                        .build()
            }

            val user = this.userRepository.save(UserEntity(
                    firstName = createUserDto.firstName,
                    lastName = createUserDto.lastName,
                    street = createUserDto.street,
                    streetNo = createUserDto.streetNo,
                    zipCode = createUserDto.zipCode,
                    city = createUserDto.city,
                    email = createUserDto.email,
                    firebaseUid = firebaseToken.uid,
                    location = createUserDto.location.toGeoPoint(this.geometryFactory),
                    locationGeoHash = encodeGeoHash(createUserDto.location),
                    phone = createUserDto.phone,
                    source = createUserDto.source
            ))

            return ResponseEntity.ok(Mono.just(toUserResource(user)))
        } catch (exception: FirebaseAuthException) {
            return unauthorized
        }
    }

    @GetMapping
    fun getUser(authentication: Authentication) =
            Mono.just(toUserResource(authentication.getUser()))

}
