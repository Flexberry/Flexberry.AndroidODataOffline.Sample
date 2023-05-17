package com.flexberry.androidodataofflinesample

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.local.dao.AppDataDao
import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.datasource.AppDataRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.VoteRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.ApplicationUserRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
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
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAppData() = runBlocking {
        val querySettings = QuerySettings()
        appDataDao = AppDataRoomDataSource(db, querySettings).AppDataDao()
        println("Start Test")
        val appData = AppDataEntity(UUID.randomUUID(),true)
        println(appData)
        appDataDao.insertAppData(appData)
        val allAppData = appDataDao.getAppData().first()
        println(allAppData)
        TestCase.assertEquals(allAppData[0].isOnline, appData.isOnline)
    }

    @Test
    fun appDataFilterTest() {
        val querySettings = QuerySettings()
            .filter(Filter.equalFilter("isOnline", "true"))
            .orderBy("__primaryKey")
            .top(5)

        val ds = AppDataRoomDataSource(db,querySettings)
        val objs = ds.readObjects(querySettings)

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.isOnline })
        }
    }
}