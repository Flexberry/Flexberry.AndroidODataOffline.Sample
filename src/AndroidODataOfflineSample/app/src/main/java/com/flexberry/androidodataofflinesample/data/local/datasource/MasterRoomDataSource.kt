package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataBaseManager
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import javax.inject.Inject

class MasterRoomDataSource @Inject constructor(
    dataBaseManager: RoomDataBaseManager
) : RoomDataSource<MasterEntity>(MasterEntity::class, dataBaseManager) {
    fun getMastersWithRelations(querySettings: QuerySettings? = null): List<MasterEntity> {
        return this.readObjects(querySettings, MasterEntity.Views.MasterEntityE)
    }
}