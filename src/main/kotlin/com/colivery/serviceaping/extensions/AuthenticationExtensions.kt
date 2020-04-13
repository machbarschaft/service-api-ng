package com.colivery.serviceaping.extensions

import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

fun Authentication.getUserDetails() =
        this.principal as UserDetails
