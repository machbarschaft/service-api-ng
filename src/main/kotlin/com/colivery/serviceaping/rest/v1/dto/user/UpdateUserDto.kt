package com.colivery.serviceaping.rest.v1.dto.user

import com.colivery.serviceaping.persistence.Source
import com.colivery.serviceaping.rest.v1.dto.App
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
data class UpdateUserDto(
        @field:NotEmpty(groups = [App::class])
        val firstName: String,
        @field:NotEmpty(groups = [App::class])
        val lastName: String,
        @field:NotEmpty(groups = [App::class])
        val street: String,
        @field:NotEmpty(groups = [App::class])
        val streetNo: String,
        @field:NotEmpty
        val zipCode: String,
        @field:NotEmpty
        val city: String,
        @field:NotEmpty(groups = [App::class])
        @field:Email(groups = [App::class])
        val email: String,
        @field:NotNull
        val location: GeoPointResource,
        @field:NotEmpty
        val phone: String,
        @field:NotNull
        val source: Source
)
