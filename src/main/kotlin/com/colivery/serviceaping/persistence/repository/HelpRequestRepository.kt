package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.HelpRequestEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface HelpRequestRepository : CrudRepository<HelpRequestEntity, UUID> {

    fun findAllByAdminUser_Id(adminUserId: String): Set<HelpRequestEntity>
    fun findById(helpRequestId: String): HelpRequestEntity?

}
