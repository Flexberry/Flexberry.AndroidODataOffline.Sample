package com.flexberry.androidodataofflinesample.data.local.datasource.room

import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.dao.BaseDao
import kotlin.reflect.KClass

/**
 * Информация о базе данных сущности.
 *
 * @param T Тип сущности.
 * @param kotlinClass Класс сущности.
 * @param dao [BaseDao] интерфейс.
 * @param tableName Имя таблицы.
 * @param details Список детейлов.
 */
class RoomDataBaseEntityInfo<T : Any> (
    val kotlinClass: KClass<T>,
    val dao: BaseDao<T>,
    val tableName: String,
    val details: List<String>? = null
) {
    /**
     * Имя типа сущности.
     */
    val typeName = kotlinClass.simpleName!!

    /**
     * Вставить сущности в текущий [BaseDao].
     *
     * @param appData Список сущностей.
     * @return Количество вставленных сущностей.
     */
    fun insertObjects(appData: List<Any>): Int {
        return dao.insertObjects(appData as List<T>).size
    }

    /**
     * Получить сущности из текущего [BaseDao].
     *
     * @param query Запрос.
     * @return Список сущностей.
     */
    fun getObjects(query: SimpleSQLiteQuery): List<T> {
        return dao.getObjects(query)
    }

    /**
     * Обновить сущности в текущем [BaseDao].
     *
     * @param appData Список сущностей.
     * @return Количество обновленных сущностей.
     */
    fun updateObjects(appData: List<Any>): Int {
        return dao.updateObjects(appData as List<T>)
    }

    /**
     * Удалить сущности из текущего [BaseDao].
     *
     * @param appData Список сущностей.
     * @return Количество удаленных сущностей.
     */
    fun deleteObjects(appData: List<Any>): Int {
        return dao.deleteObjects(appData as List<T>)
    }

    /**
     * Содержит ли тип указанный детейл.
     *
     * @param detailName Имя детейла.
     * @return True если содержит, иначе False.
     */
    fun hasDetail(detailName: String?): Boolean {
        return details?.contains(detailName) ?: false
    }
}