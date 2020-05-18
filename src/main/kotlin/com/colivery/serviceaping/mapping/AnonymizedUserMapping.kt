package com.colivery.serviceaping.mapping

import com.colivery.serviceaping.business.spatial.decodeGeoHash
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.rest.v1.resources.AnonymizedUserResource

fun toAnonymizedUserResource(user: UserEntity) =
        AnonymizedUserResource(
                id = user.id,
                firstName = user.firstName,
                zipCode = user.zipCode,
                city = user.city,
                locationGeoHash = user.locationGeoHash,
                centerOfLocationGeoHash = decodeGeoHash(user.locationGeoHash),
                source = user.source
        )