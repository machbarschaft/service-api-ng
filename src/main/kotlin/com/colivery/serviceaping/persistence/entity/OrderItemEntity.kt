package com.colivery.serviceaping.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "order_item")
@EntityListeners(AuditingEntityListener::class)
data class OrderItemEntity(
        @JoinColumn(name = "order_id")
        @ManyToOne(targetEntity = OrderEntity::class)
        val order: OrderEntity,

        @Column(nullable = false)
        val description: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}
