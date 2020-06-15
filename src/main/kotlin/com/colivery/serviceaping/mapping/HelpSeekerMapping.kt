package com.colivery.serviceaping.mapping

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
                enteredBy = adminUser
        )

fun toHelpSeekerResource(helpSeeker: HelpSeekerEntity) =
        HelpSeekerResource(
                id = helpSeeker.id,
                enteredBy = helpSeeker.enteredBy,
                fullName = helpSeeker.fullName,
                phone = helpSeeker.phone,
                source = helpSeeker.source,
                user = helpSeeker.user
        )
