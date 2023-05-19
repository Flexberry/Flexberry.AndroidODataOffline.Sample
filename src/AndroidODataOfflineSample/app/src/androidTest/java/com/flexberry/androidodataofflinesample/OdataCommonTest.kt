package com.flexberry.androidodataofflinesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSourceCommon
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import java.util.UUID

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
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android",
            EMail = "q@q.q",
            Creator = "Android"
        )

        val obj2 = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android 2",
            EMail = "q@q.q",
            Creator = "Android"
        )

        val cntCreate = ds.createObjects(obj1, obj2)

        Assert.assertEquals(cntCreate, 2)

        val obj3 = NetworkApplicationUser(
            __PrimaryKey = obj2.__PrimaryKey,
            Name = "Test from android 2 changed",
            EMail = obj2.EMail,
            Editor = obj2.Creator
        )

        val cntUpdate = ds.updateObjects(obj3)

        Assert.assertEquals(cntUpdate, 1)

        val cntDelete = ds.deleteObjects(obj1, obj2)

        Assert.assertEquals(cntDelete, 2)
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
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android. Boolean test",
            EMail = "b@b.b",
            Creator = "Android",
            Activated = false
        )

        val cntCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(cntCreate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Name", "Test from android. Boolean test"),
                Filter.equalFilter("Activated", false)
            )
            .top(10)

        val userObjsRead = dsUser.readObjects(NetworkApplicationUser::class, querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val cntDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(cntDelete, 1)
    }

    /**
     * Тест фильтра по полю типа [Double] для типа [NetworkApplicationUser].
     */
    @Test
    fun applicationUserDoubleFilterTest() {
        val dsUser = OdataDataSourceCommon()
        val objUser1 = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android. Double test 1",
            EMail = "double@test.com",
            Creator = "Android",
            Karma = 58.4
        )

        val objUser2 = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android. Double test 2",
            EMail = "double2@test.com",
            Creator = "Android",
            Karma = -12.34
        )

        val cntCreate = dsUser.createObjects(objUser1, objUser2)

        Assert.assertEquals(cntCreate, 2)

        val querySettings = QuerySettings()
            .filter(
                Filter.containsFilter("Name", "Test from android. Double test"),
                Filter.equalFilter("Karma", 58.4)
            )
            .top(10)

        val userObjsRead = dsUser.readObjects(NetworkApplicationUser::class, querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val cntDelete = dsUser.deleteObjects(objUser1, objUser2)

        Assert.assertEquals(cntDelete, 2)
    }

    /**
     * Тест сохранения объекта [NetworkApplicationUser] с детейлом [NetworkVote].
     */
    @Test
    fun applicationUserWithVotesCreateDeleteTest() {
        val dsUser = OdataDataSourceCommon()
        val objUser = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android. Save with detail.",
            EMail = "detail@save.com",
            Creator = "Android",
        )

        objUser.Votes = listOf(
            NetworkVote(
                __PrimaryKey = UUID.randomUUID(),
                VoteType = VoteType.Like,
                Creator = "Android",
                Author = objUser
            ),
            NetworkVote(
                __PrimaryKey = UUID.randomUUID(),
                VoteType = VoteType.Dislike,
                Creator = "Android",
                Author = objUser
            )
        )

        dsUser.createObjects(objUser)
    }

    /**
     * Тест создания, обновления, удаления объектов типа [NetworkVote].
     */
    @Test
    fun voteCreateReadUpdateFilterDeleteTest() {
        val dsCommon = OdataDataSourceCommon()

        val objUser = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android read",
            EMail = "r@r.r",
            Creator = "Android"
        )

        val cntUserCreate = dsCommon.createObjects(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            __PrimaryKey = UUID.randomUUID(),
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser
        )

        val cntVoteCreate = dsCommon.createObjects(objVote)

        Assert.assertEquals(cntVoteCreate, 1)

        val objVote2 = NetworkVote(
            __PrimaryKey = objVote.__PrimaryKey,
            VoteType = VoteType.Dislike,
            Creator = "Android",
            Author = objUser
        )

        val cntVoteUpdate = dsCommon.updateObjects(objVote2)

        Assert.assertEquals(cntVoteUpdate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.equalFilter("VoteType", VoteType.Dislike)
            )
            .top(10)

        val voteObjsRead = dsCommon.readObjects(NetworkVote::class, querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val cntVoteDelete = dsCommon.deleteObjects(objVote)

        Assert.assertEquals(cntVoteDelete, 1)

        val cntUserDelete = dsCommon.deleteObjects(objUser)

        Assert.assertEquals(cntUserDelete, 1)
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
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android, filter test",
            EMail = "f@f.f",
            Creator = "Android",
            CreateTime = dateNow
        )

        val cntUserCreate = dsCommon.createObjects(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            __PrimaryKey = UUID.randomUUID(),
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser,
            CreateTime = dateNow
        )

        val cntVoteCreate = dsCommon.createObjects(objVote)

        Assert.assertEquals(cntVoteCreate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.greaterFilter("CreateTime", dateNowMinusOneMinute)
            )
            .top(10)

        val voteObjsRead = dsCommon.readObjects(NetworkVote::class, querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val cntVoteDelete = dsCommon.deleteObjects(objVote)

        Assert.assertEquals(cntVoteDelete, 1)

        val cntUserDelete = dsCommon.deleteObjects(objUser)

        Assert.assertEquals(cntUserDelete, 1)
    }
}