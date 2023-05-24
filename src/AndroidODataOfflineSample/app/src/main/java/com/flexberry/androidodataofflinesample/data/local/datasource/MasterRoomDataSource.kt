package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSourceTypeManager
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import javax.inject.Inject

class MasterRoomDataSource @Inject constructor(
    typeManager: RoomDataSourceTypeManager
) : RoomDataSource<MasterEntity>(MasterEntity::class, typeManager) {

}