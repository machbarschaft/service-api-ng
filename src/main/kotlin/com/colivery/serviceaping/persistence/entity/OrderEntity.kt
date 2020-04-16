package com.colivery.serviceaping.persistence.entity

import com.colivery.serviceaping.persistence.OrderStatus
import org.hibernate.annotations.Type
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
        var user: UserEntity,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, targetEntity =
        OrderItemEntity::class)
        @JoinColumn(name = "order_id")
        var items: List<OrderItemEntity>? = null,

        @ManyToOne(optional = true, fetch = FetchType.EAGER)
        var driverUser: UserEntity? = null,

        var hint: String? = null,

        var maxPrice: Int? = null,

        @Column(nullable = false)
        @Type(type = "com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType")
        @Enumerated(EnumType.STRING)
        var status: OrderStatus
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}
