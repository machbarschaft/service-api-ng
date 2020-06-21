package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toHelpRequestEntity
import com.colivery.serviceaping.mapping.toHelpRequestResource
import com.colivery.serviceaping.persistence.repository.HelpRequestRepository
import com.colivery.serviceaping.rest.v1.dto.`help-request`.CreateHelpRequestDto
import com.colivery.serviceaping.rest.v1.dto.`help-request`.UpdateHelpRequestStatusDto
import com.colivery.serviceaping.rest.v1.resources.HelpRequestResource
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
@RequestMapping("/v1/help-request", produces = [MediaType.APPLICATION_JSON_VALUE])
class HelpRequestRestService(
        private val helpRequestRepository: HelpRequestRepository
) {
    companion object {
        private val logger = LoggerFactory.getLogger(HelpRequestRestService::class.java)
    }

    @PostMapping
    fun createHelpRequest(@RequestBody helpRequest: CreateHelpRequestDto, authentication: Authentication):
            ResponseEntity<Mono<HelpRequestResource>> {
        val errors: Errors = BeanPropertyBindingResult(helpRequest, "help_request")

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.empty())
        }

        val adminUser = authentication.getUser()

        val helpRequestEntity = this.helpRequestRepository.save(toHelpRequestEntity(helpRequest, adminUser))

        return ResponseEntity.ok(Mono.just(toHelpRequestResource(helpRequestEntity)))
    }

    @GetMapping("{uuid}")
    fun getHelpRequest(@PathVariable("uuid") helpRequestId: String): ResponseEntity<Mono<HelpRequestResource>> {
        val uuid = UUID.fromString(helpRequestId) ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.empty())

        val helpRequest = this.helpRequestRepository.findById(uuid)

        if (helpRequest.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Mono.empty())
        }

        return ResponseEntity.ok(Mono.just(toHelpRequestResource(helpRequest.get())))
    }

    @GetMapping("/")
    fun getHelpRequestList(authentication: Authentication): Flux<HelpRequestResource> {
        return Flux.fromIterable(this.helpRequestRepository.findAllByAdminUser(authentication.getUser()))
                .map { helpRequestEntity -> toHelpRequestResource(helpRequestEntity) }
    }

    @PutMapping("{uuid}")
    fun updateHelpRequestStatus(@RequestBody updateHelpRequestStatusDto: UpdateHelpRequestStatusDto,
                                @PathVariable("uuid") helpRequestId: UUID): ResponseEntity<Mono<HelpRequestResource>> {
        val helpRequest = this.helpRequestRepository.findById(helpRequestId)

        if (helpRequest.isEmpty) {
            return ResponseEntity.notFound().build()
        }

        val helpRequestEntity = helpRequest.get()
        helpRequestEntity.requestStatus = updateHelpRequestStatusDto.status
        helpRequestEntity.updatedAt = LocalDateTime.now()

        val helpRequestEntityStored = this.helpRequestRepository.save(helpRequestEntity)

        return ResponseEntity.ok(Mono.just(toHelpRequestResource(helpRequestEntityStored)))
    }

}