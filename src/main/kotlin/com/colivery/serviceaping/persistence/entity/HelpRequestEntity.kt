package com.colivery.serviceaping.persistence.entity

import com.colivery.serviceaping.persistence.RequestStatus
import org.hibernate.annotations.Type
import org.locationtech.jts.geom.Point
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`help_request`")
@EntityListeners(AuditingEntityListener::class)
data class HelpRequestEntity(
        @Column(nullable = true, name = "request_text")
        var requestText: String,

        @Column(nullable = false, name = "status")
        @Type(type = "com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType")
        @Enumerated(EnumType.STRING)
        var requestStatus: RequestStatus,

        @ManyToOne(optional = false, fetch = FetchType.EAGER)
        @JoinColumn(name = "admin_user_id")
        var adminUser: UserEntity,

        @ManyToOne(optional = false, fetch = FetchType.EAGER)
        @JoinColumn(name = "help_seeker_id")
        var helpSeeker: HelpSeekerEntity,

        @ManyToOne(optional = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "helper_id")
        var helper: UserEntity? = null,

        @Column(nullable = true)
        var location: Point? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}
