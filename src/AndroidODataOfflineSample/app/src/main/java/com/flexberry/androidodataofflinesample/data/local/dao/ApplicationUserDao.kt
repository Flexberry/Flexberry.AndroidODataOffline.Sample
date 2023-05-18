package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity

@Dao
interface ApplicationUserDao {
    @Insert
    fun insertObjects(users: List<ApplicationUserEntity>) : List<Long>

    @RawQuery
    fun getObjects(query: SimpleSQLiteQuery) : List<ApplicationUserEntity>

    @Update
    fun updateObjects(users: List<ApplicationUserEntity>): Int

    @Delete
    fun deleteObjects(users: List<ApplicationUserEntity>): Int
}