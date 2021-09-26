package com.colivery.serviceaping.rest.v1.resources

import com.colivery.serviceaping.persistence.Source
import java.util.*

class HelpSeekerResource(
    val id: UUID?,
    val enteredBy: UserResource,
    val fullName: String?,
    val phone: String?,
    val source: Source,
    val zipCode: String?,
    val streetNo: String?,
    val street: String?,
    val city: String?,
    val user: UserResource?
)
