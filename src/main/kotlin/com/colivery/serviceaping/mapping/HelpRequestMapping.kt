package com.colivery.serviceaping.mapping

import com.colivery.serviceaping.extensions.toGeoPoint
import com.colivery.serviceaping.extensions.toGeoPointResource
import com.colivery.serviceaping.persistence.entity.HelpRequestEntity
import com.colivery.serviceaping.persistence.entity.HelpSeekerEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.rest.v1.dto.`help-request`.CreateHelpRequestDto
import com.colivery.serviceaping.rest.v1.resources.HelpRequestResource
import org.locationtech.jts.geom.GeometryFactory

fun toHelpRequestEntity(helpRequest: CreateHelpRequestDto, adminUser: UserEntity, helpSeeker:
HelpSeekerEntity, geometryFactory: GeometryFactory) =
        HelpRequestEntity(
                requestText = helpRequest.requestText,
                requestStatus = helpRequest.requestStatus,
                adminUser = adminUser,
                helpSeeker = helpSeeker,
                location = helpRequest.location?.toGeoPoint(geometryFactory)
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
                helper = toUserResourceNullable(helpRequest.helper),
                location = (helpRequest.location
                        ?: helpRequest.helpSeeker.user?.location
                        ?: helpRequest.helpSeeker.enteredBy.location)?.toGeoPointResource()
        )
