package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByFirebaseUid(firebaseUid: String): UserEntity?

}
