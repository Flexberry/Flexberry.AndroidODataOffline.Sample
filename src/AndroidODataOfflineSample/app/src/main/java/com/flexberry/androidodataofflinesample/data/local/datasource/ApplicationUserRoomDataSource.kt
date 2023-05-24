package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSourceTypeManager
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import javax.inject.Inject

class ApplicationUserRoomDataSource @Inject constructor(
    typeManager: RoomDataSourceTypeManager
) : RoomDataSource<ApplicationUserEntity>(ApplicationUserEntity::class, typeManager) {

}