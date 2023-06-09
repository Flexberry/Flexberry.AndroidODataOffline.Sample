package com.flexberry.androidodataofflinesample.data.local.datasource.room

import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import javax.inject.Inject

/**
 * Менеджер сущностей для Room.
 *
 * @param db Текущая база данных.
 */
class RoomDataBaseManager @Inject constructor(
    db: LocalDatabase
) {
    /**
     * Список [RoomDataBaseEntityInfo] типов.
     */
    private val roomDataBaseInfoMap = listOf(
        RoomDataBaseEntityInfo(
            kotlinClass = AppDataEntity::class,
            dao = db.getAppDataDao(),
            tableName = AppDataEntity.tableName
        ),
        RoomDataBaseEntityInfo(
            kotlinClass = MasterEntity::class,
            dao = db.getMasterDao(),
            tableName = MasterEntity.tableName,
            details = listOf(
                RoomDataBaseRelation(
                    entityProperty = "details",
                    kotlinClass =  DetailEntity::class,
                    relationProperty =  "masterId",
                )
            )
        ),
        RoomDataBaseEntityInfo(
            kotlinClass = DetailEntity::class,
            dao = db.getDetailDao(),
            tableName = DetailEntity.tableName,
            masters = listOf(
                RoomDataBaseRelation(
                    entityProperty = "master",
                    kotlinClass = MasterEntity::class,
                    relationProperty = "masterId"
                )
            )
        ),
        RoomDataBaseEntityInfo(
            kotlinClass = ApplicationUserEntity::class,
            dao = db.getApplicationUserDao(),
            tableName = ApplicationUserEntity.tableName
        ),
        RoomDataBaseEntityInfo(
            kotlinClass = VoteEntity::class,
            dao = db.getVoteDao(),
            tableName = VoteEntity.tableName
        ),
    )

    /**
     * Получить информацию о базе данных по его имени типа.
     *
     * @param typeName Имя типа.
     * @return [RoomDataBaseEntityInfo] указанного типа.
     */
    fun getDataBaseInfoForTypeName(typeName: String?): RoomDataBaseEntityInfo<*>? {
        return roomDataBaseInfoMap.firstOrNull { x -> x.typeName == typeName }
    }
}