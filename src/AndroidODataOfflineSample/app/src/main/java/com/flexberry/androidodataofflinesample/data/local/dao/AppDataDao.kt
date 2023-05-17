package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDataDao {
    @Insert
    suspend fun insertAppData(appData: AppDataEntity)

    @Query("select * FROM AppData")
    fun getAppData(): Flow<List<AppDataEntity>>
}