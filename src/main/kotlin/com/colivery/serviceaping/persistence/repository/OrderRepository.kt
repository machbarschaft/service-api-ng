package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.OrderEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import org.locationtech.jts.geom.Polygon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.transaction.Transactional

interface OrderRepository : JpaRepository<OrderEntity, UUID> {

    fun findAllByUser(user: UserEntity): Set<OrderEntity>
    fun findAllByDriverUser(user: UserEntity): Set<OrderEntity>

    @Transactional(Transactional.TxType.MANDATORY)
    @Query("update OrderEntity o set o.status = 'CONSUMER_CANCELLED' " +
            "where o.user = :user")
    @Modifying
    fun updateCreatedOrdersToConsumerCancelled(user: UserEntity): Int

    @Query("select o from OrderEntity o where within(o.user.location, :circle) = true and o" +
            ".status = 'TO_BE_DELIVERED'")
    fun searchOpenOrdersInRange(circle: Polygon): Set<OrderEntity>
}
