package com.flexberry.androidodataofflinesample.data.local.datasource

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import javax.inject.Inject

class VoteRoomDataSource @Inject constructor(
    private val db: LocalDatabase
) : RoomDataSource<VoteEntity>() {
    private fun VoteDao() = db.getVoteDao()

    override fun createObjects(listObjects: List<VoteEntity>): Int {
        return VoteDao().insertObjects(listObjects).size
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
}