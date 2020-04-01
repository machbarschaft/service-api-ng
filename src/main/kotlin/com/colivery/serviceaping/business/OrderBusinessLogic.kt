package com.colivery.serviceaping.business

import com.colivery.serviceaping.dao.OrderDao
import com.colivery.serviceaping.dto.OrderDto

fun transformToDto(orderDao: OrderDao) =
        OrderDto(orderDao.id, orderDao.userId, orderDao.driverUserId)
