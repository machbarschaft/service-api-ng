package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.persistence.entity.InvitationCodeEntity
import com.colivery.serviceaping.persistence.repository.InvitationCodeRepository
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@Validated
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/v1/invitation-code", produces = [MediaType.APPLICATION_JSON_VALUE])
class InvitationCodeRestService(
    private val invitationCodeRepository: InvitationCodeRepository
) {

    @GetMapping
    fun getInvitationCodes(): Mono<List<String?>> {
        return Mono.just(invitationCodeRepository.findAllByUsedFalse().map { item -> item.code }.toList())
    }

    @PostMapping
    fun createInvitationCode(@NotNull @NotEmpty @RequestParam code: String): Mono<String> {
        val invitationCode = InvitationCodeEntity(code)
        invitationCodeRepository.save(invitationCode)
        return Mono.just(invitationCode.code!!)
    }
}