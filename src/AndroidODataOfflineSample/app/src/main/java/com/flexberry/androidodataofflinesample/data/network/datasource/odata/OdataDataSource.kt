package com.flexberry.androidodataofflinesample.data.network.datasource.odata

import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import kotlin.reflect.KClass

/**
 * Источник данных OData.
 *
 * @param T Тип объекта.
 * @param odataObjectClass Класс объекта.
 * @see [NetworkDataSource].
 */
open class OdataDataSource<T : Any> (private val odataObjectClass: KClass<T>) : NetworkDataSource<T>
{
    /**
     * Общий источник данных OData.
     */
    private val odataDataSourceCommon = OdataDataSourceCommon()

    /**
     * Создать объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество созданных объектов.
     */
    override fun createObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.createObjects(dataObjects.asList())
    }

    /**
     * Создать объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество созданных объектов.
     */
    override fun createObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.createObjects(listObjects)
    }

    /**
     * Вычитать объекты.
     *
     * @param querySettings Параметры ограничения.
     * @return Список объектов.
     */
    override fun readObjects(querySettings: QuerySettings?): List<T> {
        return odataDataSourceCommon.readObjects(odataObjectClass, querySettings) as List<T>
    }

    /**
     * Обновить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество обновленных объектов.
     */
    override fun updateObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.updateObjects(dataObjects.asList())
    }

    /**
     * Обновить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество обновленных объектов.
     */
    override fun updateObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.updateObjects(listObjects)
    }

    /**
     * Удалить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество удаленных объектов.
     */
    override fun deleteObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.deleteObjects(dataObjects.asList())
    }

    /**
     * Удалить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество удаленных объектов.
     */
    override fun deleteObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.deleteObjects(listObjects)
    }
}