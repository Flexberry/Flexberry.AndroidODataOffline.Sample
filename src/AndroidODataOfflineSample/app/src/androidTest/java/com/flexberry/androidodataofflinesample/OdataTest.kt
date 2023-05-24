package com.flexberry.androidodataofflinesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.DetailOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.MasterOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
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
import java.util.UUID

/**
 * Тесты для [OdataDataSource].
 */
@RunWith(AndroidJUnit4::class)
class OdataTest {
    /**
     * Тест вычитки всех объектов через [ApplicationUserOdataDataSource].
     */
    @Test
    fun applicationUserReadTest() {
        val ds = ApplicationUserOdataDataSource()
        val objs = ds.readObjects()

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.Name != null })
        }
    }

    /**
     * Тест создания, обновления, удаления объкетов через [ApplicationUserOdataDataSource].
     */
    @Test
    fun applicationUserCreateUpdateDeleteTest() {
        val ds = ApplicationUserOdataDataSource()
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
     * Тест фильтра по имени + сортировка, через [ApplicationUserOdataDataSource].
     */
    @Test
    fun applicationUserFilterTest() {
        val ds = ApplicationUserOdataDataSource()
        val querySettings = QuerySettings()
            .filter(Filter.equalFilter("Name", "NameForTest"))
            .orderBy("Name")
            .top(5)
        val objs = ds.readObjects(querySettings)

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.Name != null })
        }
    }

    /**
     * Тест фильтра по полю типа [Boolean] через [ApplicationUserOdataDataSource].
     */
    @Test
    fun applicationUserBooleanFilterTest() {
        val dsUser = ApplicationUserOdataDataSource()
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

        val userObjsRead = dsUser.readObjects(querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val countDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(countDelete, 1)
    }

    /**
     * Тест фильтра по полю типа [Double] через [ApplicationUserOdataDataSource].
     */
    @Test
    fun applicationUserDoubleFilterTest() {
        val dsUser = ApplicationUserOdataDataSource()
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

        val userObjsRead = dsUser.readObjects(querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val countDelete = dsUser.deleteObjects(objUser1, objUser2)

        Assert.assertEquals(countDelete, 2)
    }

    /**
     * Тест сохранения объекта с детейлом через [ApplicationUserOdataDataSource].
     */
    @Test
    fun applicationUserWithVotesCreateDeleteTest() {
        val dsUser = ApplicationUserOdataDataSource()
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
    }

    /**
     * Тест создания, обновления, удаления объектов через [VoteOdataDataSource].
     */
    @Test
    fun voteCreateReadUpdateFilterDeleteTest() {
        val dsUser = ApplicationUserOdataDataSource()
        val dsVote = VoteOdataDataSource()

        val objUser = NetworkApplicationUser(
            Name = "Test from android read",
            EMail = "r@r.r",
            Creator = "Android"
        )

        val countUserCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(countUserCreate, 1)

        val objVote = NetworkVote(
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser
        )

        val countVoteCreate = dsVote.createObjects(objVote)

        Assert.assertEquals(countVoteCreate, 1)

        val objVote2 = NetworkVote(
            __PrimaryKey = objVote.__PrimaryKey,
            VoteType = VoteType.Dislike,
            Creator = "Android",
            Author = objUser
        )

        val countVoteUpdate = dsVote.updateObjects(objVote2)

        Assert.assertEquals(countVoteUpdate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.equalFilter("VoteType", VoteType.Dislike)
            )
            .top(10)

        val voteObjsRead = dsVote.readObjects(querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val countVoteDelete = dsVote.deleteObjects(objVote)

        Assert.assertEquals(countVoteDelete, 1)

        val countUserDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(countUserDelete, 1)
    }

    /**
     * Тест фильтра по полю типа [Date] через [VoteOdataDataSource].
     */
    @Test
    fun voteDatetimeFilterTest() {
        val dsUser = ApplicationUserOdataDataSource()
        val dsVote = VoteOdataDataSource()
        val dateNow = Date()
        val dateNowMinusOneMinute = Date(dateNow.time - 60 * 1000)

        val objUser = NetworkApplicationUser(
            Name = "Test from android, filter test",
            EMail = "f@f.f",
            Creator = "Android",
            CreateTime = dateNow
        )

        val countUserCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(countUserCreate, 1)

        val objVote = NetworkVote(
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser,
            CreateTime = dateNow
        )

        val countVoteCreate = dsVote.createObjects(objVote)

        Assert.assertEquals(countVoteCreate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.greaterFilter("CreateTime", dateNowMinusOneMinute)
            )
            .top(10)

        val voteObjsRead = dsVote.readObjects(querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val countVoteDelete = dsVote.deleteObjects(objVote)

        Assert.assertEquals(countVoteDelete, 1)

        val countUserDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(countUserDelete, 1)
    }

    /**
     * Тест объектов [NetworkMaster]+[NetworkDetail]. Создание, обновление, чтение, удаление.
     */
    @Test
    fun masterWithDetailCreateUpdateFilterDeleteTest() {
        val commonDataSource = OdataDataSourceCommon()
        val masterDataSource = MasterOdataDataSource()
        val detailDataSource = DetailOdataDataSource()

        // Создаем мастера
        val master1 = NetworkMaster(
            Name = "test master1"
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
        val countMastersCreated = masterDataSource.createObjects(master1)

        Assert.assertEquals(countMastersCreated, 1)

        // Вычитываем детейлы.
        val details = commonDataSource.readObjects<NetworkDetail>(
            QuerySettings()
                .filter(Filter.equalFilter("Master.__PrimaryKey", master1.__PrimaryKey))
                .top(10)
        )

        // Проверяем количество детейлов.
        Assert.assertEquals(details.size, 2)

        // Вычитываем мастера по полному представлению.
        val masters = masterDataSource.getMastersFull(
            QuerySettings()
                .filter(Filter.equalFilter("__PrimaryKey", master1.__PrimaryKey))
                .top(10)
        )

        // Проверяем количество вычитанных мастеров.
        Assert.assertEquals(masters.size, 1)

        // Проверяем количество детейлов.
        Assert.assertEquals(masters[0].Detail?.size ?: 0, 2)

        // Вычитываем детейлы по полному представлению.
        val fullDetails = detailDataSource.getDetailsFull(
            QuerySettings()
                .filter(Filter.equalFilter("Master.__PrimaryKey", master1.__PrimaryKey))
                .top(10)
        )

        // Проверяем вычитались ли имена мастеров.
        val countNotNullMasterName = fullDetails.count { it.Master.Name?.isNotEmpty() == true }

        // Проверяем количество детейлов.
        Assert.assertEquals(countNotNullMasterName, 2)

        // Удаляем детейлы и проверим количество.
        val countDetailsDeleted = commonDataSource.deleteObjects(master1.Detail!!)

        Assert.assertEquals(countDetailsDeleted, 2)

        // Удаляем мастеров и проверим количество.
        val countMastersDeleted = commonDataSource.deleteObjects(master1)

        Assert.assertEquals(countMastersDeleted, 1)
    }
}