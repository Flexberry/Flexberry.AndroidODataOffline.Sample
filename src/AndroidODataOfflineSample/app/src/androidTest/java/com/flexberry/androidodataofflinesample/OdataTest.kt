package com.flexberry.androidodataofflinesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
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

        val cntCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(cntCreate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Name", "Test from android. Boolean test"),
                Filter.equalFilter("Activated", false)
            )
            .top(10)

        val userObjsRead = dsUser.readObjects(querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val cntDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(cntDelete, 1)
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

        val cntCreate = dsUser.createObjects(objUser1, objUser2)

        Assert.assertEquals(cntCreate, 2)

        val querySettings = QuerySettings()
            .filter(
                Filter.containsFilter("Name", "Test from android. Double test"),
                Filter.equalFilter("Karma", 58.4)
            )
            .top(10)

        val userObjsRead = dsUser.readObjects(querySettings)

        Assert.assertTrue(userObjsRead.isNotEmpty())

        val cntDelete = dsUser.deleteObjects(objUser1, objUser2)

        Assert.assertEquals(cntDelete, 2)
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

        val cntUserCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser
        )

        val cntVoteCreate = dsVote.createObjects(objVote)

        Assert.assertEquals(cntVoteCreate, 1)

        val objVote2 = NetworkVote(
            __PrimaryKey = objVote.__PrimaryKey,
            VoteType = VoteType.Dislike,
            Creator = "Android",
            Author = objUser
        )

        val cntVoteUpdate = dsVote.updateObjects(objVote2)

        Assert.assertEquals(cntVoteUpdate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.equalFilter("VoteType", VoteType.Dislike)
            )
            .top(10)

        val voteObjsRead = dsVote.readObjects(querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val cntVoteDelete = dsVote.deleteObjects(objVote)

        Assert.assertEquals(cntVoteDelete, 1)

        val cntUserDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(cntUserDelete, 1)
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

        val cntUserCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser,
            CreateTime = dateNow
        )

        val cntVoteCreate = dsVote.createObjects(objVote)

        Assert.assertEquals(cntVoteCreate, 1)

        val querySettings = QuerySettings()
            .filter(
                Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
                Filter.greaterFilter("CreateTime", dateNowMinusOneMinute)
            )
            .top(10)

        val voteObjsRead = dsVote.readObjects(querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val cntVoteDelete = dsVote.deleteObjects(objVote)

        Assert.assertEquals(cntVoteDelete, 1)

        val cntUserDelete = dsUser.deleteObjects(objUser)

        Assert.assertEquals(cntUserDelete, 1)
    }
}