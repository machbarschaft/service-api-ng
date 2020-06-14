package com.colivery.serviceaping.persistence.entity

import com.colivery.serviceaping.persistence.Source
import org.hibernate.annotations.Type
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`help_seeker`")
@EntityListeners(AuditingEntityListener::class)
data class HelpSeekerEntity(
        @ManyToOne(optional = true, fetch = FetchType.EAGER)
        var user: UserEntity,

        var fullName: String? = null,

        var phone: String? = null,

        @Column(nullable = false)
        @Type(type = "com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType")
        @Enumerated(EnumType.STRING)
        var source: Source,

        @ManyToOne(optional = false, fetch = FetchType.EAGER)
        var enteredBy: UserEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null
}
