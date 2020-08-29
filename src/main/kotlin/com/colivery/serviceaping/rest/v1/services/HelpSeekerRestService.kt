package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.extensions.getUser
import com.colivery.serviceaping.mapping.toHelpSeekerEntity
import com.colivery.serviceaping.mapping.toHelpSeekerResource
import com.colivery.serviceaping.mapping.toUserResource
import com.colivery.serviceaping.persistence.repository.HelpSeekerRepository
import com.colivery.serviceaping.rest.v1.dto.`help-seeker`.CreateHelpSeekerDto
import com.colivery.serviceaping.rest.v1.exception.BadRequestException
import com.colivery.serviceaping.rest.v1.resources.HelpSeekerResource
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@Validated
@RequestMapping("/v1/help-seeker", produces = [MediaType.APPLICATION_JSON_VALUE])
class HelpSeekerRestService(
        private val helpSeekerRepository: HelpSeekerRepository
) {

    @PostMapping
    fun createHelpSeeker(@RequestBody helpSeeker: CreateHelpSeekerDto, authentication: Authentication):
            Mono<HelpSeekerResource> {
        val errors: Errors = BeanPropertyBindingResult(helpSeeker, "help_seeker")

        if (errors.hasErrors()) {
            throw BadRequestException()
        }

        val helpSeekerEntity = this.helpSeekerRepository.save(toHelpSeekerEntity(helpSeeker, authentication.getUser()))

        val resource = toHelpSeekerResource(helpSeekerEntity)
        return Mono.just(resource)
    }

    @GetMapping("/search/{phoneNumber}")
    @PreAuthorize("hasRole('ROLE_HOTLINE')")
    fun searchHelpSeekerByPhone(@PathVariable phoneNumber: String) =
            Mono.justOrEmpty(this.helpSeekerRepository.findByPhone(phoneNumber)?.let {
                toHelpSeekerResource(it)
            })

}
