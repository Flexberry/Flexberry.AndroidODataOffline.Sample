package com.flexberry.androidodataofflinesample.data.local.datasource

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

class AppDataRoomDataSource(private val db: LocalDatabase) : RoomDataSource<AppDataEntity>() {
    private fun AppDataDao() = db.getAppDataDao()

    override fun createObjects(listObjects: List<AppDataEntity>): List<Long> {
        return AppDataDao().insertObjects(listObjects)
    }

    override fun readObjects(querySettings: QuerySettings?): List<AppDataEntity> {
        var queryParamsValue = querySettings?.getRoomDataSourceValue()
        Log.v("queryParamsValue", queryParamsValue.toString())

        var finalQuery = StringBuilder()
        finalQuery.append("SELECT * FROM AppData")

        queryParamsValue?.forEach{
            if (!it.isNullOrEmpty()) {
                finalQuery.append(it)
            }
        }

        Log.v("finalQuery", finalQuery.toString())

        val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery.toString());

        return AppDataDao().getObjects(simpleSQLiteQuery)
    }

    override fun updateObjects(listObjects: List<AppDataEntity>): Int {
        return AppDataDao().updateObjects(listObjects)
    }

    override fun deleteObjects(listObjects: List<AppDataEntity>): Int {
        return AppDataDao().deleteObjects(listObjects)
    }
}