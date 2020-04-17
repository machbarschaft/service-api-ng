package com.colivery.serviceaping.mapping

import com.colivery.serviceaping.extensions.toGeoPointResource
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.rest.v1.resources.UserResource

fun toUserResource(user: UserEntity) =
        UserResource(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                street = user.street,
                streetNo = user.streetNo,
                zipCode = user.zipCode,
                city = user.city,
                email = user.email,
                location = user.location.toGeoPointResource(),
                locationGeoHash = user.locationGeoHash,
                phone = user.phone,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
        )
