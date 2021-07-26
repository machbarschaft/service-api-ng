package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.persistence.repository.HelpRequestRepository
import com.colivery.serviceaping.persistence.repository.HelpSeekerRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/cronjobs")
class CronjobController(
    @Value("\${machbarschaft.cronjobSecret}")
    private val cronjobSecret: String,

    private val helpRequestRepository: HelpRequestRepository,
    private val helpSeekerRepository: HelpSeekerRepository
) {

    private fun assertCronjobAccess(authorizationHeader: String) {
        if (authorizationHeader != cronjobSecret) {
            throw SecurityException("Invalid cronjob authorization")
        }
    }

    @GetMapping("/cleanup/helpRequests")
    fun deleteOldHelpRequests(@RequestHeader(name = "X-Cronjob-Auth") authorizationHeader: String) {
        this.assertCronjobAccess(authorizationHeader)

        this.helpRequestRepository.findAllByUpdatedAtBeforeAndInactive(
            LocalDateTime.now()
                .minusDays(30)
        ).forEach(this.helpRequestRepository::delete)
    }

    @GetMapping("/cleanup/helpSeekers")
    fun deleteOldHelpSeekers() {
        // TODO: still open questions (to be solved in Sprint Review 2021/07/26
    }

}
