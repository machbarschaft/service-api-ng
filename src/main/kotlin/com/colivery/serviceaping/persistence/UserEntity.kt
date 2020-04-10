package com.colivery.serviceaping.persistence

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.geo.Point
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user")
data class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID? = null,

        @Column(nullable = false)
        var firstName: String,

        @Column(nullable = false)
        var lastName: String,

        @Column(nullable = false)
        var street: String,

        @Column(nullable = false)
        var streetNo: String,

        @Column(nullable = false)
        var zipCode: String,

        @Column(nullable = false)
        var city: String,

        @Column(nullable = false)
        var email: String,

        @Column(nullable = false)
        var location: Point,

        @Column(nullable = false)
        var locationGeoHash: String,

        @Column(nullable = false)
        var phone: String,

        @CreatedDate
        var createdAt: LocalDateTime,

        @LastModifiedDate
        var updatedAt: LocalDateTime
)
