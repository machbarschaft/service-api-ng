package com.colivery.serviceaping.rest.v1.resources

import com.colivery.serviceaping.persistence.Source
import java.time.LocalDateTime
import java.util.*

data class UserResource(
        val id: UUID?,
        val firstName: String?,
        val lastName: String?,
        val street: String?,
        val streetNo: String?,
        val zipCode: String?,
        val city: String?,
        val email: String?,
        val location: GeoPointResource?,
        val locationGeoHash: String?,
        val phone: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val source: Source
)
