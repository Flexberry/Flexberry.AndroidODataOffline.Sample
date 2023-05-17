package com.flexberry.androidodataofflinesample.data.local.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flexberry.androidodataofflinesample.data.local.dao.AppDataDao
import com.flexberry.androidodataofflinesample.data.local.dao.ApplicationUserDao
import com.flexberry.androidodataofflinesample.data.local.dao.VoteDao
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.utils.Converters

@Database(
    entities = [AppDataEntity::class, ApplicationUserEntity::class, VoteEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getAppDataDao(): AppDataDao;
    abstract fun getApplicationUserDao(): ApplicationUserDao;
    abstract fun getVoteDao(): VoteDao;

    companion object {
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(LocalDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        LocalDatabase::class.java, "localDatabase.db").allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}