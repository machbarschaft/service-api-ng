package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.OrderEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface OrderRepository : JpaRepository<OrderEntity, UUID> {

    fun findAllByUser(user: UserEntity): Set<OrderEntity>
    fun findAllByDriverUser(user: UserEntity): Set<OrderEntity>

    @Query("update OrderEntity order set order.status = 'CONSUMER_CANCELLED' " +
            "where order.user = :user")
    @Modifying
    fun updateCreatedOrdersToConsumerCancelled(user: UserEntity): Int

}
