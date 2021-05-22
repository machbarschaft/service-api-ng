package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<UserEntity, UUID> {

    fun findByFirebaseUid(firebaseUid: String): UserEntity?
    fun existsByFirebaseUid(firebaseUid: String): Boolean
    fun findByPhone(phoneNumber: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
    fun findAllByRoleEquals(role: UserEntity.Role): List<UserEntity>

}
