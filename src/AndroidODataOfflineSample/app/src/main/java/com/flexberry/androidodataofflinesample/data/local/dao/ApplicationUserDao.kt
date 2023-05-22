package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.relations.ApplicationUserWithVotes
import java.util.UUID

@Dao
interface ApplicationUserDao {
    @Insert
    fun insertObjects(users: List<ApplicationUserEntity>) : List<Long>

    @RawQuery
    fun getObjects(query: SimpleSQLiteQuery) : List<ApplicationUserEntity>

    @Query("SELECT * FROM ApplicationUser WHERE __primaryKey in (:pks)")
    fun getObjectsWithDetails(pks: List<UUID>) : List<ApplicationUserWithVotes?>

    @Update
    fun updateObjects(users: List<ApplicationUserEntity>): Int

    @Delete
    fun deleteObjects(users: List<ApplicationUserEntity>): Int
}