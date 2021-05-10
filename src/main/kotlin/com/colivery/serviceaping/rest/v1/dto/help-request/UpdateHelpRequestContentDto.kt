package com.colivery.serviceaping.rest.v1.dto.`help-request`

import com.colivery.serviceaping.persistence.RequestStatus
import com.colivery.serviceaping.rest.v1.dto.`help-seeker`.UpdateHelpSeekerDto
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

@Validated
data class UpdateHelpRequestContentDto(
        @field:NotNull
        val requestText: String,

        @field:NotNull
        val requestStatus: RequestStatus,

        @field:NotNull
        val helpSeeker: UpdateHelpSeekerDto,
        // references to the user id
        @field:Null
        val helper: UUID? = null
)
