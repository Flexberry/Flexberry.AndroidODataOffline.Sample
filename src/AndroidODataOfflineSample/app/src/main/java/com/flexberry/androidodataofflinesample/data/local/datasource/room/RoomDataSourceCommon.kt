package com.flexberry.androidodataofflinesample.data.local.datasource.room

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.di.RoomDataSourceManager
import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.FilterType
import com.flexberry.androidodataofflinesample.data.query.OrderType
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import javax.inject.Inject
import kotlin.reflect.KClass

open class RoomDataSourceCommon @Inject constructor(
    @RoomDataSourceManager val typeManager: RoomDataSourceTypeManager
) {
    fun createObjects(vararg dataObjects: Any): Int {
        return createObjects(dataObjects.asList())
    }

    fun createObjects(listObjects: List<Any>): Int {
        var countResult = 0

        listObjects.forEach { entityObject ->
            val entityObjectSimpleName = entityObject::class.simpleName
            val entityObjectTypeInfo = typeManager.getInfoByTypeName(entityObjectSimpleName)!!

            countResult += entityObjectTypeInfo.insertObjects(listOf(entityObject))
        }

        return countResult
    }

    fun readObjects(kotlinClass: KClass<*>, querySettings: QuerySettings?): List<Any> {
        val entityObjectSimpleName = kotlinClass.simpleName
        val entityObjectTypeInfo = typeManager.getInfoByTypeName(entityObjectSimpleName)!!
        val tableName = entityObjectTypeInfo.tableName
        val queryParamsValue = querySettings?.getRoomDataSourceValue()
        Log.v("queryParamsValue", queryParamsValue.toString())

        val finalQuery = StringBuilder()
        finalQuery.append("SELECT * FROM $tableName")

        queryParamsValue?.forEach{
            if (it.isNotEmpty()) {
                finalQuery.append(it)
            }
        }

        Log.v("finalQuery", finalQuery.toString())

        val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery.toString());

        return entityObjectTypeInfo.getObjects(simpleSQLiteQuery)
    }

    inline fun <reified T: Any> readObjects(querySettings: QuerySettings? = null) : List<T> {
        return readObjects(T::class, querySettings) as List<T>
    }

    fun deleteObjects(listObjects: List<Any>): Int {
        var countResult = 0

        listObjects.forEach { entityObject ->
            val entityObjectSimpleName = entityObject::class.simpleName
            val entityObjectTypeInfo = typeManager.getInfoByTypeName(entityObjectSimpleName)!!

            countResult += entityObjectTypeInfo.deleteObjects(listOf(entityObject))
        }

        return countResult
    }

    fun deleteObjects(vararg dataObjects: Any): Int {
        return deleteObjects(dataObjects.asList())
    }

    fun updateObjects(listObjects: List<Any>): Int {
        var countResult = 0

        listObjects.forEach { entityObject ->
            val entityObjectSimpleName = entityObject::class.simpleName
            val entityObjectTypeInfo = typeManager.getInfoByTypeName(entityObjectSimpleName)!!

            countResult += entityObjectTypeInfo.updateObjects(listOf(entityObject))
        }

        return countResult
    }

    fun updateObjects(vararg dataObjects: Any): Int {
        return updateObjects(dataObjects.asList())
    }

    /**
     * Получить список строковых значений для [QuerySettings].
     *
     * @return Список строковых значений.
     */
    private fun QuerySettings.getRoomDataSourceValue(): MutableList<String> {
        val elements: MutableList<String> = mutableListOf()

        if (this.selectList != null) {
            val selectValue = this.selectList!!.joinToString(",")
            val selectValueFull = selectValue.ifEmpty { "*" }
            elements.add(selectValueFull)
        }

        if (this.filterValue != null) {
            val filterVal = this.filterValue!!.getRoomDataSourceValue()
            val filterValFull = if (filterVal.isEmpty()) "" else " WHERE $filterVal"
            elements.add(filterValFull)
        }

        if (this.orderList != null) {
            val orderValue = this.orderList!!
                .joinToString(",") { x -> "${x.first} ${x.second.getRoomDataSourceValue()}"}
            val orderValueFull = if (orderValue.isEmpty()) "" else " ORDER BY $orderValue"
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