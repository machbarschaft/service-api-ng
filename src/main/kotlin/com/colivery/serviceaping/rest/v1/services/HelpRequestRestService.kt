package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toHelpRequestEntity
import com.colivery.serviceaping.mapping.toHelpRequestResource
import com.colivery.serviceaping.persistence.repository.HelpRequestRepository
import com.colivery.serviceaping.persistence.repository.HelpSeekerRepository
import com.colivery.serviceaping.persistence.repository.UserRepository
import com.colivery.serviceaping.rest.v1.dto.`help-request`.CreateHelpRequestDto
import com.colivery.serviceaping.rest.v1.dto.`help-request`.UpdateHelpRequestContentDto
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
        private val helpSeekerRepository: HelpSeekerRepository,
        private val userRepository: UserRepository
) {
    @PostMapping
    fun createHelpRequest(@RequestBody helpRequest: CreateHelpRequestDto, authentication: Authentication):
            Mono<ResponseEntity<HelpRequestResource>> {
        val errors: Errors = BeanPropertyBindingResult(helpRequest, "help_request")

        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
        }

        // added to check if the help seeker uid is valid
        val helpSeekerUuid: UUID
        try {
            helpSeekerUuid = UUID.fromString(helpRequest.helpSeeker)
        } catch (e: Exception) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
        }
        val helpSeeker = this.helpSeekerRepository.findById(helpSeekerUuid)
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
    fun updateHelpRequest(@RequestBody updateHelpRequestDto: UpdateHelpRequestContentDto,
                          @PathVariable("uuid") helpRequestId: UUID): Mono<ResponseEntity<HelpRequestResource>> {
        val helpRequest = this.helpRequestRepository.findById(helpRequestId)

        if (helpRequest.isEmpty) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
        }

        val helpRequestEntity = helpRequest.get()
        helpRequestEntity.requestText = updateHelpRequestDto.requestText
        val helpSeeker = helpRequestEntity.helpSeeker
        if (updateHelpRequestDto.helpSeeker.fullName != null) {
            helpSeeker.fullName = updateHelpRequestDto.helpSeeker.fullName
        }
        if (updateHelpRequestDto.helpSeeker.phone != null) {
            helpSeeker.phone = updateHelpRequestDto.helpSeeker.phone
        }
        helpRequestEntity.helpSeeker = this.helpSeekerRepository.save(helpSeeker)
        helpRequestEntity.requestStatus = updateHelpRequestDto.requestStatus

        // get helper
        if (updateHelpRequestDto.helper == null) {
            helpRequestEntity.helper = null
        } else {
            val helper = this.userRepository.findById(updateHelpRequestDto.helper!!)
            helpRequestEntity.helper = helper.orElse(null)
        }

        val helpRequestEntityStored = this.helpRequestRepository.save(helpRequestEntity)
        val resource = toHelpRequestResource(helpRequestEntityStored)

        return Mono.just(ResponseEntity.ok(resource))
    }

    @PutMapping("{uuid}/status")
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
