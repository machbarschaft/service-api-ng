package com.colivery.serviceaping.mapping

import com.colivery.serviceaping.persistence.entity.HelpRequestEntity
import com.colivery.serviceaping.persistence.entity.HelpSeekerEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.rest.v1.dto.`help-request`.CreateHelpRequestDto
import com.colivery.serviceaping.rest.v1.resources.HelpRequestResource

fun toHelpRequestEntity(helpRequest: CreateHelpRequestDto, adminUser: UserEntity, helpSeeker: HelpSeekerEntity) =
        HelpRequestEntity(
                requestText = helpRequest.requestText,
                requestStatus = helpRequest.requestStatus,
                adminUser = adminUser,
                helpSeeker = helpSeeker
        )

fun toHelpRequestResource(helpRequest: HelpRequestEntity) =
        HelpRequestResource(
                id = helpRequest.id,
                createdAt =  helpRequest.createdAt,
                updatedAt =  helpRequest.updatedAt,
                requestStatus =  helpRequest.requestStatus,
                requestText =  helpRequest.requestText,
                adminUser =  toUserResource(helpRequest.adminUser),
                helpSeeker =  toHelpSeekerResource(helpRequest.helpSeeker),
                helper = toUserResourceNullable(helpRequest.helper)
        )
