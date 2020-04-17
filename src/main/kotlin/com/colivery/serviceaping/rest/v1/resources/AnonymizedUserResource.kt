package com.colivery.serviceaping.rest.v1.resources

data class AnonymizedUserResource(
        val firstName: String,
        val zipCode: String,
        val location: GeoPointResource
)
