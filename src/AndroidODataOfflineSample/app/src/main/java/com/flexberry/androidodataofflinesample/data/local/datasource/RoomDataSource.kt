package com.flexberry.androidodataofflinesample.data.local.datasource

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.dao.BaseDao
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.FilterType
import com.flexberry.androidodataofflinesample.data.query.OrderType
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

/**
 * Источник данных Room.
 *
 * @param T Тип объекта.
 * @param dao Соответствующий DAO-класс.
 * @param tableName Соответствующая таблица в БД.
 * @see [LocalDataSource].
 */
open class RoomDataSource<T: Any>(val dao: BaseDao<T>,
                                  private val tableName: String) : LocalDataSource<T> {
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
        return dao.insertObjects(listObjects).size
    }

    /**
     * Вычитать объекты.
     *
     * @param querySettings Параметры ограничения.
     * @return Список объектов.
     */
    override fun readObjects(querySettings: QuerySettings?): List<T> {
        var queryParamsValue = querySettings?.getRoomDataSourceValue()
        Log.v("queryParamsValue", queryParamsValue.toString())

        var finalQuery = StringBuilder()
        finalQuery.append("SELECT * FROM $tableName")

        queryParamsValue?.forEach{
            if (!it.isNullOrEmpty()) {
                finalQuery.append(it)
            }
        }

        Log.v("finalQuery", finalQuery.toString())

        val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery.toString());

        return dao.getObjects(simpleSQLiteQuery)
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
        return dao.updateObjects(listObjects)
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
        return dao.deleteObjects(listObjects)
    }

    /**
     * Получить список строковых значений для [QuerySettings].
     *
     * @return Список строковых значений.
     */
    protected fun QuerySettings.getRoomDataSourceValue(): MutableList<String> {
        val elements: MutableList<String> = mutableListOf()

        if (this.selectList != null) {
            val selectValue = this.selectList!!.joinToString(",")
            val selectValueFull = if (selectValue.isNullOrEmpty()) "*" else selectValue
            elements.add(selectValueFull)
        }

        if (this.filterValue != null) {
            val filterVal = "${this.filterValue!!.getRoomDataSourceValue()}"
            val filterValFull = if (filterVal.isNullOrEmpty()) "" else " WHERE $filterVal"
            elements.add(filterValFull)
        }

        if (this.orderList != null) {
            val orderValue = this.orderList!!
                .joinToString(",") { x -> "${x.first} ${x.second.getRoomDataSourceValue()}"}
            val orderValueFull = if (orderValue.isNullOrEmpty()) "" else " ORDER BY $orderValue"
            elements.add(orderValueFull)
        }

        elements.add(if (this.topValue != null) " LIMIT ${this.topValue}" else "")

        elements.add(if (this.skipValue != null) " OFFSET ${this.skipValue}" else "")

        return elements
    }

    /**
     * Получить строковое значение для [Filter].
     *
     * @return Строковое значение.
     */
    private fun Filter.getRoomDataSourceValue(): String {
        var result = ""

        when (this.filterType) {
            FilterType.Equal,
            FilterType.NotEqual,
            FilterType.Greater,
            FilterType.GreaterOrEqual,
            FilterType.Less,
            FilterType.LessOrEqual -> {
                result = "$paramName ${filterType.getRoomDataSourceValue()} '$paramValue'"
            }

            FilterType.Has,
            FilterType.Contains -> {
                result = "$paramName ${filterType.getRoomDataSourceValue()} '%$paramValue%'"
            }

            FilterType.StartsWith -> {
                result = "$paramName ${filterType.getRoomDataSourceValue()} '$paramValue%')"
            }
            FilterType.EndsWith -> {
                result = "$paramName ${filterType.getRoomDataSourceValue()} '%$paramValue')"
            }

            FilterType.And -> {
                result = filterParams
                    ?.joinToString(" ${filterType.getRoomDataSourceValue()} ")
                    { x -> x.getRoomDataSourceValue() }.toString()
            }

            FilterType.Or -> {
                result = filterParams
                    ?.joinToString(" ${filterType.getRoomDataSourceValue()} ")
                    { x -> "(${x.getRoomDataSourceValue()})" }.toString()
            }

            FilterType.Not -> {
                result = "${filterType.getRoomDataSourceValue()} ${filterParams?.get(0)
                    ?.getRoomDataSourceValue()}"
            }
        }

        return result
    }

    /**
     * Получить строковое значение для [OrderType].
     *
     * @return Строковое значение.
     */
    private fun OrderType.getRoomDataSourceValue(): String {
        return when (this) {
            OrderType.Asc -> "asc"
            OrderType.Desc -> "desc"
        }
    }

    /**
     * Получить строковое значение для [FilterType].
     *
     * @return Строковое значение.
     */
    private fun FilterType.getRoomDataSourceValue(): String {
        return when (this) {
            FilterType.Equal -> "="
            FilterType.NotEqual -> "<>"
            FilterType.Greater -> ">"
            FilterType.GreaterOrEqual -> ">="
            FilterType.Less -> "<"
            FilterType.LessOrEqual -> "<="
            FilterType.Has -> "like"
            FilterType.Contains -> "like"
            FilterType.StartsWith -> "like"
            FilterType.EndsWith -> "like"
            FilterType.And -> "and"
            FilterType.Or -> "or"
            FilterType.Not -> "not"
        }
    }
}