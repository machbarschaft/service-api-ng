package com.colivery.serviceaping.client

import com.colivery.serviceaping.rest.v1.resources.LocationResource

interface AddressCandidate {
    val score: Int
    fun toLocationResource() : LocationResource
}