package com.flexberry.androidodataofflinesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
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

        val cntCreate = ds.createObjects(listOf(obj1, obj2))

        Assert.assertEquals(cntCreate, 2)

        val obj3 = NetworkApplicationUser(
            __PrimaryKey = obj2.__PrimaryKey,
            Name = "Test from android 2 changed",
            EMail = obj2.EMail,
            Editor = obj2.Creator
        )

        val cntUpdate = ds.updateObjects(listOf(obj3))

        Assert.assertEquals(cntUpdate, 1)

        val cntDelete = ds.deleteObjects(listOf(obj1, obj2))

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
    fun voteCreateAndReadTest() {
        val dsUser = ApplicationUserOdataDataSource()
        val dsVote = VoteOdataDataSource()

        val objUser = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android read",
            EMail = "r@r.r",
            Creator = "Android"
        )

        val cntUserCreate = dsUser.createObject(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            __PrimaryKey = UUID.randomUUID(),
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser
        )

        val cntVoteCreate = dsVote.createObject(objVote)

        Assert.assertEquals(cntVoteCreate, 1)

        val querySettings = QuerySettings()
            .filter(Filter.equalFilter("Author/__PrimaryKey", objUser.__PrimaryKey))
            .top(10)

        val voteObjsRead = dsVote.readObjects(querySettings)

        Assert.assertTrue(voteObjsRead.isNotEmpty())

        val cntVoteDelete = dsVote.deleteObject(objVote)

        Assert.assertEquals(cntVoteDelete, 1)

        val cntUserDelete = dsUser.deleteObject(objUser)

        Assert.assertEquals(cntUserDelete, 1)
    }

    @Test
    fun voteCreateUpdateDeleteTest() {
        val dsUser = ApplicationUserOdataDataSource()
        val dsVote = VoteOdataDataSource()

        val objUser = NetworkApplicationUser(
            __PrimaryKey = UUID.randomUUID(),
            Name = "Test from android cud",
            EMail = "q@q.q",
            Creator = "Android"
        )

        val cntUserCreate = dsUser.createObject(objUser)

        Assert.assertEquals(cntUserCreate, 1)

        val objVote = NetworkVote(
            __PrimaryKey = UUID.randomUUID(),
            VoteType = VoteType.Like,
            Creator = "Android",
            Author = objUser
        )

        val cntVoteCreate = dsVote.createObject(objVote)

        Assert.assertEquals(cntVoteCreate, 1)

        val objVote2 = NetworkVote(
            __PrimaryKey = objVote.__PrimaryKey,
            VoteType = VoteType.Dislike,
            Creator = "Android",
            Author = objUser
        )

        val cntVoteUpdate = dsVote.updateObject(objVote2)

        Assert.assertEquals(cntVoteUpdate, 1)

        val cntVoteDelete = dsVote.deleteObject(objVote)

        Assert.assertEquals(cntVoteDelete, 1)

        val cntUserDelete = dsUser.deleteObject(objUser)

        Assert.assertEquals(cntUserDelete, 1)
    }
}