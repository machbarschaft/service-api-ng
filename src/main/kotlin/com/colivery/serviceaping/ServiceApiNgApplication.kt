package com.colivery.serviceaping

import com.colivery.serviceaping.configuration.FirebaseProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(FirebaseProperties::class)
class ServiceApiNgApplication

fun main(args: Array<String>) {
    runApplication<ServiceApiNgApplication>(*args)
}
