package com.colivery.serviceaping.extensions

import com.colivery.serviceaping.persistence.entity.UserEntity
import org.springframework.security.core.Authentication

fun Authentication.getUser() =
        this.credentials as UserEntity
