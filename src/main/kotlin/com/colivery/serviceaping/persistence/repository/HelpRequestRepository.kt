package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.HelpRequestEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface HelpRequestRepository : CrudRepository<HelpRequestEntity, UUID> {

    fun findAllByAdminUser(adminUser: UserEntity): Set<HelpRequestEntity>

}
