package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataBaseManager
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import javax.inject.Inject

class VoteRoomDataSource @Inject constructor(
    dataBaseManager: RoomDataBaseManager
) : RoomDataSource<VoteEntity>(VoteEntity::class, dataBaseManager) {

}