package com.flexberry.androidodataofflinesample.data.local.datasource.room

import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import com.flexberry.androidodataofflinesample.data.query.View
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Источник данных Room.
 *
 * @param T Тип объекта.
 * @param dao Соответствующий DAO-класс.
 * @param tableName Соответствующая таблица в БД.
 * @see [LocalDataSource].
 */
open class RoomDataSource<T: Any> @Inject constructor(
    private val entityObjectClass: KClass<T>,
    dataBaseManager: RoomDataBaseManager
) : LocalDataSource<T> {
    private val roomDataSourceCommon = RoomDataSourceCommon(dataBaseManager)

    /**
     * Создать объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество созданных объектов.
     */
    override fun createObjects(vararg dataObjects: T): Int {
        return this.createObjects(dataObjects.asList())
    }

    /**
     * Создать объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество созданных объектов.
     */
    override fun createObjects(listObjects: List<T>): Int {
        return roomDataSourceCommon.createObjects(listObjects)
    }

    /**
     * Вычитать объекты.
     *
     * @param querySettings Параметры ограничения.
     * @return Список объектов.
     */
    override fun readObjects(querySettings: QuerySettings?, view: View?): List<T> {
        return roomDataSourceCommon.readObjects(entityObjectClass, querySettings, view) as List<T>
    }

    /**
     * Обновить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество обновленных объектов.
     */
    override fun updateObjects(vararg dataObjects: T): Int {
        return this.updateObjects(dataObjects.asList())
    }

    /**
     * Обновить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество обновленных объектов.
     */
    override fun updateObjects(listObjects: List<T>): Int {
        return roomDataSourceCommon.updateObjects(listObjects)
    }

    /**
     * Удалить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество удаленных объектов.
     */
    override fun deleteObjects(vararg dataObjects: T): Int {
        return this.deleteObjects(dataObjects.asList())
    }

    /**
     * Удалить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество удаленных объектов.
     */
    override fun deleteObjects(listObjects: List<T>): Int {
        return roomDataSourceCommon.deleteObjects(listObjects)
    }
}