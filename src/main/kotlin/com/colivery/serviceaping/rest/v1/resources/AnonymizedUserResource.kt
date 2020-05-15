package com.colivery.serviceaping.rest.v1.resources

import com.colivery.serviceaping.persistence.Source
import java.util.*

data class AnonymizedUserResource(
        val id: UUID?,
        val firstName: String?,
        val zipCode: String,
        val city: String,
        val locationGeoHash: String,
        val centerOfLocationGeoHash: GeoPointResource,
        val source: Source
)
