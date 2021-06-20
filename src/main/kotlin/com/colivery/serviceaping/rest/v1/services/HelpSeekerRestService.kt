package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toHelpSeekerEntity
import com.colivery.serviceaping.mapping.toHelpSeekerResource
import com.colivery.serviceaping.persistence.repository.HelpSeekerRepository
import com.colivery.serviceaping.rest.v1.dto.`help-seeker`.CreateHelpSeekerDto
import com.colivery.serviceaping.rest.v1.exception.BadRequestException
import com.colivery.serviceaping.rest.v1.exception.NotFoundException
import com.colivery.serviceaping.rest.v1.resources.HelpSeekerResource
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
@Validated
@RequestMapping("/v1/help-seeker", produces = [MediaType.APPLICATION_JSON_VALUE])
class HelpSeekerRestService(
    private val helpSeekerRepository: HelpSeekerRepository
) {

    @PostMapping
    fun createHelpSeeker(
        @RequestBody helpSeeker: CreateHelpSeekerDto,
        authentication: Authentication
    ):
            Mono<HelpSeekerResource> {
        val errors: Errors = BeanPropertyBindingResult(helpSeeker, "help_seeker")

        if (errors.hasErrors()) {
            throw BadRequestException()
        }

        val helpSeekerEntity =
            this.helpSeekerRepository.save(toHelpSeekerEntity(helpSeeker, authentication.getUser()))

        val resource = toHelpSeekerResource(helpSeekerEntity)
        return Mono.just(resource)
    }

    @PutMapping("/{helpSeekerId}")
    @PreAuthorize("hasRole('ROLE_HOTLINE')")
    fun updateHelpSeeker(
        @PathVariable helpSeekerId: UUID,
        @RequestBody helpSeekerData: CreateHelpSeekerDto
    ): Mono<HelpSeekerResource> {
        val errors: Errors = BeanPropertyBindingResult(helpSeekerData, "help_seeker")

        if (errors.hasErrors()) {
            throw BadRequestException()
        }

        val currentEntry = this.helpSeekerRepository.findById(helpSeekerId).orElseThrow {
            NotFoundException()
        }

        currentEntry.phone = helpSeekerData.phone
        currentEntry.source = helpSeekerData.source
        currentEntry.fullName = helpSeekerData.fullName

        this.helpSeekerRepository.save(currentEntry)

        return Mono.just(toHelpSeekerResource(currentEntry))
    }

    @GetMapping("/search/{phoneNumber}")
    @PreAuthorize("hasRole('ROLE_HOTLINE')")
    fun searchHelpSeekerByPhone(@PathVariable phoneNumber: String) =
        Mono.justOrEmpty(this.helpSeekerRepository.findByPhone(phoneNumber)?.let {
            toHelpSeekerResource(it)
        })

}
