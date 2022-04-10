package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.InvitationCodeEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface InvitationCodeRepository : CrudRepository<InvitationCodeEntity, UUID> {
    fun findByCode(code: String): InvitationCodeEntity

    fun findAllByUsedFalse(): List<InvitationCodeEntity>
}