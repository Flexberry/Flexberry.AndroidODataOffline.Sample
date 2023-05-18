package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity

@Dao
interface AppDataDao {
    @Insert
    fun insertObjects(appData: List<AppDataEntity>): List<Long>

    @RawQuery
    fun getObjects(query: SimpleSQLiteQuery): List<AppDataEntity>

    @Update
    fun updateObjects(appData: List<AppDataEntity>): Int

    @Delete
    fun deleteObjects(appData: List<AppDataEntity>): Int
}