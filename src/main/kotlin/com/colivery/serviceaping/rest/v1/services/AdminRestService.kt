package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.mapping.toUserResource
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.persistence.repository.UserRepository
import com.colivery.serviceaping.rest.v1.dto.user.PatchUserAdminDto
import com.colivery.serviceaping.rest.v1.resources.UserResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Transactional
@RequestMapping("/v1/admin", produces = [MediaType.APPLICATION_JSON_VALUE])
class AdminRestService(private val userRepository: UserRepository) {

    @PutMapping("/users/{email}")
    fun updateUserToAdmin(@PathVariable email: String): Mono<ResponseEntity<String>> {
        val user = userRepository.findByEmail(email)
                ?: return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("user $email does not exist"))

        user.role = UserEntity.Role.ADMIN
        userRepository.save(user)

        return Mono.just(ResponseEntity.status(HttpStatus.OK).build())
    }

    @PatchMapping("/users/{userId}")
    fun updateUserAdmin(@PathVariable userId: String, @RequestBody @Valid userPatch: PatchUserAdminDto,
                        response: ServerHttpResponse)
            : Mono<UserResource> {

        var user = userRepository.findByFirebaseUid(userId)
        if (user == null) {
            response.statusCode = HttpStatus.NOT_FOUND
            return Mono.empty()
        }

        user.role = UserEntity.Role.valueOf(userPatch.role!!)
        user = userRepository.save(user)

        return Mono.just(toUserResource(user))
    }
}
