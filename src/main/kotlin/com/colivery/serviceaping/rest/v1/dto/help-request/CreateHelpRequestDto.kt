package com.colivery.serviceaping.rest.v1.dto.`help-request`

import com.colivery.serviceaping.persistence.RequestStatus
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.NotNull

@Validated
class CreateHelpRequestDto (
    @field:NotNull
    val requestText: String,

    @field:NotNull
    val requestStatus: RequestStatus,

    @field:NotNull
    val adminUser: UUID
)
