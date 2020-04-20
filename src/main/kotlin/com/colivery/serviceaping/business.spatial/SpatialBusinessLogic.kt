package com.colivery.serviceaping.business.spatial

import com.colivery.serviceaping.extensions.toGeoPointResource
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource

// TODO: Refactor this away from static stuff and do real business logic functions
fun encodeGeoHash(geoPointResource: GeoPointResource) =
        GeoHash.encode(geoPointResource.latitude, geoPointResource.longitude)

// TODO: Refactor this away from static stuff and do real business logic functions
fun decodeGeoHash(geoHash: String) =
        GeoHash.decode(geohash = geoHash).toGeoPointResource()
