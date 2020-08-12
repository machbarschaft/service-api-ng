package com.colivery.serviceaping.rest.v1.dto.`help-seeker`

import com.colivery.serviceaping.persistence.Source
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
class CreateHelpSeekerDto(

        val fullName: String,

        val phone: String,

        @field:NotNull
        val source: Source

)
