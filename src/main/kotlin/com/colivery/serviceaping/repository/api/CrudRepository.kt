package com.colivery.serviceaping.repository.api

import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CrudRepository<T> {

    fun findAll(): Flux<T>
    fun findById(id: String): Mono<T>

    fun create(element: T)
    fun update(element: T)
    fun delete(element: T)

}
