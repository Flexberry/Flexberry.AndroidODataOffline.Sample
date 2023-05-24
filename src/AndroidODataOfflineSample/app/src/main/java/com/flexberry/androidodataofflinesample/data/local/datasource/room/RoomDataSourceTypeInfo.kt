package com.flexberry.androidodataofflinesample.data.local.datasource.room

import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.dao.BaseDao
import kotlin.reflect.KClass

class RoomDataSourceTypeInfo<T : Any> (
    val kotlinClass: KClass<T>,
    val dao: BaseDao<T>,
    val tableName: String
) {
    val typeName = kotlinClass.simpleName!!

    fun insertObjects(appData: List<Any>): Int {
        return dao.insertObjects(appData as List<T>).size
    }

    fun getObjects(query: SimpleSQLiteQuery): List<T> {
        return dao.getObjects(query)
    }

    fun updateObjects(appData: List<Any>): Int {
        return dao.updateObjects(appData as List<T>)
    }

    fun deleteObjects(appData: List<Any>): Int {
        return dao.deleteObjects(appData as List<T>)
    }
}