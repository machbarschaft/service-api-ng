package com.colivery.serviceaping.persistence

import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface OrderRepository : JpaRepository<OrderEntity, String>