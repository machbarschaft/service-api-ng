package com.colivery.serviceaping.rest.v1.services

import com.colivery.serviceaping.persistence.PassbaseStatus
import com.colivery.serviceaping.util.PassbaseUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@Validated
@RequestMapping("/v1/passbase", produces = [MediaType.APPLICATION_JSON_VALUE])
class PassbaseRestService
    (
    @Value("\${passbase_api_key}")
    private val passbaseAPIKey: String
) {

    @GetMapping("/{id}")
    fun getVerified(@PathVariable id: String): Mono<PassbaseStatus> {
        kotlin.runCatching {
            val responseBody = PassbaseUtil.getPassbaseUserById(id, passbaseAPIKey).toFuture().get()!!
            if (responseBody.status == "processing") {
                return Mono.just(PassbaseStatus.PROCESSING)
            }
            return if(responseBody.score > 0.4){
                Mono.just(PassbaseStatus.SUCCESS)
            } else {
                Mono.just(PassbaseStatus.FAILED)
            }
        }
        return Mono.just(PassbaseStatus.PROCESSING)
    }
}