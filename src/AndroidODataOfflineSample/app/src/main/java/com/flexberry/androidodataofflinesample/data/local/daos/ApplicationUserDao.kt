package com.flexberry.androidodataofflinesample.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationUserDao {
    @Insert
    suspend fun insertApplicationUser(applicationUser: ApplicationUserEntity)

    @Query("SELECT * FROM ApplicationUser")
    fun getApplicationUsers(): Flow<List<ApplicationUserEntity>>
}