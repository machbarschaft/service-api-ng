package com.colivery.serviceaping.dao

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.firestore.annotation.PropertyName

class OrderDao {
    @DocumentId
    lateinit var id: String

    @PropertyName("user_id")
    lateinit var userId: String

    var driverUserId: String? = null
}
