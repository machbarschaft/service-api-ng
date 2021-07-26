package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.HelpRequestEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.*

interface HelpRequestRepository : CrudRepository<HelpRequestEntity, UUID> {

    @Query("from HelpRequestEntity where requestStatus <> 'DELETED'")
    fun findAllWithoutDeleted(): Set<HelpRequestEntity>
    fun findAllByAdminUser(adminUser: UserEntity): Set<HelpRequestEntity>

    @Query(
        "from HelpRequestEntity where (requestStatus = 'DELETED' or requestStatus = 'CLOSED') " +
                "and updatedAt < :date"
    )
    fun findAllByUpdatedAtBeforeAndInactive(date: LocalDateTime): Set<HelpRequestEntity>

}
