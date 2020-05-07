package com.colivery.serviceaping.rest.v1.dto.user

import javax.validation.constraints.NotNull

data class PatchUserAdminDto(@field: NotNull val isAdmin: Boolean?)
