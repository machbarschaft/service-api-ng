package com.colivery.serviceaping.persistence.entity

import com.colivery.serviceaping.persistence.entity.OrderEntity
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

        @JoinColumn(name = "order_id")
        @ManyToOne(targetEntity = OrderEntity::class)
        var order: OrderEntity,

        @Column(nullable = false)
        var description: String,

        @CreatedDate
        var createdAt: LocalDateTime,

        @LastModifiedDate
        var updatedAt: LocalDateTime
)
