package com.colivery.serviceaping.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class RawPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence?): String {
        return rawPassword.toString()
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return rawPassword.toString() == encodedPassword
    }
}
