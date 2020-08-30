package com.colivery.serviceaping.rest.v1.dto.`help-request`

import com.colivery.serviceaping.rest.v1.dto.`help-seeker`.UpdateHelpSeekerDto
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
data class UpdateHelpRequestContentDto(
        @field:NotNull
        val requestText: String,

        @field:NotNull
        val helpSeeker: UpdateHelpSeekerDto
)
