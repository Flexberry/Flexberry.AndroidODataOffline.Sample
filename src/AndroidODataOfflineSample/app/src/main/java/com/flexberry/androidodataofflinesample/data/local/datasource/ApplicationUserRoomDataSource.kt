package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

class ApplicationUserRoomDataSource(db: LocalDatabase,
                            queryParams: QuerySettings
) : RoomDataSource<ApplicationUserEntity>(db, queryParams) {
    fun ApplicationUserDao() = db.getApplicationUserDao()
}