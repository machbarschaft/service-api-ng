package com.colivery.serviceaping.extensions

import com.colivery.serviceaping.business.spatial.Coordinate
import com.colivery.serviceaping.rest.v1.resources.GeoPointResource

fun Coordinate.toGeoPointResource(): GeoPointResource =
        GeoPointResource(
                latitude = this.latitude,
                longitude = this.longitude
        )