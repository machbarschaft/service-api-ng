package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.OrderItemEntity
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface OrderItemRepository : CrudRepository<OrderItemEntity, UUID>
