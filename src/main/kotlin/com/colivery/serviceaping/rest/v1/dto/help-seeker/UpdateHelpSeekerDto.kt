package com.colivery.serviceaping.rest.v1.dto.`help-seeker`

import org.springframework.validation.annotation.Validated

@Validated
class UpdateHelpSeekerDto(

        val fullName: String?,

        val phone: String?

)
