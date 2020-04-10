package com.colivery.serviceaping.persistence

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "order_item")
data class OrderItemEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID,

        @Column(nullable = false)
        var description: String,

        @CreatedDate
        var createdAt: LocalDateTime,

        @LastModifiedDate
        var updatedAt: LocalDateTime
)