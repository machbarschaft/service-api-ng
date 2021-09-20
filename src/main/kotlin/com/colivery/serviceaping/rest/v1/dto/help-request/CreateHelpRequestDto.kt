package com.colivery.serviceaping.rest.v1.dto.`help-request`

import com.colivery.serviceaping.persistence.RequestStatus
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
class CreateHelpRequestDto (
    @field:NotNull
    val requestText: String,

    @field:NotNull
    val requestStatus: RequestStatus,

    @field:NotNull
    val helpSeeker: String,

    val location: GeoPointResource?
)
