package com.colivery.serviceaping.util

import org.springframework.web.context.request.ServletWebRequest

internal fun extractBearerToken(request: ServletWebRequest) =
        request.getHeader("Authorization")
                ?.drop(7)
