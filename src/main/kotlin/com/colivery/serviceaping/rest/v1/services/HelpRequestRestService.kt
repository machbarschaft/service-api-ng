package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toHelpRequestEntity
import com.colivery.serviceaping.mapping.toHelpRequestResource
import com.colivery.serviceaping.persistence.repository.HelpRequestRepository
import com.colivery.serviceaping.persistence.repository.HelpSeekerRepository
import com.colivery.serviceaping.rest.v1.dto.`help-request`.CreateHelpRequestDto
import com.colivery.serviceaping.rest.v1.dto.`help-request`.UpdateHelpRequestStatusDto
import com.colivery.serviceaping.rest.v1.exception.BadRequestException
import com.colivery.serviceaping.rest.v1.resources.HelpRequestResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
@Validated
@RequestMapping("/v1/help-request", produces = [MediaType.APPLICATION_JSON_VALUE])
class HelpRequestRestService(
        private val helpRequestRepository: HelpRequestRepository,
        private val helpSeekerRepository: HelpSeekerRepository
) {
    @PostMapping
    fun createHelpRequest(@RequestBody helpRequest: CreateHelpRequestDto, authentication: Authentication):
            Mono<ResponseEntity<HelpRequestResource>> {
        val errors: Errors = BeanPropertyBindingResult(helpRequest, "help_request")

        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
        }

        val helpSeeker = this.helpSeekerRepository.findById(UUID.fromString(helpRequest.helpSeeker))

        if (helpSeeker.isEmpty) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
        }

        val adminUser = authentication.getUser()
        val entity = toHelpRequestEntity(helpRequest, adminUser, helpSeeker.get())
        val helpRequestEntity = this.helpRequestRepository.save(entity)
        val resource = toHelpRequestResource(helpRequestEntity)

        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(resource))
    }

    @GetMapping("{uuid}")
    fun getHelpRequest(@PathVariable("uuid") helpRequestId: String): Mono<ResponseEntity<HelpRequestResource>> {
        val uuid = UUID.fromString(helpRequestId) ?: throw BadRequestException()

        val helpRequest = this.helpRequestRepository.findById(uuid)

        if (helpRequest.isEmpty) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
        }

        val resource = toHelpRequestResource(helpRequest.get())
        return Mono.just(ResponseEntity.ok(resource))
    }

    @GetMapping
    fun getHelpRequestList(): Flux<HelpRequestResource> {
        return Flux.fromIterable(this.helpRequestRepository.findAll())
                .map { helpRequestEntity -> toHelpRequestResource(helpRequestEntity) }
    }

    @PutMapping("{uuid}")
    fun updateHelpRequestStatus(@RequestBody updateHelpRequestStatusDto: UpdateHelpRequestStatusDto,
                                @PathVariable("uuid") helpRequestId: UUID): Mono<ResponseEntity<HelpRequestResource>> {
        val helpRequest = this.helpRequestRepository.findById(helpRequestId)

        if (helpRequest.isEmpty) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
        }

        val helpRequestEntity = helpRequest.get()
        helpRequestEntity.requestStatus = updateHelpRequestStatusDto.status
        helpRequestEntity.updatedAt = LocalDateTime.now()

        val helpRequestEntityStored = this.helpRequestRepository.save(helpRequestEntity)
        val resource = toHelpRequestResource(helpRequestEntityStored)

        return Mono.just(ResponseEntity.ok(resource))
    }

}
