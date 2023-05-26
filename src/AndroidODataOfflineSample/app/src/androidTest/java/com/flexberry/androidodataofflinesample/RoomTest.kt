package com.flexberry.androidodataofflinesample

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.datasource.AppDataRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.VoteRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.ApplicationUserRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.DetailRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.MasterRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSourceCommon
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataBaseManager
import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID

/**
 * Тесты для [RoomDataSource].
 */
@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var db: LocalDatabase
    private lateinit var dataBaseManager: RoomDataBaseManager

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java)
            // Позволяет выполнять запросы в основном потоке - только для теста
            .allowMainThreadQueries()
            .build()

        dataBaseManager = RoomDataBaseManager(db)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Тест CRUD-операций для [AppDataEntity].
     */
    @Test
    @Throws(Exception::class)
    fun appDataCRUDTest() {
        val ds = AppDataRoomDataSource(dataBaseManager)

        val appData1 = AppDataEntity(UUID.randomUUID(),true)
        val appData2 = AppDataEntity(UUID.randomUUID(),false)

        // Добавление
        val insertCount = ds.createObjects(listOf(appData1, appData2))
        Assert.assertEquals(insertCount, 2)

        // Вычитка
        val oneObjects = ds.readObjects(
            QuerySettings(Filter.equalFilter(AppDataEntity::primarykey.name, appData1.primarykey)))

        Assert.assertEquals(oneObjects.size, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("IsOnline", false)
            )
        val filterData = ds.readObjects(querySettings)
        if (filterData.any()) {
            Assert.assertTrue(filterData.any { x -> !x.isOnline })
        }

        // Обновление
        val appData3 = AppDataEntity(appData1.primarykey, false)
        val appData4 = AppDataEntity(appData2.primarykey, true)
        val updateCount = ds.updateObjects(listOf(appData3, appData4))
        Assert.assertEquals(updateCount, 2)

        // Вычитка
        val checkUpdated = ds.readObjects()
        Assert.assertTrue(!checkUpdated[0].isOnline && checkUpdated[1].isOnline)

        // Удаление
        val deletedCount = ds.deleteObjects(listOf(appData1))
        Assert.assertEquals(deletedCount, 1)

        // Вычитка
        val checkDeleted = ds.readObjects()
        Assert.assertTrue(checkDeleted.count() == 1)
    }

    /**
     * Тест CRUD-операций для [VoteEntity].
     */
    @Test
    @Throws(Exception::class)
    fun voteCRUDTest() {
        val ds = VoteRoomDataSource(dataBaseManager)

        var vote1 = VoteEntity(
            primarykey = UUID.randomUUID(),
            voteType = VoteType.Like,
            editor = "Flexberry"
        )

        var vote2 = VoteEntity(
            primarykey = UUID.randomUUID(),
            voteType = VoteType.Dislike,
            editor = "Caseberry"
        )

        // Добавление
        val insertCount = ds.createObjects(listOf(vote1, vote2))
        Assert.assertEquals(insertCount, 2)

        // Вычитка
        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("VoteType", VoteType.Like)
            )

        val data = ds.readObjects(querySettings)
        Assert.assertTrue(data.isNotEmpty())

        // Обновление
        vote1.voteType = VoteType.Dislike
        val updateCount = ds.updateObjects(listOf(vote1))
        Assert.assertEquals(updateCount, 1)

        // Вычитка
        val checkUpdated = ds.readObjects()
        Assert.assertTrue(checkUpdated.first { x -> x.primarykey == vote1.primarykey }?.voteType == VoteType.Dislike)

        // Удаление
        val deletedCount = ds.deleteObjects(listOf(vote1))
        Assert.assertEquals(deletedCount, 1)

        // Вычитка
        val checkDeleted = ds.readObjects()
        Assert.assertTrue(checkDeleted.first()?.editor == "Caseberry")
    }

    /**
     * Тест CRUD-операций для [ApplicationUserEntity].
     */
    @Test
    @Throws(Exception::class)
    fun applicationUserCRUDTest() {
        val ds = ApplicationUserRoomDataSource(dataBaseManager)

        var user1 = ApplicationUserEntity(
            primarykey = UUID.randomUUID(),
            name = "Mouse",
            vip = true
        )

        var user2 = ApplicationUserEntity(
            primarykey = UUID.randomUUID(),
            name = "Dog",
            vip = false
        )

        var user3 = ApplicationUserEntity(
            primarykey = UUID.randomUUID(),
            name = "Cat",
            vip = true
        )

        // Добавление
        val insertCount = ds.createObjects(listOf(user1, user2, user3))
        Assert.assertEquals(insertCount, 3)

        // Вычитка
        val querySettings = QuerySettings()
            .orderBy("Name")

        val data = ds.readObjects(querySettings)
        Assert.assertTrue(data.isNotEmpty())

        // Обновление
        user1.email = "mouse@mail.ru"
        val updateCount = ds.updateObjects(listOf(user1))
        Assert.assertEquals(updateCount, 1)

        // Вычитка
        val checkUpdated = ds.readObjects()
        Assert.assertTrue(checkUpdated.first { x -> x.primarykey == user1.primarykey }?.email == "mouse@mail.ru")

        // Удаление
        val deletedCount = ds.deleteObjects(listOf(user1, user2))
        Assert.assertEquals(deletedCount, 2)

        // Вычитка
        val checkDeleted = ds.readObjects()
        Assert.assertTrue(checkDeleted.first()?.name == "Cat")
    }

    /**
     * Тест [QuerySettings].
     */
    @Test
    @Throws(Exception::class)
    fun querySettingsTest() {
        val ds = ApplicationUserRoomDataSource(dataBaseManager)

        var user1 = ApplicationUserEntity(
            primarykey = UUID.randomUUID(),
            name = "Rat",
            vip = true
        )

        var user2 = ApplicationUserEntity(
            primarykey = UUID.randomUUID(),
            name = "Dog",
            vip = false
        )

        var user3 = ApplicationUserEntity(
            primarykey = UUID.randomUUID(),
            name = "Cat",
            vip = true
        )

        // Добавление
        val insertCount = ds.createObjects(listOf(user1, user2, user3))
        Assert.assertEquals(insertCount, 3)

        // Проверка hasFilter (оператор Like)
        var querySettings = QuerySettings()
            .filter(
                Filter.hasFilter("Name", "at")
            )

        val data = ds.readObjects(querySettings)
        if (data.any()) {
            Assert.assertTrue(data.any { x -> x.name == "Cat" })
            Assert.assertTrue(data.any { x -> x.name == "Rat" })
        }
    }

    /**
     * Тест создания и чтения записи [MasterEntity] из [MasterRoomDataSource].
     */
    @Test
    @Throws(Exception::class)
    fun masterCreateReadTest() {
        val dsMaster = MasterRoomDataSource(dataBaseManager)
        val master1name = "master1 dsk!!!fhsdfgh"

        val master1 = MasterEntity(
            name = master1name
        )

        val countMastersCreated = dsMaster.createObjects(master1)

        Assert.assertEquals(countMastersCreated, 1)

        val masters = dsMaster.readObjects(
            QuerySettings()
                .filter(Filter.equalFilter("name", master1name))
                .top(10)
        )

        Assert.assertEquals(masters.size, 1)

        val countMastersDeleted = dsMaster.deleteObjects(master1)

        Assert.assertEquals(countMastersDeleted, 1)
    }

    /**
     * Тест создания и чтения записей [MasterEntity] и [DetailEntity] из [RoomDataSourceCommon].
     * Создание детейла вместе с мастером.
     */
    @Test
    @Throws(Exception::class)
    fun masterCommonWithDetailsCreateReadDeleteTest() {
        val dsCommon = RoomDataSourceCommon(dataBaseManager)
        val dsMaster = MasterRoomDataSource(dataBaseManager)
        val master1name = "master1 sgdfvmnsdeasdfcbfmnbsv"
        val detail1name = "detail1 dfjshfkjsdfkdjshf"
        val detail2name = "detail1 qewrljnqwlqlwker"

        val master1 = MasterEntity(
            name = master1name
        )

        master1.details = listOf(
            DetailEntity(
                name = detail1name,
                master = master1
            ),
            DetailEntity(
                name = detail2name,
                master = master1
            ),
        )

        // Создаем мастера с детейлами.
        val countMastersCreated = dsCommon.createObjects(master1)

        Assert.assertEquals(countMastersCreated, 1)

        // Вычитаем отдельно детейлы и проверим что все сохранилось.
        val detailsReaded = dsCommon.readObjects<DetailEntity>(
            QuerySettings()
                .filter(Filter.inFilter("name", listOf(
                    detail1name,
                    detail2name
                )))
                .top(10)
        )

        Assert.assertEquals(detailsReaded.size, 2)

        // Вычитаем мастера полностью и проверим что он есть вместе с детейлами.
        val mastersReaded = dsMaster.getMastersWithRelations(
            QuerySettings(Filter.equalFilter(MasterEntity::name.name, master1name))
                .top(10)
        )

        Assert.assertEquals(mastersReaded.size, 1)
        Assert.assertEquals(mastersReaded[0].details?.size ?: 0, 2)

        // Удаляем наши объекты.
        val countDetailsDeleted = dsCommon.deleteObjects(master1.details!!)

        Assert.assertEquals(countDetailsDeleted, 2)

        val countMastersDeleted = dsCommon.deleteObjects(master1)

        Assert.assertEquals(countMastersDeleted, 1)
    }

    /**
     * Тест создания и чтения записи [MasterEntity] из [RoomDataSourceCommon].
     */
    @Test
    @Throws(Exception::class)
    fun masterCommonTest() {
        val dsCommon = RoomDataSourceCommon(dataBaseManager)
        val masterName = "master1 dskfhsdfgh"

        val master1 = MasterEntity(
            name = masterName
        )

        val countMastersCreated = dsCommon.createObjects(master1)

        Assert.assertEquals(countMastersCreated, 1)

        val masters = dsCommon.readObjects<MasterEntity>(
            QuerySettings()
                .filter(Filter.equalFilter("name", masterName))
                .top(10)
        )

        Assert.assertEquals(masters.size, 1)

        val countMastersDeleted = dsCommon.deleteObjects(master1)

        Assert.assertEquals(countMastersDeleted, 1)
    }

    /**
     * Тест сохранения [DetailEntity] и вычитки его с полными связями.
     */
    @Test
    @Throws(Exception::class)
    fun detailCreateReadTest() {
        val dsCommon = RoomDataSourceCommon(dataBaseManager)
        val dsDetail = DetailRoomDataSource(dataBaseManager)
        val master1name = "master1 sgdfvmnsdeasdfcbfmnbsv"
        val detail1name = "detail1 dfjshfkjsdfkdjshf"
        val detail2name = "detail1 qewrljnqwlqlwker"

        // Готовим данные.
        val master1 = MasterEntity(
            name = master1name
        )

        master1.details = listOf(
            DetailEntity(
                name = detail1name,
                master = master1
            ),
            DetailEntity(
                name = detail2name,
                master = master1
            ),
        )

        // Создаем мастера с детейлами.
        val countMastersCreated = dsCommon.createObjects(master1)

        Assert.assertEquals(countMastersCreated, 1)

        // Вычитываем детейлы со связями, ограничивая по имени мастера.
        val detailsRead = dsDetail.getDetailsWithRelations(
            QuerySettings()
                .filter(Filter.equalFilter("${DetailEntity::master.name}.${MasterEntity::name.name}", master1name))
                .top(5)
        )

        // Проверяем что все объекты на месте.
        Assert.assertEquals(detailsRead.size, 2)
        // И что имя мастера прогрузилось.
        Assert.assertEquals(detailsRead.filter { it.master?.name == master1name }.size, 2)
    }

    /**
     * Обновление уже сохраненного объекта.
     */
    @Test
    @Throws(Exception::class)
    fun createExistedTest() {
        val masterRoomDataSource = MasterRoomDataSource(dataBaseManager)
        val master1 = MasterEntity(name = "tricky master createExisted")

        var countMasterCreated = masterRoomDataSource.createObjects(master1)

        Assert.assertEquals(countMasterCreated, 1)

        countMasterCreated = masterRoomDataSource.createObjects(master1)

        Assert.assertEquals(countMasterCreated, 1)
    }

    /**
     * Обновление несохраненного объекта.
     */
    @Test
    @Throws(Exception::class)
    fun updateNewTest() {
        val masterRoomDataSource = MasterRoomDataSource(dataBaseManager)
        val master1 = MasterEntity(name = "tricky master updateNew")

        val countMasterUpdated = masterRoomDataSource.updateObjects(master1)

        Assert.assertEquals(countMasterUpdated, 1)

        val mastersLoaded = masterRoomDataSource.readObjects(
            QuerySettings(Filter.equalFilter("primarykey", master1.primarykey))
        )

        Assert.assertEquals(mastersLoaded.size, 1)
    }

    /**
     * Удаление несуществующего объекта.
     */
    @Test
    @Throws(Exception::class)
    fun deleteNotExistedTest() {
        val masterRoomDataSource = MasterRoomDataSource(dataBaseManager)
        val master1 = MasterEntity(name = "tricky master deleteNotExisted")

        val countMasterDeleted = masterRoomDataSource.deleteObjects(master1)

        Assert.assertEquals(countMasterDeleted, 1)
    }
}