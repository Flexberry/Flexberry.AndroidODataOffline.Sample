package com.flexberry.androidodataofflinesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSourceCommon
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkDetail
import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * Тесты для [OdataDataSourceCommon].
 */
@RunWith(AndroidJUnit4::class)
class OdataCommonTest {
    /**
     * Тест вычитки всех объектов типа [NetworkApplicationUser].
     */
    @Test
    fun applicationUserReadTest() {
        val ds = OdataDataSourceCommon()
        val objs = ds.readObjects(NetworkApplicationUser::class) as List<NetworkApplicationUser>

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.Name != null })
        }
    }

    /**
     * Тест типизированной вычитки всех объектов типа [NetworkApplicationUser].
     */
    @Test
    fun applicationUserTypedReadTest() {
        val ds = OdataDataSourceCommon()
        val objs = ds.readObjects<NetworkApplicationUser>()

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.Name != null })
        }
    }

    /**
     * Тест создания, обновления, удаления объкетов типа [NetworkApplicationUser].
     */
    @Test
    fun applicationUserCreateUpdateDeleteTest() {
        val ds = OdataDataSourceCommon()
        val obj1 = NetworkApplicationUser(
            Name = "Test from android",
            EMail = "q@q.q",
            Creator = "Android"
        )

        val obj2 = NetworkApplicationUser(
            Name = "Test from android 2",
            EMail = "q@q.q",
            Creator = "Android"
        )

        val countCreate = ds.createObjects(obj1, obj2)

        Assert.assertEquals(countCreate, 2)

        val obj3 = NetworkApplicationUser(
            __PrimaryKey = obj2.__PrimaryKey,
            Name = "Test from android 2 changed",
            EMail = obj2.EMail,
            Editor = obj2.Creator
        )

        val countUpdate = ds.updateObjects(obj3)

        Assert.assertEquals(countUpdate, 1)

        val countDelete = ds.deleteObjects(obj1, obj2)

        Assert.assertEquals(countDelete, 2)
    }

    /**
     * Тест фильтра по имени + сортировка, для типа [NetworkApplicationUser].
     */
    @Test
    fun applicationUserFilterTest() {
        val ds = OdataDataSourceCommon()
        val querySettings = QuerySettings()
            .filter(Filter.equalFilter("Name", "NameForTest"))
            .orderBy("Name")
            .top(5)
        val objs = ds.readObjects(NetworkApplicationUser::class, querySettings)
            as List<NetworkApplicationUser>

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.Name != null })
        }
    }

    /**
     * Тест фильтра по полю типа [Boolean] для типа [NetworkApplicationUser].
     */
    @Test
    fun applicationUserBooleanFilterTest() {
        val dsUser = OdataDataSourceCommon()
        val objUser = NetworkApplicationUser(
            Name = "Test from android. Boolean test",
            EMail = "b@b.b",
            Creator = "Android",
            Activated = false
        )

        val countCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(countCreate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Name", "Test from android. Boolean test"),
                Filter.equalFilter("Activated", false)
            )
            .top(10)

        val userObjsRead = dsUser.readObjects(NetworkApplicationUser::class, querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val countDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(countDelete, 1)
    }

    /**
     * Тест фильтра по полю типа [Double] для типа [NetworkApplicationUser].
     */
    @Test
    fun applicationUserDoubleFilterTest() {
        val dsUser = OdataDataSourceCommon()
        val objUser1 = NetworkApplicationUser(
            Name = "Test from android. Double test 1",
            EMail = "double@test.com",
            Creator = "Android",
            Karma = 58.4
        )

        val objUser2 = NetworkApplicationUser(
            Name = "Test from android. Double test 2",
            EMail = "double2@test.com",
            Creator = "Android",
            Karma = -12.34
        )

        val countCreate = dsUser.createObjects(objUser1, objUser2)

        Assert.assertEquals(countCreate, 2)

        val querySettings = QuerySettings()
            .filter(
                Filter.containsFilter("Name", "Test from android. Double test"),
                Filter.equalFilter("Karma", 58.4)
            )
            .top(10)

        val userObjsRead = dsUser.readObjects(NetworkApplicationUser::class, querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val countDelete = dsUser.deleteObjects(objUser1, objUser2)

        Assert.assertEquals(countDelete, 2)
    }

    /**
     * Тест сохранения объекта [NetworkApplicationUser] с детейлом [NetworkVote].
     */
    @Test
    fun applicationUserWithVotesCreateDeleteTest() {
        val dsUser = OdataDataSourceCommon()
        val objUser = NetworkApplicationUser(
            Name = "Test from android. Save with detail.",
            EMail = "detail@save.com",
            Creator = "Android",
        )

        objUser.Votes = listOf(
            NetworkVote(
                VoteType = VoteType.Like,
                Creator = "Android",
                Author = objUser
            ),
            NetworkVote(
                VoteType = VoteType.Dislike,
                Creator = "Android",
                Author = objUser
            )
        )

        dsUser.createObjects(objUser)

        val objectsReaded = dsUser.readObjects<NetworkVote>(
            QuerySettings()
                .filter(Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey))
                .top(10)
        )

        Assert.assertEquals(objectsReaded.size, 2)
    }

    /**
     * Тест создания уже созданного объекта [NetworkApplicationUser].
     */
    @Test
    fun applicationUserCreateSavedTest() {
        val dataSource = OdataDataSourceCommon()
        val objUser = NetworkApplicationUser(
            Name = "Test from android. CreateSaved.",
            EMail = "CreateSaved@save.com",
            Creator = "Android",
        )

        dataSource.createObjects(objUser)
        val countCreated = dataSource.createObjects(objUser)

        Assert.assertEquals(countCreated, 1)

        val countDelete = dataSource.deleteObjects(objUser)

        Assert.assertEquals(countDelete, 1)
    }

    /**
     * Тест обновления несуществующего объекта [NetworkApplicationUser].
     */
    @Test
    fun applicationUserUpdateNewTest() {
        val dataSource = OdataDataSourceCommon()
        val objUser = NetworkApplicationUser(
            Name = "Test from android. UpdateNew.",
            EMail = "UpdateNew@save.com",
            Creator = "Android",
        )

        val countUpdated = dataSource.updateObjects(objUser)

        Assert.assertEquals(countUpdated, 1)

        val dataObjects = dataSource.readObjects<NetworkApplicationUser>(
            QuerySettings()
                .filter(Filter.equalFilter("__PrimaryKey", objUser.__PrimaryKey))
                .top(10)
        )

        Assert.assertEquals(dataObjects.size, 1)

        val countDelete = dataSource.deleteObjects(objUser)

        Assert.assertEquals(countDelete, 1)
    }

    /**
     * Тест удаления несуществующего объекта [NetworkApplicationUser].
     */
    @Test
    fun applicationUserDeleteUnsavedTest() {
        val dataSource = OdataDataSourceCommon()
        val objUser = NetworkApplicationUser(
            Name = "Test from android. DeleteUnsaved.",
            EMail = "DeleteUnsaved@save.com",
            Creator = "Android",
        )

        val countDeleted = dataSource.deleteObjects(objUser)

        Assert.assertEquals(countDeleted, 1)
    }

    /**
     * Тест создания, обновления, удаления объектов типа [NetworkVote].
     */
    @Test
    fun voteCreateReadUpdateFilterDeleteTest() {
        val dsCommon = OdataDataSourceCommon()

        val objUser = NetworkApplicationUser(
            Name = "Test from android read",
            EMail = "r@r.r",
            Creator = "Android"
        )

        val countUserCreate = dsCommon.createObjects(objUser)

        Assert.assertEquals(countUserCreate, 1)

        val objVote = NetworkVote(
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser
        )

        val countVoteCreate = dsCommon.createObjects(objVote)

        Assert.assertEquals(countVoteCreate, 1)

        val objVote2 = NetworkVote(
            __PrimaryKey = objVote.__PrimaryKey,
            VoteType = VoteType.Dislike,
            Creator = "Android",
            Author = objUser
        )

        val countVoteUpdate = dsCommon.updateObjects(objVote2)

        Assert.assertEquals(countVoteUpdate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.equalFilter("VoteType", VoteType.Dislike)
            )
            .top(10)

        val voteObjsRead = dsCommon.readObjects(NetworkVote::class, querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val countVoteDelete = dsCommon.deleteObjects(objVote)

        Assert.assertEquals(countVoteDelete, 1)

        val countUserDelete = dsCommon.deleteObjects(objUser)

        Assert.assertEquals(countUserDelete, 1)
    }

    /**
     * Тест фильтра по полю типа [Date] для типа [NetworkVote].
     */
    @Test
    fun voteDatetimeFilterTest() {
        val dsCommon = OdataDataSourceCommon()
        val dateNow = Date()
        val dateNowMinusOneMinute = Date(dateNow.time - 60 * 1000)

        val objUser = NetworkApplicationUser(
            Name = "Test from android, filter test",
            EMail = "f@f.f",
            Creator = "Android",
            CreateTime = dateNow
        )

        val countUserCreate = dsCommon.createObjects(objUser)

        Assert.assertEquals(countUserCreate, 1)

        val objVote = NetworkVote(
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser,
            CreateTime = dateNow
        )

        val countVoteCreate = dsCommon.createObjects(objVote)

        Assert.assertEquals(countVoteCreate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.greaterFilter("CreateTime", dateNowMinusOneMinute)
            )
            .top(10)

        val voteObjsRead = dsCommon.readObjects(NetworkVote::class, querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val countVoteDelete = dsCommon.deleteObjects(objVote)

        Assert.assertEquals(countVoteDelete, 1)

        val countUserDelete = dsCommon.deleteObjects(objUser)

        Assert.assertEquals(countUserDelete, 1)
    }

    /**
     * Тест объектов [NetworkMaster]+[NetworkDetail]. Создание, обновление, чтение, удаление.
     */
    @Test
    fun masterWithDetailCreateUpdateFilterDeleteTest() {
        val dataSource = OdataDataSourceCommon()

        // Создаем мастеров
        val master1 = NetworkMaster(
            Name = "test master1"
        )
        val master2 = NetworkMaster(
            Name = "test master2"
        )

        // Задаем детейлы.
        master1.Detail = listOf(
            NetworkDetail(
                Name = "test detail 1",
                Master = master1
            ),
            NetworkDetail(
                Name = "test detail 2",
                Master = master1
            )
        )

        // Сохраняем мастера.
        val countMastersCreated = dataSource.createObjects(master1, master2)

        Assert.assertEquals(countMastersCreated, 2)

        // Мастер для обновления.
        val master1Updated = NetworkMaster(
            __PrimaryKey = master1.__PrimaryKey,
            Name = "test master updated"
        )

        // Обновляем мастера.
        val countMastersUpdated = dataSource.updateObjects(master1Updated)

        Assert.assertEquals(countMastersUpdated, 1)

        // Вычитываем мастера.
        val masters = dataSource.readObjects<NetworkMaster>(
            QuerySettings()
                .filter(Filter.equalFilter("__PrimaryKey", master1.__PrimaryKey))
                .top(10)
        )

        // Проверяем количество вычитанных мастеров.
        Assert.assertEquals(masters.size, 1)
        // Проверяем обновилось ли имя мастера.
        Assert.assertEquals(masters[0].Name, "test master updated")

        // Вычитываем детейлы.
        val details = dataSource.readObjects<NetworkDetail>(
            QuerySettings()
                .filter(Filter.equalFilter("Master.__PrimaryKey", master1.__PrimaryKey))
                .top(10)
        )

        // Проверяем количество детейлов.
        Assert.assertEquals(details.size, 2)

        // Удаляем детейлы и проверим количество.
        val countDetailsDeleted = dataSource.deleteObjects(master1.Detail!!)

        Assert.assertEquals(countDetailsDeleted, 2)

        // Удаляем мастеров и проверим количество.
        val countMastersDeleted = dataSource.deleteObjects(master1, master2)

        Assert.assertEquals(countMastersDeleted, 2)
    }
}