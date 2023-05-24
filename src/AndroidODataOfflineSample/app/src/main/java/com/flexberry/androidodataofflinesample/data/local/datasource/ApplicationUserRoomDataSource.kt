package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataBaseManager
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import javax.inject.Inject

class ApplicationUserRoomDataSource @Inject constructor(
    dataBaseManager: RoomDataBaseManager
) : RoomDataSource<ApplicationUserEntity>(ApplicationUserEntity::class, dataBaseManager) {

}