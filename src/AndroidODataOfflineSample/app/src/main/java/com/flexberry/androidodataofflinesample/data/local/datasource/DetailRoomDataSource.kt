package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataBaseManager
import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import javax.inject.Inject

class DetailRoomDataSource @Inject constructor(
    dataBaseManager: RoomDataBaseManager
) : RoomDataSource<DetailEntity>(DetailEntity::class, dataBaseManager) {
    /**
     * Получить объекты [DetailEntity] со связями.
     */
    fun getDetailsWithRelations(querySettings: QuerySettings? = null): List<DetailEntity> {
        return this.readObjects(querySettings, DetailEntity.Views.DetailEntityE)
    }
}