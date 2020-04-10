package com.colivery.serviceaping.persistence

import javax.persistence.*

@Entity
@Table(name = "order")
data class OrderEntity(
        @Id
        var id: String,

        @Column(nullable = false)
        var userId: String,

        @Column(nullable = false)
        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        val items: List<OrderItemEntity>? = null,

        var driverUserId: String? = null
)
