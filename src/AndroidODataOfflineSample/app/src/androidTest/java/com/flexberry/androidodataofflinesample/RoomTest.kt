package com.flexberry.androidodataofflinesample

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.local.daos.AppDataDao
import com.flexberry.androidodataofflinesample.data.local.databases.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var appDataDao: AppDataDao
    private lateinit var db: LocalDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java)
            // Позволяет выполнять запросы в основном потоке - только для теста
            .allowMainThreadQueries()
            .build()
        appDataDao = db.getAppDataDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAppData() = runBlocking {
        println("Start Test")
        val appData = AppDataEntity(UUID.randomUUID(),true)
        println(appData)
        appDataDao.insertAppData(appData)
        val allAppData = appDataDao.getAppData().first()
        println(allAppData)
        TestCase.assertEquals(allAppData[0].isOnline, appData.isOnline)
    }
}