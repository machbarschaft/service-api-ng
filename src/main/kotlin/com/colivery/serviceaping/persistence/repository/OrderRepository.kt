package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface OrderRepository : JpaRepository<OrderEntity, UUID>
