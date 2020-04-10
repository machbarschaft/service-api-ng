package com.colivery.serviceaping.persistence

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class OrderItemEntity(
        @Id
        var id: String,

        var description: String
)