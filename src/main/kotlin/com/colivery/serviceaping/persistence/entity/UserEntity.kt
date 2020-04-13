package com.colivery.serviceaping.persistence.entity

import org.locationtech.jts.geom.Point
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`user`")
@EntityListeners(AuditingEntityListener::class)
data class UserEntity(
        @Column(nullable = false)
        val firstName: String,

        @Column(nullable = false)
        val lastName: String,

        @Column(nullable = false)
        val street: String,

        @Column(nullable = false)
        val streetNo: String,

        @Column(nullable = false)
        val zipCode: String,

        @Column(nullable = false)
        val city: String,

        @Column(nullable = false)
        val email: String,

        @Column(nullable = false)
        val location: Point,

        @Column(nullable = false)
        val locationGeoHash: String,

        @Column(nullable = false)
        val phone: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}
