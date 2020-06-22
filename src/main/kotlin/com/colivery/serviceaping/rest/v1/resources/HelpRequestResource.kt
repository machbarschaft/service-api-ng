package com.colivery.serviceaping.rest.v1.resources

import com.colivery.serviceaping.persistence.RequestStatus
import java.time.LocalDateTime
import java.util.*

class HelpRequestResource (
    val id: UUID?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val requestStatus: RequestStatus,
    val requestText: String,
    val adminUser: UserResource,
    val helpSeeker: HelpSeekerResource
)
