package com.colivery.serviceaping

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
class ServiceApiNgApplication

fun main(args: Array<String>) {
    runApplication<ServiceApiNgApplication>(*args)
}
