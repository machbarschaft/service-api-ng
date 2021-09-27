package com.colivery.serviceaping.mapping

import com.colivery.serviceaping.persistence.Source
import com.colivery.serviceaping.persistence.entity.HelpSeekerEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.rest.v1.dto.`help-seeker`.CreateHelpSeekerDto
import com.colivery.serviceaping.rest.v1.resources.HelpSeekerResource

fun toHelpSeekerEntity(helpSeeker: CreateHelpSeekerDto, adminUser: UserEntity) =
    HelpSeekerEntity(
        // ToDo: is this necessary?
        user = null,
        fullName = helpSeeker.fullName,
        phone = helpSeeker.phone,
        source = helpSeeker.source,
        enteredBy = adminUser,
        street = helpSeeker.street,
        streetNumber = helpSeeker.streetNo,
        postCode = helpSeeker.zipCode,
        city = helpSeeker.city
    )

fun toHelpSeekerResource(helpSeeker: HelpSeekerEntity) =
    if ((helpSeeker.source == Source.HOTLINE)) {
        HelpSeekerResource(
            id = helpSeeker.id,
            enteredBy = toUserResource(helpSeeker.enteredBy),
            fullName = helpSeeker.fullName,
            phone = helpSeeker.phone,
            source = helpSeeker.source,
            zipCode = helpSeeker.enteredBy.zipCode,
            city = helpSeeker.enteredBy.city,
            street = helpSeeker.enteredBy.street,
            streetNo = helpSeeker.enteredBy.streetNo,
            user = helpSeeker.user?.let(::toUserResource)
        )
    } else {
        HelpSeekerResource(
            id = helpSeeker.id,
            enteredBy = toUserResource(helpSeeker.enteredBy),
            fullName = helpSeeker.fullName,
            phone = helpSeeker.phone,
            source = helpSeeker.source,
            zipCode = helpSeeker.postCode,
            city = helpSeeker.city,
            street = helpSeeker.street,
            streetNo = helpSeeker.streetNumber,
            user = helpSeeker.user?.let(::toUserResource)
        )
    }

