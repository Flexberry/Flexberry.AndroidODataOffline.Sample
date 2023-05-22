package com.flexberry.androidodataofflinesample.data.local.datasource

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.entities.relations.VoteWithUser
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import java.util.UUID

class VoteRoomDataSource(private val db: LocalDatabase) : RoomDataSource<VoteEntity>() {
    private fun VoteDao() = db.getVoteDao()

    override fun createObjects(listObjects: List<VoteEntity>): List<Long> {
        return VoteDao().insertObjects(listObjects)
    }

    override fun readObjects(querySettings: QuerySettings?): List<VoteEntity> {
        var queryParamsValue = querySettings?.getRoomDataSourceValue()
        Log.v("queryParamsValue", queryParamsValue.toString())

        var finalQuery = StringBuilder()
        finalQuery.append("SELECT * FROM Vote")

        queryParamsValue?.forEach{
            if (!it.isNullOrEmpty()) {
                finalQuery.append(it)
            }
        }

        Log.v("finalQuery", finalQuery.toString())

        val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery.toString());

        return VoteDao().getObjects(simpleSQLiteQuery)
    }

    override fun updateObjects(listObjects: List<VoteEntity>): Int {
        return VoteDao().updateObjects(listObjects)
    }

    override fun deleteObjects(listObjects: List<VoteEntity>): Int {
        return VoteDao().deleteObjects(listObjects)
    }

    fun readObjectWithMaster(pks: List<UUID>) : List<VoteWithUser?> {
        return VoteDao().getObjectsWithMaster(pks)
    }
}