package com.colivery.serviceaping.util

import org.springframework.http.HttpHeaders

internal fun extractBearerToken(headers: HttpHeaders) =
        headers.getFirst(HttpHeaders.AUTHORIZATION)
                ?.drop(7)
