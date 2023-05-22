package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.FilterType
import com.flexberry.androidodataofflinesample.data.query.OrderType
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

open class RoomDataSource<T: Any>() {
    open fun createObjects(listObjects: List<T>): List<Long> {
        return emptyList();
    }

    open fun readObjects(querySettings: QuerySettings? = null): List<T> {
        return mutableListOf();
    }

    open fun updateObjects(listObjects: List<T>): Int {
        return 0;
    }

    open fun deleteObjects(listObjects: List<T>): Int {
        return 0;
    }

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

            FilterType.Has -> {
                result = "$paramName ${filterType.getRoomDataSourceValue()} '%$paramValue%'"
            }

            FilterType.Contains,
            FilterType.StartsWith,
            FilterType.EndsWith -> {
                result = "${filterType.getRoomDataSourceValue()}($paramName,'$paramValue')"
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

    private fun OrderType.getRoomDataSourceValue(): String {
        return when (this) {
            OrderType.Asc -> "asc"
            OrderType.Desc -> "desc"
        }
    }

    private fun FilterType.getRoomDataSourceValue(): String {
        return when (this) {
            FilterType.Equal -> "="
            FilterType.NotEqual -> "<>"
            FilterType.Greater -> ">"
            FilterType.GreaterOrEqual -> ">="
            FilterType.Less -> "<"
            FilterType.LessOrEqual -> "<="
            FilterType.Has -> "like" // TODO for sqlite
            FilterType.Contains -> "contains" // TODO for sqlite
            FilterType.StartsWith -> "startswith" // TODO for sqlite
            FilterType.EndsWith -> "endswith" // TODO for sqlite
            FilterType.And -> "and"
            FilterType.Or -> "or"
            FilterType.Not -> "not"
        }
    }
}