package com.flexberry.androidodataofflinesample.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.model.AppData
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDataDao {
    @Insert
    suspend fun insertAppData(appData: AppDataEntity)

    @Query("SELECT * FROM AppData")
    fun getAppData(): Flow<List<AppDataEntity>>
}