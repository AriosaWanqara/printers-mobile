package com.example.printermobile.domain.services

import com.example.printermobile.data.database.repositories.SystemTypeRepository
import com.example.printermobile.domain.models.SystemType
import javax.inject.Inject

class AddSystemType @Inject constructor(private val repository: SystemTypeRepository) {
    suspend operator fun invoke(systemType: SystemType){
        repository.addSystemType(systemType)
    }
}