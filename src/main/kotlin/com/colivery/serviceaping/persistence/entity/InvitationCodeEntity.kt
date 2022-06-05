package com.colivery.serviceaping.persistence.entity

import org.hibernate.Hibernate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`invitation_codes`")
@EntityListeners(AuditingEntityListener::class)
data class InvitationCodeEntity(

    @Column(unique = true)
    var code: String? = null,
    var used: Boolean? = false,

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var usedBy: UserEntity? = null

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as InvitationCodeEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , code = $code , used = $used )"
    }
}