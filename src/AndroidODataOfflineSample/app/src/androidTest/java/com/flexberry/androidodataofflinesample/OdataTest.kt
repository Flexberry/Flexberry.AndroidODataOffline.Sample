package com.flexberry.androidodataofflinesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import java.util.UUID


@RunWith(AndroidJUnit4::class)
class OdataTest {
    @Test
    fun applicationUserReadTest() {
        val ds = ApplicationUserOdataDataSource()
        val objs = ds.readObjects()

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.Name != null })
        }
    }

    @Test
    fun applicationUserCreateUpdateDeleteTest() {
        val ds = ApplicationUserOdataDataSource()
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

    @Test
    fun voteCreateReadUpdateFilterDeleteTest() {
        val dsUser = ApplicationUserOdataDataSource()
        val dsVote = VoteOdataDataSource()

        val objUser = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android read",
            EMail = "r@r.r",
            Creator = "Android"
        )

        val cntUserCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            __PrimaryKey = UUID.randomUUID(),
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

    @Test
    fun voteDatetimeFilterTest() {
        val dsUser = ApplicationUserOdataDataSource()
        val dsVote = VoteOdataDataSource()
        val dateNow = Date()
        val dateNowMinusOneMinute = Date(dateNow.time - 60 * 1000)

        val objUser = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android, filter test",
            EMail = "f@f.f",
            Creator = "Android",
            CreateTime = dateNow
        )

        val cntUserCreate = dsUser.createObjects(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            __PrimaryKey = UUID.randomUUID(),
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