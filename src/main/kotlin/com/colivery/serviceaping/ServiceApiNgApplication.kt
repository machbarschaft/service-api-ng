package com.colivery.serviceaping

import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@Configuration
class ServiceApiNgApplication {
    @Bean
    fun geometryFactory() =
            GeometryFactory(PrecisionModel(PrecisionModel.FLOATING), 4326)
}

fun main(args: Array<String>) {
    runApplication<ServiceApiNgApplication>(*args)
}
