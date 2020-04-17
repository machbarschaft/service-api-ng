package com.colivery.serviceaping.business.spatial

import com.colivery.serviceaping.rest.v1.resources.GeoPointResource

// TODO: Refactor this away from static stuff and do real business logic functions
fun calculateGeoHash(geoPointResource: GeoPointResource) =
        GeoHash.encode(geoPointResource.latitude, geoPointResource.longitude)
