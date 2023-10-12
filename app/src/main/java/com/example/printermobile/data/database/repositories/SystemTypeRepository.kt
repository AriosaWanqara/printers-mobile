package com.example.printermobile.data.database.repositories

import com.example.printermobile.data.database.dao.SystemTypeDAO
import com.example.printermobile.data.database.entities.SystemTypeEntity
import com.example.printermobile.domain.models.SystemType
import javax.inject.Inject


class SystemTypeRepository @Inject constructor(private val dao: SystemTypeDAO){

    suspend fun getSystemTypeById(id:Int): SystemType? {
        return dao.getSystemTypeById(id)?.getSystemTypeFromEntity()
    }

    suspend fun addSystemType(systemType: SystemType){
        dao.insertAll(getEntityFromSystemType(systemType))
    }

    private fun getEntityFromSystemType(systemType: SystemType): SystemTypeEntity {
        return SystemTypeEntity(
            1,
            systemType.getName(),
            systemType.getIsRestaurant()
        )
    }
}