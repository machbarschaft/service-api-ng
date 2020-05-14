package com.colivery.serviceaping.rest.v1.dto.user

import com.colivery.serviceaping.persistence.Source
import com.colivery.serviceaping.rest.v1.dto.App
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
data class CreateUserDto(
        @NotEmpty(groups = [App::class])
        val firstName: String,
        @NotEmpty(groups = [App::class])
        val lastName: String,
        @NotEmpty(groups = [App::class])
        val street: String,
        @NotEmpty(groups = [App::class])
        val streetNo: String,
        @NotEmpty
        val zipCode: String,
        @NotEmpty
        val city: String,
        @NotEmpty(groups = [App::class])
        @Email(groups = [App::class])
        val email: String,
        @NotNull
        val location: GeoPointResource,
        @NotEmpty
        val phone: String,
        @NotNull
        val source: Source
)
