package com.flexberry.androidodataofflinesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
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
        val settings = ds.getSettingsBuilder()
        val settingsValue = settings.filter("Name eq 'NameForTest'").orderBy("Name asc").top(5).build()
        val objs = ds.readObjects(settingsValue)

        if (objs.any()) {
            Assert.assertTrue(objs.any { x -> x.Name != null })
        }
    }
}