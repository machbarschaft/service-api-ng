package com.colivery.serviceaping.rest.v1.dto.user

import com.colivery.serviceaping.persistence.entity.UserEntity
import com.colivery.serviceaping.rest.v1.dto.EnumValue
import javax.validation.constraints.NotNull

data class PatchUserAdminDto(
        @field: NotNull
        @field: EnumValue(UserEntity.Role::class)
        val role: String?
)
