package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSourceTypeManager
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import javax.inject.Inject

class AppDataRoomDataSource @Inject constructor(
    typeManager: RoomDataSourceTypeManager
) : RoomDataSource<AppDataEntity>(AppDataEntity::class, typeManager) {

}