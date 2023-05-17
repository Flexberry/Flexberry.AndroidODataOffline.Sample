package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import kotlinx.coroutines.flow.first
import java.util.UUID

class AppDataRoomDataSource(db: LocalDatabase,
                         queryParams: QuerySettings
) : RoomDataSource<AppDataEntity>(db, queryParams) {
    fun AppDataDao() = db.getAppDataDao()

    fun createObjects() {

    }

    fun readObjects() {
        val appData = AppDataEntity(UUID.randomUUID(),true)
        val allAppData = AppDataDao().getAppData()
    }
}