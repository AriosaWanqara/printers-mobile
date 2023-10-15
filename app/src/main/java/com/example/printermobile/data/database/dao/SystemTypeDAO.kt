package com.example.printermobile.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.printermobile.data.database.entities.SystemTypeEntity

@Dao
interface SystemTypeDAO {

    @Query("SELECT * FROM SystemTypeEntity")
    fun getAll(): List<SystemTypeEntity>

    @Query("SELECT * FROM SystemTypeEntity WHERE id IN (:id)")
    fun getSystemTypeById(id:Int): SystemTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg systemTypeEntity: SystemTypeEntity)

}