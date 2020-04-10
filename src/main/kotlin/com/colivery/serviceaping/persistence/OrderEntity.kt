package com.colivery.serviceaping.persistence

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "order")
data class OrderEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID,

        @ManyToOne(optional = false)
        var user: UserEntity,

        @Column(nullable = false)
        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        val items: List<OrderItemEntity>? = null,

        @ManyToOne
        var driverUser: UserEntity? = null,

        var hint: String? = null,

        var maxPrice: Int? = null,

        @Column(nullable = false)
        var status: OrderStatus,

        @CreatedDate
        var createdAt: LocalDateTime,

        @LastModifiedDate
        var updatedAt: LocalDateTime
)
