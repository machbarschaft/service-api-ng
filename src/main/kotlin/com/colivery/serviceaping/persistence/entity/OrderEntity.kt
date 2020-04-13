package com.colivery.serviceaping.persistence.entity

import com.colivery.serviceaping.persistence.OrderStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`order`")
@EntityListeners(AuditingEntityListener::class)
data class OrderEntity(
        @ManyToOne(optional = false, fetch = FetchType.EAGER)
        val user: UserEntity,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, targetEntity =
        OrderItemEntity::class)
        @JoinColumn(name = "order_id")
        val items: List<OrderItemEntity>? = null,

        @ManyToOne(optional = true, fetch = FetchType.EAGER)
        val driverUser: UserEntity? = null,

        val hint: String? = null,

        val maxPrice: Int? = null,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        val status: OrderStatus
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}
