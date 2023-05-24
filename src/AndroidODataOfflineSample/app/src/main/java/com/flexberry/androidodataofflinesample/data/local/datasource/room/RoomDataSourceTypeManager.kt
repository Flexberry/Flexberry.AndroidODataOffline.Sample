package com.flexberry.androidodataofflinesample.data.local.datasource.room

import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import javax.inject.Inject

/**
 * Менеджер типов для Room.
 *
 * @param db Текущая база данных.
 */
class RoomDataSourceTypeManager @Inject constructor(
    db: LocalDatabase
) {
    /**
     * Список [RoomDataSourceTypeInfo] типов.
     */
    private val roomTypeMap = listOf(
        RoomDataSourceTypeInfo(
            kotlinClass = AppDataEntity::class,
            dao = db.getAppDataDao(),
            tableName = AppDataEntity.tableName
        ),
        RoomDataSourceTypeInfo(
            kotlinClass = MasterEntity::class,
            dao = db.getMasterDao(),
            tableName = MasterEntity.tableName,
            details = listOf("details")
        ),
        RoomDataSourceTypeInfo(
            kotlinClass = DetailEntity::class,
            dao = db.getDetailDao(),
            tableName = DetailEntity.tableName
        ),
        RoomDataSourceTypeInfo(
            kotlinClass = ApplicationUserEntity::class,
            dao = db.getApplicationUserDao(),
            tableName = ApplicationUserEntity.tableName
        ),
        RoomDataSourceTypeInfo(
            kotlinClass = VoteEntity::class,
            dao = db.getVoteDao(),
            tableName = VoteEntity.tableName
        ),
    )

    /**
     * Получить информацию о типе по его имени.
     *
     * @param typeName Имя типа.
     * @return [RoomDataSourceTypeInfo] указанного типа.
     */
    fun getInfoByTypeName(typeName: String?): RoomDataSourceTypeInfo<*>? {
        return roomTypeMap.firstOrNull { x -> x.typeName == typeName }
    }
}