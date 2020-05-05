package com.colivery.serviceaping.client

import com.colivery.serviceaping.client.esri.EsriAddressCandidate

interface FindAddressesResponse {
    val candidates: List<EsriAddressCandidate>
}