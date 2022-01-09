package com.colivery.serviceaping.rest.v1.dto.`help-request`

import com.colivery.serviceaping.persistence.RequestStatus
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
data class UpdateHelpRequestDto (
    @field:NotNull
    val postCode: String,
    @field:NotNull
    val city: String,
    @field:NotNull
    val street: String,
    @field:NotNull
    val streetNumber: String,
    @field:NotNull
    val phone: String,
    @field:NotNull
    val firstName: String,
    @field:NotNull
    val lastName: String,

    @field:NotNull
    val status: RequestStatus,
    @field:NotNull
    val requestText: String,

    val location: GeoPointResource?
)