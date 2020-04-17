package com.colivery.serviceaping.rest.v1.dto.user

import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
data class UpdateUserDto(
        @NotEmpty
        val firstName: String,
        @NotEmpty
        val lastName: String,
        @NotEmpty
        val street: String,
        @NotEmpty
        val streetNo: String,
        @NotEmpty
        val zipCode: String,
        @NotEmpty
        val city: String,
        @NotNull
        val location: GeoPointResource,
        @NotEmpty
        val phone: String
)
