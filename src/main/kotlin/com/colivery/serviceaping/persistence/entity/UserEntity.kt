package com.colivery.serviceaping.persistence.entity

import com.colivery.serviceaping.persistence.Source
import org.hibernate.annotations.Type
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

        @Column(nullable = false)
        val firebaseUid: String,

        @Column(nullable = false)
        @Type(type = "com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType")
        @Enumerated(EnumType.STRING)
        var source: Source

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}
