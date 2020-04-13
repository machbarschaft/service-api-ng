package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.OrderEntity
import com.colivery.serviceaping.persistence.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface OrderRepository : CrudRepository<OrderEntity, UUID> {

    fun findAllByUser(user: UserEntity): Set<OrderEntity>
    fun findAllByDriverUser(user: UserEntity): Set<OrderEntity>

}
