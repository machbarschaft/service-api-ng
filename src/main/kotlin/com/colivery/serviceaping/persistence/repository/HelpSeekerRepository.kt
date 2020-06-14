package com.colivery.serviceaping.persistence.repository

import com.colivery.serviceaping.persistence.entity.HelpSeekerEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface HelpSeekerRepository : CrudRepository<HelpSeekerEntity, UUID>
