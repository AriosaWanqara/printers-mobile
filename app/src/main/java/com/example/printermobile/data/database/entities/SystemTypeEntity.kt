package com.example.printermobile.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.printermobile.domain.models.SystemType

@Entity
data class SystemTypeEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 1,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "is_restaurant")
    var isRestaurant: Boolean,
){
    fun getSystemTypeFromEntity():SystemType{
        return SystemType(
            this.id,
            this.name,
            this.isRestaurant
        )
    }
}