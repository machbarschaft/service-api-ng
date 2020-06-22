package com.colivery.serviceaping.rest.v1.dto.`help-request`

import com.colivery.serviceaping.persistence.RequestStatus
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
data class UpdateHelpRequestStatusDto (
    @field:NotNull
    val status: RequestStatus
)
