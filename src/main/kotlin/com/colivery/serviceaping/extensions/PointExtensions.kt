package com.colivery.serviceaping.extensions

import com.colivery.serviceaping.rest.v1.resources.GeoPointResource
import org.locationtech.jts.geom.Point

fun Point.toGeoPointResource() =
        GeoPointResource(
                latitude = this.y,
                longitude = this.x
        )
