package com.colivery.serviceaping

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceApiNgApplication

fun main(args: Array<String>) {
    runApplication<ServiceApiNgApplication>(*args)
}
